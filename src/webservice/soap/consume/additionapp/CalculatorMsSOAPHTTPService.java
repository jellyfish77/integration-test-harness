
package webservice.soap.consume.additionapp;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "calculator_msSOAP_HTTP_Service", targetNamespace = "http://tempuri.org/calculator_ms", wsdlLocation = "file:/home/otto/eclipse-jee-workspace/integration-test-harness/src/webservice/soap/consume/additionapp/calculator_msService.wsdl")
public class CalculatorMsSOAPHTTPService
    extends Service
{

    private final static URL CALCULATORMSSOAPHTTPSERVICE_WSDL_LOCATION;
    private final static WebServiceException CALCULATORMSSOAPHTTPSERVICE_EXCEPTION;
    private final static QName CALCULATORMSSOAPHTTPSERVICE_QNAME = new QName("http://tempuri.org/calculator_ms", "calculator_msSOAP_HTTP_Service");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/home/otto/eclipse-jee-workspace/integration-test-harness/src/webservice/soap/consume/additionapp/calculator_msService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CALCULATORMSSOAPHTTPSERVICE_WSDL_LOCATION = url;
        CALCULATORMSSOAPHTTPSERVICE_EXCEPTION = e;
    }

    public CalculatorMsSOAPHTTPService() {
        super(__getWsdlLocation(), CALCULATORMSSOAPHTTPSERVICE_QNAME);
    }

    public CalculatorMsSOAPHTTPService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CALCULATORMSSOAPHTTPSERVICE_QNAME, features);
    }

    public CalculatorMsSOAPHTTPService(URL wsdlLocation) {
        super(wsdlLocation, CALCULATORMSSOAPHTTPSERVICE_QNAME);
    }

    public CalculatorMsSOAPHTTPService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CALCULATORMSSOAPHTTPSERVICE_QNAME, features);
    }

    public CalculatorMsSOAPHTTPService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CalculatorMsSOAPHTTPService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CalculatorMsPortType
     */
    @WebEndpoint(name = "calculator_msSOAP_HTTP_Port")
    public CalculatorMsPortType getCalculatorMsSOAPHTTPPort() {
        return super.getPort(new QName("http://tempuri.org/calculator_ms", "calculator_msSOAP_HTTP_Port"), CalculatorMsPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CalculatorMsPortType
     */
    @WebEndpoint(name = "calculator_msSOAP_HTTP_Port")
    public CalculatorMsPortType getCalculatorMsSOAPHTTPPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/calculator_ms", "calculator_msSOAP_HTTP_Port"), CalculatorMsPortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CALCULATORMSSOAPHTTPSERVICE_EXCEPTION!= null) {
            throw CALCULATORMSSOAPHTTPSERVICE_EXCEPTION;
        }
        return CALCULATORMSSOAPHTTPSERVICE_WSDL_LOCATION;
    }

}