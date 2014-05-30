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

import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.exception.ElementNotFoundException;
import it.proximacentauri.index.exception.InvalidBalanceItemException;
import it.proximacentauri.index.exception.MissingParamException;
import it.proximacentauri.index.model.ErrorBeanJson;
import it.proximacentauri.index.model.IndexJson;
import it.proximacentauri.index.model.RangeJson;
import it.proximacentauri.index.util.JSonModelUtil;
import it.proximacentauri.index.util.StringUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/indices")
public class IndexController extends BaseController {
	final private Logger log = LoggerFactory.getLogger(IndexController.class);

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<IndexJson> listIndex(@RequestParam(value = "category", required = false) Long category) {
		log.info("Get list of all index detail of category {}", category);

		Map<BIndex, List<BIndexRange>> map = null;

		if (category != null)
			map = service.findIndexDetails(category);
		else
			map = service.listIndexDetails();

		return JSonModelUtil.convertDetailsToIndexJson(map);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public IndexJson postIndex(@RequestBody IndexJson json) {

		BIndex index = null;

		// check if the id, was defined
		if (json.getId() == null) {
			log.info("Create a new index {}", json.getName());

			// create e new index
			index = new BIndex();
		} else {
			// update the index
			index = service.findIndex(json.getId());

			if (index == null)
				throw new ElementNotFoundException();

			log.info("Update the index  {}", json.getName());
		}

		// validation step
		validateJsonAndSave(json, index);

		return JSonModelUtil.convertDetailsToIndexJson(index);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Object getIndex(@PathVariable(value = "id") long id) {
		log.info("get index by id {}", id);

		// find the index
		final BIndex index = service.findIndex(id);

		if (index == null)
			throw new ElementNotFoundException();

		return JSonModelUtil.convertDetailsToIndexJson(index);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Object updateIndex(@PathVariable(value = "id") long id, @RequestBody IndexJson json) {
		log.info("update the index with id {} and data {}", id, json);

		// find the index
		final BIndex index = service.findIndex(id);

		if (index == null)
			throw new ElementNotFoundException();

		// validation step
		validateJsonAndSave(json, index);

		return JSonModelUtil.convertDetailsToIndexJson(index);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteIndex(@PathVariable(value = "id") long id) {
		log.info("delete the index with id {}", id);

		// find the index
		final BIndex index = service.findIndex(id);

		if (index == null)
			throw new ElementNotFoundException();

		// delete the index
		service.deleteIndex(index);
	}

	/**
	 * Validate the input json, update the index and save it
	 * 
	 * @param json
	 *            the input json
	 * @param index
	 *            the reference index
	 * @return the saved index
	 */
	private BIndex validateJsonAndSave(IndexJson json, BIndex index) {
		// the required field are name and formula
		if (!StringUtil.isValid(json.getName())) {
			throw new MissingParamException("name");
		}

		if (!StringUtil.isValid(json.getFormula())) {
			throw new MissingParamException("formula");
		}

		// copy the data
		index.setName(json.getName());
		index.setDescription(json.getDescription());
		index.setFormula(json.getFormula());
		index.setUnit(json.getUnit());

		// work on range
		final Set<BIndexRange> list = new HashSet<BIndexRange>();
		if (json.getRange() != null) {
			for (RangeJson rangeJson : json.getRange()) {
				log.trace("{}", rangeJson);

				if (!StringUtil.isValid(rangeJson.getColor().toString())) {
					throw new MissingParamException("color");
				}

				if (rangeJson.getInferior() == null && rangeJson.getSuperior() == null) {
					throw new MissingParamException("inferior or superior");
				}

				final BIndexRange range = new BIndexRange();
				range.setColor(rangeJson.getColor());
				range.setSuperior(rangeJson.getSuperior());
				range.setInferior(rangeJson.getInferior());
				list.add(range);
			}
		}

		// work on category
		Long categoryId = null;
		if (json.getCategory() != null && json.getCategory().getId() != null) {
			categoryId = json.getCategory().getId();
		}

		service.saveIndex(index, list, categoryId);

		return index;
	}

	/**
	 * Handle invalid input formula
	 * 
	 * @param e
	 *            the exception
	 * @return error bean for invalid formula
	 */
	@ExceptionHandler(org.mbertoli.jfep.ParseError.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleParseError(org.mbertoli.jfep.ParseError e) {
		return ErrorBeanJson.INVALID_FORMULA;
	}

	/**
	 * Handle invalid input formula
	 * 
	 * @param e
	 *            the exception
	 * @return error bean for invalid formula
	 */
	@ExceptionHandler(InvalidBalanceItemException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleMissingParamException(InvalidBalanceItemException e) {
		return ErrorBeanJson.buildInvalidBalanceItemBean(e.getCode());
	}
}
