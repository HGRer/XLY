package homework.week12;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Hello world!
 *
 */
public class App {
	public static AtomicInteger sign = new AtomicInteger(0);
	
	public static void main(String[] args) throws Exception {
//		queueProducer();
//		queueConsumer();
		
		publish();
//		subscribe();
		Thread.sleep(10000);
		sign.incrementAndGet();
		Thread.sleep(2000);
	}
	
	static void queueProducer() throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue("test");
        
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        
        String text = "Hello world! From: " + Thread.currentThread().getName();
        TextMessage message = session.createTextMessage(text);

        // Tell the producer to send the message
        System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
        producer.send(message);
        
        // Clean up
        session.close();
        connection.close();
	}
	
	static void queueConsumer() throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue("test");
		
		MessageConsumer consumer = session.createConsumer(destination);
		Message message = consumer.receive(10000);
		if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Received -> " + text);
		} else {
			System.out.println("Received -> " + message);
		}
		
        session.close();
        connection.close();
	}
	
	static void publish() {
		Thread thread = new Thread(() -> {
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
				Connection connection = connectionFactory.createConnection();
				connection.start();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				
				Topic topic = session.createTopic("hello");
		        MessageProducer producer = session.createProducer(topic);
		        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		        
		        while(sign.intValue() == 0) {
		        	String text = Thread.currentThread().getName() + " makes -> " + System.currentTimeMillis();
		        	TextMessage message = session.createTextMessage(text);
		        	producer.send(message);
		        	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
		        
		        session.close();
		        connection.close();
		        System.out.println(Thread.currentThread().getName() + " down");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});
		thread.setName("Thread Tom");
		thread.start();
	}
	
	static void subscribe() throws Exception {
		Thread thread = new Thread(() -> {
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
				Connection connection = connectionFactory.createConnection();
				connection.start();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				
				Topic topic = session.createTopic("hello");
				MessageConsumer consumer = session.createConsumer(topic);
		        
		        while(sign.intValue() == 0) {
		        	TextMessage message = (TextMessage) consumer.receive(1000);
		        	if (message != null) {
		        		System.out.println(Thread.currentThread().getName() + " get " + message.getText());
		        	}
		        }
		        
		        session.close();
		        connection.close();
		        System.out.println(Thread.currentThread().getName() + " down");
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});
		thread.setName("Thread Sue");
		thread.start();
		// -------------------------------------------------------------------------------------
		Thread t2 = new Thread(() -> {
			try {
				ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
				Connection connection = connectionFactory.createConnection();
				connection.start();
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Topic topic = session.createTopic("hello");
				MessageConsumer consumer = session.createConsumer(topic);
				
				consumer.setMessageListener((message) -> {
					if (message != null && sign.intValue() == 0) {
						TextMessage m = (TextMessage) message;
						try {
							System.out.println("on message -> " + m.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				});
				
				while(sign.intValue() == 0) {
					
				}
				session.close();
				connection.close();
				System.out.println(Thread.currentThread().getName() + " down");
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		t2.setName("Mike");
		t2.start();
	}
}
