package sale.notification.message;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.jpmorgan.sale.notification.message.constant.EnumAdjustmentOperation;
import com.jpmorgan.sale.notification.message.consumer.SaleConsumer;
import com.jpmorgan.sale.notification.message.model.Adjustment;
import com.jpmorgan.sale.notification.message.model.Product;
import com.jpmorgan.sale.notification.message.model.Sale;
import com.jpmorgan.sale.notification.message.producer.SaleProducer;

/**
 * @author Davis Gordon Dun
 * */
public class SaleNotificationMessageTest {
	
	@Rule
	public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();
	
	private static SaleProducer producer;
	private static SaleConsumer consumer;
	
	@BeforeClass
	public static void beforeClass() throws JMSException, NamingException, InterruptedException {
		producer = new SaleProducer();
		producer.create("producer", "saleBroker.q");
		consumer = new SaleConsumer();
		consumer.create("consumer", "saleBroker.q");
	}
	
	@AfterClass
	public static void afterClass() throws JMSException {
		producer.closeConnection();
		consumer.closeConnection();
	}
		
	@Test
	public void test() {
		try {
			// Products
			Product p1 = new Product("apple", BigDecimal.valueOf(10D));
			Product p2 = new Product("orange", BigDecimal.valueOf(5.9D));
			Product p3 = new Product("banana", BigDecimal.valueOf(8.4D));
			Product p4 = new Product("strawberry", BigDecimal.valueOf(15.5D));
			
			// Adjustments
			Adjustment add = new Adjustment(BigDecimal.valueOf(20D), EnumAdjustmentOperation.ADD);
			Adjustment sub = new Adjustment(BigDecimal.valueOf(0.05D), EnumAdjustmentOperation.SUBTRACT);
			Adjustment mult = new Adjustment(BigDecimal.valueOf(2D), EnumAdjustmentOperation.MULTIPLY);
			
			// List Product/Adjustemnt
			List<Product> products = Arrays.asList(p1, p2, p3, p4);
			List<Adjustment> adjustments = Arrays.asList(add, sub, mult);
			
			// Random totalMessages
			int totalMessages = getRandomInt(50, 80);
			
			// Types that was sent to get adjustment
			Set<String> typesSent = new HashSet<>();
			
			for(int x=0; x<totalMessages; x++){
				// get a product randomically to sent
				Product p = products.get(getRandomInt(0, products.size()-1));
				// build a sale with randomically units between 1 and 12
				Sale s = new Sale(p, getRandomInt(1, 12));
				s.setId(x);
				// build a adjustment if the product type was been sent and if the return random number is even
				if(x>=5 && typesSent.contains(p.getType()) && (getRandomInt(1, 10)%2==0) )
					s.setAdjustment(adjustments.get(getRandomInt(0, adjustments.size()-1)));
				// send the sale
				producer.sendSale(s);
				// set the type to be adjust
				typesSent.add(p.getType());
			}
			
			Thread.sleep(2000);
			
			assertEquals(totalMessages, producer.getTotal());
			
			assertEquals(producer.getTotal(), consumer.getTotal());
			
		} catch (Exception e) {
		    fail("a Exception occurred");
		}
	}
	
	private static int getRandomInt(int min, int max){
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
	
}
