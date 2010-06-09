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


import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.RuleVisitor;

/**
 * @author Adrian Marte
 */
public class SubclassAtom extends AbstractDescribable implements AtomicFormula {

	private Term superClass;

	private Term subClass;

	public SubclassAtom(Term subClass, Term superClass) {
		Assertions.notNull("subClass", subClass);
		Assertions.notNull("superClas", superClass);

		this.subClass = subClass;
		this.superClass = superClass;
	}

	public Term getSuperClass() {
		Assertions.notNull("superClas", superClass);

		return superClass;
	}

	public void setSuperClass(Term superClass) {
		this.superClass = superClass;
	}

	public Term getSubClass() {
		return subClass;
	}

	public void setSubClass(Term subClass) {
		Assertions.notNull("subClass", subClass);

		this.subClass = subClass;
	}

	public void accept(AtomicFormulaVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(FormulaVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(ClauseVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(RuleVisitor visitor) {
		visitor.visit(this);
	}

}
