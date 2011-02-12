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
import java.util.Collection;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * A test suite that executes all positive entailment tests in the RIF-BLD test
 * suite.
 * 
 * @author Adrian Marte
 */
@RunWith(Parameterized.class)
public class PositiveEntailmentTest extends AbstractEntailmentTest {

	private static final String POSITIVE_ENTAILMENT_DIR = "BLD_v1.22/Approved/PositiveEntailmentTest/";

	private static final boolean EXPECTED_EVALUATION = true;

	public PositiveEntailmentTest(String testDirecotry, String testCaseName,
			boolean expectedEvaluation) {
		super(testDirecotry, testCaseName, expectedEvaluation);
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

			data.add(new Object[] { POSITIVE_ENTAILMENT_DIR, testCaseName,
					EXPECTED_EVALUATION });
		}

		return data;
	}

}
