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
package it.proximacentauri.index.exception;

/**
 * Exception for handle a missing parameter for a request
 * 
 * @author proximacentauri
 * 
 */
public class MissingParamException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1819950010032610846L;

	// the param name
	private String paramName = null;

	public MissingParamException(String paramName) {
		super("Missing param " + paramName);
		this.paramName = paramName;
	}

	public String getParamName() {
		return paramName;
	}
}
