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
 * @author Adrian Marte
 */
public class List extends AbstractDescribable implements Term {

	private java.util.List<Term> elements;
	private java.util.List<Term> restElements;

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

	public void setRestElements(java.util.List<Term> restElements) {
		Assertions.notNull("restElements", restElements);
		this.restElements = restElements;
	}

	public java.util.List<Term> getRestElements() {
		return restElements;
	}

	// TODO am: add rest?!? - Done
	// TODO larizgoitia: make the rest transparent to the user of the list class

}
