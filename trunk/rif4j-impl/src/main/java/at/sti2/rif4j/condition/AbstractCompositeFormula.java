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

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;

/**
 * A composite formula is a formula consisting of multiple formulas.
 * 
 * @author Adrian Marte
 * @see Formula
 */
abstract class AbstractCompositeFormula extends AbstractDescribable {

	private List<Formula> formulas;

	public AbstractCompositeFormula() {
		formulas = new ArrayList<Formula>();
	}

	/**
	 * Creates a new composite formula with the specified list of formulas.
	 * 
	 * @param formulas
	 *            The list of formulas this composite formulas contains.
	 * @throws NullPointerException
	 *             If formulas is <code>null</code>.
	 */
	public AbstractCompositeFormula(List<Formula> formulas) {
		Assertions.notNull("formulas", formulas);

		this.formulas = formulas;
	}

	/**
	 * Return the list of formulas this composite formula contains. If this
	 * composite formulas does not contain any formula, an empty list is
	 * returned.
	 * 
	 * @return The list of formulas this composite formula contains, or an empty
	 *         list if this composite formulas does not contain any formula.
	 */
	public List<Formula> getFormulas() {
		return formulas;
	}

	/**
	 * Sets the list of formulas of this composite formula.
	 * 
	 * @param formulas
	 *            The list of formulas this composite formula should contain.
	 * @throws NullPointerException
	 *             If formulas is <code>null</code>.
	 */
	public void setFormulas(List<Formula> formulas) {
		Assertions.notNull("formulas", formulas);

		this.formulas = formulas;
	}

}
