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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "bi_index")
public class BIndex {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(columnDefinition = "TEXT")
	private String description;
	private String formula;
	@Column(length = 5)
	private String unit;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private BIndexCategory category;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "Index_Id")
	private Set<BIndexRange> range = new HashSet<BIndexRange>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "Index_Id")
	private Set<BIndexSnap> snaps = new HashSet<BIndexSnap>();

	private String arguments;

	public Long getId() {
		return id;
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

	public Set<BIndexRange> getRange() {
		return range;
	}

	public void addRange(BIndexRange range) {
		range.setBIndex(this);
		this.range.add(range);
	}

	public Set<BIndexSnap> getSnaps() {
		return snaps;
	}

	public void addSnaps(BIndexSnap snaps) {
		snaps.setIndex(this);
		this.snaps.add(snaps);
	}

	public BIndexCategory getCategory() {
		return category;
	}

	public void setCategory(BIndexCategory category) {
		this.category = category;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public void setArguments(Set<String> arguments) {
		this.arguments = StringUtils.join(arguments.toArray(), ",");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BIndex [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", formula=");
		builder.append(formula);
		builder.append(", unit=");
		builder.append(unit);
		builder.append(", category=");
		builder.append(category);
		builder.append(", arguments=");
		builder.append(arguments);
		builder.append("]");
		return builder.toString();
	}

}
