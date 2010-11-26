package at.sti2.rif4j;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;
import at.sti2.rif4j.serializer.xml.XmlSerializer;

public class RIFParseAndSerializeTest extends XMLTestCase
{
	private XmlParser parser = null;
	private XmlSerializer serializer = null;
	private PresentationSerializer presentationSerializer = null;
	
	@Before
	public void setUp() throws Exception
	{
		XMLUnit.setIgnoreAttributeOrder(true);		
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
	}
	
	@After
	public void tearDown() throws Exception
	{}
	
	@Test
	public void testParseAndSerializeXML_RIF_BLD() throws SAXException, IOException, ParserConfigurationException 
	{
		String [] rifFiles = TestUtils.getRIFTestFiles("src/test/resources");
		assertNotNull(rifFiles);
		
		for (int i = 0; i < rifFiles.length; i++)
		{
			parser = new XmlParser(true);
			serializer = new XmlSerializer(true);
			
			String fileName = rifFiles[i];
			testParseAndSerializeXML_RIF_BLD(fileName);		
			
			parser = null;
			serializer = null;
		}
	}
	
	@Test
	public void testSerializePresentationSyntax_RIF_BLD() throws SAXException, IOException, ParserConfigurationException
	{
		String [] rifFiles = TestUtils.getRIFTestFiles("src/test/resources");
		assertNotNull(rifFiles);

		for (int i = 0; i < rifFiles.length; i++)
		{
			parser = new XmlParser(true);
			presentationSerializer = new PresentationSerializer();
			
			String fileName = rifFiles[i];
			
			testSerializePresentation_RIF_BLD(fileName);		
			
			parser = null;
			presentationSerializer = null;
		}		
	}
	
	private void testParseAndSerializeXML_RIF_BLD(String fileName) throws SAXException, IOException, ParserConfigurationException
	{
		System.out.println("Testing " + fileName);
		
		// Parse RIFDocument		
		Reader rifXmlFileReader = TestUtils.getFileReader(fileName);
		assertNotNull("Test file " + fileName + " could not be found", rifXmlFileReader);
		
		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);
		
		// Serialize to XML
		String serializedRIFXmlDocument = serializer.serialize(rifDocument);		
						
		// Compare to original		
		File file = new File(TestUtils.getFileURI(fileName));		
		String originalRIFXml = FileUtils.readFileToString(file);		
		originalRIFXml = originalRIFXml.replaceAll("ordered=\"yes\"", "");				
				
		assertXMLEqual("Serialized and original documents do not match for file " + fileName,  originalRIFXml, serializedRIFXmlDocument);
	}
	
	private void testSerializePresentation_RIF_BLD(String fileName) throws SAXException, IOException, ParserConfigurationException
	{						
		if ( TestUtils.getFileURI(fileName + "ps") == null )
			return;
		
		System.out.println("Testing " + fileName);
		
		// Parse RIFDocument		
		Reader rifXmlFileReader = TestUtils.getFileReader(fileName);					
		assertNotNull("Test file " + fileName + " could not be found", rifXmlFileReader);				
		
		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);
		
		// Serialize to presentation syntax
		presentationSerializer.visit(rifDocument);
		String serializedRIF = presentationSerializer.getString().replace("\r","").replace("\t", "").replace("\n", "").replace(" ","").trim();
		
		// Compare to original
		File file = new File(TestUtils.getFileURI(fileName + "ps"));
		String originalRIF = FileUtils.readFileToString(file).replace("\t", "").replace("\r","").replace("\n", "").replace(" ","").trim();
				
		assertEquals("Serialized and original documents do not match for file " + fileName,  originalRIF, serializedRIF);
	}
}
