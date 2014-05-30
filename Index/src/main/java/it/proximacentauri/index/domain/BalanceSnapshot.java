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
package it.proximacentauri.index.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "bi_balancesnapshot")
public class BalanceSnapshot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3054106133948876301L;

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime;
	private BigDecimal value;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "balance_code")
	private BalanceItem balanceItem;

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date timestamp) {
		this.datetime = timestamp;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BalanceItem getBalanceItem() {
		return balanceItem;
	}

	public void setBalanceItem(BalanceItem balanceItem) {
		this.balanceItem = balanceItem;
	}

}
