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
package at.sti2.wsmo4j.reasoner.rif;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;

/**
 * Integration tests for {@link WsmlRifReasoner}.
 * 
 * @author Adrian Marte
 */
public class ITReasonRuleML2010 {

	private WsmlRifReasoner reasoner;

	@Before
	public void setUp() {
		reasoner = new WsmlRifReasoner();
	}

	@After
	public void shutDown() {
		reasoner = null;
	}

	@Test
	public void testRuleML2010() {
		Document document = TestUtils.parseDocument("RuleML2010_Document.xml");
		Formula formula = TestUtils.parseFormula("RuleML2010_Query.xml");
		
		reasoner.register(document);
		boolean entailed = reasoner.entails(formula);

		Assert.assertTrue(entailed);
	}

}
