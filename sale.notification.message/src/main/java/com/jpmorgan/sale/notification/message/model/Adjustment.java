package com.jpmorgan.sale.notification.message.model;

import java.math.BigDecimal;

import com.jpmorgan.sale.notification.message.constant.EnumAdjustmentOperation;

public class Adjustment {
	
	private BigDecimal value;
	private EnumAdjustmentOperation operation;
	
	public Adjustment() {
		
	}
	
	public Adjustment(BigDecimal value, EnumAdjustmentOperation operation) {
		this.value = value;
		this.operation = operation;
	}
	
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public EnumAdjustmentOperation getOperation() {
		return operation;
	}

	public void setOperation(EnumAdjustmentOperation operation) {
		this.operation = operation;
	}
	
	@Override
	public String toString() {
		return "Adjustment: value = "+value + ", operation = " + operation.name();
	}

}
