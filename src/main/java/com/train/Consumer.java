package com.train;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class Consumer {
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
	
	static String[] stocks = new String[] { "stock1", "stock2", "stock3" };
	public static final String brokerURL = "tcp://localhost:61616";
	public Consumer() throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(brokerURL);
    	connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }
	
	protected abstract Destination  createDestination(String consume) throws JMSException ;
	
	
	public Session getSession() {
		return session;
	}
}
