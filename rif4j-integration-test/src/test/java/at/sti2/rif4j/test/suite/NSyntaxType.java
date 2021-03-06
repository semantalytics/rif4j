//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.15 at 06:35:01 PM MEZ 
//


package at.sti2.rif4j.test.suite;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import at.sti2.rif4j.test.suite.NSyntaxType;


/**
 * <p>Java class for nSyntaxType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="nSyntaxType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="RIF/XML"/>
 *     &lt;enumeration value="RDF/XML"/>
 *     &lt;enumeration value="NTriples"/>
 *     &lt;enumeration value="Turtle"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "nSyntaxType")
@XmlEnum
public enum NSyntaxType {

    @XmlEnumValue("RIF/XML")
    RIF_XML("RIF/XML"),
    @XmlEnumValue("RDF/XML")
    RDF_XML("RDF/XML"),
    @XmlEnumValue("NTriples")
    N_TRIPLES("NTriples"),
    @XmlEnumValue("Turtle")
    TURTLE("Turtle");
    private final String value;

    NSyntaxType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NSyntaxType fromValue(String v) {
        for (NSyntaxType c: NSyntaxType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
