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
package at.sti2.rif4j.rule;

import java.util.Collections;
import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.Describable;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

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
public class ImpliesFormula extends AbstractDescribable implements Clause,
		Describable {

	private List<AtomicFormula> head;

	private Formula body;

	/**
	 * Creates a new implies formula with the specified body and head.
	 * 
	 * @param body
	 *            The body of the implies formula, i.e. the "if" part.
	 * @param head
	 *            The head of the implies formula, i.e. the "then" part.
	 */
	public ImpliesFormula(Formula body, AtomicFormula head) {
		this.head = Collections.singletonList(head);
		this.body = body;
	}

	/**
	 * Creates a new implies formula with the specified body and head.
	 * 
	 * @param body
	 *            The body of the implies formula, i.e. the "if" part.
	 * @param head
	 *            The head of the implies formula, i.e. the "then" part. The
	 *            list of {@link AtomicFormula}s should be interpreted as a
	 *            conjunction of {@link AtomicFormula}s.
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
	 * @param head
	 *            The head of this implies formula.
	 * @throws NullPointerException
	 *             If the <code>head</code> parameter is <code>null</code>.
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
	 * @param body
	 *            The body to set for this implies formula.
	 * @throws NullPointerException
	 *             If the <code>body</code> parameter is <code>null</code>.
	 */
	public void setBody(Formula body) {
		Assertions.notNull("body", body);

		this.body = body;
	}

	@Override
	public void accept(ClauseVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void accept(RuleVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept((RuleVisitor) serializer);
		return serializer.getString();
	}
}
