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
package at.sti2.rif4j.reasoner.iris;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.reasoner.ReasoningException;
import at.sti2.rif4j.rule.Document;

/**
 * @author Adrian Marte
 */
public class IrisRifReasonerIT {

	private static String CLASS_MEMBERSHIPT_PREFIX = "BLD_v1.22/Approved/PositiveEntailmentTest/"
			+ "Class_Membership/Class_Membership";

	private IrisRifReasoner reasoner;

	@Before
	public void setUp() {
		reasoner = new IrisRifReasoner();
	}

	@Test
	public void testClassMembership() throws ReasoningException {
		Document document = TestUtils.parseDocument(CLASS_MEMBERSHIPT_PREFIX
				+ "-premise.rif");
		Formula formula = TestUtils.parseFormula(CLASS_MEMBERSHIPT_PREFIX
				+ "-conclusion.rif");

		reasoner.register(document);
		Assert.assertTrue(reasoner.entails(formula));
	}

}
