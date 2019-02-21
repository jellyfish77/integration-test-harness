
package webservice.soap.consume.additionapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResAdd complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResAdd">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://tempuri.org/calculator_ms}addC"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResAdd", propOrder = {
    "addC"
})
public class ResAdd {

    protected int addC;

    /**
     * Gets the value of the addC property.
     * 
     */
    public int getAddC() {
        return addC;
    }

    /**
     * Sets the value of the addC property.
     * 
     */
    public void setAddC(int value) {
        this.addC = value;
    }

}
