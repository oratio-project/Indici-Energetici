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

import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexCategory;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.model.BalanceItemJson;
import it.proximacentauri.index.model.BalanceSnapshotDetailJson;
import it.proximacentauri.index.model.CategoryJson;
import it.proximacentauri.index.model.DatePartJson;
import it.proximacentauri.index.model.DatetimeJson;
import it.proximacentauri.index.model.IndexJson;
import it.proximacentauri.index.model.IndexSnapshotJson;
import it.proximacentauri.index.model.RangeJson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class JSonModelUtil {

	/**
	 * Convert a list of {@link BIndexCategory} to {@link CategoryJson}
	 * 
	 * @param list
	 *            the source list
	 * @return the converted list
	 */
	public static List<CategoryJson> convertToCategoryJson(final List<BIndexCategory> list) {
		final List<CategoryJson> result = new ArrayList<CategoryJson>();

		for (BIndexCategory indexCategory : list) {
			result.add(convertToCategoryJson(indexCategory));
		}
		return result;

	}

	/**
	 * Convert a list of {@link BIndexCategory} to {@link CategoryJson}
	 * 
	 * @param list
	 *            the source list
	 * @return the converted list
	 */
	public static CategoryJson convertToCategoryJson(final BIndexCategory category) {
		final CategoryJson categoryJson = new CategoryJson();
		categoryJson.setName(category.getName());
		categoryJson.setId(category.getId());
		return categoryJson;
	}

	/**
	 * Convert a list of {@link Date} to {@link DatetimeJson}
	 * 
	 * @param list
	 *            the list of date
	 * @return the converted list
	 */
	public static List<DatetimeJson> convertToDatetimeJson(final List<Date> list) {
		final List<DatetimeJson> result = new ArrayList<DatetimeJson>();

		for (Date timestamp : list) {
			final DatetimeJson balanceJson = new DatetimeJson();

			balanceJson.setDatetime(timestamp);
			result.add(balanceJson);
		}

		return result;

	}

	/**
	 * Convert a list of {@link Integer} to {@link DatePartJson}
	 * 
	 * @param list
	 *            the list of date part
	 * @return the converted list
	 */
	public static List<DatePartJson> convertToDatePartJson(List<Integer> list) {
		final List<DatePartJson> result = new ArrayList<DatePartJson>();

		for (Integer partDate : list) {
			final DatePartJson datePartJson = new DatePartJson();

			datePartJson.setDatepart(partDate);
			result.add(datePartJson);
		}

		return result;
	}

	public static List<BalanceSnapshotDetailJson> convertToBalanceSnapshotDetailJson(final List<Pair<BalanceItem, BalanceSnapshot>> list) {
		final List<BalanceSnapshotDetailJson> result = new ArrayList<BalanceSnapshotDetailJson>();

		for (Pair<BalanceItem, BalanceSnapshot> element : list) {
			final BalanceSnapshotDetailJson balanceSnapshotDetailJson = new BalanceSnapshotDetailJson();

			// first element is {@link BalanceItem}
			final BalanceItem item = (BalanceItem) element.getLeft();

			// second is {@link BalanceSnapshot}
			final BalanceSnapshot snapshot = (BalanceSnapshot) element.getRight();

			// copy data
			balanceSnapshotDetailJson.setCode(item.getCode());
			balanceSnapshotDetailJson.setName(item.getName());
			balanceSnapshotDetailJson.setDatetime(snapshot.getDatetime());
			balanceSnapshotDetailJson.setDescription(item.getDescription());
			balanceSnapshotDetailJson.setValue(snapshot.getValue());
			balanceSnapshotDetailJson.setUnit(item.getUnit());

			result.add(balanceSnapshotDetailJson);
		}

		return result;

	}

	/**
	 * Convert a list of {@link BalanceItem} to {@link BalanceItemJson}
	 * 
	 * @param list
	 *            the list of date
	 * @return the converted list
	 */
	public static List<BalanceItemJson> convertToBalanceItemJson(List<BalanceItem> list) {
		final List<BalanceItemJson> result = new ArrayList<BalanceItemJson>();

		for (BalanceItem item : list) {
			result.add(convertToBalanceItemJson(item));
		}
		return result;
	}

	/**
	 * Convert a {@link BalanceItem} to {@link BalanceItemJson}
	 * 
	 * @param item
	 *            the item to convert
	 * @return the item to convert
	 */
	public static BalanceItemJson convertToBalanceItemJson(BalanceItem item) {

		final BalanceItemJson balanceItemJson = new BalanceItemJson();
		balanceItemJson.setName(item.getName());
		balanceItemJson.setDescription(item.getDescription());
		balanceItemJson.setUnit(item.getUnit());
		balanceItemJson.setCode(item.getCode());

		return balanceItemJson;
	}

	/**
	 * Convert a list of {@link BIndex} with {@link BIndexRange} to
	 * {@link IndexJson}
	 * 
	 * @param list
	 *            the list of {@link BIndex}
	 * @return the converted list
	 */
	public static List<IndexJson> convertDetailsToIndexJson(Map<BIndex, List<BIndexRange>> map) {
		final List<IndexJson> result = new ArrayList<IndexJson>();

		for (BIndex bindex : map.keySet()) {
			final IndexJson index = new IndexJson();
			index.setName(bindex.getName());
			index.setDescription(bindex.getDescription());
			index.setFormula(bindex.getFormula());
			index.setUnit(bindex.getUnit());
			index.setId(bindex.getId());
			index.setArguments(bindex.getArguments());

			index.setRange(convertToRangeJson(map.get(bindex)));

			BIndexCategory category = bindex.getCategory();

			if (category != null) {
				CategoryJson categoryJson = new CategoryJson();
				categoryJson.setId(category.getId());
				categoryJson.setName(category.getName());
				index.setCategory(categoryJson);
			}

			result.add(index);
		}

		return result;
	}

	/**
	 * Convert a {@link BIndex} with {@link BIndexRange} to {@link IndexJson}
	 * 
	 * @param bsindex
	 *            the index to convert
	 * @return the converted index
	 */
	public static IndexJson convertDetailsToIndexJson(BIndex bindex) {
		final IndexJson indexJson = new IndexJson();
		indexJson.setName(bindex.getName());
		indexJson.setDescription(bindex.getDescription());
		indexJson.setFormula(bindex.getFormula());
		indexJson.setUnit(bindex.getUnit());
		indexJson.setId(bindex.getId());
		indexJson.setArguments(bindex.getArguments());

		indexJson.setRange(convertToRangeJson(new ArrayList<BIndexRange>(bindex.getRange())));

		// convert category
		BIndexCategory category = bindex.getCategory();

		if (category != null) {
			CategoryJson categoryJson = new CategoryJson();
			categoryJson.setId(category.getId());
			categoryJson.setName(category.getName());
			indexJson.setCategory(categoryJson);
		}

		return indexJson;
	}

	/**
	 * Convert a list of {@link BIndex} with {@link BIndexRange} and
	 * {@link BIndexSnap} to {@link IndexJson}
	 * 
	 * @param list
	 *            the list of {@link BIndex}
	 * @return the converted list
	 */
	public static List<IndexJson> convertToIndexJson(Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> map) {
		final List<IndexJson> result = new ArrayList<IndexJson>();

		for (BIndex bindex : map.keySet()) {
			final IndexJson index = new IndexJson();
			index.setName(bindex.getName());
			index.setDescription(bindex.getDescription());
			index.setFormula(bindex.getFormula());
			index.setUnit(bindex.getUnit());
			index.setId(bindex.getId());

			index.setRange(convertToRangeJson(map.get(bindex).getLeft()));
			index.setSnap(convertToIndexSnapshotJson(map.get(bindex).getRight(), bindex));

			result.add(index);
		}

		return result;
	}

	/**
	 * Convert a list of {@link BIndexSnap} to {@link IndexSnapshotJson}
	 * 
	 * @param list
	 *            the source list
	 * @return the converted list
	 */
	public static List<IndexSnapshotJson> convertToIndexSnapshotJson(List<BIndexSnap> list, BIndex index) {
		final List<IndexSnapshotJson> result = new ArrayList<IndexSnapshotJson>();

		for (BIndexSnap snap : list) {
			final IndexSnapshotJson snapshotIndexJson = new IndexSnapshotJson();

			// copy data
			// snapshotIndexJson.setId(snap.getBIndex().getId());
			snapshotIndexJson.setDatetime(snap.getDatetime());
			snapshotIndexJson.setFormula(snap.getFormula());
			snapshotIndexJson.setValue(snap.getValue());
			snapshotIndexJson.setUnit(index.getUnit());
			snapshotIndexJson.setArguments(snap.getArguments());

			result.add(snapshotIndexJson);
		}
		return result;
	}

	/**
	 * Convert a list of {@link BIndexRange} to {@link RangeJson}
	 * 
	 * @param list
	 *            the list of BIndexRange
	 * @return the converted list
	 */
	public static List<RangeJson> convertToRangeJson(List<BIndexRange> list) {
		final List<RangeJson> result = new ArrayList<RangeJson>();
		for (BIndexRange bindexrange : list) {
			final RangeJson range = new RangeJson();
			range.setColor(bindexrange.getColor());
			range.setInferior(bindexrange.getInferior());
			range.setSuperior(bindexrange.getSuperior());

			result.add(range);
		}
		return result;
	}
}
