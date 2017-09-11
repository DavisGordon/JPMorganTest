/**
 * 
 */
package com.jpmorgan.sale.notification.message.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Davis Gordon
 *
 */
public class Product implements Serializable {

	private static final long serialVersionUID = -2444493199003035329L;

	private String type;
	
	private BigDecimal value;
	
	public Product() {
		
	}
	
	public Product(String type, BigDecimal value) {
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
