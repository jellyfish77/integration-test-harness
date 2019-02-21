
package webservice.soap.consume.additionapp;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ErrMessage", targetNamespace = "http://tempuri.org/calculator_ms")
public class AdditionFault1
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ErrMessage faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public AdditionFault1(String message, ErrMessage faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public AdditionFault1(String message, ErrMessage faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.tempuri.calculator_ms.ErrMessage
     */
    public ErrMessage getFaultInfo() {
        return faultInfo;
    }

}
