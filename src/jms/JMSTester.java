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

import org.apache.log4j.Logger;
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
	final static Logger logger = Logger.getLogger(JMSTester.class);

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
			JMSTester.logger.info("Context Details...");			
			Enumeration ctxEnvironnment = ctx.getEnvironment().elements();
			// ctxEnvironnment.elements().
			while (ctxEnvironnment.hasMoreElements()) {
				//TypeKey entry = (TypeKey) ctxEnvironnment.nextElement();
				//System.out.println(entry);
				JMSTester.logger.info(Arrays.asList(ctxEnvironnment.nextElement()));				
			}
/*
			for (Map.Entry<KeyType, ValueType> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
*/
			JMSTester.logger.info("Connecting to JMS Provider... ");
			connection = factory.createConnection();
			JMSTester.logger.info("Connected to JMS Provider Ok");
			ConnectionMetaData connectionMetaData = connection.getMetaData();
			JMSTester.logger.info("JMSProviderName: " + connectionMetaData.getJMSProviderName());
			JMSTester.logger.info("JMSProviderVersion: " + connectionMetaData.getProviderVersion());
			JMSTester.logger.info("JMSVersion: " + connectionMetaData.getJMSVersion());
			// Create the JMS Session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// Lookup the request and response queues
			JMSTester.logger.info("Looking up inbound destination name: '" + inDestName + "'... ");
			inDest = (Destination) ctx.lookup(inDestName);
			JMSTester.logger.info("Look up inbound destination ok, retrieved destination: " + inDest.toString());			
			JMSTester.logger.info("Looking up outbound destination name: '" + outDestName + "'... ");			
			outDest = (Destination) ctx.lookup(outDestName);
			JMSTester.logger.info("Look up outbound destination ok, retrieved destination: " + outDest.toString());			
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
			JMSTester.logger.info("[MESSAGE SENT]");
			JMSTester.logger.info("Automatically Assigned Headers...");
			JMSTester.logger.info("JMSMessageID: " + msg.getJMSMessageID());
			// log Application Specific Properties
			JMSTester.logger.info("Application Specific Properties...");
			Enumeration propertyNames = msg.getPropertyNames();
			while (propertyNames.hasMoreElements()) {
				String name = (String) propertyNames.nextElement();
				Object value = msg.getObjectProperty(name);
				JMSTester.logger.info(name + " = " + value);
			}
		} catch (JMSException jmse) {
			JMSTester.logger.error("Error sending MapMessage!", jmse);
			//jmse.printStackTrace();
			System.exit(1);
		}
	}

	/* Receive Messages From Consumer */
	public void onMessage(Message message) {
		// System.out.print("Received message from Topic: ");

		try {
			// automatically assigned headers
			JMSTester.logger.info("[MESSAGE RECEIVED]");
			JMSTester.logger.info("Automatically Assigned Headers...");
			JMSTester.logger.info("JMSDestination: " + message.getJMSDestination().toString());
			JMSTester.logger.info("JMSDeliveryMode: "
					+ (javax.jms.DeliveryMode.PERSISTENT == message.getJMSDeliveryMode() ? "PERSISTANT"
							: "NONPERSISTANT"));
			JMSTester.logger.info("JMSMessageID: " + message.getJMSMessageID());
			JMSTester.logger.info("JMSTimestamp: " + message.getJMSTimestamp() + " ("
					+ (System.currentTimeMillis() - message.getJMSTimestamp()) + "ms)");
			// JMSTester.logger.info("<currentTimeMillis: " + System.currentTimeMillis() +
			// ">");
			// JMSTester.logger.info("Duration (ms): " + (System.currentTimeMillis() -
			// message.getJMSTimestamp()));
			JMSTester.logger.info("JMSExpiration: " + message.getJMSExpiration());
			JMSTester.logger.info("JMSRedelivered: " + message.getJMSRedelivered());
			JMSTester.logger.info("JMSPriority: " + message.getJMSPriority());

			// log developer assigned headers
			JMSTester.logger.info("Developer Assigned Headers...");
			JMSTester.logger.info("JMSReplyTo: " + message.getJMSReplyTo());
			JMSTester.logger.info("JMSCorrelationID: " + message.getJMSCorrelationID());
			JMSTester.logger.info("JMSType: " + message.getJMSType());

			// log Properties
			JMSTester.logger.info("Properties...");
			Enumeration propertyNames = message.getPropertyNames();
			while (propertyNames.hasMoreElements()) {
				String name = (String) propertyNames.nextElement();
				Object value = message.getObjectProperty(name);
				JMSTester.logger.info(name + " = " + value);
			}
			// TextMessage textMessage = (TextMessage) message;
			// JMSTester.logger.info("Message Consumed: " + textMessage.getText());
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
			JMSTester.logger.info("Invalid arguments.");			
			System.exit(0);
		}

		JMSTester.logger.info("================== JMSTester Application Started ==============================");
		//JMSTester.logger.info("JMSTester Application Started");
		JMSTester.logger.info("Program arguments...");		
		JMSTester.logger.info("host: " + host);
		JMSTester.logger.info("port: " + port);
		JMSTester.logger.info("connFactoryName: " + connFactoryName);
		JMSTester.logger.info("inDestName: " + inDestName);
		JMSTester.logger.info("outDestName: " + outDestName);
		JMSTester.logger.info("numMessages: " + numMessages);

		// JMSTester.logger.info("Press enter to quit application");

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
//			JMSTester.logger.info("Message Produced: " + textMessage);
//		}

		MapMessage mapMessage;
		for (int i=0; i<numMessages; i++) {		
			try {
				JMSTester.logger.info("Building MapMessage " + i + " of " + numMessages);
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
				JMSTester.logger.info("Sending MapMessage...");
				jmsTester.sendMessage(mapMessage);
				//JMSTester.logger.info("[MESSAGE SENT] (JMSMessageID: " + mapMessage.getJMSMessageID() +")");
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				JMSTester.logger.error("Error sending MapMessage!", e);
			}
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
