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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class AbstractLocalPositiveEntailmentTest extends
		AbstractEntailmentTest {

	public static final boolean EXPECTED_EVALUATION = true;

	private static final String POSITIVE_ENTAILMENT_DIR = "BLD_v1.22/Approved/PositiveEntailmentTest/";

	private static final String PREMISE_SUFFIX = "-premise";

	private static final String CONCLUSION_SUFFIX = "-conclusion";

	private static final String FILE_EXTENSION = ".rif";

	public static String SINGLE_TEST = null;

	public AbstractLocalPositiveEntailmentTest(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		super(testName, premiseUri, conclusionUri, expectedEvaluation);
	}

	@Parameters
	public static Collection<Object[]> data() throws FileNotFoundException {
		List<Object[]> data = new ArrayList<Object[]>();

		File directory = new File("src/test/resources/"
				+ POSITIVE_ENTAILMENT_DIR);

		if (!directory.exists()) {
			throw new FileNotFoundException(
					"Could not locate directory containing the test cases: "
							+ directory);
		}

		File[] testCaseDirectories = directory.listFiles();

		for (File testCaseDirectory : testCaseDirectories) {
			String testCaseName = testCaseDirectory.getName();

			if (testCaseName.startsWith(".")) {
				continue;
			}

			if (SINGLE_TEST != null && !testCaseName.equals(SINGLE_TEST)) {
				continue;
			}

			String premiseFileName = testCaseName + PREMISE_SUFFIX
					+ FILE_EXTENSION;
			String premisePath = directory + "/" + testCaseName + "/"
					+ premiseFileName;
			URI premiseUri = new File(premisePath).toURI();

			String conclusionFileName = testCaseName + CONCLUSION_SUFFIX
					+ FILE_EXTENSION;
			String conclusionPath = directory + "/" + testCaseName + "/"
					+ conclusionFileName;
			URI conclusionUri = new File(conclusionPath).toURI();

			data.add(new Object[] { testCaseName, premiseUri, conclusionUri,
					EXPECTED_EVALUATION });
		}

		return data;
	}

}
