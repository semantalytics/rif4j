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

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * A test suite to test the translation of some of RIF's official test cases.
 * 
 * {@link http://www.w3.org/2005/rules/wiki/Category:Test_Case}
 */
public class ITTranslateTestCases extends AbstractITTranslate {

	/**
	 * {@link http://www.w3.org/2005/rules/wiki/All_Builtins}
	 */
	@Test
	public void testAllBuiltins() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(1, "All_Builtins.xml");
	}

	/**
	 * {@link http://www.w3.org/2005/rules/wiki/Factorial_Forward_Chaining}
	 */
	@Test
	public void testFactorialForwardChaining() throws SAXException,
			IOException, ParserConfigurationException {
		assertNumberOfExpressions(2, "Factorial_Forward_Chaining.xml");
	}

	/**
	 * {@link http://www.w3.org/2005/rules/wiki/Class_Membership}
	 */
	@Test
	public void testClassMembership() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(4, "Class_Membership.xml");
	}

	/**
	 * {@link http://www.w3.org/2005/rules/wiki/Classification_non-inheritance}
	 */
	@Test
	public void testClassificationNonInheritance() throws SAXException,
			IOException, ParserConfigurationException {
		assertNumberOfExpressions(2, "Classification_non-inheritance.xml");
	}
}
