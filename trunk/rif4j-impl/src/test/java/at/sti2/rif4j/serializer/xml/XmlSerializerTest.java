package at.sti2.rif4j.serializer.xml;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;

public class XmlSerializerTest
{

	private XmlParser	parser;

	@Before
	public void setUp() throws Exception
	{
		parser = new XmlParser(true);
	}

	private Reader openTestFile(String fileName)
	{
		return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(fileName));
	}

	@Ignore
	@Test
	public void testRoundTrip() throws IOException, SAXException, ParserConfigurationException
	{
		Reader reader = openTestFile("rif-bld-example.xml");

		try
		{
			Document original = parser.parseDocument(reader);

			XmlSerializer serializer = new XmlSerializer(true);
			original.accept(serializer);
			String output = serializer.serialize(original);

			parser = new XmlParser(true);
			Document replica = parser.parseDocument(new StringReader(output));

			assertEquals(original, replica);
		}
		finally
		{
			reader.close();
		}
	}
}
