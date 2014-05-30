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
package it.proximacentauri.index.util;

/**
 * Simple string utils class
 * 
 * @author proximacentauri
 * 
 */
public abstract class StringUtil {

	/**
	 * Check if a string is valid
	 * 
	 * @param string
	 *            the string to check
	 * @return true for valid, false otherwise
	 */
	public static boolean isValid(final String string) {
		if (string == null)
			return false;
		if (string.isEmpty())
			return false;
		return true;
	}
}
