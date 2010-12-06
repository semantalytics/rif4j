/*
 * Copyright 2010 STI Innsbruck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.sti2.rif4j.serializer.xml;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;

/**
 * @author Daniel Winkler
 * @author Iker Larizgoitia Abad
 */
public class XmlSerializerTest extends XMLTestCase {

	private static final Logger logger = LoggerFactory
			.getLogger(XmlSerializerTest.class);

	private XmlParser parser = null;
	private XmlSerializer serializer = null;

	@Before
	public void setUp() throws Exception {
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSerialize_RIF_BLD() throws SAXException,
			IOException, ParserConfigurationException {
		String[] rifFiles = TestUtils.getRIFTestFiles("src/test/resources");
		assertNotNull(rifFiles);

		for (int i = 0; i < rifFiles.length; i++) {
			parser = new XmlParser(true);
			serializer = new XmlSerializer(true);

			String fileName = rifFiles[i];
			testParseAndSerializeXML_RIF_BLD(fileName);

			parser = null;
			serializer = null;
		}
	}

	private void testParseAndSerializeXML_RIF_BLD(String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		logger.debug("Testing " + fileName);

		// Parse RIFDocument
		Reader rifXmlFileReader = TestUtils.getFileReader(fileName);
		assertNotNull("Test file " + fileName + " could not be found",
				rifXmlFileReader);

		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);

		// Serialize to XML
		String serializedRIFXmlDocument = serializer.serialize(rifDocument);

		// Compare to original
		File file = new File(TestUtils.getFileURI(fileName));
		String originalRIFXml = FileUtils.readFileToString(file);
		originalRIFXml = originalRIFXml.replaceAll("ordered=\"yes\"", "");

		assertXMLEqual(
				"Serialized and original documents do not match for file "
						+ fileName, originalRIFXml, serializedRIFXmlDocument);
	}
}
