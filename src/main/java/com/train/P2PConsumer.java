package com.train;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class P2PConsumer extends Consumer {
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

	public static final String brokerURL = "tcp://localhost:61616";

	public P2PConsumer() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	public static void main(String[] args) throws JMSException {
		P2PConsumer consumer = new P2PConsumer();
		for (String stock : stocks) {
			Destination destination = consumer.createDestination("STOCKS."
					+ stock);
			MessageConsumer messageConsumer = consumer.getSession()
					.createConsumer(destination);
//			  while (true) {
//	                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
//				  ObjectMessage message = ( ObjectMessage) messageConsumer.receive();
//	                if (null != message) {
//	                    System.out.println("收到消息" + message.getObject().toString());
//	                } else {
//	                    break;
//	                }
//	            }
			messageConsumer.setMessageListener(new Listener());
		}
	}

	public Session getSession() {
		return session;
	}

	@Override
	protected Destination createDestination(String consume) throws JMSException {
		// TODO Auto-generated method stub
		return this.getSession().createQueue(consume);
	}
}
