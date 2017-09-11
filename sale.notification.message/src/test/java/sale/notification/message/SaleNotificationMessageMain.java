package sale.notification.message;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpmorgan.sale.notification.message.constant.EnumAdjustmentOperation;
import com.jpmorgan.sale.notification.message.consumer.SaleConsumer;
import com.jpmorgan.sale.notification.message.model.Adjustment;
import com.jpmorgan.sale.notification.message.model.Product;
import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.producer.SaleProducer;

/**
 * @author Davis Gordon
 *
 */
public class SaleNotificationMessageMain {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaleNotificationMessageMain.class);
	
	public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();
	
	private static SaleProducer producer;
	private static SaleConsumer consumer;

	public static void main(String[] args) {
		new SaleNotificationMessageMain();
	}
	
	public SaleNotificationMessageMain() {
		try{
		beforeClass();
		test();
		afterClass();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		
	public static void beforeClass() throws JMSException, NamingException, InterruptedException {
		producer = new SaleProducer();
		producer.create("producer", "saleBroker.q");
		consumer = new SaleConsumer();
		consumer.create("consumer", "saleBroker.q");
	}
		
	public static void afterClass() throws JMSException {
		producer.closeConnection();
		consumer.closeConnection();
	}
	
	private static int getRandomInt(int min, int max){
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
		
	public void test() {
		try {
			
			Product p1 = new Product("apple", BigDecimal.valueOf(10D));
			Product p2 = new Product("orange", BigDecimal.valueOf(5.9D));
			Product p3 = new Product("banana", BigDecimal.valueOf(8.4D));
			Product p4 = new Product("strawberry", BigDecimal.valueOf(15.5D));
			
			Adjustment add = new Adjustment(BigDecimal.valueOf(20D), EnumAdjustmentOperation.ADD);
			Adjustment sub = new Adjustment(BigDecimal.valueOf(0.05D), EnumAdjustmentOperation.SUBTRACT);
			Adjustment mult = new Adjustment(BigDecimal.valueOf(2D), EnumAdjustmentOperation.MULTIPLY);
			
			List<Product> products = Arrays.asList(p1, p2, p3, p4);
			List<Adjustment> adjustments = Arrays.asList(add, sub, mult);
			
			int totalMessages = getRandomInt(50, 80);
			
			Set<String> typesSent = new HashSet<>();
			
			for(int x=0; x<totalMessages; x++){
				
				Product p = products.get(getRandomInt(0, products.size()-1));
				Sale s = new Sale(p, getRandomInt(1, 12));
				s.setId(x);
				if(x>=5 && typesSent.contains(p.getType()) && (getRandomInt(1, 10)%2==0) )
					s.setAdjustment(adjustments.get(getRandomInt(0, adjustments.size()-1)));
				
				producer.sendSale(s);
				typesSent.add(p.getType());
			}
			
			Thread.sleep(2000);
			
			LOGGER.debug("Total sent: " + producer.getTotal());
			LOGGER.debug("Total received: " + consumer.getTotal());
		
		} catch (Exception e) {
		    fail("a Exception occurred");
		}
	}
}
