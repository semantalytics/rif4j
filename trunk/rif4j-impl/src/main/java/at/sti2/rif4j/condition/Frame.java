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
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.RuleVisitor;

/**
 * @author Adrian Marte
 */
public class Frame extends AbstractDescribable implements AtomicFormula {

	private Term object;

	private List<Attribute> attributes;

	public Frame(Term object) {
		this(object, new ArrayList<Attribute>());
	}

	public Frame(Term object, List<Attribute> attributes) {
		Assertions.notNull("object", object);
		Assertions.notNull("attributes", attributes);

		this.object = object;
		this.attributes = attributes;
	}

	public Term getObject() {
		return object;
	}

	public void setObject(Term object) {
		Assertions.notNull("object", object);

		this.object = object;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		Assertions.notNull("attributes", attributes);

		this.attributes = attributes;
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

}
