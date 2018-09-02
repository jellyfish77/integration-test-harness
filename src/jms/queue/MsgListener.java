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
			System.out.println("[MESSAGE RECEIVED]");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println("Local Time: " + dateFormat.format(date));
			System.out.println("Message Class: " + msg.getClass());

			// automatically assigned headers
			System.out.println("Automatically Assigned Headers\n------------------------------");
			System.out.println("JMSDestination: " + msg.getJMSDestination().toString());
			System.out.println("JMSDeliveryMode: "
					+ (javax.jms.DeliveryMode.PERSISTENT == msg.getJMSDeliveryMode() ? "PERSISTANT" : "NONPERSISTANT"));
			System.out.println("JMSMessageID: " + msg.getJMSMessageID());
			System.out.println(
					"JMSTimestamp: " + msg.getJMSTimestamp() + " (Current Time: " + System.currentTimeMillis() + ")");
			System.out.println("Transit Duration [calculated]: "
					+ convertMillisToString((System.currentTimeMillis() - msg.getJMSTimestamp())) + " ("
					+ (System.currentTimeMillis() - msg.getJMSTimestamp()) + "ms)");
			// System.out.println("<currentTimeMillis: " + System.currentTimeMillis() +
			// ">");
			// System.out.println("Duration (ms): " + (System.currentTimeMillis() -
			// msg.getJMSTimestamp()));
			System.out.println("JMSExpiration: " + msg.getJMSExpiration());
			System.out.println("JMSRedelivered: " + msg.getJMSRedelivered());
			System.out.println("JMSPriority: " + msg.getJMSPriority());

			// log developer assigned headers
			System.out.println("Developer Assigned Headers\n--------------------------");
			System.out.println("JMSReplyTo: " + msg.getJMSReplyTo());
			System.out.println("JMSCorrelationID: " + msg.getJMSCorrelationID());
			System.out.println("JMSType: " + msg.getJMSType());
			System.out.println("---------------------------------");

			if (msg instanceof TextMessage) {
				System.out.println("Message is instanceof TextMessage");
				TextMessage tmsg = (TextMessage) msg;
				System.out.println("Message contents\n-------------------------------");
				System.out.println(tmsg.getText());
				// return;
			}
			if (msg instanceof MapMessage) {
				System.out.println("Message is instanceof MapMessage");
				MapMessage mmsg = (MapMessage) msg;
				// log Properties
				System.out.println("Message Properties\n-------------------------------");
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
				System.out.println("Message is instanceof BytesMessage");
				System.out.println("Message contents\n-------------------------------");
				BytesMessage bmsg = (BytesMessage) msg;
				String strMessage = null;
				byte[] byteData = null;
				byteData = new byte[(int) bmsg.getBodyLength()];
				bmsg.readBytes(byteData);
				strMessage = new String(byteData);
				System.out.println(strMessage);

			}
			System.out.println("[END OF MESSAGE]\n");
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
