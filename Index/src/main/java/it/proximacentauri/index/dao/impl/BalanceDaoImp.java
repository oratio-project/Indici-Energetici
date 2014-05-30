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

import it.proximacentauri.index.dao.BalanceDao;
import it.proximacentauri.index.domain.BalanceItem;
import it.proximacentauri.index.domain.BalanceSnapshot;
import it.proximacentauri.index.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class BalanceDaoImp extends BaseDao implements BalanceDao {
	final private Logger log = LoggerFactory.getLogger(BalanceDaoImp.class);

	@Override
	@SuppressWarnings("unchecked")
	public List<Date> listSnapshotsTimestamps() {
		log.info("get the list of timestamp of balance snapshot");

		final Query query = em.createQuery("SELECT DISTINCT(datetime) FROM BalanceSnapshot");
		return (List<Date>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshots(Date start, Date end) {
		log.info("get balance item and balance from the datetime {} to the datetime {}", start, end);

		final Query query = em
				.createQuery("SELECT item, snap FROM BalanceItem item LEFT JOIN item.snaps snap WHERE snap.datetime BETWEEN :start AND :end");
		query.setParameter("start", start);
		query.setParameter("end", end);

		final List<Pair<BalanceItem, BalanceSnapshot>> arrayList = new ArrayList<Pair<BalanceItem, BalanceSnapshot>>();

		for (Object[] arrayObjects : (List<Object[]>) query.getResultList()) {
			// create the pair
			Pair<BalanceItem, BalanceSnapshot> pair = new Pair<BalanceItem, BalanceSnapshot>((BalanceItem) arrayObjects[0],
					(BalanceSnapshot) arrayObjects[1]);
			arrayList.add(pair);
		}
		return arrayList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pair<BalanceItem, BalanceSnapshot>> listBalanceSnapshot(Date datetime) {
		log.info("get balance item and balance for the datetime {}", datetime);

		final Query query = em
				.createQuery("SELECT item, snap FROM BalanceItem item LEFT JOIN item.snaps snap WHERE snap.datetime = :datetime");
		query.setParameter("datetime", datetime);

		final List<Pair<BalanceItem, BalanceSnapshot>> arrayList = new ArrayList<Pair<BalanceItem, BalanceSnapshot>>();

		for (Object[] arrayObjects : (List<Object[]>) query.getResultList()) {
			// create the pair
			Pair<BalanceItem, BalanceSnapshot> pair = new Pair<BalanceItem, BalanceSnapshot>((BalanceItem) arrayObjects[0],
					(BalanceSnapshot) arrayObjects[1]);
			arrayList.add(pair);
		}
		return arrayList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BalanceItem> listBalanceItem() {
		log.info("get all balance item");

		return em.createQuery("from BalanceItem").getResultList();
	}

	@Override
	public void saveBalanceSnapshot(List<BalanceSnapshot> list) {
		log.info("try to save the all balance snap shots");

		// delete all last snapshots
		{
			Set<Date> setDates = new HashSet<Date>();
			for (BalanceSnapshot snapshot : list) {

				if (!setDates.contains(snapshot.getDatetime())) {
					Query query = em.createQuery("DELETE BalanceSnapshot WHERE datetime = :datetime");

					query.setParameter("datetime", snapshot.getDatetime()).executeUpdate();
					setDates.add(snapshot.getDatetime());
				}
			}
			setDates = null;
		}

		for (BalanceSnapshot snapshot : list) {
			em.persist(snapshot);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Date> findSnapshotsTimestamps(Date start, Date end) {
		final Query query = em.createQuery("SELECT DISTINCT(datetime) FROM BalanceSnapshot WHERE datetime BETWEEN :start AND :end");
		query.setParameter("start", start);
		query.setParameter("end", end);
		return (List<Date>) query.getResultList();
	}

	@Override
	public BalanceItem findBalanceItemByCode(String code) {
		log.info("find balance item by code {}", code);
		final Query query = em.createQuery("SELECT item FROM BalanceItem item  WHERE item.code = :code");
		query.setParameter("code", code);

		@SuppressWarnings("unchecked")
		final List<BalanceItem> items = query.getResultList();

		if (items.isEmpty())
			return null;
		return items.get(0);
	}

	@Override
	public BalanceItem saveBalanceItem(BalanceItem item) {
		log.debug("save the item {}", item);
		em.persist(em.merge(item));
		return item;
	}

	@Override
	public BalanceItem deleteBalanceItem(BalanceItem item) {
		log.debug("delete the balance item {}", item.getCode());
		Query query = em.createQuery("DELETE BalanceSnapshot WHERE balanceItem.code = :code");

		query.setParameter("code", item.getCode()).executeUpdate();
		em.remove(em.merge(item));
		return item;
	}
}
