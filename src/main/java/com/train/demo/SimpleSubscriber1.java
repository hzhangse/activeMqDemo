package com.train.demo;

import java.util.concurrent.TimeUnit;

import org.springframework.context.support.GenericXmlApplicationContext;

public class SimpleSubscriber1 {

	public static void main(String[] args) throws InterruptedException {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:topic-subscriber1-app-context.xml");
		ctx.refresh();
		
		while(true){
			TimeUnit.SECONDS.sleep(3);
		}
	}

}
