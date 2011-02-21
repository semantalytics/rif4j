//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.15 at 06:35:01 PM MEZ 
//


package at.sti2.rif4j.test.suite;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import at.sti2.rif4j.test.suite.Combinations;
import at.sti2.rif4j.test.suite.DescriptionType;
import at.sti2.rif4j.test.suite.DialectType;
import at.sti2.rif4j.test.suite.DocumentType;
import at.sti2.rif4j.test.suite.NegativeEntailmentTest;
import at.sti2.rif4j.test.suite.PositiveEntailmentTest;
import at.sti2.rif4j.test.suite.StatusType;
import at.sti2.rif4j.test.suite.SyntaxTest;
import at.sti2.rif4j.test.suite.TestSuite;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the at.sti2.rif4j.test.suite package. 
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

    private final static QName _NegativeSyntaxTest_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "NegativeSyntaxTest");
    private final static QName _Description_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "description");
    private final static QName _Dialect_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "dialect");
    private final static QName _Status_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "status");
    private final static QName _ImportRejectionTest_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "ImportRejectionTest");
    private final static QName _Purpose_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "purpose");
    private final static QName _PositiveSyntaxTest_QNAME = new QName("http://www.w3.org/2009/10/rif-test#", "PositiveSyntaxTest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: at.sti2.rif4j.test.suite
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DescriptionType }
     * 
     */
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    /**
     * Create an instance of {@link DocumentType }
     * 
     */
    public DocumentType createDocumentType() {
        return new DocumentType();
    }

    /**
     * Create an instance of {@link TestSuite }
     * 
     */
    public TestSuite createTestSuite() {
        return new TestSuite();
    }

    /**
     * Create an instance of {@link SyntaxTest }
     * 
     */
    public SyntaxTest createSyntaxTest() {
        return new SyntaxTest();
    }

    /**
     * Create an instance of {@link Combinations }
     * 
     */
    public Combinations createCombinations() {
        return new Combinations();
    }

    /**
     * Create an instance of {@link DocumentType.Normative }
     * 
     */
    public DocumentType.Normative createDocumentTypeNormative() {
        return new DocumentType.Normative();
    }

    /**
     * Create an instance of {@link PositiveEntailmentTest }
     * 
     */
    public PositiveEntailmentTest createPositiveEntailmentTest() {
        return new PositiveEntailmentTest();
    }

    /**
     * Create an instance of {@link DocumentType.Presentation }
     * 
     */
    public DocumentType.Presentation createDocumentTypePresentation() {
        return new DocumentType.Presentation();
    }

    /**
     * Create an instance of {@link NegativeEntailmentTest }
     * 
     */
    public NegativeEntailmentTest createNegativeEntailmentTest() {
        return new NegativeEntailmentTest();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyntaxTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "NegativeSyntaxTest")
    public JAXBElement<SyntaxTest> createNegativeSyntaxTest(SyntaxTest value) {
        return new JAXBElement<SyntaxTest>(_NegativeSyntaxTest_QNAME, SyntaxTest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescriptionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "description")
    public JAXBElement<DescriptionType> createDescription(DescriptionType value) {
        return new JAXBElement<DescriptionType>(_Description_QNAME, DescriptionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DialectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "dialect")
    public JAXBElement<DialectType> createDialect(DialectType value) {
        return new JAXBElement<DialectType>(_Dialect_QNAME, DialectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "status")
    public JAXBElement<StatusType> createStatus(StatusType value) {
        return new JAXBElement<StatusType>(_Status_QNAME, StatusType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyntaxTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "ImportRejectionTest")
    public JAXBElement<SyntaxTest> createImportRejectionTest(SyntaxTest value) {
        return new JAXBElement<SyntaxTest>(_ImportRejectionTest_QNAME, SyntaxTest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DescriptionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "purpose")
    public JAXBElement<DescriptionType> createPurpose(DescriptionType value) {
        return new JAXBElement<DescriptionType>(_Purpose_QNAME, DescriptionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SyntaxTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2009/10/rif-test#", name = "PositiveSyntaxTest")
    public JAXBElement<SyntaxTest> createPositiveSyntaxTest(SyntaxTest value) {
        return new JAXBElement<SyntaxTest>(_PositiveSyntaxTest_QNAME, SyntaxTest.class, null, value);
    }

}