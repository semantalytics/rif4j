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
package at.sti2.rif4j.translator.iris;

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.condition.SubclassAtom;

@Deprecated
public class RdfQueryTransformer implements FormulaVisitor,
		AtomicFormulaVisitor {

	private static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";

	private static final String RDFS_SUBCLASS_OF = "http://www.w3.org/2000/01/rdf-schema#subClassOf";

	private Formula formula;

	public Formula getFormula() {
		return formula;
	}

	@Override
	public void visit(AndFormula andFormula) {
		List<Formula> newFormulas = new ArrayList<Formula>();

		for (Formula formula : andFormula.getFormulas()) {
			RdfQueryTransformer transformer = new RdfQueryTransformer();
			formula.accept(transformer);

			if (transformer.formula != null) {
				newFormulas.add(transformer.formula);
			} else {
				newFormulas.add(formula);
			}
		}

		if (newFormulas.size() > 0) {
			formula = new AndFormula(newFormulas);
		}
	}

	@Override
	public void visit(OrFormula orFormula) {
		List<Formula> newFormulas = new ArrayList<Formula>();

		for (Formula formula : orFormula.getFormulas()) {
			RdfQueryTransformer transformer = new RdfQueryTransformer();
			formula.accept(transformer);

			if (transformer.formula != null) {
				newFormulas.add(transformer.formula);
			} else {
				newFormulas.add(formula);
			}
		}

		if (newFormulas.size() > 0) {
			formula = new OrFormula(newFormulas);
		}
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		RdfQueryTransformer transformer = new RdfQueryTransformer();
		existsFormula.accept(transformer);

		if (transformer.formula != null) {
			existsFormula.setFormula(transformer.formula);
		}
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		RdfQueryTransformer transformer = new RdfQueryTransformer();
		atomicFormula.accept((AtomicFormulaVisitor) transformer);

		if (transformer.formula != null) {
			formula = transformer.formula;
		}
	}

	@Override
	public void visit(Atom atom) {
	}

	@Override
	public void visit(EqualAtom equalAtom) {
	}

	@Override
	public void visit(Frame frame) {
		List<Attribute> newAttributes = new ArrayList<Attribute>();
		List<Formula> atomicFormulas = new ArrayList<Formula>();

		for (Attribute attribute : frame.getAttributes()) {
			if (attribute.getName() instanceof Constant) {
				Constant name = (Constant) attribute.getName();
				AtomicFormula atomicFormula = null;

				if (name.getText() != null) {
					if (name.getText().equals(RDF_TYPE)) {
						atomicFormula = new MemberAtom(frame.getObject(),
								attribute.getValue());
					} else if (name.getText().equals(RDFS_SUBCLASS_OF)) {
						atomicFormula = new SubclassAtom(frame.getObject(),
								attribute.getValue());
					}
				}

				if (atomicFormula != null) {
					atomicFormulas.add(atomicFormula);
				} else {
					newAttributes.add(attribute);
				}
			}
		}

		if (atomicFormulas.size() > 0) {
			List<Formula> formulas = new ArrayList<Formula>();

			if (newAttributes.size() > 0) {
				Frame newFrame = new Frame(frame.getObject(), newAttributes);
				formulas.add(newFrame);
			}

			formulas.addAll(atomicFormulas);

			if (atomicFormulas.size() == 1) {
				formula = atomicFormulas.get(0);
			} else {
				formula = new AndFormula(formulas);
			}
		}
	}

	@Override
	public void visit(MemberAtom memberAtom) {
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
	}

}
