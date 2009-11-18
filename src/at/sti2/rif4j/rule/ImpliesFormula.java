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
package at.sti2.rif4j.rule;

import java.util.List;


import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.Describable;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.Formula;

/**
 * Represents an implies formula (implication). An implies formula consists of a
 * body, the "if" part, and a head, the "then" part. The body is a single
 * formula, whereas the head is represented as a list of atomic formulas, which
 * can be interpreted conjunctively or disjunctively.
 * 
 * @author Adrian Marte
 * @see Formula
 * @see AtomicFormula
 */
public class ImpliesFormula extends AbstractDescribable implements Clause, Describable {

	private List<AtomicFormula> head;

	private Formula body;

	/**
	 * Creates a new implies formula with the specified body and head.
	 * 
	 * @param body The body of the implies formula, i.e. the "if" part.
	 * @param head The head of the implies formula, i.e. the "then" part.
	 */
	public ImpliesFormula(Formula body, List<AtomicFormula> head) {
		this.head = head;
		this.body = body;
	}

	/**
	 * Returns the head (the "then" part) of this implies formula represented as
	 * a list of atomic formulas, which can be interpreted conjunctively or
	 * disjunctively.
	 * 
	 * @return The head of this implies formula.
	 */
	public List<AtomicFormula> getHead() {
		return head;
	}

	/**
	 * Sets the head (the "then" part) of this implies formula represented as a
	 * list of atomic formulas, which can be interpreted conjunctively or
	 * disjunctively.
	 * 
	 * @param head The head of this implies formula.
	 * @throws NullPointerException If the <code>head</code> parameter is
	 *             <code>null</code>.
	 */
	public void setHead(List<AtomicFormula> head) {
		Assertions.notNull("head", head);

		this.head = head;
	}

	/**
	 * Returns the body (the "if" part) of this implies formula.
	 * 
	 * @return The body of this implies formula.
	 */
	public Formula getBody() {
		return body;
	}

	/**
	 * Sets the body (the "if" part) of this implies formula.
	 * 
	 * @param body The body to set for this implies formula.
	 * @throws NullPointerException If the <code>body</code> parameter is
	 *             <code>null</code>.
	 */
	public void setBody(Formula body) {
		Assertions.notNull("body", body);

		this.body = body;
	}

	public void accept(ClauseVisitor visitor) {
		visitor.visit(this);
	}

	public void accept(RuleVisitor visitor) {
		visitor.visit(this);
	}
}
