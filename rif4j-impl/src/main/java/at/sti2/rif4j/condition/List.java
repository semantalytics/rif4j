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

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
public class List extends AbstractDescribable implements Term {

	private java.util.List<Term> elements;

	public List() {
		elements = new ArrayList<Term>();
	}

	public List(java.util.List<Term> elements) {
		Assertions.notNull("elements", elements);

		this.elements = elements;
	}

	public java.util.List<Term> getElements() {
		return elements;
	}

	public void setElements(java.util.List<Term> elements) {
		Assertions.notNull("elements", elements);

		this.elements = elements;
	}

	public void accept(TermVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept(serializer);
		return serializer.getString();
	}
	
	// TODO am: add rest?!?

}
