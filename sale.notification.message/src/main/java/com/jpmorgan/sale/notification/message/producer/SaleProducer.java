/**
 * 
 */
package com.jpmorgan.sale.notification.message.producer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.constant.SalePropertyConstant;
import com.jpmorgan.sale.notification.message.model.Sale;

/**
 * @author Davis Gordon
 *
 */
public class SaleProducer  {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleProducer.class);
	
	private String clientId;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;
    
    private static int total; 

    public void create(String clientId, String queueName) throws JMSException {
        
    	this.clientId = clientId;

        // Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");

        // Connection
        connection = connectionFactory.createConnection();
        connection.setClientID(clientId);

        // Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Queue
        Queue queue = session.createQueue(queueName);

        // MessageProducer
        messageProducer = session.createProducer(queue);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void sendSale(Sale sale) throws JMSException {
        synchronized (sale) {
			// Message
	        MapMessage message = session.createMapMessage();
	        message.setInt(SalePropertyConstant.SALE_ID, sale.getId());
	        message.setString(SalePropertyConstant.PRODUCT_TYPE, sale.getProduct().getType());
	        message.setString(SalePropertyConstant.PRODUCT_VALUE, sale.getProduct().getValue().toString());
	        message.setInt(SalePropertyConstant.NUMBER_SALES, sale.getUnits());
	        
	        if(sale.getAdjustment() != null){
	        	message.setString(SalePropertyConstant.ADJUSTMENT_VALUE, sale.getAdjustment().getValue().toString());
	        	message.setString(SalePropertyConstant.ADJUSTMENT_OPERATION, sale.getAdjustment().getOperation().name());
	        }
	        
	        // send the message
	        messageProducer.send(message);
	        
	        total++;
	        
	        LOGGER.debug(clientId + ": ["+total+"] " + sale);
        }
    }
    
    public int getTotal() {
		return total;
	}
}
