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
package at.sti2.rif4j.parser.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 * @author Iker Larizgoitia Abad
 */
public class PresentationSerializerTest extends XMLTestCase {

	private static final Logger logger = LoggerFactory
	.getLogger(PresentationSerializerTest.class);

	private XmlParser parser = null;
	private PresentationSerializer presentationSerializer = null;
	
	@Before
	public void setUp() throws Exception {
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
	}

	@Test
	public void testSerialize_RIF_BLD() throws SAXException,
			IOException, ParserConfigurationException {
		String[] rifFiles = TestUtils.getRIFTestFiles("src/test/resources");
		assertNotNull(rifFiles);

		for (int i = 0; i < rifFiles.length; i++) {
			parser = new XmlParser(true);
			presentationSerializer = new PresentationSerializer();

			String fileName = rifFiles[i];

			testSerializePresentation_RIF_BLD(fileName);

			parser = null;
			presentationSerializer = null;
		}
	}

	private void testSerializePresentation_RIF_BLD(String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		if (TestUtils.getFileURI(fileName + "ps") == null)
			return;

		logger.debug("Testing " + fileName);

		// Parse RIFDocument
		Reader rifXmlFileReader = TestUtils.getFileReader(fileName);
		assertNotNull("Test file " + fileName + " could not be found",
				rifXmlFileReader);

		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);

		// Serialize to presentation syntax
		String serializedRIF = presentationSerializer.serialize(rifDocument);
		serializedRIF = presentationSerializer.getString()
				.replace("\r", "").replace("\t", "").replace("\n", "")
				.replace(" ", "").trim();

		// Compare to original
		File file = new File(TestUtils.getFileURI(fileName + "ps"));
		String originalRIF = FileUtils.readFileToString(file).replace("\t", "")
				.replace("\r", "").replace("\n", "").replace(" ", "").trim();

		assertEquals("Serialized and original documents do not match for file "
				+ fileName, originalRIF, serializedRIF);
	}	
}