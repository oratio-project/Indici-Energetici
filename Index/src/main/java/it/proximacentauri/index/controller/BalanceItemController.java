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

import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.exception.ElementNotFoundException;
import it.proximacentauri.index.exception.MissingParamException;
import it.proximacentauri.index.model.BalanceItemJson;
import it.proximacentauri.index.util.JSonModelUtil;
import it.proximacentauri.index.util.StringUtil;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BalanceItemController extends BaseController {
	final private Logger log = LoggerFactory.getLogger(BalanceItemController.class);

	@ResponseBody
	@RequestMapping(value = "balanceitems", method = RequestMethod.GET)
	public List<BalanceItemJson> getBalanceItems() {
		log.info("Get list of all balance item");

		final List<BalanceItem> list = service.listBalanceItem();

		return JSonModelUtil.convertToBalanceItemJson(list);
	}

	@ResponseBody
	@RequestMapping(value = "balanceitems", method = RequestMethod.POST)
	public BalanceItemJson createBalanceItem(@RequestBody BalanceItemJson json) {

		// first the validation
		if (!StringUtil.isValid(json.getCode()))
			throw new MissingParamException("code");

		BalanceItem item = service.findBalanceItem(json.getCode());

		if (item == null) {
			// create the balance item
			item = new BalanceItem();
		}

		// update the data
		item.setName(json.getName());
		item.setCode(json.getCode());
		item.setDescription(json.getDescription());
		item.setUnit(json.getUnit());

		item = service.saveBalanceItem(item);

		return JSonModelUtil.convertToBalanceItemJson(item);
	}

	@ResponseBody
	@RequestMapping(value = "balanceitems/{code}", method = RequestMethod.GET)
	public BalanceItemJson getBalanceItem(@PathVariable String code) {
		log.info("get the balance item detail for {}", code);

		final BalanceItem item = service.findBalanceItem(code);

		if (item == null) {
			log.warn("Balance item {} not found", code);
			throw new ElementNotFoundException();
		} else
			return JSonModelUtil.convertToBalanceItemJson(item);
	}

	@ResponseBody
	@RequestMapping(value = "balanceitems/{code}", method = RequestMethod.PUT)
	public BalanceItemJson putBalanceItem(@PathVariable String code, @RequestBody BalanceItemJson json) {
		log.info("update the balance item detail for {}", code);

		BalanceItem item = service.findBalanceItem(code);

		if (item == null) {
			log.warn("Balance item {} not found", code);
			throw new ElementNotFoundException();
		}
		// update the balance item
		item.setName(json.getName());
		item.setDescription(json.getDescription());
		item.setUnit(json.getUnit());
		item = service.saveBalanceItem(item);
		return JSonModelUtil.convertToBalanceItemJson(item);

	}

	@ResponseBody
	@RequestMapping(value = "balanceitems/{code}", method = RequestMethod.DELETE)
	public BalanceItemJson deleteBalanceItem(@PathVariable String code) {
		log.info("delete the balance item detail for {}", code);

		BalanceItem item = service.findBalanceItem(code);

		if (item == null) {
			log.warn("Balance item {} not found", code);
			throw new ElementNotFoundException();
		}

		// delete the balance item
		service.deleteBalanceItem(item);

		return JSonModelUtil.convertToBalanceItemJson(item);
	}

}
