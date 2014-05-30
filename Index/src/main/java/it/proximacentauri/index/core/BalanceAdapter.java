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
package it.proximacentauri.index.core;

import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.domain.Cadency;

import java.util.Date;
import java.util.List;

/**
 * Interface class for load balance elements form server side
 * 
 * @author proximacentauri
 * 
 */
public interface BalanceAdapter {

	/**
	 * Load balance snapshot from date to date at given cadency
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @param cadency
	 *            the cadency
	 * @return the list of balance items
	 */
	List<BalanceSnapshot> laodBalanceSnapshots(List<BalanceItem> items, Date start, Date end, Cadency cadency);
}
