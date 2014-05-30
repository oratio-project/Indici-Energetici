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

import it.proximacentauri.index.core.Constant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class IndexSnapshotJson {

	private Long id;
	private String datetime;
	private BigDecimal value;
	private String formula;
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	private List<String> arguments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = Constant.ISO_8601_SDF.format(datetime);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndexSnapshotJson [id=");
		builder.append(id);
		builder.append(", datetime=");
		builder.append(datetime);
		builder.append(", value=");
		builder.append(value);
		builder.append(", formula=");
		builder.append(formula);
		builder.append(", arguments=");
		builder.append(arguments);
		builder.append("]");
		return builder.toString();
	}

}
