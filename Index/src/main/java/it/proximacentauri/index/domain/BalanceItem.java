/**
 * @author kirk for Proxima Centauri s.r.l. Torino
 * @email giorgio.daltoe@gmail.com
 * @email giorgio.daltoe@proxima-centauri.it
 */
package it.proximacentauri.index.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "bi_balanceitem")
public class BalanceItem implements Serializable {

	private static final long serialVersionUID = -4474013696742923195L;

	@Id
	private String code;
	private String name;
	private String description;
	@Column(length = 5)
	private String unit;

	// additional, contain additional parameter for adapter
	private String additional;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "balance_code")
	private Set<BalanceSnapshot> snaps = new HashSet<BalanceSnapshot>();

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public boolean addSnaps(BalanceSnapshot snap) {
		snap.setBalanceItem(this);
		return snaps.add(snap);
	}

	public Set<BalanceSnapshot> getSnaps() {
		return snaps;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BalanceItem [code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", unit=");
		builder.append(unit);
		builder.append("]");
		return builder.toString();
	}
}
