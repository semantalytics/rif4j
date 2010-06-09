package at.sti2.rif4j.parser.xml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;

public class XmlParserTest {

	private XmlParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser();
	}

	private Reader openTestFile(String fileName) {
		return new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream(fileName));
	}

	// Just tests if parser generates correct number of rules.
	@Test
	public void parseClassMembership() throws IOException, SAXException,
			ParserConfigurationException {
		Reader reader = openTestFile("Class_Membership.xml");

		try {
			Document document = parser.parseDocument(reader);

			Assert.assertNotNull("Document must not be null", document);
			Assert.assertNotNull("Group must not be null", document.getGroup());

			List<Rule> rules = document.getGroup().getAllRules();

			Assert.assertEquals("Incorrect number of rules", 4, rules.size());
		} finally {
			reader.close();
		}
	}

}
