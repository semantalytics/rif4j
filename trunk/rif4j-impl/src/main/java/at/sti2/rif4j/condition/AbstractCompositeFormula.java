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

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * A composite formula is a formula consisting of multiple formulas.
 * 
 * @author Adrian Marte
 * @see Formula
 */
abstract class AbstractCompositeFormula extends AbstractDescribable implements
		CompositeFormula {

	private List<Formula> formulas;

	public AbstractCompositeFormula() {
		formulas = new ArrayList<Formula>();
	}

	/**
	 * Creates a new composite formula with the specified list of formulas.
	 * 
	 * @param formulas The list of formulas this composite formulas contains.
	 * @throws NullPointerException If formulas is <code>null</code>.
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
	 * @param formulas The list of formulas this composite formula should
	 *            contain.
	 * @throws NullPointerException If formulas is <code>null</code>.
	 */
	public void setFormulas(List<Formula> formulas) {
		Assertions.notNull("formulas", formulas);

		this.formulas = formulas;
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept((CompositeFormulaVisitor) serializer);
		return serializer.getString();
	}

}
