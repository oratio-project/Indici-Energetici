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

import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexCategory;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BaseTime;
import it.proximacentauri.index.util.Pair;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BIndexDao {

	/**
	 * Get all categories {@link BIndexCategory}
	 * 
	 * @return the list of categories
	 */
	List<BIndexCategory> listCategories();

	/**
	 * Get all index defined, for a given timestamp and a given category
	 * 
	 * @param timestamp
	 * @param categoryId
	 * @return the list of elements
	 */
	Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> listIndexSnapshot(Date timestamp, Integer categoryId);

	/**
	 * Get all snapshot {@link Timestamp}
	 * 
	 * @return the list of timestamp
	 */
	List<Date> listSnapshotIndexTimestamps();

	/**
	 * Get all index {@link BIndex}
	 * 
	 * @return the list of index
	 */
	Map<BIndex, List<BIndexRange>> listIndex();

	/**
	 * Get all index {@link BIndex} of a category
	 * 
	 * @param categoryId
	 * @return the list of index of a category
	 */
	Map<BIndex, List<BIndexRange>> findIndex(Long category);

	/**
	 * Get all index {@link BIndex} with {@link BIndexRange} and
	 * {@link BIndexSnap}
	 * 
	 * @param category
	 * @param endDate
	 * @param startDate
	 * 
	 * @return the list of index with ranges and snapshots
	 */
	Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> listIndexSnapshots(Integer category, Date startDate, Date endDate);
	
	/**
	 * Get {@link BIndexSnap} of {@link BIndex}
	 * 
	 * @param id
	 * 			id of {@link BIndex}
	 * @param endDate
	 * @param startDate
	 * 
	 * @return the snapshots of index
	 */
	Pair<BIndex, List<BIndexSnap>> getIndexSnapshots(long id, Date startDate, Date endDate);
	
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
	List<Integer> listDateTimestamps(BaseTime base, Integer year, Integer month, Integer day, Integer hour);

	/**
	 * Get all index {@link BIndex}
	 * 
	 * @return the list of index
	 */
	List<BIndex> listIndexSimple();

	/**
	 * Save the list of balance snapshots
	 * 
	 * @param list
	 */
	void saveIndexSnapshot(List<BIndexSnap> list);

	/**
	 * Find a index by id
	 * 
	 * @param id
	 *            the id of index
	 * @return the index, null if non present
	 */
	public BIndex findIndexById(Long id);

	/**
	 * Save a new index
	 * 
	 * @param index
	 *            the index to save
	 * @return the saved index
	 */
	public BIndex saveIndex(BIndex index);

	/**
	 * Delete a index
	 * 
	 * @param index
	 *            the index to delete
	 */
	public void deleteIndex(BIndex index);

	/**
	 * Find a index by category id
	 * 
	 * @param categoryId
	 *            the category id
	 * @return the index category
	 */
	public BIndexCategory findCategoryById(Long categoryId);

	/**
	 * Save a a category to the database
	 * 
	 * @param category
	 *            the category to save
	 * @return the saved category
	 */
	public BIndexCategory saveCategory(BIndexCategory category);

	/**
	 * Delete a category to the database
	 * 
	 * @param category
	 *            the category to delete
	 * @return the deleted category
	 */
	public BIndexCategory deleteCategory(BIndexCategory category);
}
