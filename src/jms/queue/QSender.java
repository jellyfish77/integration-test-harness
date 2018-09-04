package jms.queue;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.*;
import javax.naming.*;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

import jdk.internal.org.objectweb.asm.tree.IntInsnNode;

/*
invoke with:
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory jmsINQ TextMessage 1
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory mapMessageINQ MapMessage 10
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory IIBCDCatalogINQ TextMessage /home/otto/Documents/test-data/cd-catalog.xml 3
java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin:/home/otto/eclipse-workspace/lib/activemq-all-5.15.0.jar jms.queue.QSender queueConnectionFactory IIBinvoiceINQ TextMessage /home/otto/Documents/test-data/invoice.xml 10
*/

public class QSender {

	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	// private Queue responseQ = null;
	private Queue requestQ = null;

	public QSender(String queuecf, String requestQueue) {
		try {
			// Connect to the provider and get the JMS connection
			System.out.print("Creating Initial Context for JNDI... ");
			Context ctx = new InitialContext();			
			System.out.println("[OK]");
			System.out.print("Creating Queue Connection Factory... ");
			QueueConnectionFactory qFactory = (QueueConnectionFactory) ctx.lookup(queuecf);
			System.out.println("[Created " + qFactory.toString() + " OK]");
			System.out.print("Creating Queue Connection (to JMS provider)... ");
			qConnect = qFactory.createQueueConnection();
			System.out.println("[Created " + qConnect.toString() + " OK]");
			System.out.print("Creating queue session...");
			qSession = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("[Created " + qSession.toString() + " OK]");
			System.out.print("Looking up queue \"" + requestQueue + "\" ...");
			requestQ = (Queue) ctx.lookup(requestQueue);
			System.out.println("[Found " + requestQ.toString() + " OK]");
			System.out.print("Starting Queue Connection... ");
			qConnect.start();
			System.out.println("[Started " + qConnect.toString() + " OK]");
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

	/**
	 * from:
	 * https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
	 * 
	 * @param path
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(String argv[]) {
		String queuecf = null;
		String requestq = null;
		String messageType = null;
		String msgsPath = null;
		//String xmlTextToSend = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><CATALOG><CD><TITLE>Empire Burlesque</TITLE><ARTIST>Bob Dylan</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>Columbia</COMPANY><PRICE>10.90</PRICE><YEAR>1985</YEAR></CD><CD><TITLE>Hide your heart</TITLE><ARTIST>Bonnie Tyler</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>CBS Records</COMPANY><PRICE>9.90</PRICE><YEAR>1988</YEAR></CD><CD><TITLE>Greatest Hits</TITLE><ARTIST>Dolly Parton</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>RCA</COMPANY><PRICE>9.90</PRICE><YEAR>1982</YEAR></CD><CD><TITLE>Still got the blues</TITLE><ARTIST>Gary Moore</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Virgin records</COMPANY><PRICE>10.20</PRICE><YEAR>1990</YEAR></CD><CD><TITLE>Eros</TITLE><ARTIST>Eros Ramazzotti</ARTIST><COUNTRY>EU</COUNTRY><COMPANY>BMG</COMPANY><PRICE>9.90</PRICE><YEAR>1997</YEAR></CD><CD><TITLE>One night only</TITLE><ARTIST>Bee Gees</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Polydor</COMPANY><PRICE>10.90</PRICE><YEAR>1998</YEAR></CD><CD><TITLE>Sylvias Mother</TITLE><ARTIST>Dr.Hook</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>CBS</COMPANY><PRICE>8.10</PRICE><YEAR>1973</YEAR></CD><CD><TITLE>Maggie May</TITLE><ARTIST>Rod Stewart</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Pickwick</COMPANY><PRICE>8.50</PRICE><YEAR>1990</YEAR></CD><CD><TITLE>Romanza</TITLE><ARTIST>Andrea Bocelli</ARTIST><COUNTRY>EU</COUNTRY><COMPANY>Polydor</COMPANY><PRICE>10.80</PRICE><YEAR>1996</YEAR></CD><CD><TITLE>When a man loves a woman</TITLE><ARTIST>Percy Sledge</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>Atlantic</COMPANY><PRICE>8.70</PRICE><YEAR>1987</YEAR></CD><CD><TITLE>Black angel</TITLE><ARTIST>Savage Rose</ARTIST><COUNTRY>EU</COUNTRY><COMPANY>Mega</COMPANY><PRICE>10.90</PRICE><YEAR>1995</YEAR></CD><CD><TITLE>1999 Grammy Nominees</TITLE><ARTIST>Many</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>Grammy</COMPANY><PRICE>10.20</PRICE><YEAR>1999</YEAR></CD><CD><TITLE>For the good times</TITLE><ARTIST>Kenny Rogers</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Mucik Master</COMPANY><PRICE>8.70</PRICE><YEAR>1995</YEAR></CD><CD><TITLE>Big Willie style</TITLE><ARTIST>Will Smith</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>Columbia</COMPANY><PRICE>9.90</PRICE><YEAR>1997</YEAR></CD><CD><TITLE>Tupelo Honey</TITLE><ARTIST>Van Morrison</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Polydor</COMPANY><PRICE>8.20</PRICE><YEAR>1971</YEAR></CD><CD><TITLE>Soulsville</TITLE><ARTIST>Jorn Hoel</ARTIST><COUNTRY>Norway</COUNTRY><COMPANY>WEA</COMPANY><PRICE>7.90</PRICE><YEAR>1996</YEAR></CD><CD><TITLE>The very best of</TITLE><ARTIST>Cat Stevens</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Island</COMPANY><PRICE>8.90</PRICE><YEAR>1990</YEAR></CD><CD><TITLE>Stop</TITLE><ARTIST>Sam Brown</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>A and M</COMPANY><PRICE>8.90</PRICE><YEAR>1988</YEAR></CD><CD><TITLE>Bridge of Spies</TITLE><ARTIST>T'Pau</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Siren</COMPANY><PRICE>7.90</PRICE><YEAR>1987</YEAR></CD><CD><TITLE>Private Dancer</TITLE><ARTIST>Tina Turner</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>Capitol</COMPANY><PRICE>8.90</PRICE><YEAR>1983</YEAR></CD><CD><TITLE>Midt om natten</TITLE><ARTIST>Kim Larsen</ARTIST><COUNTRY>EU</COUNTRY><COMPANY>Medley</COMPANY><PRICE>7.80</PRICE><YEAR>1983</YEAR></CD><CD><TITLE>Pavarotti Gala Concert</TITLE><ARTIST>Luciano Pavarotti</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>DECCA</COMPANY><PRICE>9.90</PRICE><YEAR>1991</YEAR></CD><CD><TITLE>The dock of the bay</TITLE><ARTIST>Otis Redding</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>Stax Records</COMPANY><PRICE>7.90</PRICE><YEAR>1968</YEAR></CD><CD><TITLE>Picture book</TITLE><ARTIST>Simply Red</ARTIST><COUNTRY>EU</COUNTRY><COMPANY>Elektra</COMPANY><PRICE>7.20</PRICE><YEAR>1985</YEAR></CD><CD><TITLE>Red</TITLE><ARTIST>The Communards</ARTIST><COUNTRY>UK</COUNTRY><COMPANY>London</COMPANY><PRICE>7.80</PRICE><YEAR>1987</YEAR></CD><CD><TITLE>Unchain my heart</TITLE><ARTIST>Joe Cocker</ARTIST><COUNTRY>USA</COUNTRY><COMPANY>EMI</COMPANY><PRICE>8.20</PRICE><YEAR>1987</YEAR></CD></CATALOG>";
		int numMessages = 0;
		if (argv.length <= 5) {
			queuecf = argv[0];
			requestq = argv[1];
			messageType = argv[2];
			msgsPath = argv[3];
			numMessages = Integer.parseInt(argv[4]);
		} else {
			System.out.println("Invalid argument(s).");
			System.exit(0);
		}
		QSender qSender = new QSender(queuecf, requestq);
		try {
			System.out.println("QSender Application Started");
			// qSender.qSession.createSender(qSender.requestQ).send(msg);
			System.out.print("Creating QueueSender object for Queue '" + qSender.requestQ.getQueueName() + "' "
					+ " [JNDI Name: " + requestq + "]...");
			QueueSender queueSender = qSender.qSession.createSender(qSender.requestQ);
			System.out.println("[OK]");
			for (int i = 0; i < numMessages; i++) {
				System.out.print("Sending " + " file(s) in folder \"" + msgsPath + " as " + messageType + "(s), iteration " + " " + (i + 1) + " of " + numMessages + "...");
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				System.out.println("Local Time: " + dateFormat.format(date));

				if (messageType.equals("TextMessage")) {										
					File[] files = new File(msgsPath).listFiles();
					//If this pathname does not denote a directory, then listFiles() returns null. 

					for (File file : files) {
					    if (file.isFile()) {
					        //results.add(file.getName());
					    	TextMessage textMessage;
							try {
								textMessage = qSender.qSession.createTextMessage(readFile(file.getAbsolutePath(), StandardCharsets.UTF_8));
								//textMessage.setJMSType(messageType);
								textMessage.setJMSType(messageType); // why?
								System.out.print("Created " + textMessage.getJMSType() + " for file \"" + file.getName() + "\", sending... ");
								queueSender.send(textMessage);
							} catch (IOException e) {							
								e.printStackTrace();
								continue; // skip current for iteration
							}
					    }
					}
				}
				/*
				if (messageType.equals("cdCatalogMessage")) {
					TextMessage textMessage = qSender.qSession.createTextMessage(xmlTextToSend);
					textMessage.setJMSType("TextMessage");
					System.out.print("Created " + textMessage.getJMSType() + " " + (i + 1) + " of " + numMessages
							+ ", sending... ");
					queueSender.send(textMessage);
				}
				*/
				if (messageType.equals("MapMessage")) {
					MapMessage mapMessage;
					mapMessage = qSender.qSession.createMapMessage();
					mapMessage.setJMSType(messageType);
					System.out.print("Created " + mapMessage.getJMSType() + " " + (i + 1) + " of " + numMessages
							+ ", sending... ");
					mapMessage.setInt("Age", 88);
					mapMessage.setFloat("Weight", 234);
					mapMessage.setString("Name", "Smith");
					mapMessage.setObject("Height", new Double(150.32));
					mapMessage.setStringProperty("sender", "integration-test-harness");
					mapMessage.setBooleanProperty("some_bool", true);
					mapMessage.setStringProperty("JMSXAppID", "eclipse");
					mapMessage.setStringProperty("JMSXGroupID", "ERF-001");
					// jmsTester.session.createProducer(jmsTester.inDest).send(mapMessage);
					// mapMessage.setJMSReplyTo(jmsTester.outDest);
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
