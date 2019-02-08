package jms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.springframework.jndi.JndiAccessor;

/*
 * 
 * Args:
 * 
 * connFactoryName inDestName outDestName numMessages
 * 
 * e.g.:
 * 
 * <host> <port> ConnectionFactory dynamicQueues/TEST.MapMessage.IN.Q dynamicQueues/TEST.MapMessage.OUT.Q
 * 
 */

public class JMSTester implements javax.jms.MessageListener {

	private Connection connection = null;
	private Session session = null;
	private Destination outDest = null;
	private Destination inDest = null;

	public JMSTester(Properties env, String connFactoryName, String inDestName, String outDestName) {
		try {
			// Connect to the provider and get the JMS connection
			Context ctx;
			if (env == null) {
				// lookup context from jndi.properties
				ctx = new InitialContext();
			} else {
				// use supplied context
				ctx = new InitialContext(env);
			}
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(connFactoryName);
			System.out.println("\nContext Details");
			System.out.println("===============");
			Enumeration ctxEnvironnment = ctx.getEnvironment().elements();
			// ctxEnvironnment.elements().
			while (ctxEnvironnment.hasMoreElements()) {
				//TypeKey entry = (TypeKey) ctxEnvironnment.nextElement();
				//System.out.println(entry);
				 System.out.println(Arrays.asList(ctxEnvironnment.nextElement()));				
			}
/*
			for (Map.Entry<KeyType, ValueType> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
*/
			System.out.print("Connecting to JMS Provider... ");
			connection = factory.createConnection();
			System.out.println("[OK]");
			ConnectionMetaData connectionMetaData = connection.getMetaData();
			System.out.println("JMSProviderName: " + connectionMetaData.getJMSProviderName());
			System.out.println("JMSProviderVersion: " + connectionMetaData.getProviderVersion());
			System.out.println("JMSVersion: " + connectionMetaData.getJMSVersion());
			// Create the JMS Session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// Lookup the request and response queues
			System.out.print("Looking up " + inDestName + "... ");
			inDest = (Destination) ctx.lookup(inDestName);
			System.out.println("[OK]");
			System.out.print("Looking up " + outDestName + "... ");
			outDest = (Destination) ctx.lookup(outDestName);
			System.out.println("[OK]");

			MessageConsumer consumer = session.createConsumer(outDest);
			// Register this class to be a JMS message listener object for the consumer
			consumer.setMessageListener(this);
			// Now that setup is complete, start the Connection
			connection.start();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		} catch (NamingException jne) {
			jne.printStackTrace();
			System.exit(1);
		}
	}

	/*
	 * private void sendTextMessage(String strMessage) { try { TextMessage msg =
	 * session.createTextMessage(); msg.setText(strMessage); MessageProducer
	 * producer = session.createProducer(inDest); producer.send(msg); } catch
	 * (JMSException jmse) { jmse.printStackTrace(); System.exit(1); } }
	 */

	private void sendMessage(Message msg) {
		try {
			MessageProducer producer = session.createProducer(inDest);
			producer.send(msg);
			System.out.println("[MESSAGE PRODUCED]");
			System.out.println("Automatically Assigned Headers\n------------------------------");
			System.out.println("JMSMessageID: " + msg.getJMSMessageID());
			// log Application Specific Properties
			System.out.println("Application Specific Properties\n-------------------------------");
			Enumeration propertyNames = msg.getPropertyNames();
			while (propertyNames.hasMoreElements()) {
				String name = (String) propertyNames.nextElement();
				Object value = msg.getObjectProperty(name);
				System.out.println(name + " = " + value);
			}
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		}
	}

	/* Receive Messages From Consumer */
	public void onMessage(Message message) {
		// System.out.print("Received message from Topic: ");

		try {
			// automatically assigned headers
			System.out.println("[MESSAGE RECEIVED]");
			System.out.println("Automatically Assigned Headers\n------------------------------");
			System.out.println("JMSDestination: " + message.getJMSDestination().toString());
			System.out.println("JMSDeliveryMode: "
					+ (javax.jms.DeliveryMode.PERSISTENT == message.getJMSDeliveryMode() ? "PERSISTANT"
							: "NONPERSISTANT"));
			System.out.println("JMSMessageID: " + message.getJMSMessageID());
			System.out.println("JMSTimestamp: " + message.getJMSTimestamp() + " ("
					+ (System.currentTimeMillis() - message.getJMSTimestamp()) + "ms)");
			// System.out.println("<currentTimeMillis: " + System.currentTimeMillis() +
			// ">");
			// System.out.println("Duration (ms): " + (System.currentTimeMillis() -
			// message.getJMSTimestamp()));
			System.out.println("JMSExpiration: " + message.getJMSExpiration());
			System.out.println("JMSRedelivered: " + message.getJMSRedelivered());
			System.out.println("JMSPriority: " + message.getJMSPriority());

			// log developer assigned headers
			System.out.println("Developer Assigned Headers\n--------------------------");
			System.out.println("JMSReplyTo: " + message.getJMSReplyTo());
			System.out.println("JMSCorrelationID: " + message.getJMSCorrelationID());
			System.out.println("JMSType: " + message.getJMSType());

			// log Properties
			System.out.println("Properties\n-------------------------------");
			Enumeration propertyNames = message.getPropertyNames();
			while (propertyNames.hasMoreElements()) {
				String name = (String) propertyNames.nextElement();
				Object value = message.getObjectProperty(name);
				System.out.println(name + " = " + value);
			}
			// TextMessage textMessage = (TextMessage) message;
			// System.out.println("Message Consumed: " + textMessage.getText());
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}

	private void exit() {
		try {
			connection.close();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String argv[]) {

		String host = null;
		String port = null;
		String connFactoryName = null;
		String inDestName = null;
		String outDestName = null;
		int numMessages = 0;

		if (argv.length == 6) {
			host = argv[0];
			port = argv[1];
			connFactoryName = argv[2];
			inDestName = argv[3];
			outDestName = argv[4];
			numMessages = Integer.parseInt(argv[5]);
		} else {
			System.out.println("Invalid arguments.");
			System.exit(0);
		}

		System.out.println("JMSTester Application Started");		
		
		System.out.println("\nProgram Arguments");
		System.out.println("=================");
		System.out.println("host: " + host);
		System.out.println("port: " + port);
		System.out.println("connFactoryName: " + connFactoryName);
		System.out.println("inDestName: " + inDestName);
		System.out.println("outDestName: " + outDestName);
		System.out.println("numMessages: " + numMessages);

		// System.out.println("Press enter to quit application");

		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		env.put(Context.PROVIDER_URL, "tcp://" + host + ":" + port);

		// JMSTester jmsTester = new JMSTester(env, connFactoryName, inDestName,
		// outDestName);
		JMSTester jmsTester = new JMSTester(null, connFactoryName, inDestName, outDestName);

		// spawn n test messages with a uuid to test if they are received
//		for(int i=0; i<numMessages; i++) {
//			UUID uuid = UUID.randomUUID();
//			String textMessage = "Message " + (i + 1) + " [" + uuid.toString() + "]";
//			jmsTester.sendTextMessage(textMessage);	
//			System.out.println("Message Produced: " + textMessage);
//		}

		MapMessage mapMessage;
		try {
			mapMessage = jmsTester.session.createMapMessage();
			mapMessage.setInt("Age", 88);
			mapMessage.setFloat("Weight", 234);
			mapMessage.setString("Name", "Smith");
			mapMessage.setObject("Height", new Double(150.32));
			mapMessage.setStringProperty("sender", "integration-test-harness");
			mapMessage.setBooleanProperty("some_bool", true);
			mapMessage.setStringProperty("JMSXAppID", "eclipse");
			mapMessage.setStringProperty("JMSXGroupID", "ERF-001");
			// jmsTester.session.createProducer(jmsTester.inDest).send(mapMessage);
			mapMessage.setJMSReplyTo(jmsTester.outDest);
			jmsTester.sendMessage(mapMessage);
			// System.out.println("[MESSAGE PRODUCED] (JMSMessageID: " +
			// mapMessage.getJMSMessageID() +")");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * try { // Read all standard input and send it as a message BufferedReader
		 * stdin = new BufferedReader(new InputStreamReader(System.in)); while (true) {
		 * //System.out.print("> "); String textMessage = stdin.readLine(); if
		 * (textMessage == null || textMessage.trim().length() <= 0) { jmsTester.exit();
		 * } jmsTester.sendTextMessage(textMessage); } } catch (IOException ioe) {
		 * ioe.printStackTrace(); }
		 */

	}

}
