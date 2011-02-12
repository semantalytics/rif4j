package at.sti2.rif4j.test;

import java.io.FileNotFoundException;
import java.net.URI;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.reasoner.Reasoner;
import at.sti2.rif4j.rule.Document;

@RunWith(Parameterized.class)
public abstract class AbstractEntailmentTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Reasoner reasoner;

	private String testName;

	private URI premiseUri;

	private URI conclusionUri;

	private boolean expectedEvaluation;

	public AbstractEntailmentTest(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		this.testName = testName;
		this.premiseUri = premiseUri;
		this.conclusionUri = conclusionUri;
		this.expectedEvaluation = expectedEvaluation;
	}

	@Before
	public void setUp() {
		reasoner = createReasoner();
	}

	@Test
	public void testEntailment() throws FileNotFoundException {
		logger.debug("Testing positive entailment of " + premiseUri);

		Document document = TestUtils.parseDocument(premiseUri.toString());
		Formula formula = TestUtils.parseFormula(conclusionUri.toString());

		reasoner.register(document);
		boolean entails = reasoner.entails(formula);
		logger.debug("Result is: " + entails);

		Assert.assertEquals(testName, expectedEvaluation, entails);
	}

	protected abstract Reasoner createReasoner();

}
