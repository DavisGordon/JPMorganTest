package com.jpmorgan.sale.notification.message.saledata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.consumer.SaleConsumer;
import com.jpmorgan.sale.notification.message.model.Sale;

public class SaleBroker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleBroker.class);
	
	private static SaleBroker instance;
	
	private SaleData saleData = new SaleData();
	
	private SaleBroker(SaleConsumer saleConsumer) {
		
		Observer adj = new ExecuteAdjustmentAgent(saleData);
		Observer log10 = new Log10MessageAgent(saleData);
		Observer log50 = new Log50MessageAgent(saleData, saleConsumer);
		
		saleData.addObserver(adj, log10, log50);
	}
	
	public static SaleBroker getInstance(SaleConsumer saleConsumer){
		if(instance==null)
			instance = new SaleBroker(saleConsumer);
		return instance;
	}
	
	public void registerSale(Sale sale) throws Exception{
		LOGGER.debug("REGISTER SALE");
		this.saleData.setSale(sale);
	}
	
}
