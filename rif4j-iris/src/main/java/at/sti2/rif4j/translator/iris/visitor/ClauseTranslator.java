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
package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.ImpliesFormula;
import at.sti2.rif4j.transformer.LloydToporTransformer;

public class ClauseTranslator implements ClauseVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(ClauseTranslator.class);

	private List<IRule> rules = new ArrayList<IRule>();

	private Map<IPredicate, IRelation> facts;

	private IFacts factsFactory;

	public ClauseTranslator() {
		reset();
	}

	private void reset() {
		rules = new ArrayList<IRule>();
		facts = new HashMap<IPredicate, IRelation>();
		factsFactory = new Facts(new SimpleRelationFactory());
	}

	public List<IRule> getRules() {
		return rules;
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	@Override
	public void visit(ImpliesFormula implies) {
		LloydToporTransformer transformer = new LloydToporTransformer();
		List<ImpliesFormula> transformedFormulas = transformer
				.transform(implies);

		for (ImpliesFormula transformedFormula : transformedFormulas) {
			for (AtomicFormula atomicFormula : transformedFormula.getHead()) {
				List<ILiteral> headLiterals = new ArrayList<ILiteral>();
				List<ILiteral> additionalBodyLiterals = new ArrayList<ILiteral>();

				AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
				atomicFormula.accept(atomicFormulaTranslator);

				// The translator should only create one literal. If there are
				// more than one literal in the head, we shift all others but
				// the first literal to the body (see
				// Equality_in_conclusion2-premise).
				List<ILiteral> transformedHeadLiterals = atomicFormulaTranslator
						.getLiterals();
				if (transformedHeadLiterals.size() > 1) {
					logger.debug("Found more than one literal in the head of an implication. "
							+ "Shifting all literals but the first one to the body.");
					headLiterals.add(transformedHeadLiterals.get(0));

					for (int i = 1; i < transformedHeadLiterals.size(); i++) {
						additionalBodyLiterals.add(transformedHeadLiterals
								.get(i));
					}
				} else {
					headLiterals.addAll(atomicFormulaTranslator.getLiterals());
				}

				FormulaTranslator formulaTranslator = new FormulaTranslator();
				transformedFormula.getBody().accept(formulaTranslator);
				List<ILiteral> bodyLiterals = formulaTranslator.getLiterals();
				bodyLiterals.addAll(additionalBodyLiterals);

				rules.add(Factory.BASIC.createRule(headLiterals, bodyLiterals));
			}
		}
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulatTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulatTranslator);

		List<ILiteral> literals = atomicFormulatTranslator.getLiterals();

		// Only add facts.
		if (literals.size() == 1) {
			ILiteral literal = literals.get(0);
			IPredicate predicate = literal.getAtom().getPredicate();
			ITuple tuple = literal.getAtom().getTuple();

			// We have an equality atom, therefore we create a rule of the form
			// EQUAl(?x, ?y) :- true.
			if (predicate.equals(EqualBuiltin.PREDICATE)) {
				IRule rule = Factory.BASIC.createRule(literals,
						new ArrayList<ILiteral>());

				rules.add(rule);
			} else {
				IRelation relation = factsFactory.get(predicate);

				// Only add ground tuples, i.e. tuples without any variables, to
				// the relation.
				if (tuple.isGround()) {
					relation.add(tuple);
				}

				facts.put(predicate, relation);
			}

		}
	}

}
