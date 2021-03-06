
package webservice.soap.consume.additionapp;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "calculator_msPortType", targetNamespace = "http://tempuri.org/calculator_ms")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface CalculatorMsPortType {


    /**
     * 
     * @param reqAdd
     * @return
     *     returns org.tempuri.calculator_ms.ResAdd
     * @throws AdditionFault1
     */
    @WebMethod(operationName = "Addition", action = "Addition")
    @WebResult(name = "ResAdd", targetNamespace = "http://tempuri.org/calculator_ms", partName = "ResAdd")
    public ResAdd addition(
        @WebParam(name = "ReqAdd", targetNamespace = "http://tempuri.org/calculator_ms", partName = "ReqAdd")
        ReqAdd reqAdd)
        throws AdditionFault1
    ;

}
