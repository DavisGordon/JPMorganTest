package com.jpmorgan.sale.notification.message.saledata;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.consumer.SaleMessageListener;
import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.model.SaleItem;

/**
 * @author Davis Gordon
 *
 */
public class Log10MessageAgent implements Observer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleMessageListener.class);
    
    private static int TOTALMESSSAGES;
	
    Subject saleData = null;
    
    public Log10MessageAgent(Subject saleData) {
    	TOTALMESSSAGES = 0;
    	this.saleData = saleData;
        saleData.addObserver(this);
    }
    
    public void update(Sale sale) {
    	
    	synchronized (sale) {
    		
        	if(TOTALMESSSAGES==10){
        		
        		LOGGER.info("10th SALE MESSAGE RECEIVED REPORT");
        		
        		final Map<String, Set<SaleItem>> sales = saleData.getSaleItens();
        		BigDecimal totalValue = null;
        		for (String type : sales.keySet()) {
        			totalValue = BigDecimal.ZERO;
        			int totalSales = sales.get(type).size();
        			for(SaleItem s : sales.get(type))
        				totalValue = totalValue.add(s.getValue());
        			
        			LOGGER.info("PRODUCT TYPE: " + type + ", NUMBER SALES: " + totalSales + ", TOTAL VALUE: " + totalValue);
        			
    			}
        		
        		TOTALMESSSAGES = 0;
        	}
        	
        	TOTALMESSSAGES++;
			
		}
        
    }
}
