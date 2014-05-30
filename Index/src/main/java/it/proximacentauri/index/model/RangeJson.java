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
package it.proximacentauri.index.model;

import java.math.BigDecimal;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class RangeJson {

	private Character color;
	private BigDecimal inferior;
	private BigDecimal superior;

	public Character getColor() {
		return color;
	}

	public void setColor(Character color) {
		this.color = color;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RangeJson [color=");
		builder.append(color);
		builder.append(", inferior=");
		builder.append(inferior);
		builder.append(", superior=");
		builder.append(superior);
		builder.append("]");
		return builder.toString();
	}

}
