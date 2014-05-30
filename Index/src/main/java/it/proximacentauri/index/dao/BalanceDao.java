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
package it.proximacentauri.index.dao;

import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.util.Pair;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BalanceDao {

	/**
	 * Return the list of {@link BalanceItem} and {@link BalanceSnapshot} from a
	 * given {@link Timestamp} to a given {@link Timestamp}
	 * 
	 * @param startDate
	 *            the timestamp from which start to find snapshots
	 * @param endDate
	 *            the timestamp from which end to find snapshots
	 * @return the list of elements
	 */
	List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshots(Date startDate, Date endDate);

	/**
	 * Return the list of {@link BalanceItem} and {@link BalanceSnapshot} for a
	 * given {@link Timestamp}
	 * 
	 * @param timestamp
	 *            the timestamp required
	 * @return the list of elements
	 */
	List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshot(Date timestamp);

	/**
	 * Get the list of all {@link BalanceSnapshot}
	 * 
	 * @return the list of {@link BalanceSnapshot}
	 */
	List<Date> listSnapshotsTimestamps();

	/**
	 * Save the list of balance snapshots
	 * 
	 * @param list
	 */
	void saveBalanceSnapshot(List<BalanceSnapshot> list);

	/**
	 * Find the {@link BalanceSnapshot} from date to date
	 * 
	 * @return the list of {@link BalanceSnapshot}
	 */
	List<Date> findSnapshotsTimestamps(Date start, Date end);

	/**
	 * Get the list of all {@link BalanceItem}
	 * 
	 * @return the list of {@link BalanceItem}
	 */
	List<BalanceItem> listBalanceItem();

	/**
	 * Find a balance item by code
	 * 
	 * @param code
	 *            the item code
	 * @return the balance item
	 */
	BalanceItem findBalanceItemByCode(String code);

	/**
	 * Save a balance item
	 * 
	 * @param item
	 *            the item to save
	 * @return the saved item
	 */
	BalanceItem saveBalanceItem(BalanceItem item);

	/**
	 * Delete the balance item
	 * 
	 * @param item
	 *            the item to save
	 * @return the deleted item
	 */
	BalanceItem deleteBalanceItem(BalanceItem item);
}
