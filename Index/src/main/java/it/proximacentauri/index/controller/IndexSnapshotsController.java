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
package it.proximacentauri.index.controller;

import it.proximacentauri.index.core.Constant;
import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BaseTime;
import it.proximacentauri.index.model.CalculateIndexJson;
import it.proximacentauri.index.model.DatePartJson;
import it.proximacentauri.index.model.DatetimeJson;
import it.proximacentauri.index.model.IndexJson;
import it.proximacentauri.index.report.BIndexCompare;
import it.proximacentauri.index.util.JSonModelUtil;
import it.proximacentauri.index.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexSnapshotsController extends BaseController {
	final private Logger log = LoggerFactory.getLogger(IndexSnapshotsController.class);

	@ResponseBody
	@RequestMapping(value = "indexsnapdates", method = RequestMethod.GET)
	public List<DatetimeJson> getIndexSnapshotDates() {
		log.info("Get list of all snapshot index dates");

		final List<Date> list = service.listSnapshotIndexTimestamps();

		return JSonModelUtil.convertToDatetimeJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "indexsnapdates/{base}", method = RequestMethod.GET)
	public List<DatePartJson> getTimestamps(@PathVariable("base") BaseTime base,
			@RequestParam(value = "year", required = false) Integer year, @RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "day", required = false) Integer day, @RequestParam(value = "hour", required = false) Integer hour) {
		log.info("Get list of all {} of snapshot index dates where dates is " + "year: {}, month: {}, day: {}, hour: {}", base, year,
				month, day, hour);

		final List<Integer> list = service.listDateTimestamps(base, year, month, day, hour);

		return JSonModelUtil.convertToDatePartJson(list);

	}

	@ResponseBody
	@RequestMapping(value = "indexsnapshots", method = RequestMethod.GET)
	public List<IndexJson> getIndexSnapshots(@RequestParam(value = "category", required = false) Integer category,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date endDate) {
		log.info("Get list of all snapshot for index of category {} from {} to {}", category, startDate, endDate);

		final Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> list = service.listIndexSnapshots(category, startDate, endDate);

		return JSonModelUtil.convertToIndexJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "indexsnapshots/{datetime}", method = RequestMethod.GET)
	public List<IndexJson> getIndexSnapshotDetail(@PathVariable("datetime") @DateTimeFormat(pattern = Constant.ISO_8601) Date datetime,
			@RequestParam(value = "category", required = false) Integer category) {
		log.info("Get detail of snapshot index at timestamp {} and category {}", datetime, category);

		final Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> list = service.findIndexSnapshot(datetime, category);

		return JSonModelUtil.convertToIndexJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "indexsnapshots", method = RequestMethod.POST)
	public Object calculateIndex(@RequestBody CalculateIndexJson json) {
		log.info("calculate index for {}", json);

		service.calculateIndex(json.getStart(), json.getEnd());

		return "{ \"result\" : \"ok\"}";
	}
	
	/**
	 * export data based on query parameters
	 */
	@RequestMapping(value = "/indexreport", method = RequestMethod.GET)
	public ModelAndView export(@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date endDate
			) {

		// load the snapshots from db
		final Pair<BIndex, List<BIndexSnap>> result = service.getIndexSnapshots(id, startDate, endDate);
	
		// prepare the query map for jasper report
		final Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", result.getRight());
		parameterMap.put("INDEX", result.getLeft());
		parameterMap.put("FROMDATE", startDate);
		parameterMap.put("TODATE", endDate);
		parameterMap.put("format", "pdf");
		// set the output file name (work only for custom extension)
		parameterMap.put("filename", "report_" + result.getLeft().getName());

		return new ModelAndView("indexReport", parameterMap);
	}
	
	/**
	 * export data based on query parameters
	 */
	@RequestMapping(value = "/indexcomparedatereport", method = RequestMethod.GET)
	public ModelAndView exportcomparedate(@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "start", required = true) @DateTimeFormat(pattern = Constant.ISO_8601) Date startDate,
			@RequestParam(value = "end", required = true) @DateTimeFormat(pattern = Constant.ISO_8601) Date endDate,
			@RequestParam(value = "comparestart", required = true) @DateTimeFormat(pattern = Constant.ISO_8601) Date compareStartDate,
			@RequestParam(value = "compareend", required = true) @DateTimeFormat(pattern = Constant.ISO_8601) Date compareEndDate
			) {

		// load the snapshots from db
		final Pair<BIndex, List<BIndexSnap>> list1 = service.getIndexSnapshots(id, startDate, endDate);
		final Pair<BIndex, List<BIndexSnap>> list2 = service.getIndexSnapshots(id, compareStartDate, compareEndDate);
	
		final List<BIndexSnap> listall = new ArrayList<BIndexSnap>(list1.getRight());
		listall.addAll(list2.getRight());
		
		// prepare the query map for jasper report
		final Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", listall);
		parameterMap.put("INDEX", list1.getLeft());
		parameterMap.put("FROMDATE", startDate);
		parameterMap.put("TODATE", endDate);
		parameterMap.put("COMPAREFROMDATE", compareStartDate);
		parameterMap.put("COMPARETODATE", compareEndDate);
		parameterMap.put("format", "pdf");
		// set the output file name (work only for custom extension)
		parameterMap.put("filename", "report_" + list1.getLeft().getName() + "_compare_date");

		return new ModelAndView("indexCompareDateReport", parameterMap);
	}
	
	/**
	 * export data based on query parameters
	 */
	@RequestMapping(value = "/compareindexreport", method = RequestMethod.GET)
	public ModelAndView exportcompare(@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "compare_id", required = true) Long compare_id,
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date endDate
			) {

		// load the snapshots from db
		final Pair<BIndex, List<BIndexSnap>> list1 = service.getIndexSnapshots(id, startDate, endDate);
		final Pair<BIndex, List<BIndexSnap>> list2 = service.getIndexSnapshots(compare_id, startDate, endDate);
	
		final Map<Date, BigDecimal> temp_map = new HashMap<Date, BigDecimal>();

		for (BIndexSnap snap : list1.getRight()) {
			temp_map.put(snap.getDatetime(), snap.getValue());
		}
		
		final List<BIndexCompare> list = new ArrayList<BIndexCompare>();
		
		for (BIndexSnap snap : list2.getRight()) {
			BIndexCompare index_compare = new BIndexCompare();
			
			index_compare.setDatetime(snap.getDatetime());
			index_compare.setIndex1_value(temp_map.get(snap.getDatetime()));
			index_compare.setIndex2_value(snap.getValue());
			
			list.add(index_compare);
		}
		
		// prepare the query map for jasper report
		final Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", list);
		parameterMap.put("INDEX", list1.getLeft());
		parameterMap.put("COMPAREINDEX", list2.getLeft());
		parameterMap.put("FROMDATE", startDate);
		parameterMap.put("TODATE", endDate);
		parameterMap.put("format", "pdf");
		// set the output file name (work only for custom extension)
		parameterMap.put("filename", "report_" + list1.getLeft().getName() + "_" + list2.getLeft().getName());

		return new ModelAndView("indexCompareReport", parameterMap);
	}
}
