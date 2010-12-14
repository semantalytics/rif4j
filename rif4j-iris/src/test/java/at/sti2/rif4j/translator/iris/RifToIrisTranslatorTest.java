package at.sti2.rif4j.translator.iris;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.Namespaces;
import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.XmlSchemaDatatype;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;
import at.sti2.rif4j.translator.iris.visitor.ExpressionFlattener;

public class RifToIrisTranslatorTest {

	public static String TEST_DIR = "core/testCases/";
	private int varNumber;
	
	@Before
	public void setUp() throws Exception {
		varNumber = ExpressionFlattener.getCurrentVariableNumber() + 1;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTranslateRule() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuiltin_literal_not_identical_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtin_literal-not-identical-premise.rif";

		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- NOT_EXACT_EQUAL(1, '1'), NOT_EXACT_EQUAL(1, 2), NOT_EXACT_EQUAL(Hello world@, 'Hello world@').");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);		
	}

	@Test
	public void testBuiltin_anyURI_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_anyURI-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();		
		expectedRules.add("http://example.org/example#ok() :- IS_ANYURI(http://www.example.org), IS_NOT_ANYURI(1), IS_ANYURI(?var"+ varNumber +"), TO_ANYURI('http://www.example.org', ?var"+ varNumber +").");
	
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);				
	}

	@Test
	public void testBuiltin_Binary_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_Binary-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- IS_HEXBINARY(HexBinary(AABB)), IS_BASE64BINARY(Base64Binary(ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz/+0123456789)), IS_NOT_BASE64BINARY('foo'), IS_BASE64BINARY(?var"+ varNumber +"), TO_BASE64('ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz/+0123456789', ?var"+ varNumber +").");
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);			
	}
	
	@Test
	public void testBuiltin_boolean_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_boolean-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- IS_BOOLEAN(true), IS_NOT_BOOLEAN('foo'), IS_BOOLEAN(?var"+ varNumber +"), TO_BOOLEAN('1', ?var"+ varNumber +"), BOOLEAN_EQUAL(false, false), BOOLEAN_LESS(false, true), BOOLEAN_GREATER(true, false).");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}

	@Test
	public void testBuiltin_List_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_List-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- IS_BOOLEAN(true), IS_NOT_BOOLEAN('foo'), IS_BOOLEAN(?var"+ varNumber +"), TO_BOOLEAN('1', ?var"+ varNumber +"), BOOLEAN_EQUAL(false, false), BOOLEAN_LESS(false, true), BOOLEAN_GREATER(true, false).");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}
	
	@Test
	public void testBuiltin_Numeric_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_Numeric-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- IS_BOOLEAN(true), IS_NOT_BOOLEAN('foo'), IS_BOOLEAN(?var"+ varNumber +"), TO_BOOLEAN('1', ?var"+ varNumber +"), BOOLEAN_EQUAL(false, false), BOOLEAN_LESS(false, true), BOOLEAN_GREATER(true, false).");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}

	@Test
	public void testBuiltin_boolean_TO_Boolean_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_boolean_TO_Boolean-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- IS_BOOLEAN(?var"+ varNumber +"), TO_BOOLEAN('1', ?var"+ varNumber +").");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}

//	@Test
//	public void testMakeXSD()
//	{
//		XmlSchemaDatatype [] types = XmlSchemaDatatype.values();
//		for (int i = 0; i < types.length; i++) {
//			System.out.println("TO_" + types[i].name().toUpperCase() + "(Namespaces.XSD_NAMESPACE, XmlSchemaDatatype." + types[i].name().toUpperCase() + ".getName()),");
//		}
//
//		for (int i = 0; i < types.length; i++) {
//			System.out.println("case TO_" + types[i].name().toUpperCase() + ":");
//			System.out.println("	return factory.createTo" + types[i].name().toLowerCase() + "(terms);");
//		}
//		
//		System.out.println(XmlSchemaDatatype.STRING.toString());
//		System.out.println(XmlSchemaDatatype.STRING.getUri());
//		System.out.println(XmlSchemaDatatype.STRING.getName());
//	}
	
	private void testTranslation(String rifXmlFileName, List<String> expectedFacts, List<String> expectedRules) throws SAXException, IOException, ParserConfigurationException
	{
		Document rifDocument = getRIFDocument(rifXmlFileName);
		System.out.println("Translating rif document: " + rifXmlFileName +  "******");
		showPresentationSyntax(rifDocument);			
		
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(rifDocument);	
		
		List<IRule> rules = translator.getRules();
		Map<IPredicate, IRelation> facts = translator.getFacts();

		assertEquals("Unexpected number of facts", expectedFacts.size(),expectedFacts.size());
		assertEquals("Unexpected number of rules", expectedRules.size(),rules.size());		
		
		boolean allRulesContained = true;

		//TODO Check facts
		
		for (IRule iRule : rules) 
		{
			System.out.println(iRule.toString());
			allRulesContained &= expectedRules.contains(iRule.toString());
		}
		
		assertTrue(allRulesContained);		
		System.out.println("****************");
	}
	
	private Document getRIFDocument(String rifXmlFileName) throws SAXException,
			IOException, ParserConfigurationException {
		Reader rifXmlFileReader = TestUtils.getFileReader(rifXmlFileName);
		assertNotNull("Test file " + rifXmlFileName + " could not be found",
				rifXmlFileReader);

		XmlParser parser = new XmlParser(true);
		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		return rifDocument;
	}
	
	private void showPresentationSyntax(Document rifDocument) {	
		PresentationSerializer serializer = new PresentationSerializer();		
		String serialization = serializer.serialize(rifDocument);
		System.out.println(serialization);
	}
}
