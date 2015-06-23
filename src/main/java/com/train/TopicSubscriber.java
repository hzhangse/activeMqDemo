package com.train;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicSubscriber {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		try {
			final Connection connection = factory.createConnection();
			connection.setClientID("topictest1");
			connection.start();
			final Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic("myTopic.messages");
			javax.jms.TopicSubscriber consumer = session
					.createDurableSubscriber(topic, "topictest1");
			// MessageConsumer consumer = session.createConsumer(topic);

			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					TextMessage tm = (TextMessage) message;
					try {
						System.out.println("Received message: " + tm.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					} finally {
						try {
							session.close();
							connection.stop();
							connection.close();
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
}
