/**
 * 
 */
package com.jpmorgan.sale.notification.message.consumer;

import java.math.BigDecimal;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.constant.EnumAdjustmentOperation;
import com.jpmorgan.sale.notification.message.constant.SalePropertyConstant;
import com.jpmorgan.sale.notification.message.model.Adjustment;
import com.jpmorgan.sale.notification.message.model.Product;
import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.saledata.SaleBroker;

/**
 * @author Davis Gordon
 *
 */
public class SaleMessageListener implements MessageListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleMessageListener.class);
	
	private static int total;
	
	private String clientId;
	private SaleBroker saleBroker;
	 
	public SaleMessageListener(String clientId, SaleBroker saleBroker) {
		this.clientId = clientId;
		this.saleBroker = saleBroker;
	}

	/* (non-Javadoc)
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {
		
        if (message != null) {
            // cast the message to the correct type
            MapMessage mapMessage = (MapMessage) message;
            
            try{
            	// create a product
            	Product p1 = new Product(mapMessage.getString(SalePropertyConstant.PRODUCT_TYPE), 
            			new BigDecimal(mapMessage.getString(SalePropertyConstant.PRODUCT_VALUE)));
            	
            	// create a sale
            	Sale sale = new Sale(p1, mapMessage.getInt(SalePropertyConstant.NUMBER_SALES));
            	sale.setId(mapMessage.getInt(SalePropertyConstant.SALE_ID));
            	
            	if(mapMessage.itemExists(SalePropertyConstant.ADJUSTMENT_VALUE) 
            			&& mapMessage.itemExists(SalePropertyConstant.ADJUSTMENT_OPERATION)){
            		
            		Adjustment adjustment = new Adjustment(new BigDecimal((mapMessage.getString(SalePropertyConstant.ADJUSTMENT_VALUE))), 
            				EnumAdjustmentOperation.valueOf(mapMessage.getString(SalePropertyConstant.ADJUSTMENT_OPERATION)));
            		
            		sale.setAdjustment(adjustment);
            	}
            	
            	// Register the sale to the broker
        		this.saleBroker.registerSale(sale);
        		total++;
            		            
	            LOGGER.debug(clientId + ": ["+total+"] " + sale);

            } catch (Exception e) {
				e.printStackTrace();
			}
        } else {
        	LOGGER.debug(clientId + ": no message received");
        }
	}
	
	public int getTotal() {
		return total;
	}

}
