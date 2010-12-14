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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;

/**
 * A test suite that executes all positive entailment tests in the RIF-BLD test
 * suite.
 * 
 * @author Adrian Marte
 */
public class PositiveEntailmentTest {

	private static final Logger logger = LoggerFactory
			.getLogger(PositiveEntailmentTest.class);

	private static final String POSITIVE_ENTAILMENT_DIR = "BLD_v1.22/Approved/PositiveEntailmentTest/";

	private static final String PREMISE_SUFFIX = "-premise";

	private static final String CONCLUSION_SUFFIX = "-conclusion";

	private static final String FILE_EXTENSION = ".rif";

	private IrisRifReasoner reasoner;

	@Before
	public void setUp() {
		reasoner = new IrisRifReasoner();
	}

	@Test
	public void testPositiveEntailment() throws FileNotFoundException {
		File directory = new File("src/test/resources/"
				+ POSITIVE_ENTAILMENT_DIR);

		if (!directory.exists()) {
			throw new FileNotFoundException(
					"Could not locate directory containing the test cases: "
							+ directory);
		}

		File[] testCaseDirectories = directory.listFiles();

		List<String> failingTests = new ArrayList<String>();
		boolean entailsAll = true;

		for (File testCaseDirectory : testCaseDirectories) {
			String testCaseName = testCaseDirectory.getName();

			if (testCaseName.startsWith(".")) {
				continue;
			}

			logger.debug("Testing positive entailment of " + testCaseName);

			String premiseFileName = testCaseName + PREMISE_SUFFIX
					+ FILE_EXTENSION;
			String premisePath = POSITIVE_ENTAILMENT_DIR + testCaseName + "/"
					+ premiseFileName;

			String conclusionFileName = testCaseName + CONCLUSION_SUFFIX
					+ FILE_EXTENSION;
			String conclusionPath = POSITIVE_ENTAILMENT_DIR + testCaseName
					+ "/" + conclusionFileName;

			Document document = TestUtils.parseDocument(premisePath);
			Formula formula = TestUtils.parseFormula(conclusionPath);

			boolean entails = reasoner.entails(document, formula);
			entailsAll &= entails;

			if (!entails) {
				failingTests.add(testCaseName);
			}

			logger.debug("Result is: " + entails);
		}

		Assert.assertTrue("The failing tests are: " + failingTests, entailsAll);
	}

}
