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
package at.sti2.wsmo4j.translator.rif;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.wsmo.common.BuiltIn;
import org.wsmo.common.Identifier;
import org.wsmo.factory.FactoryContainer;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.CompositeFormula;
import at.sti2.rif4j.condition.CompositeFormulaVisitor;
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
import at.sti2.rif4j.condition.TermVisitor;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.ImpliesFormula;
import at.sti2.rif4j.rule.RuleVisitor;

/**
 * @author Adrian Marte
 */
class RifEntityTransformer implements TermVisitor, ClauseVisitor,
		FormulaVisitor, AtomicFormulaVisitor, CompositeFormulaVisitor,
		RuleVisitor {

	private Term term;

	private java.util.List<Term> terms;

	private LogicalExpression expression;

	private java.util.List<LogicalExpression> expressions;

	private static int uniqueVariableCounter = 0;

	private FactoryContainer factories;

	private RifToWsmlConstantMapper constantMapper;

	private RifToWsmlBuiltinMapper builtinMapper;

	public RifEntityTransformer(FactoryContainer factories) {
		this.factories = factories;
		constantMapper = new RifToWsmlConstantMapper();
		builtinMapper = new RifToWsmlBuiltinMapper();
	}

	private void reset() {
		term = null;
		terms = null;
		expression = null;
		expressions = null;
	}

	public Term getTerm() {
		return term;
	}

	public LogicalExpression getExpression() {
		return expression;
	}

	public java.util.List<Term> getTerms() {
		return terms;
	}

	public java.util.List<LogicalExpression> getExpressions() {
		return expressions;
	}

	private org.omwg.ontology.Variable createUniqueVariable() {
		uniqueVariableCounter++;

		return factories.getLogicalExpressionFactory().createVariable(
				"var" + this.hashCode() + uniqueVariableCounter);
	}

	private LogicalExpression makeConjunction(
			Collection<LogicalExpression> expressions) {
		LogicalExpression conjunction = null;

		for (LogicalExpression expression : expressions) {
			if (conjunction != null) {
				conjunction = factories.getLogicalExpressionFactory()
						.createConjunction(conjunction, expression);
			} else {
				conjunction = expression;
			}
		}

		return conjunction;
	}

	protected LogicalExpression makeConjunction(
			LogicalExpression... expressions) {
		return makeConjunction(Arrays.asList(expressions));
	}

	protected LogicalExpression makeDisjunction(
			Collection<LogicalExpression> expressions) {
		LogicalExpression disjunction = null;

		for (LogicalExpression expression : expressions) {
			if (disjunction != null) {
				disjunction = factories.getLogicalExpressionFactory()
						.createDisjunction(disjunction, expression);
			} else {
				disjunction = expression;
			}
		}

		return disjunction;
	}

	protected Set<org.omwg.ontology.Variable> convertVariables(
			Collection<Variable> variables) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		Set<org.omwg.ontology.Variable> wsmlVariables = new HashSet<org.omwg.ontology.Variable>();

		for (Variable variable : variables) {
			transformer.reset();
			variable.accept(transformer);
			Term wsmlVariable = transformer.getTerm();

			if (wsmlVariable != null
					&& wsmlVariable instanceof org.omwg.ontology.Variable) {
				wsmlVariables.add((org.omwg.ontology.Variable) wsmlVariable);
			}
		}

		return wsmlVariables;
	}

	private java.util.List<LogicalExpression> convertFormulas(
			Collection<Formula> formulas) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		java.util.List<LogicalExpression> wsmlFormulas = new ArrayList<LogicalExpression>();

		for (Formula formula : formulas) {
			transformer.reset();
			formula.accept(transformer);

			LogicalExpression wsmlFormula = transformer.getExpression();

			if (wsmlFormula != null) {
				wsmlFormulas.add(wsmlFormula);
			}
		}

		return wsmlFormulas;
	}

	public void visit(Constant constant) {
		Term wsmlTerm = constantMapper.toWsmlTerm(constant);

		if (wsmlTerm == null) {
			wsmlTerm = factories.getXmlDataFactory().createString(
					constant.getText().trim());
		}

		term = wsmlTerm;
	}

	public void visit(Expression expression) {
		String operatorIri = expression.getOperator().getText().trim();
		String wsmlOperatorIri = builtinMapper.toWsmlBuiltin(operatorIri);

		if (wsmlOperatorIri == null) {
			wsmlOperatorIri = operatorIri;
		}

		Identifier wsmlId = factories.getWsmoFactory().createIRI(
				wsmlOperatorIri);
		java.util.List<Term> wsmlTerms = new ArrayList<Term>();

		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		// Create a unique variable representing the result of the expression.
		org.omwg.ontology.Variable variable = createUniqueVariable();
		wsmlTerms.add(variable);
		this.term = variable;

		// A list for additional expressions, which are conjuncted with the
		// WSML atom representation of this expression. For instance,
		// http://atomUri(xsd#string(1^^xsd#integer), "foo") is
		// transformed to _"http://atomUri"(?uniqueVarName, "foo")
		// and _"toString"(?uniqueVarName, 1).
		java.util.List<LogicalExpression> wsmlExpressions = new ArrayList<LogicalExpression>();

		for (Argument argument : expression.getArguments()) {
			transformer.reset();
			argument.getValue().accept(transformer);

			if (transformer.getTerm() != null) {
				wsmlTerms.add(transformer.getTerm());
			}

			if (transformer.getTerms() != null) {
				wsmlTerms.addAll(transformer.getTerms());
			}

			if (transformer.getExpression() != null) {
				wsmlExpressions.add(transformer.getExpression());
			}
		}

		this.expression = factories.getLogicalExpressionFactory().createAtom(
				wsmlId, wsmlTerms);

		if (wsmlExpressions.size() > 0) {
			wsmlExpressions.add(0, this.expression);
			this.expression = makeConjunction(wsmlExpressions);
		}
	}

	public void visit(ExternalExpression externalExpression) {
		externalExpression.getExpression().accept(this);
	}

	public void visit(List list) {
		terms = new ArrayList<Term>();

		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		java.util.List<Term> wsmlTerms = new ArrayList<Term>();
		java.util.List<LogicalExpression> wsmlExpressions = new ArrayList<LogicalExpression>();

		for (at.sti2.rif4j.condition.Term element : list.getElements()) {
			transformer.reset();
			element.accept(transformer);

			if (transformer.getTerm() != null) {
				wsmlTerms.add(transformer.getTerm());
			}

			if (transformer.getTerms() != null) {
				wsmlTerms.addAll(transformer.getTerms());
			}

			if (transformer.getExpression() != null) {
				wsmlExpressions.add(transformer.getExpression());
			}
		}

		// FIXME Use a list data type, if available.
		this.terms = wsmlTerms;

		if (wsmlExpressions.size() > 0) {
			wsmlExpressions.add(0, this.expression);
			this.expression = makeConjunction(wsmlExpressions);
		}
	}

	public void visit(Variable variable) {
		term = factories.getLogicalExpressionFactory().createVariable(
				variable.getName());
	}

	public void visit(ImpliesFormula implies) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		implies.getBody().accept(transformer);
		LogicalExpression wsmlBody = transformer.getExpression();

		java.util.List<LogicalExpression> wsmlHeadExpressions = new ArrayList<LogicalExpression>();

		for (AtomicFormula atomicFormula : implies.getHead()) {
			transformer.reset();
			atomicFormula.accept((AtomicFormulaVisitor) transformer);

			LogicalExpression wsmlHeadExpression = transformer.getExpression();

			if (wsmlHeadExpression != null) {
				wsmlHeadExpressions.add(wsmlHeadExpression);
			}
		}

		LogicalExpression wsmlHead = makeConjunction(wsmlHeadExpressions);

		if (wsmlHead != null && wsmlBody != null) {
			expression = factories.getLogicalExpressionFactory()
					.createLogicProgrammingRule(wsmlHead, wsmlBody);
		}
	}

	public void visit(AtomicFormula atomicFormula) {
		atomicFormula.accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		java.util.List<LogicalExpression> wsmlFormulas = convertFormulas(existsFormula
				.getFormulas());
		LogicalExpression wsmlClause = makeConjunction(wsmlFormulas);

		if (wsmlClause != null) {
			expression = wsmlClause;

			// TODO Respect existential quanitfication.
			// Set<org.omwg.ontology.Variable> wsmlVariables =
			// convertVariables(existsFormula
			// .getVariables());

			// expression = factories.getLogicalExpressionFactory()
			// .createExistentialQuantification(wsmlVariables, wsmlClause);
		}
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		externalFormula.getAtom().accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(ForallFormula forallFormula) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);
		forallFormula.getClause().accept((ClauseVisitor) transformer);

		LogicalExpression wsmlClause = transformer.getExpression();

		if (wsmlClause != null) {
			expression = wsmlClause;

			// TODO Respect universal quanitfication.
			// Set<org.omwg.ontology.Variable> wsmlVariables =
			// convertVariables(forallFormula
			// .getVariables());

			// expression = factories.getLogicalExpressionFactory()
			// .createUniversalQuantification(wsmlVariables, wsmlClause);
		}
	}

	@Override
	public void visit(CompositeFormula compositeFormula) {
		compositeFormula.accept((CompositeFormulaVisitor) this);
	}

	@Override
	public void visit(at.sti2.rif4j.condition.Atom atom) {
		String operatorIri = atom.getOperator().getText().trim();
		String wsmlOperatorIri = builtinMapper.toWsmlBuiltin(operatorIri);

		if (wsmlOperatorIri == null) {
			wsmlOperatorIri = operatorIri;
		}

		Identifier wsmlId = factories.getWsmoFactory().createIRI(
				wsmlOperatorIri);

		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		java.util.List<Term> wsmlTerms = new ArrayList<Term>();

		// A list for additional expressions, which are conjuncted with the
		// WSML atom representation of this atom. For instance,
		// http://atomUri(xsd#string(1^^xsd#integer), "foo") is
		// transformed to _"http://atomUri"(?uniqueVarName, "foo")
		// and _"toString"(?uniqueVarName, 1).
		java.util.List<LogicalExpression> wsmlExpressions = new ArrayList<LogicalExpression>();

		for (Argument argument : atom.getArguments()) {
			transformer.reset();

			at.sti2.rif4j.condition.Term value = argument.getValue();

			value.accept(transformer);

			if (transformer.getTerm() != null) {
				wsmlTerms.add(transformer.getTerm());
			}

			if (transformer.getTerms() != null) {
				wsmlTerms.addAll(transformer.getTerms());
			}

			if (transformer.getExpression() != null) {
				wsmlExpressions.add(transformer.getExpression());
			}
		}

		this.expression = factories.getLogicalExpressionFactory().createAtom(
				wsmlId, wsmlTerms);

		if (wsmlExpressions.size() > 0) {
			wsmlExpressions.add(0, this.expression);
			this.expression = makeConjunction(wsmlExpressions);
		}
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		// An EqualAtom currently has exactly 2 terms. If one of the two terms
		// is an expression, we assume that the expression represents a function
		// built-in, otherwise the equals/assignment makes no sense. If both
		// terms are expressions, we assume that both represent a function
		// built-in and we put the results of both functions in an equality
		// relation.

		if (equalAtom.getTerms().size() != 2) {
			return;
		}

		// The identifier for WSML's equal.
		Identifier equalId = factories.getWsmoFactory().createIRI(
				BuiltIn.EQUAL.getFullName());

		RifEntityTransformer transformer = new RifEntityTransformer(factories);
		java.util.List<Term> wsmlTerms = new ArrayList<Term>();

		// A list for additional expressions, which are conjuncted with the
		// WSML atom representation of this atom. For instance,
		// http://atomUri(xsd#string(1^^xsd#integer), "foo") is
		// transformed to _"http://atomUri"(?uniqueVarName, "foo")
		// and _"toString"(?uniqueVarName, 1).
		java.util.List<LogicalExpression> wsmlExpressions = new ArrayList<LogicalExpression>();

		for (at.sti2.rif4j.condition.Term value : equalAtom.getTerms()) {
			transformer.reset();

			value.accept(transformer);

			if (transformer.getTerm() != null) {
				wsmlTerms.add(transformer.getTerm());
			}

			if (transformer.getTerms() != null) {
				wsmlTerms.addAll(transformer.getTerms());
			}

			if (transformer.getExpression() != null) {
				wsmlExpressions.add(transformer.getExpression());
			}
		}

		this.expression = factories.getLogicalExpressionFactory().createAtom(
				equalId, wsmlTerms);

		if (wsmlExpressions.size() > 0) {
			wsmlExpressions.add(0, this.expression);
			this.expression = makeConjunction(wsmlExpressions);
		}
	}

	@Override
	public void visit(Frame frame) {
		java.util.List<LogicalExpression> wsmlAttributes = new ArrayList<LogicalExpression>();

		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		frame.getObject().accept(transformer);
		Term wsmlInstanceId = transformer.getTerm();

		for (Attribute attribute : frame.getAttributes()) {
			at.sti2.rif4j.condition.Term name = attribute.getName();
			at.sti2.rif4j.condition.Term value = attribute.getValue();

			transformer.reset();
			name.accept(transformer);

			Term wsmlAttributeId = transformer.getTerm();

			transformer.reset();
			value.accept(transformer);

			Term wsmlAttributeValue = transformer.getTerm();
			java.util.List<Term> wsmlAttributeValues = transformer.getTerms();

			if (wsmlInstanceId != null && wsmlAttributeId != null) {
				if (wsmlAttributeValue != null) {
					LogicalExpression molecule = factories
							.getLogicalExpressionFactory()
							.createAttributeValue(wsmlInstanceId,
									wsmlAttributeId, wsmlAttributeValue);
					wsmlAttributes.add(molecule);
				} else if (wsmlAttributeValues != null) {
					LogicalExpression molecule = factories
							.getLogicalExpressionFactory()
							.createAttribusteValues(wsmlInstanceId,
									wsmlAttributeId, wsmlAttributeValues);
					wsmlAttributes.add(molecule);
				}
			}
		}

		expression = makeConjunction(wsmlAttributes);
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		memberAtom.getInstance().accept(transformer);
		Term wsmlInstanceId = transformer.getTerm();

		transformer.reset();
		memberAtom.getClassOfInstance().accept(transformer);

		Term wsmlConceptId = transformer.getTerm();
		java.util.List<Term> wsmlConceptIds = transformer.getTerms();

		if (wsmlInstanceId != null) {
			if (wsmlConceptId != null) {
				expression = factories
						.getLogicalExpressionFactory()
						.createMemberShipMolecule(wsmlInstanceId, wsmlConceptId);
			} else if (wsmlConceptIds != null) {
				expression = factories.getLogicalExpressionFactory()
						.createMemberShipMolecules(wsmlInstanceId,
								wsmlConceptIds);
			}
		}
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);

		subclassAtom.getSubClass().accept(transformer);
		Term wsmlSubclassId = transformer.getTerm();

		transformer.reset();
		subclassAtom.getSuperClass().accept(transformer);

		Term wsmlSuperclassId = transformer.getTerm();
		java.util.List<Term> wsmlSuperclassIds = transformer.getTerms();

		if (wsmlSubclassId != null) {
			if (wsmlSuperclassId != null) {
				expression = factories.getLogicalExpressionFactory()
						.createSubConceptMolecule(wsmlSubclassId,
								wsmlSuperclassId);
			} else if (wsmlSuperclassIds != null) {
				expression = factories.getLogicalExpressionFactory()
						.createSubConceptMolecules(wsmlSubclassId,
								wsmlSuperclassIds);
			}
		}
	}

	@Override
	public void visit(AndFormula andFormula) {
		java.util.List<LogicalExpression> wsmlFormulas = convertFormulas(andFormula
				.getFormulas());

		expression = makeConjunction(wsmlFormulas);
	}

	@Override
	public void visit(OrFormula orFormula) {
		java.util.List<LogicalExpression> wsmlFormulas = convertFormulas(orFormula
				.getFormulas());

		expression = makeDisjunction(wsmlFormulas);
	}

	@Override
	public void visit(Clause clause) {
		clause.accept((ClauseVisitor) this);
	}

}
