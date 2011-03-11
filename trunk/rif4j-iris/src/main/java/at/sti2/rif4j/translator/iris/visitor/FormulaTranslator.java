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
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.factory.Factory;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.OrFormula;

public class FormulaTranslator implements FormulaVisitor {

	private List<ILiteral> literals;

	private List<IQuery> queries;

	public FormulaTranslator() {
		reset();
	}

	private void reset() {
		literals = new ArrayList<ILiteral>();
		queries = new ArrayList<IQuery>();
	}

	public List<ILiteral> getLiterals() {
		return literals;
	}

	public List<IQuery> getQueries() {
		return queries;
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		existsFormula.getFormula().accept(this);
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		// This is an externally defined predicate.
		ExpressionFlattener flattener = new ExpressionFlattener();
		flattener.flatten(externalFormula);

		literals.addAll(flattener.getLiterals());

		IQuery query = Factory.BASIC.createQuery(literals);
		queries.add(query);
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulaTranslator);

		literals.addAll(atomicFormulaTranslator.getLiterals());

		IQuery query = Factory.BASIC.createQuery(literals);
		queries.add(query);
	}

	@Override
	public void visit(AndFormula andFormula) {
		for (Formula formula : andFormula.getFormulas()) {
			FormulaTranslator formulaTranslator = new FormulaTranslator();
			formula.accept(formulaTranslator);

			literals.addAll(formulaTranslator.getLiterals());
		}

		IQuery query = Factory.BASIC.createQuery(literals);
		queries.add(query);
	}

	@Override
	public void visit(OrFormula orFormula) {
		for (Formula formula : orFormula.getFormulas()) {
			FormulaTranslator formulaTranslator = new FormulaTranslator();
			formula.accept(formulaTranslator);

			List<ILiteral> disjunctLiterals = formulaTranslator.getLiterals();

			IQuery query = Factory.BASIC.createQuery(disjunctLiterals);
			queries.add(query);

			literals.addAll(formulaTranslator.getLiterals());
		}
	}

}
