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
package at.sti2.rif4j.reasoner.iris.rdb;

import java.net.URI;

import org.junit.After;
import org.junit.Ignore;

import at.sti2.rif4j.reasoner.AbstractLocalPositiveEntailmentTest;
import at.sti2.rif4j.reasoner.Reasoner;
import at.sti2.rif4j.reasoner.ReasonerFactory;

@Ignore
public class LocalPositiveEntailmentIT extends
		AbstractLocalPositiveEntailmentTest {

	private static final ReasonerFactory factory = new IrisRdbRifReasonerFactory();

	public LocalPositiveEntailmentIT(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		super(testName, premiseUri, conclusionUri, expectedEvaluation);
	}

	@Override
	protected Reasoner createReasoner() {
		return factory.createReasoner();
	}
	
	@After
	public void tearDown() {
		Reasoner reasoner = getReasoner();
		
		if (reasoner instanceof IrisRdbRifReasoner) {
			((IrisRdbRifReasoner) reasoner).dispose();
		}
	}

}
