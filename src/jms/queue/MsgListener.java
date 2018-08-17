package jms.queue;


import java.util.Enumeration;

import javax.jms.*;
import javax.jms.IllegalStateException;


public class MsgListener implements MessageListener {

	public void onMessage(Message message) {
		try {
			// Get the data from the message
			Message msg = (Message) message;
			
			// automatically assigned headers			
			System.out.println("[MESSAGE RECEIVED]");
			System.out.println("Automatically Assigned Headers\n------------------------------");			
			System.out.println("JMSDestination: " + msg.getJMSDestination().toString());			
			System.out.println("JMSDeliveryMode: " + (javax.jms.DeliveryMode.PERSISTENT == msg.getJMSDeliveryMode() ? "PERSISTANT" : "NONPERSISTANT"));
			System.out.println("JMSMessageID: " + msg.getJMSMessageID());
			System.out.println("JMSTimestamp: " + msg.getJMSTimestamp() + " (" + (System.currentTimeMillis() - msg.getJMSTimestamp()) + "ms)");
			//System.out.println("<currentTimeMillis: " + System.currentTimeMillis() + ">");
			//System.out.println("Duration (ms): " + (System.currentTimeMillis() - msg.getJMSTimestamp()));
			System.out.println("JMSExpiration: " + msg.getJMSExpiration());
			System.out.println("JMSRedelivered: " + msg.getJMSRedelivered());			
			System.out.println("JMSPriority: " + msg.getJMSPriority());
			
			//log developer assigned headers
			System.out.println("Developer Assigned Headers\n--------------------------");
			System.out.println("JMSReplyTo: " + msg.getJMSReplyTo());
			System.out.println("JMSCorrelationID: " + msg.getJMSCorrelationID());
			System.out.println("JMSType: " + msg.getJMSType());
			
			if (msg instanceof TextMessage) {
				TextMessage tmsg = (TextMessage) msg;
				System.out.println("MapMessage contents\n-------------------------------"); 
				System.out.println(tmsg.getText());
			}
			if (msg instanceof MapMessage) {				
				MapMessage mmsg = (MapMessage) msg;
				// log Properties
				System.out.println("MapMessage Properties\n-------------------------------");
				Enumeration propertyNames = mmsg.getPropertyNames();
				while(propertyNames.hasMoreElements()) {
					String name = (String)propertyNames.nextElement();
					Object value = mmsg.getObjectProperty(name);
					System.out.println(name + " = " + value);
				}			
				//TextMessage textMessage = (TextMessage) message;			
				//System.out.println("Message Consumed: " + textmsg.getText());
				
				
				
			}			
			else {
				// ok, try log generic messasge details				
				System.out.println("[Generic] Message Properties\n-------------------------------");
				
				//System.out.println(msg.Get);
				//throw new IllegalStateException("Invalid message type");
			}
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		} catch (Exception jmse) {
			jmse.printStackTrace();
			System.exit(1);
		}
	}

}
