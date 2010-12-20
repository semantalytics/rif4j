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
package at.sti2.rif4j.serializer.presentation;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 * @author Iker Larizgoitia Abad
 */
@RunWith(Parameterized.class)
public class PresentationSerializerTest extends XMLTestCase {

	private static final Logger logger = LoggerFactory
			.getLogger(PresentationSerializerTest.class);

	private String fileName;

	private XmlParser parser = null;

	private PresentationSerializer presentationSerializer = null;

	public PresentationSerializerTest(String fileName) {
		this.fileName = fileName;
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();

		String[] rifFiles = TestUtils.getRIFTestFiles("src/test/resources");
		assertNotNull(rifFiles);

		for (int i = 0; i < rifFiles.length; i++) {
			String fileName = rifFiles[i];

			data.add(new Object[] { fileName });
		}

		return data;
	}

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser(true);
		presentationSerializer = new PresentationSerializer();

		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
	}

	@Test
	public void testSerialize_RIF_BLD() throws SAXException, IOException,
			ParserConfigurationException {
		if (TestUtils.getFileUri(fileName + "ps") == null)
			return;

		logger.debug("Serializing " + fileName);

		// Parse RIFDocument
		Reader rifXmlFileReader = TestUtils.getFileReader(fileName);
		assertNotNull("Test file " + fileName + " could not be found",
				rifXmlFileReader);

		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);

		// Serialize to presentation syntax
		String serializedRIF = presentationSerializer.serialize(rifDocument);
		serializedRIF = presentationSerializer.getString().replace("\r", "")
				.replace("\t", "").replace("\n", "").replace(" ", "").trim();

		// Compare to original
		File file = new File(TestUtils.getFileUri(fileName + "ps"));
		String originalRIF = FileUtils.readFileToString(file).replace("\t", "")
				.replace("\r", "").replace("\n", "").replace(" ", "").trim();

		assertEquals("Serialized and original documents do not match for file "
				+ fileName, originalRIF, serializedRIF);
	}

}