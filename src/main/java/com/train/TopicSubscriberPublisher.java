package com.train;

import javax.jms.Destination;
import javax.jms.JMSException;

public class TopicSubscriberPublisher extends JMSPublisher {

	
	
	@Override
	protected void createDestination() throws JMSException {
		destinations = new Destination[stocks.length];
		for(int i = 0; i < stocks.length; i++) {
			destinations[i] = session.createTopic("STOCKS." + stocks[i]);
		}
	}
	public TopicSubscriberPublisher() throws JMSException {
		super();
	}

	public static void main(String[] args) throws JMSException {
		
	        // Create publisher		
		TopicSubscriberPublisher publisher = new TopicSubscriberPublisher();
	        
	        // Set topics
		publisher.createDestination();
			
		for(int i = 0; i < 2; i++) {
			publisher.sendMessage();
			System.out.println("Publisher '" + i + " price messages");
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
	    }
	    // Close all resources
	    publisher.close();
	}

	
}
