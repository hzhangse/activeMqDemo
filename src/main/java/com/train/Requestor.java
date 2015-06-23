package com.train;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Requestor implements MessageListener {
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
	
	public void onMessage(Message message) {
        String messageText = null;
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                messageText = textMessage.getText();
                System.out.println("messageText = " + messageText);
            }
        } catch (JMSException e) {
            //Handle the exception appropriately
        }
    }


	public Requestor() throws JMSException{
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"tcp://localhost:61616");
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Destination adminQueue = session.createQueue("clientQueueName");

			// Setup a message producer to send message to the queue the server
			// is consuming from
			this.producer = session.createProducer(adminQueue);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create a temporary queue that this client will listen for
			// responses on then create a consumer
			// that consumes message from this temporary queue...for a real
			// application a client should reuse
			// the same temp queue for each message to the server...one temp
			// queue per client
			Destination tempDest = session.createTemporaryQueue();
			MessageConsumer responseConsumer = session.createConsumer(tempDest);

			// This class will handle the messages to the temp queue as well
			responseConsumer.setMessageListener(this);

			// Now create the actual message you want to send
			TextMessage txtMessage = session.createTextMessage();
			txtMessage.setText("MyProtocolMessage");

			// Set the reply to field to the temp queue you created above, this
			// is the queue the server
			// will respond to
			txtMessage.setJMSReplyTo(tempDest);

			// Set a correlation ID so when you get a response you know which
			// sent message the response is for
			// If there is never more than one outstanding message to the server
			// then the
			// same correlation ID can be used for all the messages...if there
			// is more than one outstanding
			// message to the server you would presumably want to associate the
			// correlation ID with this
			// message somehow...a Map works good
			String correlationId = "id_"+System.currentTimeMillis();
			txtMessage.setJMSCorrelationID(correlationId);
			this.producer.send(txtMessage);
		} catch (JMSException e) {
			// Handle the exception appropriately
		}
	}

	public static void main(String[] args) throws JMSException {
		

	}

}
