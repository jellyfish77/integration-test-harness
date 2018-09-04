package jms.queue;

import java.io.*;
import javax.jms.*;
import javax.naming.*;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QReceiver queueConnectionFactory jmsOUTQ
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QReceiver queueConnectionFactory mapMessageOUTQ
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QReceiver queueConnectionFactory IIBCDCatalogOUTQ
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QReceiver queueConnectionFactory IIBinvoiceOUTQ 
*/

public class QReceiver {

	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	private Queue requestQ = null;

	public QReceiver(String queuecf, String requestQueue) {
		try {
			// Connect to the provider and get the JMS connection
			Context ctx = new InitialContext();
			QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(queuecf);
			qConnect = qFactory.createQueueConnection();
			// Create the JMS Session
			qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			// Lookup the request queue
			requestQ = (Queue) ctx.lookup(requestQueue);
			// Now that setup is complete, start the Connection
			qConnect.start();
			// Create the message listener
			QueueReceiver qReceiver = qSession.createReceiver(requestQ);
			System.out.print("Starting message listener...");
			qReceiver.setMessageListener(new MsgListener());
			System.out.println("[OK]");
			System.out.println("Waiting for messages on queue '" + requestQ.getQueueName() + "' [JNDI Name: " + requestQueue + "]...");
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
		if (argv.length == 2) {
			queuecf = argv[0];
			requestq = argv[1];
		} else {
			System.out.println("Invalid arguments. Should be: ");
			System.out.println("java QReceiver connFactory OUTQueue");
			System.exit(0);
		}
		QReceiver lender = new QReceiver(queuecf, requestq);
		try {
			// Run until enter is pressed
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("QReceiver application started");
			System.out.println("Press enter to quit application\n");
			stdin.readLine();
			lender.exit();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
