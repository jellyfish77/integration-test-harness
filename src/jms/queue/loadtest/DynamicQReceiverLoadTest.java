package jms.queue.loadtest;

import java.io.IOException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
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
import jms.queue.QReceiver;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/utils/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.loadtest.DynamicQReceiverLoadTest 10 queueConnectionFactory LoadTest.Q

git commit ./src/jms/queue/loadtest/DynamicQReceiverLoadTest.java -m 'message'
*/


public class DynamicQReceiverLoadTest implements Runnable {
	
	private int threadID;
	private QueueConnectionFactory qFactory = null;
	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	private Queue requestQ = null;	
	
	public DynamicQReceiverLoadTest(int threadID, String connFactory, String queueName) {	
		this.threadID = threadID;		
		String dynQueueName = "dynamicQueues/" + queueName + "." + threadID;
		//String dynQueueName = queueName + "." + threadID;
		
		try {
			System.out.print("Creating Initial Context for JNDI... ");
			Context ctx = new InitialContext();
			System.out.println("[OK]");
			System.out.print("Creating Queue Connection Factory... ");
			qFactory = (ActiveMQConnectionFactory) ctx.lookup(connFactory); // use ActiveMQ API instead ofJMS API
			System.out.println("[Created " + qFactory.toString() + " OK]");
			System.out.print("Creating Queue Connection (to JMS provider)... ");
			qConnect = (ActiveMQConnection) qFactory.createQueueConnection(); // use ActiveMQ API instead ofJMS API
			System.out.println("[Created " + qConnect.toString() + " OK]");
			System.out.print("Creating queue session...");
			qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("[Created " + qSession.toString() + " OK]");
			
			// Get queue object from provider
			System.out.print("Creating queue \"" + dynQueueName + "\"... ");
			requestQ = (Queue) qSession.createQueue(dynQueueName);
			System.out.println("[OK]");
			
			// Now that setup is complete, start the Connection
			System.out.print("Starting Queue Connection... ");
			qConnect.start();
			System.out.println("[Started " + qConnect.toString() + " OK]");
			
			// create consumer for queue
			QueueReceiver qReceiver = qSession.createReceiver(requestQ);
			
			// set the message listener class for consumer
			System.out.print("Starting message listener for messages on queue '" + requestQ.getQueueName() + "' [JNDI Name: " + dynQueueName + "]...");
			qReceiver.setMessageListener(new MsgListener());			
			System.out.println("[OK]");
			
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
		//do some work that responds to interruption
		System.out.println("Started thread #" + threadID);
				
		/*
		while (more messages) {
		 // receive message	
		}
		*/		
		
		if(Thread.currentThread().isInterrupted()) {
	        //clean up
			System.out.println("\nInterrupt detected on thread #" + threadID);
	        return;	    
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
			System.out.println("System.exit("+exitCode+") called to init VM shutdown...");
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
			Thread object = new Thread(new DynamicQReceiverLoadTest(i, connFactory, queueName));
			object.start();
		}
		
		// loop to check if 
	}
 
}
