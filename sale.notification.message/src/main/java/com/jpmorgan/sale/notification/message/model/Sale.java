/**
 * 
 */
package com.jpmorgan.sale.notification.message.model;

import java.io.Serializable;

/**
 * @author Davis Gordon
 *
 */
public class Sale implements Serializable{

	private static final long serialVersionUID = 3024283104570514968L;
	
	private int id; 
	
	private Product product;
	
	private Integer units;
	
	private Adjustment adjustment;
	
	public Sale() {
		
	}
	
	public Sale(Product product, Integer units) {
		this.product = product;
		this.units = units;
	}
	
	public Sale(Product product, Integer units, Adjustment adjustment) {
		this.product = product;
		this.units = units;
		this.adjustment = adjustment;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}
	
	public Adjustment getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(Adjustment adjustment) {
		this.adjustment = adjustment;
	}
	
	@Override
	public String toString() {
		return "id=" + id + 
				", type=" + product.getType() + 
				", value=" + product.getValue() + 
				", units=" + units  + 
				( adjustment!=null ? ", " + adjustment : "");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
