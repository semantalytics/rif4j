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

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.RuleVisitor;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
public class EqualAtom extends AbstractDescribable implements AtomicFormula {

	private List<Term> terms;

	public EqualAtom(List<Term> terms) {
		Assertions.notNull("terms", terms);

		checkSize(terms);

		this.terms = terms;
	}

	private void checkSize(List<Term> terms) {
		if (terms.size() != 2) {
			throw new IllegalArgumentException(
					"Number of terms must be 2");
		}
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		checkSize(terms);

		this.terms = terms;
	}

	public void accept(AtomicFormulaVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(ClauseVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(FormulaVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(RuleVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept((AtomicFormulaVisitor) serializer);
		return serializer.getString();
	}

}
