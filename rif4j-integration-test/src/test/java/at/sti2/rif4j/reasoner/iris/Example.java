package at.sti2.rif4j.reasoner.iris;

import java.net.URI;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.manager.DocumentLoadingException;
import at.sti2.rif4j.manager.DocumentManager;
import at.sti2.rif4j.reasoner.Reasoner;
import at.sti2.rif4j.reasoner.ReasonerFactory;
import at.sti2.rif4j.reasoner.ReasoningException;
import at.sti2.rif4j.reasoner.iris.IrisRifReasonerFactory;
import at.sti2.rif4j.rule.Document;

public class Example {

	public static void main(String[] args) throws DocumentLoadingException,
			ReasoningException {
		// The URI of the premise document.
		URI premiseUri = URI
				.create("http://www.w3.org/2005/rules/test/repository/tc/Class_Membership/Class_Membership-premise.rif");

		// THe URI of the conclusion formula.
		URI conclusionUri = URI
				.create("http://www.w3.org/2005/rules/test/repository/tc/Class_Membership/Class_Membership-conclusion.rif");

		// Use the DocumentManager to load the premise and the conclusion.
		DocumentManager manager = new DocumentManager();
		Document premise = manager.loadDocument(premiseUri);
		Formula conclusion = manager.loadFormula(conclusionUri);

		// Create a RIF-BLD reasoner based on the IRIS Datalog system.
		ReasonerFactory factory = new IrisRifReasonerFactory();
		Reasoner reasoner = factory.createReasoner();

		// Register the premise document.
		reasoner.register(premise);

		// Check if the premise entails the conclusion.
		boolean entails = reasoner.entails(conclusion);

		if (entails) {
			// Do something.
		}
	}

}
