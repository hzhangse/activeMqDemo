package com.train.demo;

import java.util.concurrent.TimeUnit;

import org.springframework.context.support.GenericXmlApplicationContext;

public class SimpleSubscriber2 {

	public static void main(String[] args) throws InterruptedException {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:topic-subscriber2-app-context.xml");
		ctx.refresh();
		
		while(true){
			TimeUnit.SECONDS.sleep(3);
		}
	}

}
