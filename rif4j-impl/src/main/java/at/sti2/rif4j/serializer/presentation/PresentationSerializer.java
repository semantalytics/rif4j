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
package at.sti2.rif4j.serializer.presentation;

import java.util.HashMap;
import java.util.Map;

import at.sti2.rif4j.Describable;
import at.sti2.rif4j.RifDatatype;
import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.condition.ExternalExpression;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.List;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.condition.TermVisitor;
import at.sti2.rif4j.condition.Uniterm;
import at.sti2.rif4j.condition.UnitermVisitor;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.DocumentVisitor;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.ImpliesFormula;
import at.sti2.rif4j.rule.Import;
import at.sti2.rif4j.rule.Prefix;
import at.sti2.rif4j.rule.Rule;
import at.sti2.rif4j.rule.RuleVisitor;
import at.sti2.rif4j.serializer.RifSerializer;

public class PresentationSerializer implements DocumentVisitor, TermVisitor,
		ClauseVisitor, FormulaVisitor, AtomicFormulaVisitor, RuleVisitor,
		UnitermVisitor, RifSerializer {

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	private StringBuilder builder;

	private int level;

	private Map<String, String> prefixMappings;

	public PresentationSerializer() {
		builder = new StringBuilder();
		level = 0;
		prefixMappings = new HashMap<String, String>();
	}

	private void increaseLevel() {
		if (builder.length() > 0) {
			level++;
		}
	}

	private void decreaseLevel() {
		if (builder.length() > 0) {
			level--;
		}
	}

	private void newLine() {
		builder.append(LINE_SEPARATOR);
	}

	private void append(String text) {
		append(text, true);
	}

	private void append(String text, boolean indent) {
		if (indent) {
			indent();
		}

		builder.append(text);
	}

	private void indent() {
		if (builder.length() > 0) {
			newLine();
		}

		for (int i = 0; i < level; i++) {
			builder.append("\t");
		}
	}

	private void appendInline(String text) {
		append(text, false);
	}

	private void describe(Describable describable) {
		describe(describable, true);
	}

	private void describeInline(Describable describable) {
		describe(describable, false);
	}

	/**
	 * IRIMETA ::= '(*' IRICONST? (Frame | 'And' '(' Frame* ')')? '*)'
	 */
	private void describe(Describable describable, boolean indent) {
		if (describable.getId() != null
				|| (describable.getMetadata() != null && describable
						.getMetadata().size() > 0)) {
			append("(* ", indent);

			if (describable.getId() != null) {
				describable.getId().accept(this);
				appendInline(" ");
			}

			if (describable.getMetadata() != null
					&& describable.getMetadata().size() > 0) {
				boolean multiple = describable.getMetadata().size() > 1;

				increaseLevel();

				if (multiple) {
					append("And(", indent);
					increaseLevel();
				}

				int i = 0;
				for (Frame frame : describable.getMetadata()) {
					if (indent) {
						indent();
					} else {
						if (i++ > 0) {
							appendInline(" ");
						}
					}

					frame.accept((AtomicFormulaVisitor) this);
				}

				if (multiple) {
					decreaseLevel();
					append(")", indent);
				}

				decreaseLevel();
			}

			append(" *)", indent);
		}
	}

	@Override
	public void visit(Document document) {
		describe(document);
		append("Document(");

		increaseLevel();

		if (document.getBase() != null) {
			append("Base(");
			document.getBase().accept(this);
			appendInline(")");
		}

		for (Prefix prefix : document.getPrefixes()) {
			prefix.accept(this);
		}

		for (Import imprt : document.getImports()) {
			imprt.accept(this);
		}

		if (document.getGroup() != null) {
			document.getGroup().accept(this);
		}

		decreaseLevel();
		append(")");
	}

	@Override
	public void visit(Group group) {
		describe(group);
		append("Group(");

		increaseLevel();

		for (Rule rule : group.getRules()) {
			rule.accept(this);
		}

		for (Group childGroup : group.getGroups()) {
			childGroup.accept(this);
		}

		decreaseLevel();

		append(")");
	}

	@Override
	public void visit(Import imprt) {
		describe(imprt);
		append("Import(");
		append("<" + imprt.getLocation() + ">");

		if (imprt.getProfile() != null) {
			append(" <" + imprt.getProfile() + ">");
		}

		appendInline(")");
	}

	@Override
	public void visit(Prefix prefix) {
		// Add mapping from prefix to corresponding URI.
		prefixMappings.put(prefix.getName(), prefix.getIri().getText());

		append("Prefix(");
		appendInline(prefix.getName() + " ");
		prefix.getIri().accept(this);
		appendInline(")");
	}

	private String searchPrefix(String iri) {
		for (String prefix : prefixMappings.keySet()) {
			String location = prefixMappings.get(prefix);

			if (iri.startsWith(location) && iri.length() > location.length()) {
				String suffix = iri.substring(location.length());
				return prefix + ":" + suffix;
			}
		}

		return null;
	}

	@Override
	public void visit(Constant constant) {
		describeInline(constant);
		String language = "";

		if (constant.getLanguage() != null
				&& constant.getLanguage().length() > 0) {
			language = "@" + constant.getLanguage();
		}

		if (constant.getType() != null) {
			if (constant.getType().equals(RifDatatype.IRI.toString())) {
				// Try to map to prefix.
				String mapping = searchPrefix(constant.getText());

				if (mapping != null) {
					appendInline(mapping);
				} else {
					appendInline("<" + constant.getText() + ">");
				}
			} else if (constant.getType().equals(RifDatatype.LOCAL.toString())) {
				appendInline("_" + constant.getText());
			} else {
				appendInline("\"" + constant.getText() + language + "\"^^");

				// Try to map to prefix.
				String mapping = searchPrefix(constant.getType());

				if (mapping != null) {
					appendInline(mapping);
				} else {
					appendInline("<" + constant.getType() + ">");
				}
			}
		} else {
			appendInline("\"" + constant.getText() + language + "\"");
		}
	}

	@Override
	public void visit(Expression expression) {
		describeInline(expression);
		visit((Uniterm) expression);
	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		describeInline(externalExpression);

		// Expression is a term, therefore, do not indent.
		appendInline("External(");

		increaseLevel();
		externalExpression.getExpression().accept(this);
		decreaseLevel();

		appendInline(")");
	}

	@Override
	public void visit(List list) {
		describeInline(list);
		appendInline("List(");

		int i = 0;
		for (Term element : list.getElements()) {
			if (i++ > 0) {
				appendInline(" ");
			}

			element.accept(this);
		}

		if (list.getRestElements().size() > 0) {
			appendInline(" | ");

			i = 0;
			for (Term element : list.getRestElements()) {
				if (i++ > 0) {
					appendInline(" ");
				}

				element.accept(this);
			}
		}

		appendInline(")");
	}

	@Override
	public void visit(Variable variable) {
		describeInline(variable);
		appendInline("?" + variable.getName());
	}

	@Override
	public void visit(ImpliesFormula implies) {
		describe(implies);
		if (implies.getHead().size() > 1) {
			append("And(");

			increaseLevel();
			for (AtomicFormula atomicFormula : implies.getHead()) {
				atomicFormula.accept((AtomicFormulaVisitor) this);
			}
			decreaseLevel();

			appendInline(")");
		} else if (implies.getHead().size() == 1) {
			implies.getHead().get(0).accept((AtomicFormulaVisitor) this);
		}

		appendInline(" :- ");

		increaseLevel();
		implies.getBody().accept(this);
		decreaseLevel();
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		atomicFormula.accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		describe(existsFormula);
		append("Exists");

		for (Variable variable : existsFormula.getVariables()) {
			appendInline(" ");
			variable.accept(this);
		}

		appendInline(" (");

		increaseLevel();
		// An exists formula only contains a single formula.
		existsFormula.getFormulas().get(0).accept(this);
		decreaseLevel();

		append(")");
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		describe(externalFormula);
		append("External(");

		increaseLevel();
		externalFormula.getAtom().accept((AtomicFormulaVisitor) this);
		decreaseLevel();

		append(")");
	}

	@Override
	public void visit(ForallFormula forallFormula) {
		describe(forallFormula);
		append("Forall");

		for (Variable variable : forallFormula.getVariables()) {
			appendInline(" ");
			variable.accept(this);
		}

		appendInline(" (");

		increaseLevel();
		forallFormula.getClause().accept((ClauseVisitor) this);
		decreaseLevel();

		append(")");
	}

	private void visit(Uniterm uniterm) {
		uniterm.getOperator().accept(this);

		appendInline("(");

		int i = 0;
		for (Argument argument : uniterm.getArguments()) {
			if (i++ > 0) {
				appendInline(" ");
			}

			if (argument.getName() != null) {
				appendInline(argument.getName() + "->");
			}
			argument.getValue().accept(this);
		}

		appendInline(")");
	}

	@Override
	public void visit(Atom atom) {
		describe(atom);
		indent();
		visit((Uniterm) atom);
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		describe(equalAtom);
		indent();
		equalAtom.getTerms().get(0).accept(this);
		appendInline(" = ");
		equalAtom.getTerms().get(1).accept(this);
	}

	@Override
	public void visit(Frame frame) {
		describeInline(frame);
		frame.getObject().accept(this);
		appendInline("[");

		int i = 0;
		for (Attribute attribute : frame.getAttributes()) {
			if (i++ > 0) {
				appendInline(" ");
			}

			attribute.getName().accept(this);
			appendInline("->");
			attribute.getValue().accept(this);
		}

		appendInline("]");
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		describe(memberAtom);
		indent();
		memberAtom.getInstance().accept(this);
		appendInline("#");
		memberAtom.getClassOfInstance().accept(this);
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		describe(subclassAtom);
		indent();
		subclassAtom.getSubClass().accept(this);
		appendInline("##");
		subclassAtom.getSuperClass().accept(this);
	}

	@Override
	public void visit(AndFormula andFormula) {
		describe(andFormula);
		append("And(");

		increaseLevel();
		for (Formula formula : andFormula.getFormulas()) {
			formula.accept(this);
		}
		decreaseLevel();

		append(")");
	}

	@Override
	public void visit(OrFormula orFormula) {
		describe(orFormula);
		append("Or(");

		increaseLevel();
		for (Formula formula : orFormula.getFormulas()) {
			formula.accept(this);
		}
		decreaseLevel();

		append(")");
	}

	@Override
	public void visit(Clause clause) {
		if (clause instanceof Frame) {
			indent();
		}

		clause.accept((ClauseVisitor) this);
	}

	public String getString() {
		return builder.toString();
	}

	@Override
	public String serialize(Document rifDocument) {
		this.visit(rifDocument);
		return builder.toString();
	}

}
