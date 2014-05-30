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

import it.proximacentauri.index.domain.BIndexCategory;
import it.proximacentauri.index.exception.ElementNotFoundException;
import it.proximacentauri.index.model.CategoryJson;
import it.proximacentauri.index.model.ErrorBeanJson;
import it.proximacentauri.index.util.JSonModelUtil;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/categories")
public class CategoryController extends BaseController {
	final private Logger log = LoggerFactory.getLogger(CategoryController.class);

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public List<CategoryJson> getCategories() {
		log.info("Get list of categories");

		final List<BIndexCategory> categoriesList = service.listCategories();
		return JSonModelUtil.convertToCategoryJson(categoriesList);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public CategoryJson getCategories(@RequestBody CategoryJson json) {
		log.info("Create or update a category {}", json.getName());

		BIndexCategory category = service.findCategoryById((json.getId() == null ? -1 : json.getId()));

		if (category == null) {
			category = new BIndexCategory();
		}

		category.setName(json.getName());

		service.saveCategory(category);

		return JSonModelUtil.convertToCategoryJson(category);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CategoryJson getCategory(@PathVariable(value = "id") long id) {
		log.info("Get detail of category {}", id);

		final BIndexCategory category = service.findCategoryById(id);

		if (category == null)
			throw new ElementNotFoundException();

		return JSonModelUtil.convertToCategoryJson(category);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public CategoryJson updateCategory(@PathVariable(value = "id") long id, @RequestBody CategoryJson json) {
		log.info("Update category {}", id);

		final BIndexCategory category = service.findCategoryById(id);

		if (category == null)
			throw new ElementNotFoundException();

		category.setName(json.getName());

		service.saveCategory(category);

		return JSonModelUtil.convertToCategoryJson(category);
	}

	@ResponseBody
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteCategory(@PathVariable(value = "id") long id) {
		log.info("Delete the category {}", id);

		final BIndexCategory category = service.findCategoryById(id);

		if (category == null)
			throw new ElementNotFoundException();

		service.deleteCategory(category);
	}

	/**
	 * Handle missing delete category error in index dep
	 * 
	 * @param e
	 *            the exception
	 * @return error bean
	 */
	@ExceptionHandler(org.springframework.orm.jpa.JpaSystemException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleMissingParamException(org.springframework.orm.jpa.JpaSystemException e) {
		return ErrorBeanJson.DEPENDENCIES_ERROR_CATEGORY;
	}
}
