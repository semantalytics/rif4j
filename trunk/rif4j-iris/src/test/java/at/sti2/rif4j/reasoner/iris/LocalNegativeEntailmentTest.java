package at.sti2.rif4j.reasoner.iris;

import java.net.URI;

import at.sti2.rif4j.reasoner.Reasoner;
import at.sti2.rif4j.reasoner.ReasonerFactory;
import at.sti2.rif4j.test.AbstractLocalNegativeEntailmentTest;

public class LocalNegativeEntailmentTest extends
		AbstractLocalNegativeEntailmentTest {

	private static final ReasonerFactory factory = new IrisRifReasonerFactory();

	public LocalNegativeEntailmentTest(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		super(testName, premiseUri, conclusionUri, expectedEvaluation);
	}

	@Override
	protected Reasoner createReasoner() {
		return factory.createReasoner();
	}

}
