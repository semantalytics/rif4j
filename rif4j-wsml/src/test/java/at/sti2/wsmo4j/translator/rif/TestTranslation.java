/*
 * Copyright (c) 2009, STI Innsbruck
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omwg.logicalexpression.LogicalExpression;
import org.xml.sax.SAXException;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.wsmo4j.translator.rif.RifToWsmlTranslator;

/**
 * @author Adrian Marte
 */
public class TestTranslation {

	private XmlParser parser;

	private RifToWsmlTranslator translator;

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser();
		translator = new RifToWsmlTranslator();
	}

	@After
	public void tearDown() throws Exception {
	}

	private Reader openTestFile(String fileName) {
		return new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream(fileName));
	}

	@Test
	public void testAllBuiltins() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(1, "All_Builtins.xml");
	}

	@Test
	public void testFactorialForwardChaining() throws SAXException,
			IOException, ParserConfigurationException {
		assertNumberOfExpressions(2, "Factorial_Forward_Chaining.xml");
	}

	@Test
	public void testClassMembership() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(4, "Class_Membership.xml");
	}

	@Test
	public void testClassificationNonInheritance() throws SAXException,
			IOException, ParserConfigurationException {
		assertNumberOfExpressions(2, "Classification_non-inheritance.xml");
	}

	private void assertNumberOfExpressions(int amount, String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		Reader reader = openTestFile(fileName);

		Document document = parser.parseDocument(reader);

		List<LogicalExpression> expressions = translator.translate(document);

		for (LogicalExpression expression : expressions) {
			System.out.println(expression);
		}

		Assert.assertNotNull("Expressions must not be null.", expressions);
		Assert.assertEquals("Wrong number of resulting expressions.", amount,
				expressions.size());
	}

}
