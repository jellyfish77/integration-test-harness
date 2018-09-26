package jms.queue.loadtest;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jms.queue.MsgListener;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/utils/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.loadtest.DynamicQReceiverLoadTest 10 queueConnectionFactory LoadTest.Q

git commit /src/jms/queue/loadtest/DynamicQReceiverLoadTest.java -m 'create new class for receiving messages for activemq queue load testing'
*/


public class DynamicQReceiverLoadTest implements Runnable {
	
	private int threadID;
	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	private Queue requestQ = null;
	private String textMessage = null;
	
	public DynamicQReceiverLoadTest(int threadID, String connFactory, String queueName) {	
		this.threadID = threadID;		
		String dynQueueName = "dynamicQueues/" + queueName + "." + threadID;
		
		try {
			System.out.print("Creating Initial Context for JNDI... ");
			Context ctx = new InitialContext();
			System.out.println("[OK]");
			System.out.print("Creating Queue Connection Factory... ");
			QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(connFactory);
			System.out.println("[Created " + qFactory.toString() + " OK]");
			System.out.print("Creating Queue Connection (to JMS provider)... ");
			qConnect = qFactory.createQueueConnection();
			System.out.println("[Created " + qConnect.toString() + " OK]");
			System.out.print("Creating queue session...");
			qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("[Created " + qSession.toString() + " OK]");
			// Lookup the request queue
			requestQ = (Queue) ctx.lookup(dynQueueName);
			// Now that setup is complete, start the Connection
			System.out.print("Starting Queue Connection... ");
			qConnect.start();
			System.out.println("[Started " + qConnect.toString() + " OK]");			
			// Create the message listener
			QueueReceiver qReceiver = qSession.createReceiver(requestQ);
			System.out.print("Starting message listener...");
			qReceiver.setMessageListener(new MsgListener());
			System.out.println("[OK]");
			System.out.println("Waiting for messages on queue '" + requestQ.getQueueName() + "' [JNDI Name: " + dynQueueName + "]...");
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			exit(1);
		} catch (NamingException jne) {
			jne.printStackTrace();
			exit(1);
		}
		
	}
	
	@Override
	public void run() {
		System.out.println("Started thread #" + threadID);		
	}

	/* 
	 * 	System.exit(system call) terminates the currently running Java virtual machine by initiating its shutdown sequence. The argument serves as a status code.
	 *
	 *	By convention, a nonzero status code indicates abnormal termination.
	 *
  	 *	System.exit(0) or EXIT_SUCCESS;  ---> Success
  	 *	System.exit(1) or EXIT_FAILURE;  ---> Exception
  	 *	System.exit(-1) or EXIT_ERROR;   ---> Error
	 */
	private void exit(int exitCode) {
		try {
			qConnect.close();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
		}
		//System.exit(exitCode);
	}

	
	public static void main(String[] args) {
		int numThreads = 0;
		String connFactory = null;
		String queueName = null;

		if (args.length <= 3) {
			numThreads = Integer.parseInt(args[0]);
			connFactory = args[1];
			queueName = args[2];			
		} else {
			System.out.println("Invalid argument(s).");
			System.exit(0);
		}

		for (int i = 0; i < numThreads; i++) {
			Thread object = new Thread(new DynamicQReceiverLoadTest(i, connFactory, queueName));
			object.start();
		}
	}
 
}
