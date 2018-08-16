package jms.queue;

import java.io.*;
import javax.jms.*;
import javax.naming.*;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory jmsINQ 1
*/

public class QSender {

	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	//private Queue responseQ = null;
	private Queue requestQ = null;

	public QSender(String queuecf, String requestQueue) {
		try {
			// Connect to the provider and get the JMS connection
			Context ctx = new InitialContext();
			QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(queuecf);
			qConnect = qFactory.createQueueConnection();
			// Create the JMS Session
			qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			// Lookup the request and response queues
			requestQ = (Queue) ctx.lookup(requestQueue);
			//responseQ = (Queue) ctx.lookup(responseQueue);
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
		int numMessages = 0;
		if (argv.length == 3) {
			queuecf = argv[0];
			requestq = argv[1];			
			numMessages = Integer.parseInt(argv[2]);
		} else {
			System.out.println("Invalid arguments. Should be: ");
			System.out.println("java QSender factory queue numMessages");
			System.exit(0);
		}
		QSender qSender = new QSender(queuecf, requestq);
		try {
			System.out.println("QSender Application Started");			
			Message msg = qSender.qSession.createTextMessage("goober");
			//qSender.qSession.createSender(qSender.requestQ).send(msg);
			QueueSender queueSender = qSender.qSession.createSender(qSender.requestQ);
			for (int i=0; i<numMessages; i++) {
				System.out.print("Sending message #" + i + "...");			
				queueSender.send(msg);
				System.out.println("[OK]");
			}
			qSender.exit();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
	}
}
