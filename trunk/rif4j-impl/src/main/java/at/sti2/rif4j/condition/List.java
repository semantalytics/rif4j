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

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * <p>
 * There are two kinds of list terms: open and closed.
 * </p>
 * <p>
 * A closed list has the form List(t1 ... tm), where m>0 and t1, ..., tm are
 * terms. An open list (or a list with a tail) has the form OpenList(t1 ... tm
 * t), where m>0 and t1, ..., tm, t are terms. Open lists are usually written
 * using the following: List(t1 ... tm | t).
 * </p>
 * 
 * @author Adrian Marte
 */
public class List extends AbstractDescribable implements Term {

	private boolean isOpen;

	private java.util.List<Term> elements;

	/**
	 * Creates an empty closed {@link List}.
	 */
	public List() {
		elements = new ArrayList<Term>();
	}

	/**
	 * Creates a closed {@link List} for the specified elements.
	 * 
	 * @param elements
	 *            The elements of the list.
	 */
	public List(java.util.List<Term> elements) {
		Assertions.notNull("elements", elements);

		this.elements = elements;
	}

	/**
	 * Returns <code>true</code> if this is an open list. An open list (or a
	 * list with a tail) has the form OpenList(t1 ... tm t), where m>0 and t1,
	 * ..., tm, t are terms. Returns <code>false</code> per default.
	 * 
	 * @returns <code>true</code> if this is an open list, <code>false</code>
	 *          otherwise.
	 */
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Determines if this is a open list. An open list (or a list with a tail)
	 * has the form OpenList(t1 ... tm t), where m>0 and t1, ..., tm, t are
	 * terms.
	 * 
	 * @param isOpen
	 *            <code>true</code> if this is an open list, <code>false</code>
	 *            otherwise.
	 */
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public java.util.List<Term> getElements() {
		return elements;
	}

	public void setElements(java.util.List<Term> elements) {
		Assertions.notNull("elements", elements);

		this.elements = elements;
	}

	public List flatten() {
		java.util.List<Term> newElements = new ArrayList<Term>();

		for (Term element : elements) {
			if (element instanceof List) {
				List elementList = (List) element;
				newElements.addAll(elementList.flatten().getElements());
			} else {
				newElements.add(element);
			}
		}

		if (!elements.equals(newElements)) {
			List newList = new List(newElements);
			newList.setId(getId());
			newList.setMetadata(getMetadata());

			return newList;
		}

		return this;
	}

	@Override
	public void accept(TermVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept(serializer);
		return serializer.getString();
	}

}
