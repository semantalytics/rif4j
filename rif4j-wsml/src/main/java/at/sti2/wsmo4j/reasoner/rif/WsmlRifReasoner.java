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
package at.sti2.wsmo4j.reasoner.rif;

import java.util.List;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.ontology.Axiom;
import org.omwg.ontology.Ontology;
import org.sti2.wsmo4j.factory.WsmlFactoryContainer;
import org.wsml.reasoner.api.LPReasoner;
import org.wsml.reasoner.api.inconsistency.InconsistencyException;
import org.wsml.reasoner.impl.DefaultWSMLReasonerFactory;
import org.wsmo.common.IRI;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.factory.FactoryContainer;
import org.wsmo.factory.WsmoFactory;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.reasoner.AbstractReasoner;
import at.sti2.rif4j.rule.Document;
import at.sti2.wsmo4j.translator.rif.RifToWsmlTranslator;

/**
 * A RIF-BLD reasoner based on the WSML2Reasoner framework.
 * 
 * @author Adrian Marte
 */
public class WsmlRifReasoner extends AbstractReasoner {

	@Override
	public boolean entails(Document phi, Formula psi) {
		Ontology wsmlOntology = toOntology(phi);
		List<LogicalExpression> wsmlRules = toQueries(psi);

		LPReasoner reasoner = DefaultWSMLReasonerFactory.getFactory()
				.createFlightReasoner(null);

		try {
			reasoner.registerOntology(wsmlOntology);
		} catch (InconsistencyException e) {
			e.printStackTrace();
		}

		for (LogicalExpression wsmlRule : wsmlRules) {
			boolean entailed = reasoner.ask(wsmlRule);

			if (!entailed) {
				return entailed;
			}
		}

		return true;
	}

	@Override
	public boolean query(Document document, Formula query) {
		return entails(document, query);
	}

	private List<LogicalExpression> toQueries(Formula rule) {
		RifToWsmlTranslator translator = new RifToWsmlTranslator();
		List<LogicalExpression> expressions = translator.translate(rule);

		return expressions;
	}

	private Ontology toOntology(Document document) {
		RifToWsmlTranslator translator = new RifToWsmlTranslator();
		List<LogicalExpression> expressions = translator.translate(document);

		FactoryContainer container = new WsmlFactoryContainer();
		WsmoFactory wsmoFactory = container.getWsmoFactory();

		IRI ontologyIRI = wsmoFactory
				.createIRI("http://at.sti2.iris.rifreasoner/Ontology");
		Ontology ontology = wsmoFactory.createOntology(ontologyIRI);

		IRI axiomIRI = wsmoFactory
				.createIRI("http://at.sti2.iris.rifreasoner/Axiom");
		Axiom axiom = wsmoFactory.createAxiom(axiomIRI);

		for (LogicalExpression logicalExpression : expressions) {
			axiom.addDefinition(logicalExpression);
		}

		try {
			ontology.addAxiom(axiom);
			return ontology;
		} catch (InvalidModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
