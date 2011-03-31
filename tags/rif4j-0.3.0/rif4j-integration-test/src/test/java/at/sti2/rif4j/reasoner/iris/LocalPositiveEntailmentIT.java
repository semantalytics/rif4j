package at.sti2.rif4j.reasoner.iris;

import java.net.URI;

import org.junit.Ignore;

import at.sti2.rif4j.reasoner.AbstractLocalPositiveEntailmentTest;
import at.sti2.rif4j.reasoner.Reasoner;
import at.sti2.rif4j.reasoner.ReasonerFactory;

@Ignore
public class LocalPositiveEntailmentIT extends
		AbstractLocalPositiveEntailmentTest {

	private static final ReasonerFactory factory = new IrisRifReasonerFactory();

	public LocalPositiveEntailmentIT(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		super(testName, premiseUri, conclusionUri, expectedEvaluation);
	}

	@Override
	protected Reasoner createReasoner() {
		return factory.createReasoner();
	}

}
