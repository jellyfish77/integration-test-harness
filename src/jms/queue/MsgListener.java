package jms.queue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import javax.jms.*;
import javax.jms.IllegalStateException;

public class MsgListener implements MessageListener {

	public void onMessage(Message message) {
		try {
			// Get the data from the message
			Message msg = (Message) message;
			System.out.println("------------------------[MESSAGE RECEIVED]----------------------------");
			
			System.out.println("\nSYSTEM INFO:\n");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("\tLocal Time:         " + dateFormat.format(date));			
			System.out.println("\tMessage Class Name: " + msg.getClass().getName());

			// automatically assigned headers
			System.out.println("\nAUTOMATICALLY ASSIGNED HEADERS:\n");
			System.out.println("\tJMSDestination:                " + msg.getJMSDestination().toString());
			System.out.println("\tJMSDeliveryMode:               "
					+ (javax.jms.DeliveryMode.PERSISTENT == msg.getJMSDeliveryMode() ? "PERSISTANT" : "NONPERSISTANT"));
			System.out.println("\tJMSMessageID:                  " + msg.getJMSMessageID());
			System.out.println("\tJMSTimestamp:                  " + msg.getJMSTimestamp() + " (Current Time: " + System.currentTimeMillis() + ")");
			System.out.println("\tTransit Duration [calculated]: "
					+ convertMillisToString((System.currentTimeMillis() - msg.getJMSTimestamp())) + " ("
					+ (System.currentTimeMillis() - msg.getJMSTimestamp()) + "ms)");
			System.out.println("\tJMSExpiration:                 " + msg.getJMSExpiration());
			System.out.println("\tJMSRedelivered:                " + msg.getJMSRedelivered());
			System.out.println("\tJMSPriority:                   " + msg.getJMSPriority());

			// log developer assigned headers
			System.out.println("\nDEVELOPER ASSIGNED HEADERS:\n");
			System.out.println("\tJMSReplyTo:       " + msg.getJMSReplyTo());
			System.out.println("\tJMSCorrelationID: " + msg.getJMSCorrelationID());
			System.out.println("\tJMSType:          " + msg.getJMSType());

			if (msg instanceof TextMessage) {
				System.out.println("\nMessage is instanceof TextMessage");
				TextMessage tmsg = (TextMessage) msg;
				System.out.println("\nMESSAGE CONTENTS:\n");
				System.out.println(tmsg.getText());
				// return;
			}
			if (msg instanceof MapMessage) {
				System.out.println("\nMessage is instanceof MapMessage");
				MapMessage mmsg = (MapMessage) msg;
				// log Properties
				System.out.println("\nMESSAGE PROPERTIES:\n");
				Enumeration propertyNames = mmsg.getPropertyNames();
				while (propertyNames.hasMoreElements()) {
					String name = (String) propertyNames.nextElement();
					Object value = mmsg.getObjectProperty(name);
					System.out.println(name + " = " + value);
				}
				// return;
				// TextMessage textMessage = (TextMessage) message;
				// System.out.println("Message Consumed: " + textmsg.getText());

			}
			if (msg instanceof BytesMessage) {
				System.out.println("\nMessage is instanceof BytesMessage");
				System.out.println("\nMESSAGE CONTENTS:\n");
				BytesMessage bmsg = (BytesMessage) msg;
				String strMessage = null;
				byte[] byteData = null;
				byteData = new byte[(int) bmsg.getBodyLength()];
				bmsg.readBytes(byteData);
				strMessage = new String(byteData);
				System.out.println(strMessage);

			}			
			System.out.println("-------------------------[END OF MESSAGE]-----------------------------\n");
			
			// throw new IllegalStateException("Invalid message type");

		} catch (JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		} catch (Exception jmse) {
			jmse.printStackTrace();
			System.exit(1);
		}
	}

	private String convertMillisToString(long millis) {

		return String.format("%02d days, %02d hrs, %02d min, %02d sec", TimeUnit.MILLISECONDS.toDays(millis),
				TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

}
