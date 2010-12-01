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
package at.sti2.rif4j.condition;

import java.util.List;

import at.sti2.rif4j.Assertions;

/**
 * @author Adrian Marte
 * @author Daniel Winkler
 */
public class ExistsFormula extends AbstractCompositeFormula implements
		CompositeFormula, Formula {

	private List<Variable> variables;

	public ExistsFormula(List<Variable> variables, List<Formula> formulas) {
		// TODO am: one single formula?
		super(formulas);

		Assertions.notNull("variables", variables);

		this.variables = variables;

	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		Assertions.notNull("variables", variables);

		this.variables = variables;
	}

	public void accept(CompositeFormulaVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(FormulaVisitor visitor) {
		visitor.visit(this);
	}

}
