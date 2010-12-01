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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;

/**
 * @author Adrian Marte
 * @author Iker Larizgoitia Abad
 */
public class XmlParserTest {

	private XmlParser parser;

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser(true);
	}

	private Reader openTestFile(String fileName) {
		return new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream(fileName));
	}

	// Just tests if parser generates correct number of rules.
	@Ignore
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

	@Ignore
	@Test
	public void extractRules_ClassMembership() throws SAXException,
			IOException, ParserConfigurationException {
		Reader reader = openTestFile("Class_Membership.xml");

		try {
			Document document = parser.parseDocument(reader);

			Assert.assertNotNull("Document must not be null", document);
			Assert.assertNotNull("Group must not be null", document.getGroup());

			List<Rule> rules = document.getGroup().getAllRules();
			Assert.assertEquals("Incorrect number of rules", 4, rules.size());

			for (Rule rule : rules) {
				// System.out.println("r: " + rule.toString());
			}
		} finally {
			reader.close();
		}
	}

}