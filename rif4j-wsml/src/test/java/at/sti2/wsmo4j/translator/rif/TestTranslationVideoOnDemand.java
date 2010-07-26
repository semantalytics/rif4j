package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omwg.logicalexpression.LogicalExpression;
import org.xml.sax.SAXException;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.parser.xml.XmlParser;

public class TestTranslationVideoOnDemand {

	private XmlParser parser;

	private RifToWsmlTranslator translator;

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser();
		translator = new RifToWsmlTranslator();
	}

	@After
	public void tearDown() throws Exception {
	}

	private Reader openTestFile(String fileName) {
		return new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream(fileName));
	}

	@Test
	public void testVideoOnDemandCondition() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(1, "VideoOnDemand_Condition.xml");
	}
	
	@Test
	public void testVideoOnDemandEffect() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(1, "VideoOnDemand_Effect.xml");
	}

	private void assertNumberOfExpressions(int amount, String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		Reader reader = openTestFile(fileName);

		Formula document = parser.parseFormula(reader);

		List<LogicalExpression> expressions = translator.translate(document);

		for (LogicalExpression expression : expressions) {
			System.out.println(expression);
		}

		Assert.assertNotNull("Expressions must not be null.", expressions);
		Assert.assertEquals("Wrong number of resulting expressions.", amount,
				expressions.size());
	}

}
