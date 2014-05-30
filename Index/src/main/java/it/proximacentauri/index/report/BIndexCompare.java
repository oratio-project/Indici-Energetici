package it.proximacentauri.index.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BIndexCompare implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5752100985849566443L;
	
	private Date datetime;
	private BigDecimal index1_value;
	private BigDecimal index2_value;
	
	public Date getDatetime() {
		return datetime;
	}
	
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public BigDecimal getIndex1_value() {
		return index1_value;
	}

	public void setIndex1_value(BigDecimal index1_value) {
		this.index1_value = index1_value;
	}

	public BigDecimal getIndex2_value() {
		return index2_value;
	}

	public void setIndex2_value(BigDecimal index2_value) {
		this.index2_value = index2_value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BIndexCompare [datetime=");
		builder.append(datetime);
		builder.append(", index1_value=");
		builder.append(index1_value);
		builder.append(", index2_value=");
		builder.append(index2_value);
		builder.append("]");
		return builder.toString();
	}	
}
