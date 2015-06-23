package com.train;

import javax.jms.Destination;
import javax.jms.JMSException;

public class P2PPublisher extends JMSPublisher {

	public P2PPublisher() throws JMSException {
		super();
	}

	@Override
	protected void createDestination() throws JMSException {
		destinations = new Destination[stocks.length];
		for (int i = 0; i < stocks.length; i++) {
			destinations[i] = session.createQueue("STOCKS." + stocks[i]);
		}

	}

	public static void main(String[] args) throws JMSException {
		P2PPublisher publisher = new P2PPublisher();
		// Set topics
		publisher.createDestination();

		for (int i = 0; i < 1; i++) {
			publisher.sendObjectMessage();
			System.out.println("Published " + i + " job messages");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		publisher.close();
	}
}
