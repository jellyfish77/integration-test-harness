package jms.queue;

import java.io.*;
import javax.jms.*;
import javax.naming.*;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory jmsINQ TextMessage 1
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory mapMessageINQ MapMessage 10

*/

public class QSender {

	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	// private Queue responseQ = null;
	private Queue requestQ = null;

	public QSender(String queuecf, String requestQueue) {
		try {
			// Connect to the provider and get the JMS connection
			Context ctx = new InitialContext();
			QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(queuecf);
			qConnect = qFactory.createQueueConnection();
			// Create the JMS Session
			System.out.print("Creating queue session...");
			qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("[OK]");
			// Lookup the request and response queues
			requestQ = (Queue) ctx.lookup(requestQueue);
			// responseQ = (Queue) ctx.lookup(responseQueue);
			// Now that setup is complete, start the Connection
			qConnect.start();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		} catch (NamingException jne) {
			jne.printStackTrace();
			System.exit(1);
		}
	}

	private void exit() {
		try {
			qConnect.close();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
		System.exit(0);
	}

	public static void main(String argv[]) {
		String queuecf = null;
		String requestq = null;
		String messageType = null;
		int numMessages = 0;
		if (argv.length == 4) {
			queuecf = argv[0];
			requestq = argv[1];
			messageType = argv[2];
			numMessages = Integer.parseInt(argv[3]);
		} else {
			System.out.println("Invalid arguments. Should be: ");
			System.out.println("java QSender factory IN_Queue numMessages");
			System.exit(0);
		}
		QSender qSender = new QSender(queuecf, requestq);
		try {
			System.out.println("QSender Application Started");
			// qSender.qSession.createSender(qSender.requestQ).send(msg);
			System.out.print("Creating sender object...");
			QueueSender queueSender = qSender.qSession.createSender(qSender.requestQ);
			System.out.println("[OK]");
			System.out.println("Sending messages on queue '" + qSender.requestQ.getQueueName() + "' [JNDI Name: "
					+ requestq + "]...");
			for (int i = 0; i < numMessages; i++) {
				System.out.print("Sending " + messageType + " " + (i + 1) + " of " + numMessages + "...");
				if (messageType.equals("TextMessage")) {
					// send the text messages
					TextMessage textMessage = qSender.qSession.createTextMessage("Message #" + (i + 1));
					textMessage.setJMSType(messageType); 
					System.out.print("Created " + textMessage.getJMSType() + " " + (i + 1) + " of " + numMessages + ", sending... ");
					queueSender.send(textMessage);					
				}
				if (messageType.equals("MapMessage")) {
					// send the map messages
					MapMessage mapMessage;
					mapMessage = qSender.qSession.createMapMessage();
					mapMessage.setJMSType(messageType);
					System.out.print("Created " + mapMessage.getJMSType() + " " + (i + 1) + " of " + numMessages + ", sending... ");
					mapMessage.setInt("Age", 88);
					mapMessage.setFloat("Weight", 234);
					mapMessage.setString("Name", "Smith");
					mapMessage.setObject("Height", new Double(150.32));
					mapMessage.setStringProperty("sender", "integration-test-harness");
					mapMessage.setBooleanProperty("some_bool", true);
					mapMessage.setStringProperty("JMSXAppID", "eclipse");
					mapMessage.setStringProperty("JMSXGroupID", "ERF-001");
					// jmsTester.session.createProducer(jmsTester.inDest).send(mapMessage);
					//mapMessage.setJMSReplyTo(jmsTester.outDest);
					queueSender.send(mapMessage);
				}				

				System.out.println("[OK]");
			}
			qSender.exit();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}
}
