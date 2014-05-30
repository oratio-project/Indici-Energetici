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

import it.proximacentauri.index.core.IndexService;
import it.proximacentauri.index.exception.ElementNotFoundException;
import it.proximacentauri.index.exception.IndexDependeciesException;
import it.proximacentauri.index.exception.MissingParamException;
import it.proximacentauri.index.model.ErrorBeanJson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {

	@Autowired
	protected IndexService service;

	/**
	 * Handle element not found
	 * 
	 * @param e
	 *            the missing exception
	 * @return error bean for elemnt not found
	 */
	@ExceptionHandler(ElementNotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public Object handleElementNotFoundException(ElementNotFoundException e) {
		return ErrorBeanJson.ELEMENT_NOT_FOUND;
	}

	/**
	 * Handle missing param exception
	 * 
	 * @param e
	 *            the missing exception
	 * @return error bean for missing pram
	 */
	@ExceptionHandler(MissingParamException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleMissingParamException(MissingParamException e) {
		return ErrorBeanJson.buildMissingParamBean(e.getParamName());
	}

	/**
	 * Handle invalid dependencies exception
	 * 
	 * @param e
	 *            the exception
	 * @return error bean for invalid formula
	 */
	@ExceptionHandler(IndexDependeciesException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Object handleDependenciesException(IndexDependeciesException e) {
		return ErrorBeanJson.buildInvalidDependenciesBean(e.getDependencyIndex());
	}
}
