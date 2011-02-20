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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

/**
 * @author Adrian Marte
 * @author Iker Larizgoitia Abad
 */
@RunWith(Parameterized.class)
public class PresentationSerializerTest extends XMLTestCase {

	private static final Logger logger = LoggerFactory
			.getLogger(PresentationSerializerTest.class);

	private XmlParser parser = null;

	private PresentationSerializer presentationSerializer = null;

	private final URI fileUri;

	public PresentationSerializerTest(URI xmlFileUri) {
		this.fileUri = xmlFileUri;
	}

	// @Parameters
	// public static Collection<Object[]> data() throws FileNotFoundException {
	// Collection<Object[]> positiveEntailmentTests =
	// AbstractLocalPositiveEntailmentTest
	// .data();
	//
	// List<URI> uris = new ArrayList<URI>();
	//
	// for (Object[] test : positiveEntailmentTests) {
	// uris.add((URI) test[1]);
	// uris.add((URI) test[2]);
	// }
	//
	// Collection<Object[]> negativeEntailmentTests =
	// AbstractLocalPositiveEntailmentTest
	// .data();
	//
	// for (Object[] test : negativeEntailmentTests) {
	// uris.add((URI) test[1]);
	// uris.add((URI) test[2]);
	// }
	//
	// List<Object[]> data = new ArrayList<Object[]>();
	//
	// for (URI uri : uris) {
	// data.add(new Object[] { uri });
	// }
	//
	// return data;
	// }

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> data = new ArrayList<Object[]>();

		String directory = "src/test/resources";
		String[] rifFiles = TestUtils.getRIFTestFiles(directory);
		assertNotNull(rifFiles);

		ClassLoader loader = PresentationSerializerTest.class.getClassLoader();

		for (int i = 0; i < rifFiles.length; i++) {
			String fileName = rifFiles[i];
			URL url = loader.getResource(fileName);

			try {
				data.add(new Object[] { url.toURI() });
			} catch (URISyntaxException e) {
				logger.error("Could not create URI for " + url);
			}
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
		logger.debug("Serializing " + fileUri);

		// Parse RIF document.
		Reader rifXmlFileReader = new InputStreamReader(fileUri.toURL()
				.openStream());
		assertNotNull("Test file could not be found: " + fileUri,
				rifXmlFileReader);

		Document rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);

		// Serialize to presentation syntax
		String serializedRIF = presentationSerializer.serialize(rifDocument);
		serializedRIF = presentationSerializer.getString().replace("\r", "")
				.replace("\t", "").replace("\n", "").replace(" ", "").trim();

		try {
			// Compare to original
			File file = new File(TestUtils.getFileUri(fileUri + "ps"));
			String originalRIF = FileUtils.readFileToString(file)
					.replace("\t", "").replace("\r", "").replace("\n", "")
					.replace(" ", "").trim();

			boolean areEqual = originalRIF.equals(serializedRIF);
			if (!areEqual && logger.isDebugEnabled()) {
				logger.debug("Serialized and original documents do not match");
				logger.debug(originalRIF);
				logger.debug(serializedRIF);
			}

			assertEquals(
					"Serialized and original documents do not match for file "
							+ fileUri, originalRIF, serializedRIF);
		} catch (FileNotFoundException e) {
			// If the one of the files can not be found, we just skip the test.
		}
	}

}