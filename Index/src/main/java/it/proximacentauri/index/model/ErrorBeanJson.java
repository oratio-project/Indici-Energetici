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
package it.proximacentauri.index.model;

public final class ErrorBeanJson {

	public static final ErrorResponseJson ELEMENT_NOT_FOUND = new ErrorResponseJson(Code.ELEMENT_NOT_FOUND, Message.ELEMENT_NOT_FOUND);
	public static final ErrorResponseJson INVALID_FORMULA = new ErrorResponseJson(Code.INVALID_FORMULA, Message.INVALID_FORMULA);
	public static final ErrorResponseJson DEPENDENCIES_ERROR_CATEGORY = new ErrorResponseJson(Code.DEPENDENCIES_ERROR_CATEGORY,
			Message.DEPENDENCIES_ERROR_CATEGORY);

	static private final class Message {
		private static final String ELEMENT_NOT_FOUND = "Element not found";
		private static final String MISSING_PARAM = "Missing the param -%s-";
		private static final String INVALID_FORMULA = "The input formula was invalid";
		private static final String INVALID_BALANCE_ITEM = "The balance item -%s- not found";
		private static final String DEPENDENCIES_ERROR = "The index it's required by -%s-";
		private static final String DEPENDENCIES_ERROR_CATEGORY = "The category it's required by some index";

	}

	static private final class Code {
		private static final int ELEMENT_NOT_FOUND = 1;
		private static final int MISSING_PARAM = 2;
		private static final int INVALID_FORMULA = 3;
		private static final int INVALID_BALANCE_ITEM = 4;
		private static final int DEPENDENCIES_ERROR = 5;
		private static final int DEPENDENCIES_ERROR_CATEGORY = 6;
	}

	/**
	 * Create a new missing param bean
	 * 
	 * @param param
	 *            the param name
	 * @return the error bean
	 */
	static public ErrorResponseJson buildMissingParamBean(String param) {
		final String messageString = String.format(Message.MISSING_PARAM, param);

		return new ErrorResponseJson(Code.MISSING_PARAM, messageString);
	}

	/**
	 * Create a new invalid balance item for formula
	 * 
	 * @param code
	 *            the code name
	 * @return the error bean
	 */
	static public ErrorResponseJson buildInvalidBalanceItemBean(String code) {
		final String messageString = String.format(Message.INVALID_BALANCE_ITEM, code);

		return new ErrorResponseJson(Code.INVALID_BALANCE_ITEM, messageString);
	}

	/**
	 * Create a new error bean for dependencies exception
	 * 
	 * @param indexName
	 *            the name of index
	 * @return the error bean
	 */
	static public ErrorResponseJson buildInvalidDependenciesBean(String indexName) {
		final String messageString = String.format(Message.DEPENDENCIES_ERROR, indexName);

		return new ErrorResponseJson(Code.DEPENDENCIES_ERROR, messageString);
	}
}
