package com.train.demo;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component("messageSender")
public class SimpleMessageSender implements MessageSender {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void sendMessage(final String message) {
		jmsTemplate.send(new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
			
		});
	}
	
	public static void main(String[] args) throws InterruptedException {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:queue-producer-app-context.xml");
		ctx.refresh();
		
		TimeUnit.SECONDS.sleep(3);
		
		MessageSender messageSender = ctx.getBean("messageSender",
		MessageSender.class);
		messageSender.sendMessage("Hello Stevex, I am message from TestQueue of ActiveMQ!");
	}

}
