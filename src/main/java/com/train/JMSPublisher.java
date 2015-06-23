package com.train;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;

public abstract class JMSPublisher {
	ConnectionFactory connectionFactory;
	// Connection ：JMS 客户端到JMS Provider 的连接
	Connection connection = null;
	// Session： 一个发送或接收消息的线程
	Session session;
	// Destination ：消息的目的地;消息发送给谁.
	Destination destination;
	// MessageProducer：消息发送者
	MessageProducer producer;
	Destination[] destinations;

	String[] stocks = new String[] { "stock1", "stock2", "stock3" };

	public static final String brokerURL = "tcp://localhost:61616";

	public JMSPublisher() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		connection = connectionFactory.createConnection();
		try {
			connection.start();
		} catch (JMSException jmse) {
			connection.close();
			throw jmse;
		}
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		producer = session.createProducer(null);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
	}

	abstract protected void createDestination() throws JMSException;

	protected void sendMessage() throws JMSException {
		for (int i = 0; i < stocks.length; i++) {
			Message message = createStockMessage(stocks[i], session);
			System.out.println("Sending: "
					+ ((ActiveMQMapMessage) message).getContentMap()
					+ " on destination: " + destinations[i]);
			producer.send(destinations[i], message);
		}
	}

	protected void sendObjectMessage() throws JMSException {
		for (int i = 0; i < stocks.length; i++) {
			Message message = createObjectMessage(i, session);
			System.out.println("Sending: id: "
					+ ((ObjectMessage) message).getObject() + " on queue: "
					+ destinations[i]);
			producer.send(destinations[i], message);
		}
	}

	protected Message createStockMessage(String stock, Session session)
			throws JMSException {
		MapMessage message = session.createMapMessage();
		message.setString("stock", stock);
		message.setDouble("price", 1.00);
		message.setDouble("offer", 0.01);
		message.setBoolean("up", true);

		return message;
	}

	protected Message createObjectMessage(int i, Session session)
			throws JMSException {
		Message message = session.createObjectMessage(i);

		return message;
	}

	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}

}
