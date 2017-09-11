package com.jpmorgan.sale.notification.message.saledata;

import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.consumer.SaleConsumer;
import com.jpmorgan.sale.notification.message.consumer.SaleMessageListener;
import com.jpmorgan.sale.notification.message.model.Sale;

/**
 * @author Davis Gordon
 *
 */
public class Log50MessageAgent implements Observer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleMessageListener.class);
    
    private static int totalMessages;
	
    private SaleConsumer saleConsumer;
    private Subject saleData = null;
    
    public Log50MessageAgent(Subject saleData, SaleConsumer saleConsumer) {
        this.saleData = saleData;
        this.saleConsumer = saleConsumer;
        saleData.addObserver(this);
    }
    
    public void update(Sale sale) throws JMSException {
    	
    	synchronized (sale) {
    		
    		if(totalMessages==50){
        		
        		LOGGER.info("50th SALE MESSAGE RECEIVED REPORT");
        		LOGGER.info("PAUSING RECEIVING NEW MESSAGES");
        		saleConsumer.stopConnection();
        		
        		final Map<String, Set<Sale>> sales = saleData.getSales();
        		for (String type : sales.keySet()) {
        			for(Sale s : sales.get(type)){
        				if(s.getAdjustment()!=null){
        					LOGGER.info("SALES OF PRODUCT " + type + 
        							" HAD " + s.getAdjustment().getOperation().name() + 
        							" WITH THE VALUE " + s.getAdjustment().getValue().toString());
        				}
        			}
    			}
        		
        		LOGGER.info("STARTING RECEIVING NEW MESSAGES");
        		saleConsumer.startConnection();
        		
        		totalMessages = 0;
        	}
    		
    		totalMessages++;
		}
        
    }
}
