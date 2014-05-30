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
import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.model.BalanceSnapshotDetailJson;
import it.proximacentauri.index.model.CreateBalanceSnapshotJson;
import it.proximacentauri.index.model.DatetimeJson;
import it.proximacentauri.index.util.JSonModelUtil;
import it.proximacentauri.index.util.Pair;

import java.util.Date;
import java.util.List;

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

@Controller
public class BalanceSnapshotController extends BaseController {
	final private Logger log = LoggerFactory.getLogger(BalanceSnapshotController.class);

	@ResponseBody
	@RequestMapping(value = "balancesnapdates", method = RequestMethod.GET)
	public List<DatetimeJson> getBalanceSnapshot() {
		log.info("Get list of all balance snapshot");

		final List<Date> list = service.listSnapshotsTimestamps();

		return JSonModelUtil.convertToDatetimeJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "balancesnapshots", method = RequestMethod.GET)
	public List<BalanceSnapshotDetailJson> getBalanceSnapshotsDetails(
			@RequestParam(value = "start", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date startDate,
			@RequestParam(value = "end", required = false) @DateTimeFormat(pattern = Constant.ISO_8601) Date endDate) {
		log.info("Get detail of balance snapshot from {} to {}", startDate, endDate);

		final List<Pair<BalanceItem, BalanceSnapshot>> list = service.listBalanceSnapshot(startDate, endDate);

		return JSonModelUtil.convertToBalanceSnapshotDetailJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "balancesnapshots/{datetime}", method = RequestMethod.GET)
	public List<BalanceSnapshotDetailJson> getBalanceSnapshotDetail(
			@PathVariable("datetime") @DateTimeFormat(pattern = Constant.ISO_8601) Date datetime) {
		log.info("Get detail of balance snapshot at timestamp {}", datetime);

		final List<Pair<BalanceItem, BalanceSnapshot>> list = service.listBalanceSnapshot(datetime);

		return JSonModelUtil.convertToBalanceSnapshotDetailJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "balancesnapshots", method = RequestMethod.POST)
	public Object createSnaphost(@RequestBody CreateBalanceSnapshotJson bean) {
		log.info("start creation of snapshot {}", bean);

		service.createSnapshots(bean.getStart(), bean.getEnd(), bean.getCadency());

		return "{ \"result\" : \"ok\"}";
	}
}
