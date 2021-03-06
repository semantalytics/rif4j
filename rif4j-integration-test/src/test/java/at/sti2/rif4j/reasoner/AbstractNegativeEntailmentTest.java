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
package at.sti2.rif4j.reasoner;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.sti2.rif4j.test.suite.NegativeEntailmentTest;
import at.sti2.rif4j.test.suite.ObjectFactory;
import at.sti2.rif4j.test.suite.TestSuite;

@RunWith(Parameterized.class)
public abstract class AbstractNegativeEntailmentTest extends
		AbstractEntailmentTest {

	public static final String RIF_BLD_URL = "http://www.w3.org/2005/rules/test/repository/BLDTests.xml";

	public static final boolean EXPECTED_EVALUATION = false;

	public AbstractNegativeEntailmentTest(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		super(testName, premiseUri, conclusionUri, expectedEvaluation);
	}

	@Parameters
	public static Collection<Object[]> data() throws FileNotFoundException,
			JAXBException, MalformedURLException {
		List<Object[]> data = new ArrayList<Object[]>();

		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		TestSuite suite = (TestSuite) unmarshaller.unmarshal(new URL(
				RIF_BLD_URL));

		List<Object> testCases = suite.getTestCase();

		for (Object testCase : testCases) {
			if (testCase instanceof NegativeEntailmentTest) {
				NegativeEntailmentTest negativeTestCase = (NegativeEntailmentTest) testCase;

				List<String> premiseUrls = negativeTestCase
						.getPremiseDocument().getNormative().getRemote();
				List<String> conclusionUrls = negativeTestCase
						.getNonConclusionDocument().getNormative().getRemote();

				if (premiseUrls.size() > 0 && conclusionUrls.size() > 0) {
					String testName = negativeTestCase.getId();
					URI premiseUrl = URI.create(premiseUrls.get(0));
					URI conclusionUrl = URI.create(conclusionUrls.get(0));

					data.add(new Object[] { testName, premiseUrl,
							conclusionUrl, EXPECTED_EVALUATION });
				}
			}
		}

		return data;
	}

}
