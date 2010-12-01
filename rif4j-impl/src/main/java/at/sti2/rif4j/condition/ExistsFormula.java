/*
 * Copyright (c) 2009, STI Innsbruck
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
