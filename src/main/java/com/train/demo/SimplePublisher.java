package com.train.demo;

import java.util.concurrent.TimeUnit;

import org.springframework.context.support.GenericXmlApplicationContext;

public class SimplePublisher {

	public static void main(String[] args) throws InterruptedException {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:topic-publisher-app-context.xml");
		ctx.refresh();
		
		TimeUnit.SECONDS.sleep(3);
		
		MessageSender messageSender = ctx.getBean("messageSender",
		MessageSender.class);
		messageSender.sendMessage("Hello Stevex, this message is for topic TestTopic subscribers from ActiveMQ!");					
	}

}
