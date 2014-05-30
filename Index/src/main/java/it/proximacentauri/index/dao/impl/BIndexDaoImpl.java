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
package it.proximacentauri.index.dao.impl;

import it.proximacentauri.index.dao.BIndexDao;
import it.proximacentauri.index.domain.BIndex;
import it.proximacentauri.index.domain.BIndexCategory;
import it.proximacentauri.index.domain.BIndexCategory_;
import it.proximacentauri.index.domain.BIndexRange;
import it.proximacentauri.index.domain.BIndexSnap;
import it.proximacentauri.index.domain.BIndexSnap_;
import it.proximacentauri.index.domain.BIndex_;
import it.proximacentauri.index.domain.BaseTime;
import it.proximacentauri.index.util.Pair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BIndexDaoImpl extends BaseDao implements BIndexDao {

	final private Logger log = LoggerFactory.getLogger(BIndexDaoImpl.class);

	@Override
	@SuppressWarnings("unchecked")
	public List<BIndexCategory> listCategories() {
		log.info("get all index categories");
		return em.createQuery("from BIndexCategory").getResultList();
	}

	@Override
	public Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> listIndexSnapshot(Date timestamp, Integer categoryId) {
		log.info("get index and snapshot for category {} at date {}", categoryId, timestamp);

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

		// build join
		final Root<BIndex> bindex = query.from(BIndex.class);
		final Join<BIndex, BIndexSnap> rangeJoin = bindex.join("range", JoinType.LEFT);
		final Join<BIndex, BIndexSnap> snapsJoin = bindex.join("snaps", JoinType.LEFT);

		// build where, on timestamp
		Predicate predicate = cb.equal(snapsJoin.get(BIndexSnap_.datetime), timestamp);

		// Build category predicate
		if (categoryId != null) {
			final Join<BIndex, BIndexCategory> categoryJoin = bindex.join("category");
			predicate = cb.and(predicate, cb.equal(categoryJoin.get(BIndexCategory_.id), categoryId));
		}

		query.where(predicate);
		query.multiselect(bindex, rangeJoin, snapsJoin);

		final List<Object[]> list = em.createQuery(query).getResultList();

		final Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> indices = new HashMap<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>>();

		for (Object[] objects : list) {
			BIndex index = (BIndex) objects[0];

			List<BIndexRange> rangeList = null;
			List<BIndexSnap> snapList = null;

			if (indices.containsKey(index)) {
				rangeList = indices.get(index).getLeft();
				snapList = indices.get(index).getRight();
			} else {
				rangeList = new ArrayList<BIndexRange>();
				snapList = new ArrayList<BIndexSnap>();
				indices.put(index, new Pair<List<BIndexRange>, List<BIndexSnap>>(rangeList, snapList));
			}
			if (objects[1] != null) {
				BIndexRange range = (BIndexRange) objects[1];
				if (!rangeList.contains(range)) {
					rangeList.add(range);
				}
			}
			if (objects[2] != null) {
				BIndexSnap snap = (BIndexSnap) objects[2];
				if (!snapList.contains(snap)) {
					snapList.add(snap);
				}
			}
		}
		return indices;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> listSnapshotIndexTimestamps() {
		log.info("get all date of index snapshot");
		return (List<Date>) em.createQuery("SELECT DISTINCT(datetime) FROM BIndexSnap").getResultList();
	}

	@Override
	public List<Integer> listDateTimestamps(BaseTime base, Integer year, Integer month, Integer day, Integer hour) {
		log.info("Get list of all {} of snapshot index dates where dates is " + "year: {}, month: {}, day: {}, hour: {}", base, year,
				month, day, hour);

		Calendar startCal = null;
		Calendar endCal = null;
		if (year != null) {
			startCal = Calendar.getInstance();
			endCal = Calendar.getInstance();
			startCal.set(year, 0, 1, 0, 0);
			endCal.set(year, 11, 31, 23, 59);
			startCal.set(Calendar.SECOND, 0);
			startCal.set(Calendar.MILLISECOND, 0);
			endCal.set(Calendar.SECOND, 59);
			endCal.set(Calendar.MILLISECOND, 999);
			if (month != null) {
				startCal.set(Calendar.MONTH, month - 1);
				endCal.set(Calendar.MONTH, month - 1);
				endCal.set(Calendar.DAY_OF_MONTH, startCal.getActualMaximum(Calendar.DAY_OF_MONTH));
				if (day != null) {
					startCal.set(Calendar.DAY_OF_MONTH, day);
					endCal.set(Calendar.DAY_OF_MONTH, day);
					if (hour != null) {
						startCal.set(Calendar.HOUR_OF_DAY, hour);
						endCal.set(Calendar.HOUR_OF_DAY, hour);
					}
				}
			}
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Date> query = cb.createQuery(Date.class);

		Root<BIndexSnap> snapsRoot = query.from(BIndexSnap.class);
		query.select(snapsRoot.get(BIndexSnap_.datetime));
		query.distinct(true);

		Predicate predicate = null;

		// Build start date predicate
		if (startCal != null) {
			predicate = cb.greaterThanOrEqualTo(snapsRoot.get(BIndexSnap_.datetime), startCal.getTime());
		}
		// Build end date predicate
		if (endCal != null) {
			if (predicate != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(snapsRoot.get(BIndexSnap_.datetime), endCal.getTime()));
			} else {
				predicate = cb.lessThanOrEqualTo(snapsRoot.get(BIndexSnap_.datetime), endCal.getTime());
			}
		}

		if (predicate != null) {
			query.where(predicate);
		}

		final List<Date> listDate = em.createQuery(query).getResultList();
		final List<Integer> list = new ArrayList<Integer>();

		for (Date date : listDate) {
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(date);

			int dateValue = 0;

			switch (base) {
			case day:
				dateValue = cal.get(Calendar.DATE);
				break;
			case hour:
				dateValue = cal.get(Calendar.HOUR_OF_DAY);
				break;
			case minute:
				dateValue = cal.get(Calendar.MINUTE);
				break;
			case month:
				dateValue = cal.get(Calendar.MONTH);
				dateValue++;
				break;
			case year:
				dateValue = cal.get(Calendar.YEAR);
				break;
			default:
				break;
			}

			if (!list.contains(dateValue)) {
				list.add(dateValue);
			}
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<BIndex, List<BIndexRange>> listIndex() {
		log.info("get all index with range");
		final Query q = em.createQuery("SELECT index, range FROM BIndex index LEFT JOIN index.range range");

		final List<Object[]> list = q.getResultList();

		final Map<BIndex, List<BIndexRange>> indices = new HashMap<BIndex, List<BIndexRange>>();

		for (Object[] objects : list) {
			BIndex index = (BIndex) objects[0];

			List<BIndexRange> rangeList = null;

			if (indices.containsKey(index)) {
				rangeList = indices.get(index);
			} else {
				rangeList = new ArrayList<BIndexRange>();
				indices.put(index, rangeList);
			}

			if (objects[1] != null) {
				BIndexRange range = (BIndexRange) objects[1];
				rangeList.add(range);
			}
		}

		return indices;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<BIndex, List<BIndexRange>> findIndex(Long category) {
		log.info("get all index for category {} with range", category);

		final Query q = em
				.createQuery("SELECT index, range FROM BIndex index LEFT JOIN index.range range WHERE index.category.id = :category");

		q.setParameter("category", category);

		final List<Object[]> list = q.getResultList();

		final Map<BIndex, List<BIndexRange>> indices = new HashMap<BIndex, List<BIndexRange>>();

		for (Object[] objects : list) {
			BIndex index = (BIndex) objects[0];

			List<BIndexRange> rangeList = null;

			if (indices.containsKey(index)) {
				rangeList = indices.get(index);
			} else {
				rangeList = new ArrayList<BIndexRange>();
				indices.put(index, rangeList);
			}

			if (objects[1] != null) {
				BIndexRange range = (BIndexRange) objects[1];
				rangeList.add(range);
			}
		}

		return indices;
	}

	@Override
	public Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> listIndexSnapshots(Integer categoryId, Date startDate, Date endDate) {
		log.info("get index with range and snapshot for index of category {} from {} to {}", categoryId, startDate, endDate);

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

		// build join
		final Root<BIndex> bindex = query.from(BIndex.class);
		final Join<BIndex, BIndexSnap> rangeJoin = bindex.join("range", JoinType.LEFT);
		final Join<BIndex, BIndexSnap> snapsJoin = bindex.join("snaps", JoinType.LEFT);

		Predicate predicate = null;

		// Build category predicate
		if (categoryId != null) {
			final Join<BIndex, BIndexCategory> categoryJoin = bindex.join("category");
			predicate = cb.equal(categoryJoin.get(BIndexCategory_.id), categoryId);
		}
		// Build start date predicate
		if (startDate != null) {
			if (predicate != null) {
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), startDate));
			} else {
				predicate = cb.greaterThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), startDate);
			}
		}
		// Build end date predicate
		if (endDate != null) {
			if (predicate != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), endDate));
			} else {
				predicate = cb.lessThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), endDate);
			}
		}

		if (predicate != null) {
			query.where(predicate);
		}

		query.multiselect(bindex, rangeJoin, snapsJoin);

		final List<Object[]> list = em.createQuery(query).getResultList();

		final Map<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>> indices = new HashMap<BIndex, Pair<List<BIndexRange>, List<BIndexSnap>>>();

		for (Object[] objects : list) {
			BIndex index = (BIndex) objects[0];

			List<BIndexRange> rangeList = null;
			List<BIndexSnap> snapList = null;

			if (indices.containsKey(index)) {
				rangeList = indices.get(index).getLeft();
				snapList = indices.get(index).getRight();
			} else {
				rangeList = new ArrayList<BIndexRange>();
				snapList = new ArrayList<BIndexSnap>();
				indices.put(index, new Pair<List<BIndexRange>, List<BIndexSnap>>(rangeList, snapList));
			}
			if (objects[1] != null) {
				BIndexRange range = (BIndexRange) objects[1];
				if (!rangeList.contains(range)) {
					rangeList.add(range);
				}
			}
			if (objects[2] != null) {
				BIndexSnap snap = (BIndexSnap) objects[2];
				if (!snapList.contains(snap)) {
					snapList.add(snap);
				}
			}
		}
		return indices;
	}

	@Override
	public Pair<BIndex, List<BIndexSnap>> getIndexSnapshots(long id, Date startDate, Date endDate) {
		log.info("get snapshot for index with id {} from {} to {}", id, startDate, endDate);

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

		// build join
		final Root<BIndex> bindex = query.from(BIndex.class);
		final Join<BIndex, BIndexSnap> snapsJoin = bindex.join("snaps", JoinType.LEFT);

		Predicate predicate = cb.equal(bindex.get(BIndex_.id), id);;

		// Build start date predicate
		if (startDate != null) {
			if (predicate != null) {
				predicate = cb.and(predicate, cb.greaterThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), startDate));
			} else {
				predicate = cb.greaterThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), startDate);
			}
		}
		// Build end date predicate
		if (endDate != null) {
			if (predicate != null) {
				predicate = cb.and(predicate, cb.lessThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), endDate));
			} else {
				predicate = cb.lessThanOrEqualTo(snapsJoin.get(BIndexSnap_.datetime), endDate);
			}
		}
		query.where(predicate);
		
		query.orderBy(cb.asc(snapsJoin.get(BIndexSnap_.datetime)));
		query.multiselect(bindex, snapsJoin);

		final List<Object[]> list = em.createQuery(query).getResultList();
		
		BIndex index = null;
		List<BIndexSnap> snapList = new ArrayList<BIndexSnap>();
		
		for (Object[] objects : list) {
			index = (BIndex) objects[0];

			if (objects[1] != null) {
				BIndexSnap snap = (BIndexSnap) objects[1];
				snapList.add(snap);
			}
		}

		final Pair<BIndex, List<BIndexSnap>> snaps = new Pair<BIndex, List<BIndexSnap>>(index, snapList);

		return snaps;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BIndex> listIndexSimple() {
		log.info("get list of index");
		return em.createQuery("FROM BIndex ORDER BY id").getResultList();
	}

	@Override
	public void saveIndexSnapshot(List<BIndexSnap> list) {
		// delete all last snapshots
		{
			Set<Date> setDates = new HashSet<Date>();
			for (BIndexSnap indexSnap : list) {

				if (!setDates.contains(indexSnap.getDatetime())) {
					Query query = em.createQuery("DELETE BIndexSnap WHERE datetime = :datetime");

					query.setParameter("datetime", indexSnap.getDatetime()).executeUpdate();
					setDates.add(indexSnap.getDatetime());
				}
			}
			setDates = null;
		}

		for (BIndexSnap indexSnap : list) {
			em.persist(indexSnap);
		}

	}

	@Override
	public BIndex findIndexById(Long id) {
		log.debug("search the index with id {}", id);
		return em.find(BIndex.class, id);
	}

	@Override
	public BIndex saveIndex(BIndex index) {
		em.persist(em.merge(index));
		return index;
	}

	@Override
	public BIndexCategory findCategoryById(Long categoryId) {
		log.debug("search the category by id {}", categoryId);
		return em.find(BIndexCategory.class, categoryId);
	}

	@Override
	public void deleteIndex(BIndex index) {
		log.debug("delete the index {}", index);
		Query query = em.createQuery("DELETE BIndexSnap WHERE index.id = :id");

		query.setParameter("id", index.getId()).executeUpdate();
		em.remove(em.merge(index));
	}

	@Override
	public BIndexCategory saveCategory(BIndexCategory category) {
		log.debug("save the category {} ", category);
		em.persist(em.merge(category));
		return category;
	}

	@Override
	public BIndexCategory deleteCategory(BIndexCategory category) {
		log.debug("delete the category {} ", category);

		em.remove(em.merge(category));
		return category;
	}
}
