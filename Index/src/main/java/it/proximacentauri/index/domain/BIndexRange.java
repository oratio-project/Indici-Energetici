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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "bi_indexrange", uniqueConstraints = @UniqueConstraint(columnNames = { "Index_Id", "color" }))
public class BIndexRange implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 290555669520961673L;

	private Character color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Index_Id")
	private BIndex BIndex;

	private BigDecimal inferior;
	private BigDecimal superior;

	public BIndex getBIndex() {
		return BIndex;
	}

	public void setBIndex(BIndex bIndex) {
		BIndex = bIndex;
	}

	public BigDecimal getInferior() {
		return inferior;
	}

	public void setInferior(BigDecimal inferior) {
		this.inferior = inferior;
	}

	public BigDecimal getSuperior() {
		return superior;
	}

	public void setSuperior(BigDecimal superior) {
		this.superior = superior;
	}

	public Character getColor() {
		return color;
	}

	public void setColor(Character color) {
		this.color = color;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BIndexRange [color=");
		builder.append(color);
		builder.append(", inferior=");
		builder.append(inferior);
		builder.append(", superior=");
		builder.append(superior);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BIndexRange other = (BIndexRange) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		return true;
	}
}
