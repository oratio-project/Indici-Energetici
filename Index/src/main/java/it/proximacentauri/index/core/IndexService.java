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

import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexCategory;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.domain.BaseTime;
import it.proximacentauri.index.domain.Cadency;
import it.proximacentauri.index.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IndexService {

	/**
	 * Gets the list of {@link BalanceItem}
	 * 
	 * @return the list of balance item
	 */
	public List<BalanceItem> listBalanceItem();

	/**
	 * Gets the list of {@link Date}
	 * 
	 * @return the list of date of balance snapshot
	 */
	public List<Date> listSnapshotsTimestamps();

	/**
	 * Gets the list of {@link Object[]}
	 * 
	 * @param startDate
	 * @param endDate
	 * 
	 * @return the list of all balance snapshot from date to date
	 */
	public List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshot(Date startDate, Date endDate);

	/**
	 * Gets the list of {@link Object[]}
	 * 
	 * @return the list of all balance snapshot
	 */
	public List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshot(Date datetime);

	/**
	 * Gets the list of {@link BIndexCategory}
	 * 
	 * @return the list of index categories
	 */
	public List<BIndexCategory> listCategories();

	/**
	 * Gets the list of {@link BIndex}
	 * 
	 * @return the list of index
	 */
	public Map<BIndex, List<BIndexRange>> listIndexDetails();

	/**
	 * Gets the list of {@link BIndex}
	 * 
	 * @param category
	 *            category id
	 * @return the list of index of a category
	 */
	public Map<BIndex, List<BIndexRange>> findIndexDetails(Long category);

	/**
	 * Gets the list of {@link Date}
	 * 
	 * @return the list of date of index snapshot
	 */
	public List<Date> listSnapshotIndexTimestamps();

	/**
	 * Gets the list of {@link Object[]}
	 * 
	 * @return the list of all index snapshot
	 */
	public Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> findIndexSnapshot(Date datetime, Integer category);

	/**
	 * Gets the list of {@link Object[]}
	 * 
	 * @param category
	 * @param endDate
	 * @param startDate
	 * 
	 * @return the list of all index snapshot
	 */
	public Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> listIndexSnapshots(Integer category, Date startDate, Date endDate);
	
	/**
	 * Gets the list of {@link Object[]}
	 * 
	 * @param id
	 * @param endDate
	 * @param startDate
	 * 
	 * @return the list of index snapshots
	 */
	public Pair<BIndex, List<BIndexSnap>> getIndexSnapshots(long id, Date startDate, Date endDate);

	/**
	 * Gets the list of all part of date of index snapshot
	 * 
	 * @param base
	 *            the part of date to return
	 * @param year
	 *            if want to select only the parts of dates of a year
	 * @param month
	 *            if want to select only the parts of dates of a month
	 * @param day
	 *            if want to select only the parts of dates of a day
	 * @param hour
	 *            if want to select only the parts of dates of a hour
	 * @return the list of all part of date of index snapshot
	 */
	public List<Integer> listDateTimestamps(BaseTime base, Integer year, Integer month, Integer day, Integer hour);

	/**
	 * Create a new snapshots from date to dates
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @param cadency
	 *            the snapshost cadency
	 */
	public void createSnapshots(Date start, Date end, Cadency cadency);

	/**
	 * Create a new snapshots from date to dates
	 * 
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 */
	public void calculateIndex(Date start, Date end);

	/**
	 * Find a balance item
	 * 
	 * @param code
	 *            the balance item code
	 * @return the found balance item
	 */
	public BalanceItem findBalanceItem(String code);

	/**
	 * Save a balance item
	 * 
	 * @param item
	 *            the code of balance item
	 * 
	 * @return the balance item
	 */
	public BalanceItem saveBalanceItem(BalanceItem item);

	/**
	 * Delete a balance item
	 * 
	 * @param item
	 *            the code of balance item
	 * 
	 * @return the balance item
	 */
	public BalanceItem deleteBalanceItem(BalanceItem item);

	/**
	 * Find a index
	 * 
	 * @param id
	 *            the id of index
	 * @return the index, null if non present
	 */
	public BIndex findIndex(Long id);

	/**
	 * Save a index and check the formula arguments
	 * 
	 * @param index
	 *            the index to save
	 * @param rangeList
	 *            the list of range
	 * @param categoryId
	 * @return the saved index
	 */
	public BIndex saveIndex(BIndex index, Set<BIndexRange> rangeList, Long categoryId);

	/**
	 * Delete a index
	 * 
	 * @param index
	 *            the index to delete
	 * @return the deleted index
	 */
	public BIndex deleteIndex(BIndex index);

	/**
	 * Save a a category to the database
	 * 
	 * @param category
	 *            the category to save
	 * @return the saved category
	 */
	public BIndexCategory saveCategory(BIndexCategory category);

	/**
	 * Find a category by id
	 * 
	 * @param categoryLong
	 *            the category id
	 * @return the index category
	 */
	public BIndexCategory findCategoryById(Long categoryLong);

	/**
	 * Delete a category to the database
	 * 
	 * @param category
	 *            the category to delete
	 * @return the deleted category
	 */
	public BIndexCategory deleteCategory(BIndexCategory category);
}
