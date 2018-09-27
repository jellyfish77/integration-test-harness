package jms.queue.loadtest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jms.queue.QSender;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/utils/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.loadtest.DynamicQSenderLoadTest 10 queueConnectionFactory LoadTest.Q /home/otto/Documents/test-data/invoice.xml 1000

git:
git commit ./src/jms/queue/loadtest/DynamicQSenderLoadTest.java -m 'message'  
*/

public class DynamicQSenderLoadTest implements Runnable {

	private int threadID;
	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	private Queue requestQ = null;
	private String textMessage = null;
	private int numMessages = 0;

	public DynamicQSenderLoadTest(int threadID, String connFactory, String queueName, String textMsg, int numMessages) {
		this.threadID = threadID;
		this.textMessage = textMsg;
		String dynQueueName = "dynamicQueues/" + queueName + "." + threadID;
		this.numMessages = numMessages;
		
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

			System.out.print("Creating queue \"" + dynQueueName + "\"... ");
			requestQ = (Queue) qSession.createQueue(dynQueueName);
			System.out.println("[OK]");

			System.out.print("Starting Queue Connection... ");
			qConnect.start();
			System.out.println("[Started " + qConnect.toString() + " OK]");
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
				
		try {
			QueueSender queueSender = qSession.createSender(requestQ);
			TextMessage textMessage = qSession.createTextMessage(this.textMessage);
			for (int i=0; i<numMessages; i++) {
				queueSender.send(textMessage);
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		int numMessages = 0;
		String msgFilePath = null;
		String textMessage = null;

		if (args.length <= 6) {
			numThreads = Integer.parseInt(args[0]);
			connFactory = args[1];
			queueName = args[2];
			msgFilePath = args[3];
			numMessages = Integer.parseInt(args[4]);
		} else {
			System.out.println("Invalid argument(s).");
			System.exit(0);
		}

		try {
			textMessage = io.File.readContents(msgFilePath, java.nio.charset.StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// catch VM termination from user interrupt (ctrl+c) or system event
		// from: https://stackoverflow.com/questions/1611931/catching-ctrlc-in-java
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run() {
	            try {
	                Thread.sleep(200);
	                System.out.println("\nInterrupt detected, shutting down VM...");
	                
	                //some cleaning up code...

	            } catch (InterruptedException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    });
		
		
		for (int i = 0; i < numThreads; i++) {
			Thread object = new Thread(new DynamicQSenderLoadTest(i, connFactory, queueName, textMessage, numMessages));
			object.start();
		}
	}
}
