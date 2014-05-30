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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "bi_indexsnapshot")
public class BIndexSnap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2635528239145526182L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Index_Id")
	private BIndex index;

	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime;

	private String formula;
	private BigDecimal value;
	private String arguments;

	public BIndex getIndex() {
		return index;
	}

	public void setIndex(BIndex bIndex) {
		index = bIndex;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String bIndexFormula) {
		formula = bIndexFormula;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal bIndexValue) {
		value = bIndexValue;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date bIndexTime) {
		datetime = bIndexTime;
	}

	public List<String> getArguments() {
		String[] splitArgs = null;
		List<String> args = new ArrayList<String>();

		if (arguments != null) {
			splitArgs = arguments.split(",");
			for (String arg : splitArgs) {
				args.add(arg);
			}
		}

		return args;
	}

	public void setArguments(List<String> arguments) {
		if (arguments == null || arguments.isEmpty())
			return;
		final Iterator<String> iter = arguments.iterator();
		final StringBuilder builder = new StringBuilder(iter.next());
		while (iter.hasNext()) {
			builder.append(",").append(iter.next());
		}
		this.arguments = builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BIndexSnap [Datetime=");
		builder.append(datetime);
		builder.append(", Formula=");
		builder.append(formula);
		builder.append(", Value=");
		builder.append(value);
		builder.append(", arguments=");
		builder.append(arguments);
		builder.append("]");
		return builder.toString();
	}

}
