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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;

import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisBuiltinMapper;

public class ExpressionFlattener {

	private static int uniqueVariableCounter = 0;

	private IVariable variable;

	private java.util.List<ILiteral> literals;

	public ExpressionFlattener() {
		reset();
	}

	private void reset() {
		variable = null;
		literals = new ArrayList<ILiteral>();
	}

	/**
	 * Returns the variable representing the result of an expression. If the
	 * flattened entity is not an expression and no new variable was created,
	 * this method returns <code>null</code>
	 * 
	 * @return The variable representing the result of an expression. If the
	 *         flattened entity is not an expression and no new variable was
	 *         created, this method returns <code>null</code>
	 */
	public IVariable getVariable() {
		return variable;
	}

	public java.util.List<ILiteral> getLiterals() {
		return literals;
	}

	private IVariable createUniqueVariable() {
		uniqueVariableCounter++;

		return Factory.TERM.createVariable("var" + uniqueVariableCounter);
	}

	public void flatten(Expression expression) {
		String operatorIri = expression.getOperator().getText().trim();
		java.util.List<Argument> arguments = expression.getArguments();

		flatten(operatorIri, arguments, true);
	}

	public void flatten(Atom atom) {
		String operatorIri = atom.getOperator().getText().trim();
		java.util.List<Argument> arguments = atom.getArguments();

		flatten(operatorIri, arguments, false);
	}

	private void flatten(String operatorIri, List<Argument> arguments,
			boolean isExpression) {
		List<Argument> sortedArguments = sort(arguments);
		java.util.List<ITerm> irisTerms = new ArrayList<ITerm>();

		// A list of additional literals, which are conjuncted with the
		// IRIS representation of this expression or atom. For instance,
		// "http://atomUri(xsd#string(1^^xsd#integer), "foo")" is
		// transformed to "http://atomUri"(?uniqueVarName, "foo"), and
		// TO_STRING(?uniqueVarName, 1)".
		java.util.List<ILiteral> irisLiterals = new ArrayList<ILiteral>();

		TermTranslator termTranslator = new TermTranslator();

		for (Argument argument : sortedArguments) {
			termTranslator.reset();

			// In IRIS an argument can not be named, therefore, we can ignore
			// the name and only use the value.
			argument.getValue().accept(termTranslator);

			if (termTranslator.getTerms() != null) {
				irisTerms.addAll(termTranslator.getTerms());
			}

			if (termTranslator.getLiterals() != null) {
				irisLiterals.addAll(termTranslator.getLiterals());
			}
		}

		IAtom atom = null;

		if (isExpression) {
			// Create a unique variable representing the result of the
			// expression. As in IRIS the result is always at the last position,
			// we put the variable at the end of the list of terms.
			IVariable variable = createUniqueVariable();
			irisTerms.add(variable);

			// Store the variable in a field, so that I can later be retrieved.
			this.variable = variable;
		}

		ITerm[] terms = TermTranslator.toArray(irisTerms);
		atom = RifToIrisBuiltinMapper.toIrisBuiltin(operatorIri, terms);

		// Create an ordinary atom if this is not a mappable built-in or if it
		// is not an expression.
		if (atom == null) {
			IPredicate predicate = Factory.BASIC.createPredicate(operatorIri,
					irisTerms.size());
			ITuple tuple = TermTranslator.toTuple(irisTerms);

			atom = Factory.BASIC.createAtom(predicate, tuple);
		}

		ILiteral literal = Factory.BASIC.createLiteral(true, atom);

		this.literals.add(literal);
		this.literals.addAll(irisLiterals);
	}

	public static int getCurrentVariableNumber() {
		return uniqueVariableCounter;
	}

	private static List<Argument> sort(List<Argument> arguments) {
		// Create a copy of the list.
		List<Argument> newArguments = new ArrayList<Argument>(arguments);

		// Sort the arguments using their names in order to have a deterministic
		// order of named arguments.
		ArgumentComparator comparator = new ArgumentComparator();
		Collections.sort(newArguments, comparator);

		return newArguments;
	}

	private static class ArgumentComparator implements Comparator<Argument> {

		@Override
		public int compare(Argument argument1, Argument argument2) {
			String name1 = argument1.getName();
			String name2 = argument2.getName();

			if (name1 == null || name1.isEmpty() || name2 == null
					|| name2.isEmpty()) {
				return 0;
			}

			return argument1.getName().compareTo(argument2.getName());
		}

	}

}
