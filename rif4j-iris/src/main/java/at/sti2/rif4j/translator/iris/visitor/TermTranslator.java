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

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.factory.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.condition.ExternalExpression;
import at.sti2.rif4j.condition.List;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.condition.TermVisitor;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisConstantMapper;

public class TermTranslator implements TermVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(TermTranslator.class);

	private java.util.List<ITerm> terms;

	private java.util.List<ILiteral> literals;

	public TermTranslator() {
		reset();
	}

	public static ITuple toTuple(java.util.List<ITerm> terms) {
		ITerm[] termArray = toArray(terms);
		ITuple tuple = Factory.BASIC.createTuple(termArray);

		return tuple;
	}

	public static ITerm[] toArray(java.util.List<ITerm> terms) {
		ITerm[] termArray = new ITerm[terms.size()];
		termArray = terms.toArray(termArray);
		return termArray;
	}

	public java.util.List<ITerm> getTerms() {
		return terms;
	}

	public java.util.List<ILiteral> getLiterals() {
		return literals;
	}

	public void reset() {
		terms = new ArrayList<ITerm>();
		literals = new ArrayList<ILiteral>();
	}

	@Override
	public void visit(Constant constant) {
		if (constant == null) {
			logger.error("Constant is null");
			return;
		}

		RifToIrisConstantMapper mapper = new RifToIrisConstantMapper();

		ITerm irisTerm = mapper.toIrisTerm(constant);

		if (irisTerm != null) {
			terms.add(irisTerm);
		}
	}

	@Override
	public void visit(Expression expression) {
		ExpressionFlattener flattener = new ExpressionFlattener();
		flattener.flatten(expression);

		terms.add(flattener.getVariable());
		literals.addAll(flattener.getLiterals());
	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		externalExpression.getExpression().accept(this);
	}

	@Override
	public void visit(List list) {
		java.util.List<ITerm> irisTerms = new ArrayList<ITerm>();
		java.util.List<ILiteral> irisLiterals = new ArrayList<ILiteral>();

		for (Term element : list.getElements()) {
			TermTranslator translator = new TermTranslator();
			element.accept(translator);

			irisTerms.addAll(translator.getTerms());
			irisLiterals.addAll(translator.getLiterals());
		}

		// FIXME Create a list with all terms from irisTerms.
		java.util.List<IConcreteTerm> concreteTerms = new ArrayList<IConcreteTerm>();

		for (ITerm irisTerm : irisTerms) {
			if (irisTerm instanceof IConcreteTerm) {
				concreteTerms.add((IConcreteTerm) irisTerm);
			}
		}

		IConcreteTerm[] irisTermArray = new IConcreteTerm[concreteTerms.size()];
		concreteTerms.toArray(irisTermArray);

		IList irisList = Factory.CONCRETE.createList(irisTermArray);

		terms.add(irisList);
		literals.addAll(irisLiterals);
	}

	@Override
	public void visit(Variable variable) {
		IVariable irisVariable = Factory.TERM
				.createVariable(variable.getName());

		terms.add(irisVariable);
	}
}
