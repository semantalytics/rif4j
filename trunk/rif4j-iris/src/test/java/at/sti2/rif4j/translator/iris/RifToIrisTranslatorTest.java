package at.sti2.rif4j.translator.iris;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;

public class RifToIrisTranslatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTranslateDocument() throws SAXException, IOException, ParserConfigurationException {
		String rifXmlFileName = "core/document.rif";

		Reader rifXmlFileReader = TestUtils.getFileReader(rifXmlFileName);
		assertNotNull("Test file " + rifXmlFileName + " could not be found",
				rifXmlFileReader);

		XmlParser parser = new XmlParser(true);
		Document rifDocument = parser.parseDocument(rifXmlFileReader);
	
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(rifDocument);

		assertTrue("Rules were not found", translator.getRules().size() > 0);
		assertTrue("Facts were not found", translator.getFacts().size() > 0);
		
		assertEquals(1,translator.getRules().size());		
		assertEquals(3,translator.getFacts().size());		
		
		PresentationSerializer serializer = new PresentationSerializer();
		
		String serialization = serializer.serialize(rifDocument);
		System.out.println(serialization);		
	}

	@Test
	public void testTranslateRule() {
		fail("Not yet implemented");
	}

}
