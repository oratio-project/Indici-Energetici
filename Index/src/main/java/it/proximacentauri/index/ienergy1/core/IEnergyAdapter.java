/*******************************************************************************
 * This file is part of Oratio4Energy.
 * 
 * Oratio4Energy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Oratio4Energy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Oratio4Energy.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
package it.proximacentauri.index.ienergy1.core;

import it.proximacentauri.index.core.BalanceAdapter;
import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.domain.Cadency;
import it.proximacentauri.index.exception.UnableToCreateBalanceException;
import it.proximacentauri.index.ienergy1.json.Axe;
import it.proximacentauri.index.ienergy1.json.Cell;
import it.proximacentauri.index.ienergy1.json.MdxResult;
import it.proximacentauri.index.ienergy1.json.RowElement;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IEnergyAdapter implements BalanceAdapter {

	final private Logger logger = LoggerFactory.getLogger(IEnergyAdapter.class);

	// the http client instance
	private HttpClient client = null;

	// the url of plant
	private String url;

	// jackson object mapper
	private ObjectMapper mapper = null;

	/**
	 * Create a new ienergy adapter for V1 pai version
	 * 
	 * @param url
	 */
	public IEnergyAdapter(String url) {
		this.client = new DefaultHttpClient();
		this.url = url;
		this.mapper = new ObjectMapper();
	}

	// static definition of mdx query
	final static String MDX_QUERY_TEMPERATURE = "SELECT+[Measures].[Temperature]+ON+COLUMNS,CrossJoin([Location].[:drain:],([Time].[:startYear:].[:startMonth:].[:startDay:].[:startHour:]:[Time].[:endYear:].[:endMonth:].[:endDay:].[:endHour:]))+ON+ROWS+FROM+[Climatic]";
	final static String MDX_QUERY_HUMIDITY = "SELECT+[Measures].[Humidity]+ON+COLUMNS,CrossJoin([Location].[:drain:],([Time].[:startYear:].[:startMonth:].[:startDay:].[:startHour:]:[Time].[:endYear:].[:endMonth:].[:endDay:].[:endHour:]))+ON+ROWS+FROM[Climatic]";
	final static String MDX_QUERY_CO2 = "SELECT+[Measures].[CO2]+ON+COLUMNS,CrossJoin([Location].[:drain:],([Time].[Time].[:startYear:].[:startMonth:].[:startDay:].[:startHour:]:[Time].[:endYear:].[:endMonth:].[:endDay:].[:endHour:]))+ON+ROWS+FROM[Climatic]";
	final static String MDX_QUERY_ILLUMINANCE = "SELECT+[Measures].[Illuminance] ON COLUMNS,CrossJoin([Location].[:drain:],([Time].[Time].[:startYear:].[:startMonth:].[:startDay:].[:startHour:]:[Time].[:endYear:].[:endMonth:].[:endDay:].[:endHour:]))+ON+ROWS+FROM+[Climatic]";
	final static String MDX_QUERY_ELETTRIC = "SELECT+Measures+ON+COLUMNS,CrossJoin([Location].[:drain:],([Time].[Time].[:startYear:].[:startMonth:].[:startDay:].[:startHour:]:[Time].[:endYear:].[:endMonth:].[:endDay:].[:endHour:]))+ON+ROWS+FROM+[Electric]";

	@Override
	public List<BalanceSnapshot> laodBalanceSnapshots(List<BalanceItem> items, Date start, Date end, Cadency cadency) {
		logger.debug("start loading from start date {} to end date {} item {}", start, end, items);

		List<BalanceSnapshot> snapshots = new ArrayList<BalanceSnapshot>();

		for (BalanceItem item : items) {
			List<BalanceSnapshot> shotsList = loadBalanceSnaphost(item, start, end, cadency);
			snapshots.addAll(shotsList);
		}

		return snapshots;
	}

	private List<BalanceSnapshot> loadBalanceSnaphost(BalanceItem item, Date start, Date end, Cadency cadency) {
		logger.info("load balance item value {}", item);

		Calendar calendar = Calendar.getInstance();
		List<BalanceSnapshot> items = new ArrayList<BalanceSnapshot>();

		try {
			final String urlString = buildUrl(item, start, end);

			HttpGet get = new HttpGet(urlString);

			logger.info("perform rest call {}", urlString);

			final HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new UnableToCreateBalanceException("Error on load source -> the status code is code "
						+ response.getStatusLine().getStatusCode());
			}

			// start parsing data
			MdxResult mdx = mapper.readValue(response.getEntity().getContent(), MdxResult.class);
			logger.info("{}", mdx);

			final Map<Integer, Date> rowMapper = new HashMap<Integer, Date>();

			// convert the balance item
			final Axe axe = mdx.getAxes().get(1);

			for (int i = 1; i < axe.getElements().size(); i = i + 2) {
				RowElement element = axe.getElements().get(i);
				logger.trace("Found row elements {}", element);

				// parse the date
				final String[] splitStrings = element.getName().split("\\|");

				calendar.set(Calendar.YEAR, Integer.parseInt(splitStrings[0]));
				calendar.set(Calendar.MONTH, Integer.parseInt(splitStrings[1]) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(splitStrings[2]));
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitStrings[3]));
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);

				rowMapper.put(element.getPosition(), calendar.getTime());
			}

			for (Cell cell : mdx.getCells()) {
				int position = cell.getPosition().get(1);

				if (rowMapper.containsKey(position)) {
					// create e a new balance snapshot item
					final BalanceSnapshot snapshots = new BalanceSnapshot();
					snapshots.setDatetime(rowMapper.get(position));
					snapshots.setValue(new BigDecimal(cell.getValue()));
					snapshots.setBalanceItem(item);

					items.add(snapshots);
				}

			}

		} catch (UnsupportedEncodingException e) {
			// CAN BE INGORED
		} catch (Exception e) {
			throw new UnableToCreateBalanceException(e);
		}

		return items;
	}

	/**
	 * Build the request url based on unit of measure (horribile )
	 * 
	 * @param item
	 *            the balance item
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return the requested url
	 * @throws UnsupportedEncodingException
	 */
	private String buildUrl(BalanceItem item, Date start, Date end) throws UnsupportedEncodingException {
		// select reference query
		String mdxString = MDX_QUERY_ELETTRIC;
		if (item.getUnit() != null) {
			// check if is a climatic measures
			if (item.getUnit().equalsIgnoreCase("C")) {
				mdxString = MDX_QUERY_TEMPERATURE;
			} else if (item.getUnit().equalsIgnoreCase("%")) {
				mdxString = MDX_QUERY_HUMIDITY;
			} else if (item.getUnit().equalsIgnoreCase("ppm")) {
				mdxString = MDX_QUERY_CO2;
			} else if (item.getUnit().equalsIgnoreCase("lux")) {
				mdxString = MDX_QUERY_ILLUMINANCE;
			}
		}

		// replace drain
		if (item.getName() != null)
			mdxString = mdxString.replaceFirst(":drain:", item.getName());
		else
			mdxString = mdxString.replaceFirst(":drain:", item.getCode());

		Calendar calendar = Calendar.getInstance();

		// replace start and end date
		calendar.setTime(start);
		mdxString = mdxString.replaceAll(":startYear:", Integer.toString(calendar.get(Calendar.YEAR)));
		mdxString = mdxString.replaceAll(":startMonth:", Integer.toString(calendar.get(Calendar.MONTH) + 1));
		mdxString = mdxString.replaceAll(":startDay:", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		mdxString = mdxString.replaceAll(":startHour:", Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));

		calendar.setTime(end);
		mdxString = mdxString.replaceAll(":endYear:", Integer.toString(calendar.get(Calendar.YEAR)));
		mdxString = mdxString.replaceAll(":endMonth:", Integer.toString(calendar.get(Calendar.MONTH) + 1));
		mdxString = mdxString.replaceAll(":endDay:", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
		mdxString = mdxString.replaceAll(":endHour:", Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));

		logger.trace("build mdx query is {}", mdxString);

		return url + "/bi/mdx?q=" + mdxString;
	}

}
