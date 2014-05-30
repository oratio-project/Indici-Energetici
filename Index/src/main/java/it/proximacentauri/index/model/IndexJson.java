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

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class IndexJson {

	private Long id;
	private String name;
	private String description;
	private String formula;
	private String unit;
	private List<RangeJson> range;
	private List<IndexSnapshotJson> snap;
	private List<String> arguments;
	private CategoryJson category;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<RangeJson> getRange() {
		return range;
	}

	public void setRange(List<RangeJson> range) {
		this.range = range;
	}

	public List<IndexSnapshotJson> getSnap() {
		return snap;
	}

	public void setSnap(List<IndexSnapshotJson> snap) {
		this.snap = snap;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	public CategoryJson getCategory() {
		return category;
	}

	public void setCategory(CategoryJson category) {
		this.category = category;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndexJson [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", formula=");
		builder.append(formula);
		builder.append(", unit=");
		builder.append(unit);
		builder.append(", range=");
		builder.append(range);
		builder.append(", snap=");
		builder.append(snap);
		builder.append(", arguments=");
		builder.append(arguments);
		builder.append(", category=");
		builder.append(category);
		builder.append("]");
		return builder.toString();
	}

}
