/**
 * 
 */
package com.jpmorgan.sale.notification.message.consumer;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.saledata.SaleBroker;



/**
 * @author Davis Gordon
 *
 */
public class SaleConsumer {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleConsumer.class);
    
    private Connection connection;
    private Session session;
    private MessageListener messageListener;
    
    private SaleBroker saleBroker;
   
    public void create(String clientId, String queueName) throws JMSException, InterruptedException {
    	
    	saleBroker = SaleBroker.getInstance(this);

    	// Connection Factory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");

        // Connection
        connection = connectionFactory.createConnection();
        connection.setClientID(clientId);

        // Session
        session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        
        // Queue
        Queue queue = session.createQueue(queueName);
        
        MessageConsumer consumer = session.createConsumer(queue);
        messageListener = new SaleMessageListener(clientId, saleBroker);
        consumer.setMessageListener(messageListener);
        
        // start the connection 
        startConnection();       
    }
    
    /**
     * Start the connection
     * */
    public void startConnection() throws JMSException{
    	LOGGER.info("START CONNECTION");
    	connection.start();
    }
    
    /**
     * Stop the connection
     * */
    public void stopConnection() throws JMSException{
    	LOGGER.info("STOP CONNECTION");
    	connection.stop();
    }

    /**
     * Close the connection
     * */
    public void closeConnection() throws JMSException {
    	LOGGER.info("CLOSE CONNECTION");
        connection.close();
    }    
    
    public int getTotal() {
		return ((SaleMessageListener) messageListener).getTotal();
	}
}
