package webservices;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;


@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)

// Invocation commands:
// java -Dfile.ending=UTF-8 -classpath /home/otto/eclipse-workspace/integration-test-harness/bin webservices.WebServicePublisher

// test by visiting: http://localhost:8888/WebServicePublisher?wsdl



@HandlerChain(file="handler-chain.xml")

public class WebServicePublisher {
	
	@WebMethod
	public String sayHello(String msg){
		return "Hello "+msg;
	}
	
	public static void main(String[] args){
		// enable logging of all communication to the console
		// https://stackoverflow.com/questions/1945618/tracing-xml-request-responses-with-jax-ws		

		// monitor the the request and response messages using system property
		// This will print both request and response messages at client side.
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		
		// At the server side, you can dump incoming requests and responses to System.out using:		
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
									
				
		// Publish the endpoint
		
		//String url = "http://localhost:8888/WebServicePublisher";
		// With 0.0.0.0 you tell it to 'listen' to incoming connections to any ip-address (associated with your 
		// computer).
		String url = "http://0.0.0.0:8888/WebServicePublisher";
		System.out.println("Service is published at: " + url);		 
		Endpoint.publish(url, new WebServicePublisher());	
		
	}
	
}
