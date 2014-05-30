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
package it.proximacentauri.index.core.impl;

import it.proximacentauri.index.core.BalanceAdapter;
import it.proximacentauri.index.core.IndexService;
import it.proximacentauri.index.dao.BIndexDao;
import it.proximacentauri.index.dao.BalanceDao;
import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexCategory;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.domain.BaseTime;
import it.proximacentauri.index.domain.Cadency;
import it.proximacentauri.index.exception.IndexDependeciesException;
import it.proximacentauri.index.exception.InvalidBalanceItemException;
import it.proximacentauri.index.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class IndexServiceImpl implements IndexService {

	final private Logger log = LoggerFactory.getLogger(IndexServiceImpl.class);

	private BIndexDao indexDao;
	private BalanceDao balanceDao;
	private BalanceAdapter adapter;

	public IndexServiceImpl(BIndexDao indexDao, BalanceDao balanceDao, BalanceAdapter adapter) {
		this.indexDao = indexDao;
		this.balanceDao = balanceDao;
		this.adapter = adapter;
	}

	@Override
	public List<BalanceItem> listBalanceItem() {
		return balanceDao.listBalanceItem();
	}

	@Override
	public List<Date> listSnapshotsTimestamps() {
		return balanceDao.listSnapshotsTimestamps();
	}

	@Override
	public List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshot(Date startDate, Date endDate) {
		return balanceDao.listBalanceSnapshots(startDate, endDate);
	}

	@Override
	public List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshot(Date datetime) {
		return balanceDao.listBalanceSnapshot(datetime);
	}

	@Override
	public List<BIndexCategory> listCategories() {
		return indexDao.listCategories();
	}

	@Override
	public Map<BIndex, List<BIndexRange>> listIndexDetails() {
		return indexDao.listIndex();
	}

	@Override
	public Map<BIndex, List<BIndexRange>> findIndexDetails(Long category) {
		return indexDao.findIndex(category);
	}

	@Override
	public List<Date> listSnapshotIndexTimestamps() {
		return indexDao.listSnapshotIndexTimestamps();
	}

	@Override
	public List<Integer> listDateTimestamps(BaseTime base, Integer year, Integer month, Integer day, Integer hour) {
		return indexDao.listDateTimestamps(base, year, month, day, hour);
	}

	@Override
	public Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> findIndexSnapshot(Date datetime, Integer category) {
		return indexDao.listIndexSnapshot(datetime, category);
	}

	@Override
	public Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> listIndexSnapshots(Integer category, Date startDate, Date endDate) {
		return indexDao.listIndexSnapshots(category, startDate, endDate);
	}
	
	@Override
	public Pair<BIndex, List<BIndexSnap>> getIndexSnapshots(long id, Date startDate, Date endDate) {
		return indexDao.getIndexSnapshots(id, startDate, endDate);
	}

	@Override
	@Transactional(readOnly = false)
	public void createSnapshots(Date start, Date end, Cadency cadency) {
		// get the list of balance items

		List<BalanceItem> items = balanceDao.listBalanceItem();

		List<BalanceSnapshot> snapshots = adapter.laodBalanceSnapshots(items, start, end, cadency);

		log.trace("Loaded form source {}", snapshots.size());

		// save the snapshots into database
		balanceDao.saveBalanceSnapshot(snapshots);
	}

	@Override
	@Transactional(readOnly = false)
	public void calculateIndex(Date start, Date end) {
		// create a index calculator
		IndexCalculator calculator = new IndexCalculator();

		// get list of all index
		final List<BIndex> indexList = indexDao.listIndexSimple();

		// get list of balance snaps
		final List<Date> dates = balanceDao.findSnapshotsTimestamps(start, end);

		for (Date date : dates) {
			log.info("start index calculation for snaps {}", date);

			// load balance
			final List<Pair<BalanceItem, BalanceSnapshot>> balanceSnapshots = balanceDao.listBalanceSnapshot(date);

			// convert to map, balance item code as key an value the balance
			// snapshot
			final Map<String, BalanceSnapshot> map = new HashMap<String, BalanceSnapshot>();

			for (Pair<BalanceItem, BalanceSnapshot> pair : balanceSnapshots) {
				map.put(pair.getLeft().getCode(), pair.getRight());
			}

			final List<BIndexSnap> listBIndexSnaps = new ArrayList<BIndexSnap>();

			// contain the result of index, used for index dependencies
			// calculation
			final Map<String, BigDecimal> mapDepIndex = new HashMap<String, BigDecimal>();

			// for each index calculate value
			for (BIndex index : indexList) {
				BIndexSnap snap = calculator.calculateIndex(index, map, mapDepIndex);
				// set timestamp
				if (snap != null) {
					snap.setDatetime(date);

					listBIndexSnaps.add(snap);

					// add new value for index depends
					mapDepIndex.put("IDX_" + index.getId(), snap.getValue());
				}
			}

			// last step save the index snaps
			indexDao.saveIndexSnapshot(listBIndexSnaps);

		}
	}

	@Override
	public BalanceItem findBalanceItem(String code) {
		log.info("try to find balance {}", code);
		return balanceDao.findBalanceItemByCode(code);
	}

	@Override
	public BIndex findIndex(Long id) {
		BIndex index = indexDao.findIndexById(id);

		// fake read
		if (index != null)
			index.getRange().size();

		return index;
	}

	@Override
	public BIndex saveIndex(BIndex index, Set<BIndexRange> rangeToSave, Long categoryId) {
		log.info("try to save index with name {}", index.getId());

		IndexCalculator calculator = new IndexCalculator();

		// formula try to parse
		final Set<String> variable = calculator.parseFormula(index.getFormula());

		for (String var : variable) {
			log.trace("Check variable {}", var);

			// first check in the balance item
			final BalanceItem item = balanceDao.findBalanceItemByCode(var);

			if (item != null)
				continue;

			// try to load index
			String split[] = var.split("_");

			if (split.length == 2) {

				// must have IDX name
				if (split[0].equals("IDX")) {
					// try to parse the id
					try {
						long id = Long.parseLong(split[1]);

						final BIndex depIndex = indexDao.findIndexById(id);

						if (depIndex != null)
							continue;
					} catch (NumberFormatException e) {
						// Ignore it, handler by InvalidBalanceItemException

					}
				}
			}

			// the parameter it's not found, raise ex
			throw new InvalidBalanceItemException(var);
		}

		// add the formula arguments
		index.setArguments(variable);

		// work on range saving procedure

		// create a copy of set for dont have current modification ecxeption
		final Set<BIndexRange> rangeOriginal = index.getRange();
		final Set<BIndexRange> rangeCopy = new HashSet<BIndexRange>(index.getRange());

		// first remove the elements for set (list all elements in the range)
		for (BIndexRange range : rangeCopy) {

			// add reference to index
			range.setBIndex(index);
			if (rangeToSave.contains(range)) {
				continue;
			}

			// the element to be deleted
			log.trace("Delete range element {}", range);
			rangeOriginal.remove(range);
			rangeToSave.remove(range);
		}

		// add/update elements
		for (BIndexRange range : rangeToSave) {
			if (rangeOriginal.contains(range)) {
				log.trace("Update the range {}", range);

				for (BIndexRange originalRange : rangeOriginal) {
					if (originalRange.equals(range)) {
						originalRange.setColor(range.getColor());
						originalRange.setInferior(range.getInferior());
						originalRange.setSuperior(range.getSuperior());
						break;
					}
				}

			} else {
				log.trace("Add the range {}", range);
				index.addRange(range);
			}
		}

		// handle category
		index.setCategory(null);
		if (categoryId != null) {
			// set the category
			BIndexCategory category = indexDao.findCategoryById(categoryId);

			if (category != null) {
				index.setCategory(category);
			}

		}

		// save the index
		indexDao.saveIndex(index);

		return index;
	}

	@Override
	public BalanceItem saveBalanceItem(BalanceItem item) {
		log.info("Try to save the balance item {}", item);
		return balanceDao.saveBalanceItem(item);
	}

	@Override
	public BIndexCategory findCategoryById(Long categoryId) {
		return indexDao.findCategoryById(categoryId);
	}

	@Override
	public BIndex deleteIndex(BIndex index) {
		log.info("delete the index {}", index);

		final String indexString = "IDX_" + index.getId();

		// check index dependences
		final List<BIndex> indexList = indexDao.listIndexSimple();

		for (BIndex indexToCheck : indexList) {

			if (indexToCheck.getArguments().contains(indexString)) {

				throw new IndexDependeciesException("IDX_" + indexToCheck.getId());
			}
		}

		indexDao.deleteIndex(index);
		return null;
	}

	@Override
	public BIndexCategory saveCategory(BIndexCategory category) {
		return indexDao.saveCategory(category);
	}

	@Override
	public BIndexCategory deleteCategory(BIndexCategory category) {
		return indexDao.deleteCategory(category);
	}

	@Override
	public BalanceItem deleteBalanceItem(BalanceItem item) {
		log.info("Delete the balance item {}", item.getCode());

		// check if the balance item it's used by and index
		final List<BIndex> indexList = indexDao.listIndexSimple();

		for (BIndex indexToCheck : indexList) {

			if (indexToCheck.getArguments().contains(item.getCode())) {

				throw new IndexDependeciesException(item.getCode());
			}
		}

		// delete the element
		balanceDao.deleteBalanceItem(item);

		return item;
	}
}
