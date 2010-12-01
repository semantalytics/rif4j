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

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.RuleVisitor;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

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

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept((AtomicFormulaVisitor) serializer);
		return serializer.getString();
	}

}
