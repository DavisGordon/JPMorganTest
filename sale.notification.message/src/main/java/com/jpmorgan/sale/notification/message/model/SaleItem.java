/**
 * 
 */
package com.jpmorgan.sale.notification.message.model;

import java.math.BigDecimal;

/**
 * @author Davis Gordon Dun
 *
 */
public class SaleItem {
	
	private String type;
	
	private BigDecimal value;
	
	public SaleItem(String type, BigDecimal value) {
		this.type = type;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
	
}
