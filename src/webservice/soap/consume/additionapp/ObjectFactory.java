
package webservice.soap.consume.additionapp;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tempuri.calculator_ms package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddC_QNAME = new QName("http://tempuri.org/calculator_ms", "addC");
    private final static QName _Fcode_QNAME = new QName("http://tempuri.org/calculator_ms", "fcode");
    private final static QName _ErrMessage_QNAME = new QName("http://tempuri.org/calculator_ms", "ErrMessage");
    private final static QName _IntA_QNAME = new QName("http://tempuri.org/calculator_ms", "intA");
    private final static QName _Fmessage_QNAME = new QName("http://tempuri.org/calculator_ms", "fmessage");
    private final static QName _IntB_QNAME = new QName("http://tempuri.org/calculator_ms", "intB");
    private final static QName _ReqAdd_QNAME = new QName("http://tempuri.org/calculator_ms", "ReqAdd");
    private final static QName _ResAdd_QNAME = new QName("http://tempuri.org/calculator_ms", "ResAdd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tempuri.calculator_ms
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResAdd }
     * 
     */
    public ResAdd createResAdd() {
        return new ResAdd();
    }

    /**
     * Create an instance of {@link ReqAdd }
     * 
     */
    public ReqAdd createReqAdd() {
        return new ReqAdd();
    }

    /**
     * Create an instance of {@link ErrMessage }
     * 
     */
    public ErrMessage createErrMessage() {
        return new ErrMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "addC")
    public JAXBElement<Integer> createAddC(Integer value) {
        return new JAXBElement<Integer>(_AddC_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "fcode")
    public JAXBElement<String> createFcode(String value) {
        return new JAXBElement<String>(_Fcode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "ErrMessage")
    public JAXBElement<ErrMessage> createErrMessage(ErrMessage value) {
        return new JAXBElement<ErrMessage>(_ErrMessage_QNAME, ErrMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "intA")
    public JAXBElement<Integer> createIntA(Integer value) {
        return new JAXBElement<Integer>(_IntA_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "fmessage")
    public JAXBElement<String> createFmessage(String value) {
        return new JAXBElement<String>(_Fmessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "intB")
    public JAXBElement<Integer> createIntB(Integer value) {
        return new JAXBElement<Integer>(_IntB_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReqAdd }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "ReqAdd")
    public JAXBElement<ReqAdd> createReqAdd(ReqAdd value) {
        return new JAXBElement<ReqAdd>(_ReqAdd_QNAME, ReqAdd.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResAdd }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/calculator_ms", name = "ResAdd")
    public JAXBElement<ResAdd> createResAdd(ResAdd value) {
        return new JAXBElement<ResAdd>(_ResAdd_QNAME, ResAdd.class, null, value);
    }

}
