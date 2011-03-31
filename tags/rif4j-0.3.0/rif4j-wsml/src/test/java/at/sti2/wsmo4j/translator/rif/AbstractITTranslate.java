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
package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.omwg.logicalexpression.LogicalExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;

/**
 * A generic test to translate RIF rules to WSML.
 * 
 * @author Adrian Marte
 */
public abstract class AbstractITTranslate {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected XmlParser parser;

	protected RifToWsmlTranslator translator;

	@Before
	public void setUp() throws Exception {
		parser = new XmlParser();
		translator = new RifToWsmlTranslator();
	}

	@After
	public void tearDown() throws Exception {
	}

	protected Reader openTestFile(String fileName) {
		return new InputStreamReader(getClass().getClassLoader()
				.getResourceAsStream(fileName));
	}

	protected void assertNumberOfExpressions(int amount, String fileName)
			throws SAXException, IOException, ParserConfigurationException {
		Reader reader = openTestFile(fileName);

		Document document = parser.parseDocument(reader);

		List<LogicalExpression> expressions = translator.translate(document);

		if (expressions.size() != amount) {
			logger.debug("Problematic expressions");
			for (LogicalExpression expression : expressions) {
				logger.debug(expression.toString());
			}
		}

		Assert.assertNotNull("Expressions must not be null.", expressions);
		Assert.assertEquals("Wrong number of resulting expressions.", amount,
				expressions.size());
	}

	protected void assertNumberOfExpressionsOfFormula(int amount,
			String fileName) throws SAXException, IOException,
			ParserConfigurationException {
		Reader reader = openTestFile(fileName);

		Formula formula = parser.parseFormula(reader);

		List<LogicalExpression> expressions = translator.translate(formula);

		if (expressions.size() != amount) {
			logger.debug("Problematic expressions");
			for (LogicalExpression expression : expressions) {
				logger.debug(expression.toString());
			}
		}

		Assert.assertNotNull("Expressions must not be null.", expressions);
		Assert.assertEquals("Wrong number of resulting expressions.", amount,
				expressions.size());
	}

}
