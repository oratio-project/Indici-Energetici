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
 * Exception for handle a dependencies error during delete
 * 
 * @author proximacentauri
 * 
 */
public class IndexDependeciesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4576065038253891538L;
	// the Dependency
	private String dependencyIndex = null;

	public IndexDependeciesException(String dependencyIndex) {
		super("Dependency exception param " + dependencyIndex);
		this.dependencyIndex = dependencyIndex;
	}

	public String getDependencyIndex() {
		return dependencyIndex;
	}

}
