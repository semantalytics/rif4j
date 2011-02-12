package at.sti2.rif4j.reasoner.iris;

import java.io.FileNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;

@RunWith(Parameterized.class)
public abstract class AbstractEntailmentTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String PREMISE_SUFFIX = "-premise";

	private static final String CONCLUSION_SUFFIX = "-conclusion";

	private static final String NONCONCLUSION_SUFFIX = "-nonconclusion";

	private static final String FILE_EXTENSION = ".rif";

	private IrisRifReasoner reasoner;

	private String testDirectory;

	private String testCaseName;

	private boolean expectedEvaluation;

	public AbstractEntailmentTest(String testDirectory, String testCaseName,
			boolean expectedEvaluation) {
		this.testDirectory = testDirectory;
		this.testCaseName = testCaseName;
		this.expectedEvaluation = expectedEvaluation;
	}

	@Before
	public void setUp() {
		reasoner = new IrisRifReasoner();
	}

	@Test
	public void testEntailment() throws FileNotFoundException {
		logger.debug("Testing positive entailment of " + testCaseName);

		String premiseFileName = testCaseName + PREMISE_SUFFIX + FILE_EXTENSION;
		String premisePath = testDirectory + testCaseName + "/"
				+ premiseFileName;

		String conclusionSuffix = expectedEvaluation ? CONCLUSION_SUFFIX
				: NONCONCLUSION_SUFFIX;

		String conclusionFileName = testCaseName + conclusionSuffix
				+ FILE_EXTENSION;
		String conclusionPath = testDirectory + testCaseName + "/"
				+ conclusionFileName;

		Document document = TestUtils.parseDocument(premisePath);
		Formula formula = TestUtils.parseFormula(conclusionPath);

		reasoner.register(document);
		boolean entails = reasoner.entails(formula);
		logger.debug("Result is: " + entails);

		Assert.assertEquals(testCaseName, expectedEvaluation, entails);
	}

}
