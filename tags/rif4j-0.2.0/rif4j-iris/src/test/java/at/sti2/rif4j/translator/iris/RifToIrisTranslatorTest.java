package at.sti2.rif4j.translator.iris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
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

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;
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

	@Test
	public void testBuiltin_PlainLiteral_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_PlainLiteral-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
//		expectedRules.add("http://example.org/example#ok() :- IS_BOOLEAN(?var"+ varNumber +"), TO_BOOLEAN('1', ?var"+ varNumber +").");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}
	
	@Test
	public void testBuiltin_PlainLiteral_premise_simplified() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_PlainLiteral-premise_simplified.rif";
		
		List<String> expectedRules = new ArrayList<String>();
//		expectedRules.add("http://example.org/example#ok() :- IS_BOOLEAN(?var"+ varNumber +"), TO_BOOLEAN('1', ?var"+ varNumber +").");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}

	@Test
	public void testBuiltin_String_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_String-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- IS_STRING(Hello world@), IS_STRING('Hello world'), " +
				"IS_NORMALIZEDSTRING('Hello world'), IS_TOKEN('Hello world'), IS_LANGUAGE(en), IS_NAME(Hello), " +
				"IS_NCNAME(Hello), IS_NMTOKEN(Hello), IS_NOT_STRING(1), IS_NOT_NORMALIZEDSTRING(1), IS_NOT_TOKEN(1), " +
				"IS_NOT_LANGUAGE(1), IS_NOT_NAME(1), IS_NOT_NCNAME(1), IS_NOT_NMTOKEN(1), IS_STRING(?var"+ varNumber +"), " +
				"TO_STRING(1, ?var"+ varNumber +"), IS_NORMALIZEDSTRING(?var"+ varNumber +"), TO_INTEGER(1, ?var"+ varNumber +"), IS_TOKEN(?var"+ varNumber +"), " +
				"TO_INTEGER(HexBinary(DE), ?var"+ varNumber +"), IS_LANGUAGE(?var"+ varNumber +"), TO_INTEGER(HexBinary(DE), ?var"+ varNumber +"), " +
				"IS_NAME(?var"+ varNumber +"), TO_INTEGER(HexBinary(DE), ?var"+ varNumber +"), IS_NCNAME(?var"+ varNumber +"), " +
				"TO_INTEGER(HexBinary(DE), ?var"+ varNumber +"), IS_NMTOKEN(?var"+ varNumber +"), TO_INTEGER(HexBinary(DE), ?var"+ varNumber +"), " +
				"IRI_STRING(http://www.example.org, 'http://www.example.org'), EQUAL(-1, ?var"+ varNumber +"), " +
				"STRING_COMPARE('bar', 'foo', ?var"+ varNumber +"), EQUAL(1, ?var"+ varNumber +"), STRING_COMPARE('foo', 'bar', ?var"+ varNumber +"), " +
				"EQUAL(0, ?var"+ varNumber +"), STRING_COMPARE('bar', 'bar', ?var"+ varNumber +"), EQUAL('foobar', ?var"+ varNumber +"), " +
				"STRING_CONCAT('foo', 'bar', ?var"+ varNumber +"), EQUAL('foo,bar', ?var"+ varNumber +"), " +
				"STRING_JOIN('foo', 'bar', ',', ?var"+ varNumber +"), EQUAL('bar', ?var"+ varNumber +"), " +
				"STRING_SUBSTRING2('foobar', 3, ?var"+ varNumber +"), EQUAL('fo', ?var"+ varNumber +"), " +
				"STRING_SUBSTRING3('foobar', 0, 3, ?var"+ varNumber +"), EQUAL(3, ?var"+ varNumber +"), " +
				"STRING_LENGTH('foo', ?var"+ varNumber +"), EQUAL('FOOBAR', ?var"+ varNumber +"), STRING_TO_UPPER('FooBar', ?var"+ varNumber +"), " +
				"EQUAL('foobar', ?var"+ varNumber +"), STRING_TO_LOWER('FooBar', ?var"+ varNumber +"), " +
				"EQUAL('RIF%20Basic%20Logic%20Dialect', ?var"+ varNumber +"), " +
				//TODO implement this builtin
				"ENCODE_FOR_URI('RIF Basic Logic Dialect', ?var"+ varNumber +"), " +
				"EQUAL('http://www.example.com/~b%C3%A9b%C3%A9', ?var"+ varNumber +"), " +
				"STRING_IRI_TO_URI('http://www.example.com/~bÃ©bÃ©', ?var"+ varNumber +"), " +
				"EQUAL('javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~b%C3%A9b%C3%A9');', ?var"+ varNumber +"), " +
						"STRING_ESCAPE_HTML_URI('javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~bÃ©bÃ©');', ?var"+ varNumber +"), EQUAL('foo', ?var"+ varNumber +"), STRING_SUBSTRING_BEFORE2('foobar', 'bar', ?var"+ varNumber +"), EQUAL('bar', ?var"+ varNumber +"), STRING_SUBSTRING_AFTER2('foobar', 'foo', ?var"+ varNumber +"), EQUAL('[1=ab][2=]cd', ?var"+ varNumber +"), STRING_REPLACE3('abcd', '(ab)|(a)', '[1=$1][2=$2]', ?var"+ varNumber +"), STRING_CONTAINS2('foobar', 'foo'), STRING_STARTS_WITH2('foobar', 'foo'), " +
				"STRING_ENDS_WITH2('foobar', 'bar'), " +
				"STRING_MATCHES2('abracadabra', '^a.*a$').");
		
		List<String> expectedFacts = new ArrayList<String>();		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}
	
	@Test
	public void testBuiltin_Time_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_Time-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- " +
				"IS_DATE(2000-12-13-11:00), IS_DATETIME(2000-12-13T00:11:11.3Z), IS_DATETIMESTAMP(2000-12-13T00:11:11.3Z), " +
				"IS_TIME(00:11:11.3Z), IS_DAYTIMEDURATION(P3DT2H), IS_YEARMONTHDURATION(P1Y2M), IS_NOT_DATE('foo'), IS_NOT_DATETIME('foo'), " +
				"IS_NOT_DATETIMESTAMP('foo'), IS_NOT_TIME('foo'), IS_NOT_DAYTIMEDURATION('foo'), IS_NOT_YEARMONTHDURATION('foo'), " +
				"IS_DATE(?var76), TO_DATE('2000-12-13-11:00', ?var76), " +
				"IS_DATETIME(?var77), TO_DATETIME('2000-12-13T00:11:11.3', ?var77), " +
				"IS_DATETIMESTAMP(?var78), TO_TIME('2000-12-13T00:11:11.3Z', ?var78), " +
				"IS_TIME(?var79), TO_TIME('00:11:11.3Z', ?var79), " +
				"IS_DAYTIMEDURATION(?var80), TO_DAYTIMEDURATION('P3DT2H', ?var80), " +
				"IS_YEARMONTHDURATION(?var81), TO_YEARMONTHDURATION('P1Y2M', ?var81), " +
				"EQUAL(?var82, 2000), YEAR_FROM_DATETIME(1999-12-31T24:00:00.0Z, ?var82), " +
				"EQUAL(?var83, 5), MONTH_FROM_DATETIME(1999-05-31T13:20:00.0-05:00, ?var83), " +
				"EQUAL(?var84, 31), DAY_FROM_DATETIME(1999-05-31T13:20:00.0-05:00, ?var84), " +
				"EQUAL(?var85, 8), HOURS_FROM_DATETIME(1999-05-31T08:20:00.0-05:00, ?var85), " +
				"EQUAL(?var86, 20), MINUTES_FROM_DATETIME(1999-05-31T13:20:00.0-05:00, ?var86), " +
				"EQUAL(?var87, 0), SECONDS_FROM_DATETIME(1999-05-31T13:20:00.0-05:00, ?var87), " +
				"EQUAL(?var88, 1999), YEAR_FROM_DATE(1999-12-31Z, ?var88), " +
				"EQUAL(?var89, 5), MONTH_FROM_DATE(1999-05-31Z, ?var89), " +
				"EQUAL(?var90, 31), DAY_FROM_DATE(1999-05-31Z, ?var90), " +
				"EQUAL(?var91, 8), HOURS_FROM_TIME(08:20:00.0-05:00, ?var91), " +
				"EQUAL(?var92, 20), MINUTES_FROM_TIME(13:20:00.0-05:00, ?var92), " +
				"EQUAL(?var93, 0), SECONDS_FROM_TIME(13:20:00.0-05:00, ?var93), " +
				"EQUAL(?var94, -PT5H), TIMEZONE_FROM_DATETIME(1999-05-31T13:20:00.0-05:00, ?var94), " +
				"EQUAL(?var95, -PT5H), TIMEZONE_FROM_DATE(1999-05-31-05:00, ?var95), " +
				"EQUAL(?var96, -PT5H), TIMEZONE_FROM_TIME(13:20:00.0-05:00, ?var96), " +
				"EQUAL(?var97, 21), YEARS_FROM_DURATION(P20Y15M, ?var97), " +
				"EQUAL(?var98, 3), MONTHS_FROM_DURATION(P20Y15M, ?var98), " +
				"EQUAL(?var99, 3), DAYS_FROM_DURATION(P3DT10H, ?var99), " +
				"EQUAL(?var100, 10), HOURS_FROM_DURATION(P3DT10H, ?var100), " +
				"EQUAL(?var101, -30), MINUTES_FROM_DURATION(-P5DT12H30M, ?var101), " +
				"EQUAL(?var102, 12), SECONDS_FROM_DURATION(P3DT10H12S, ?var102), " +
				"EQUAL(?var103, P337DT2H12M), DATETIME_SUBTRACT(2000-10-30T06:12:00.0-05:00, 1999-11-28T09:00:00.0Z, ?var103), " +
				"EQUAL(?var104, P337D), DATE_SUBTRACT(2000-10-30Z, 1999-11-28Z, ?var104), " +
				"EQUAL(?var105, PT7H12M), TIME_SUBTRACT(11:12:00.0Z, 04:00:00.0Z, ?var105), " +
				"EQUAL(?var106, P6Y2M), ADD_YEARMONTHDURATIONS(P2Y11M, P3Y3M, ?var106), " +
				"EQUAL(?var107, -P4M), YEARMONTHDURATION_SUBTRACT(P2Y11M, P3Y3M, ?var107), " +
				"EQUAL(?var108, P6Y9M), YEARMONTHDURATION_MULTIPLY(P2Y11M, 2, ?var108), " +
				"EQUAL(?var109, P1Y11M), YEARMONTHDURATION_DIVIDE(P2Y11M, 1, ?var109), " +
				"EQUAL(?var110, -2), YEARMONTHDURATION_DIVIDE_BY_YEARMONTHDURATION(P3Y4M, -P1Y4M, ?var110), " +
				"EQUAL(?var111, P8DT5M), ADD_DAYTIMEDURATION(P2DT12H5M, P5DT12H, ?var111), " +
				"EQUAL(?var112, P1DT1H30M), DAYTIMEDURATION_SUBTRACT(P2DT12H, P1DT10H30M, ?var112), " +
				"EQUAL(?var113, PT4H33M), DAYTIMEDURATION_MULTIPLY(PT2H10M, 2, ?var113), " +
				"EQUAL(?var114, P2D), DAYTIMEDURATION_DIVIDE(P4D, 2, ?var114), " +
				"EQUAL(?var115, 2), DAYTIMEDURATION_DIVIDE_BY_DAYTIMEDURATION(P4D, P2D, ?var115), " +
				"EQUAL(?var116, 2001-12-30T11:12:00.0Z), ADD_YEARMONTHDURATION_TO_DATETIME(2000-10-30T11:12:00.0Z, P1Y2M, ?var116), " +
				"EQUAL(?var117, 2001-12-30Z), ADD_YEARMONTHDURATION_TO_DATE(2000-10-30Z, P1Y2M, ?var117), " +
				"EQUAL(?var118, 2000-11-02T12:27:00.0Z), ADD_DAYTIMEDURATION_TO_DATETIME(2000-10-30T11:12:00.0Z, P3DT1H15M, ?var118), " +
				"EQUAL(?var119, 2004-11-01Z), ADD_DAYTIMEDURATION_TO_DATE(2004-10-30Z, P2DT2H30M, ?var119), " +
				"EQUAL(?var120, 12:27:00.0Z), ADD_DAYTIMEDURATION_TO_TIME(11:12:00.0Z, P3DT1H15M, ?var120), " +
				"EQUAL(?var121, 02:27:00.0+03:00), ADD_DAYTIMEDURATION_TO_TIME(23:12:00.0+03:00, P1DT3H15M, ?var121), " +
				"EQUAL(?var122, 1999-08-30T11:12:00.0Z), SUBTRACT_YEARMONTHDURATION_FROM_DATETIME(2000-10-30T11:12:00.0Z, P1Y2M, ?var122), " +
				"EQUAL(?var123, 1999-08-30Z), SUBTRACT_YEARMONTHDURATION_FROM_DATE(2000-10-30Z, P1Y2M, ?var123), " +
				"EQUAL(?var124, 2000-10-27T09:57:00.0Z), SUBTRACT_DAYTIMEDURATION_FROM_DATETIME(2000-10-30T11:12:00.0Z, P3DT1H15M, ?var124), " +
				"EQUAL(?var125, 2000-10-26Z), SUBTRACT_DAYTIMEDURATION_FROM_DATE(2000-10-30Z, P3DT1H15M, ?var125), " +
				"EQUAL(?var126, 09:57:00.0Z), SUBTRACT_DAYTIMEDURATION_FROM_TIME(11:12:00.0Z, P3DT1H15M, ?var126), " +
				"DATETIME_EQUAL(2002-04-02T12:00:00.0-01:00, 2002-04-02T17:00:00.0+04:00), " +
				"DATETIME_LESS(2002-04-01T12:00:00.0-01:00, 2002-04-02T17:00:00.0+04:00), " +
				"DATETIME_GREATER(2002-04-03T12:00:00.0-01:00, 2002-04-02T17:00:00.0+04:00), " +
				"DATE_EQUAL(2004-12-25-12:00, 2004-12-26+12:00), " +
				"DATE_LESS(2004-12-24Z, 2004-12-26Z), " +
				"DATE_GREATER(2004-12-26Z, 2004-12-25Z), " +
				"TIME_EQUAL(21:30:00.0+10:30, 06:00:00.0-05:00), " +
				"TIME_LESS(20:30:00.0+10:30, 06:00:00.0-05:00), " +
				"TIME_GREATER(22:30:00.0+10:30, 06:00:00.0-05:00), " +
				"DURATION_EQUAL(P1Y, P12M), YEARMONTHDURATION_LESS(P1Y, P13M), YEARMONTHDURATION_GREATER(P1Y, P11M), " +
				"DAYTIMEDURATION_LESS(P1D, PT25H), DAYTIMEDURATION_GREATER(P1D, PT23H), " +
				"DATETIME_NOT_EQUAL(2002-04-01T12:00:00.0-01:00, 2002-04-02T17:00:00.0+04:00), " +
				"DATETIME_LESS_EQUAL(2002-04-01T12:00:00.0-01:00, 2002-04-02T17:00:00.0+04:00), " +
				"DATETIME_GREATER_EQUAL(2002-04-03T12:00:00.0-01:00, 2002-04-02T17:00:00.0+04:00), " +
				"DATE_NOT_EQUAL(2004-12-24Z, 2004-12-26Z), DATE_LESS_EQUAL(2004-12-24Z, 2004-12-26Z), " +
				"DATE_GREATER_EQUAL(2004-12-26Z, 2004-12-25Z), TIME_NOT_EQUAL(20:30:00.0+10:30, 06:00:00.0-05:00), " +
				"TIME_LESS_EQUAL(20:30:00.0+10:30, 06:00:00.0-05:00), TIME_GREATER_EQUAL(22:30:00.0+10:30, 06:00:00.0-05:00), " +
				"DURATION_NOT_EQUAL(P1Y, P1M), YEARMONTHDURATION_LESS_EQUAL(P1Y, P13M), YEARMONTHDURATION_GREATER_EQUAL(P1Y, P11M), " +
				"DAYTIMEDURATION_LESS_EQUAL(P1D, PT25H), DAYTIMEDURATION_GREATER_EQUAL(P1D, PT23H).");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}

	@Test
	public void testBuiltin_XMLLiteral_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Builtins_XMLLiteral-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		expectedRules.add("http://example.org/example#ok() :- " +
				"IS_XMLLITERAL(<br></br>), IS_NOT_XMLLITERAL(1), " +
				"IS_XMLLITERAL(?var127), TO_XMLLITERAL('<br></br>', ?var127).");
		
		List<String> expectedFacts = new ArrayList<String>();
		
		this.testTranslation(rifXmlFileName, expectedFacts, expectedRules);
	}
	
	@Test
	public void testChaining_strategy_numeric_add_1_premise() throws SAXException, IOException, ParserConfigurationException
	{
		String rifXmlFileName = TEST_DIR + "Chaining_strategy_numeric-add_1-premise.rif";
		
		List<String> expectedRules = new ArrayList<String>();
		//TODO 
		expectedRules.add("http://example.org/example#ok() :- .");
		
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

		assertEquals("Unexpected number of facts", expectedFacts.size(),facts.size());
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
