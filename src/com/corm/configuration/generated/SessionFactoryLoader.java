//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.06 at 06:38:06 PM PDT 
//


package com.corm.configuration.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for session-factory complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="session-factory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="property" type="{http://www.example.org/DSTAX-ORM-CONFIGURATION}property" maxOccurs="unbounded"/>
 *         &lt;element name="mapping" type="{http://www.example.org/DSTAX-ORM-CONFIGURATION}mapping" maxOccurs="unbounded"/>
 *         &lt;element name="strategy" type="{http://www.example.org/DSTAX-ORM-CONFIGURATION}strategy" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="scan-path" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "session-factory", propOrder = {
    "property",
    "mapping",
    "strategy"
})
public class SessionFactoryLoader {

    @XmlElement(required = true)
    protected List<Property> property;
    @XmlElement(required = true)
    protected List<Mapping> mapping;
    @XmlElement(required = true)
    protected List<Strategy> strategy;
    @XmlAttribute(name = "scan-path", required = true)
    protected String scanPath;

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Property }
     * 
     * 
     */
    public List<Property> getProperty() {
        if (property == null) {
            property = new ArrayList<Property>();
        }
        return this.property;
    }

    /**
     * Gets the value of the mapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mapping }
     * 
     * 
     */
    public List<Mapping> getMapping() {
        if (mapping == null) {
            mapping = new ArrayList<Mapping>();
        }
        return this.mapping;
    }

    /**
     * Gets the value of the strategy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the strategy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStrategy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Strategy }
     * 
     * 
     */
    public List<Strategy> getStrategy() {
        if (strategy == null) {
            strategy = new ArrayList<Strategy>();
        }
        return this.strategy;
    }

    /**
     * Gets the value of the scanPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScanPath() {
        return scanPath;
    }

    /**
     * Sets the value of the scanPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScanPath(String value) {
        this.scanPath = value;
    }

}
