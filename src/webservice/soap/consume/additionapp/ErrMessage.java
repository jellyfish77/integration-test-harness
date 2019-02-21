
package webservice.soap.consume.additionapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ErrMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tempuri.org/calculator_ms}fcode"/>
 *         &lt;element ref="{http://tempuri.org/calculator_ms}fmessage"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrMessage", propOrder = {
    "fcode",
    "fmessage"
})
public class ErrMessage {

    @XmlElement(required = true)
    protected String fcode;
    @XmlElement(required = true)
    protected String fmessage;

    /**
     * Gets the value of the fcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFcode() {
        return fcode;
    }

    /**
     * Sets the value of the fcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFcode(String value) {
        this.fcode = value;
    }

    /**
     * Gets the value of the fmessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFmessage() {
        return fmessage;
    }

    /**
     * Sets the value of the fmessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFmessage(String value) {
        this.fmessage = value;
    }

}
