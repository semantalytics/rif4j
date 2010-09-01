package at.sti2.rif4j.serializer.xml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;

public class XmlSerializerTest {

	private XmlParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser(true);
	}

	private Reader openTestFile(String fileName) {
		return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName));
	}

	@Test
	public void testRoundTrip() throws IOException, SAXException,
			ParserConfigurationException {
		Reader reader = openTestFile("rif-bld-example.xml");

		try {
			Document original = parser.parseDocument(reader);

			XmlSerializer serializer = new XmlSerializer();
			original.accept(serializer);
			String output = serializer.getString();
			System.err.println(output);

			parser = new XmlParser(true);
			Document replica = parser.parseDocument(new StringReader(output));
			
			assertEquals(original, replica);
			
		} finally {
			reader.close();
		}
	}
}
