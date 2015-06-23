package com.train;

import java.text.DecimalFormat;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicSubConsumer extends Consumer {

	public static final String brokerURL = "tcp://localhost:61616";

	public TopicSubConsumer() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	@Override
	protected Destination createDestination(String consume) throws JMSException {
		return this.getSession().createTopic(consume);
	}

	public static void main(String[] args) throws JMSException {
		TopicSubConsumer consumer = new TopicSubConsumer();
		for (String stock : stocks) {
			Destination destination = consumer.createDestination("STOCKS."
					+ stock);
			MessageConsumer messageConsumer = consumer.getSession()
					.createConsumer(destination);
//			MapMessage map = (MapMessage) messageConsumer.receive();
//			String stockStr = map.getString("stock");
//			double price = map.getDouble("price");
//			double offer = map.getDouble("offer");
//			boolean up = map.getBoolean("up");
//			DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
//			System.out.println(stockStr + "\t" + df.format(price) + "\t"
//					+ df.format(offer) + "\t" + (up ? "up" : "down"));
			messageConsumer.setMessageListener(new Listener());
		}
	}

	public Session getSession() {
		return session;
	}

}
