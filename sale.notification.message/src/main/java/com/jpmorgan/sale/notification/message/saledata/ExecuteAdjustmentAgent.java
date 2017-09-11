package com.jpmorgan.sale.notification.message.saledata;

import java.math.BigDecimal;
import java.util.Set;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.model.Adjustment;
import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.model.SaleItem;

public class ExecuteAdjustmentAgent implements Observer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteAdjustmentAgent.class);
    
    private Subject saleData = null;
    
    public ExecuteAdjustmentAgent(Subject saleData) {
        this.saleData = saleData;
        saleData.addObserver(this);
    }
    
    public void update(Sale sale) throws JMSException {
    	
    	synchronized (sale) {
    		// Verify a adjustment condition
    		if(sale.getAdjustment()!=null){
    			
    			Adjustment adjustment  = sale.getAdjustment();
    			
    			StringBuffer sbf = new StringBuffer("RUN ADJUSTMENT AGENT");
    			sbf.append(adjustment.getOperation().name()+ " "+ adjustment.getValue().toString() + " FOR ALL " + sale.getProduct().getType());
    			
    			LOGGER.debug(sbf.toString());
    			
    			// Get sales of the product type to execute the adjustment
    			final Set<SaleItem> sales = saleData.getSaleItens().get(sale.getProduct().getType());
    			for (final SaleItem s : sales) {
    				final BigDecimal value = s.getValue();
    				switch (adjustment.getOperation()) {
					case ADD:
						 s.setValue(value.add(adjustment.getValue()));
						break;
					case MULTIPLY:
						s.setValue(value.multiply(adjustment.getValue()));
						break;
					case SUBTRACT:
						s.setValue(value.subtract(adjustment.getValue()));
						break;
					}
					
				}
    		}
    		
			
		}
        
    }
}
