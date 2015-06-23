package com.train.demo;

import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component("messageListener")
public class SimpleMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(SimpleMessageListener.class);

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		
		try {
			logger.info("Message received: " + textMessage.getText());
		} catch (JMSException ex) {
			logger.error("JMS error", ex);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:queue-consumer-app-context.xml");
		ctx.refresh();
		
		while(true){
			TimeUnit.SECONDS.sleep(3);
		}
	}
}
