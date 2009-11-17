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
package at.sti2.rif4j.parser.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.sti2.rif4j.Describable;
import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.condition.ExternalExpression;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.List;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.condition.Uniterm;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Implies;
import at.sti2.rif4j.rule.Import;
import at.sti2.rif4j.rule.Rule;

/**
 * Extracts and translates RIF XML elements to a corresponding object model for
 * RIF.
 * 
 * @author Adrian Marte
 */
class XmlExtractor {

	private static XPathFactory factory;

	private static XPath xPath;

	static {
		factory = XPathFactory.newInstance();
		xPath = factory.newXPath();
		xPath.setNamespaceContext(new RifNamespaceContext());
	}

	public XmlExtractor() {
	}

	private boolean isSimpleQuery(String query) {
		return !(query.contains("/") || query.contains("[")
				|| query.contains("]") || query.contains("(") || query
				.contains(")"));
	}

	private NodeList queryNodeList(Node context, String query) {
		if (isSimpleQuery(query)) {
			// Remove namespace.
			int index = query.indexOf(":");

			if (index >= 0) {
				query = query.substring(index + 1);
			}

			NodeFilter filter = new NodeFilter(context, query);
			return new FilteredNodeList(filter);
		}

		try {
			XPathExpression expression = xPath.compile(query);
			Object result = expression
					.evaluate(context, XPathConstants.NODESET);
			return (NodeList) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Node queryNode(Node context, String query) {
		NodeList nodeList = queryNodeList(context, query);

		if (nodeList != null && nodeList.getLength() > 0) {
			return nodeList.item(0);
		}

		return null;
	}

	private Double queryNumber(Node context, String query) {
		try {
			XPathExpression expression = xPath.compile(query);
			Object result = expression.evaluate(context, XPathConstants.NUMBER);
			return (Double) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String queryString(Node context, String query) {
		try {
			XPathExpression expression = xPath.compile(query);
			Object result = expression.evaluate(context, XPathConstants.STRING);
			return (String) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void setMetadata(Node context, Describable describable) {
		Metadata metadata = extractMetadata(context);

		if (metadata != null && describable != null) {
			describable.setId(metadata.getId());
			describable.setMetadata(metadata.getData());
		}
	}

	private void setPosition(Node context, Object object,
			Map<Object, Integer> positions) {
		Double number = queryNumber(context, "count(preceding-sibling::*)");
		int position = number.intValue();

		if (positions != null) {
			positions.put(object, position);
		}
	}

	public Document extractDocument(Node context) {
		Node documentNode = queryNode(context, "rif:Document");

		Node payloadNode = queryNode(documentNode, "rif:payload");
		NodeList directiveNodes = queryNodeList(documentNode, "rif:directive");

		java.util.List<Import> imports = extractImports(directiveNodes);
		Group group = extractGroup(payloadNode);

		Document document = new Document();
		document.setImports(imports);
		document.setGroup(group);

		setMetadata(documentNode, document);

		return document;
	}

	public Group extractGroup(Node context) {
		if (context == null) {
			return null;
		}

		Node groupNode = queryNode(context, "rif:Group");

		if (groupNode == null) {
			return null;
		}

		java.util.List<Rule> rules = new ArrayList<Rule>();
		java.util.List<Group> groups = new ArrayList<Group>();

		NodeList sentenceNodes = queryNodeList(groupNode, "rif:sentence");

		for (int i = 0; i < sentenceNodes.getLength(); i++) {
			Node sentenceNode = sentenceNodes.item(i);
			Rule rule = extractRule(sentenceNode);

			if (rule != null) {
				rules.add(rule);
			}

			Group childGroup = extractGroup(sentenceNode);

			if (childGroup != null) {
				groups.add(childGroup);
			}
		}

		Group group = new Group(rules, groups);

		setMetadata(groupNode, group);

		return group;
	}

	public Rule extractRule(Node context) {
		if (context == null) {
			return null;
		}

		java.util.List<ForallFormula> forallFormulas = extractForallFormulas(context);
		if (forallFormulas.size() > 0) {
			return forallFormulas.iterator().next();
		}

		Clause clause = extractClause(context);
		if (clause != null) {
			return clause;
		}

		return null;
	}

	public java.util.List<ForallFormula> extractForallFormulas(Node context) {
		java.util.List<ForallFormula> forallFormulas = new ArrayList<ForallFormula>();

		if (context == null) {
			return forallFormulas;
		}

		NodeList forallNodes = queryNodeList(context, "rif:Forall");

		if (forallNodes == null) {
			return forallFormulas;
		}

		for (int i = 0; i < forallNodes.getLength(); i++) {
			Node forallNode = forallNodes.item(i);

			Node formulaNode = queryNode(forallNode, "rif:formula");
			Clause clause = extractClause(formulaNode);

			java.util.List<Variable> variables = new ArrayList<Variable>();

			NodeList declareNodes = queryNodeList(forallNode, "rif:declare");
			for (int j = 0; j < declareNodes.getLength(); j++) {
				Node declareNode = declareNodes.item(j);
				variables.addAll(extractVariables(declareNode));
			}

			ForallFormula forallFormula = new ForallFormula(variables, clause);
			setMetadata(forallNode, forallFormula);
			forallFormulas.add(forallFormula);
		}

		return forallFormulas;
	}

	public Clause extractClause(Node context) {
		if (context == null) {
			return null;
		}

		Implies implies = extractImplies(context);
		if (implies != null) {
			return implies;
		}

		java.util.List<AtomicFormula> atomicFormula = extractAtomicFormulas(context);
		if (atomicFormula.size() > 0) {
			return atomicFormula.iterator().next();
		}

		return null;
	}

	public Implies extractImplies(Node context) {
		if (context == null) {
			return null;
		}

		Node impliesNode = queryNode(context, "rif:Implies");

		if (impliesNode == null) {
			return null;
		}

		Node ifNode = queryNode(impliesNode, "rif:if");
		java.util.List<Formula> ifFormulas = extractFormulas(ifNode);
		Formula ifFormula = ifFormulas.iterator().next();

		java.util.List<AtomicFormula> thenFormulas = new ArrayList<AtomicFormula>();

		Node thenNode = queryNode(impliesNode, "rif:then");

		NodeList formulaNodes = queryNodeList(impliesNode,
				"rif:then/rif:And/rif:formula");
		for (int i = 0; i < formulaNodes.getLength(); i++) {
			Node formulaNode = formulaNodes.item(i);
			thenFormulas.addAll(extractAtomicFormulas(formulaNode));
		}

		thenFormulas.addAll(extractAtomicFormulas(thenNode));

		Implies implies = new Implies(ifFormula, thenFormulas);

		setMetadata(impliesNode, implies);

		return implies;
	}

	public java.util.List<AtomicFormula> extractAtomicFormulas(Node context) {
		java.util.List<AtomicFormula> formulas = new ArrayList<AtomicFormula>();

		java.util.List<MemberAtom> memberAtoms = extractMemberAtoms(context);
		formulas.addAll(memberAtoms);

		java.util.List<EqualAtom> equalAtoms = extractEqualAtoms(context);
		formulas.addAll(equalAtoms);

		java.util.List<SubclassAtom> subclassAtoms = extractSubclassAtoms(context);
		formulas.addAll(subclassAtoms);

		java.util.List<Atom> atoms = extractAtoms(context);
		formulas.addAll(atoms);

		java.util.List<Frame> frames = extractFrames(context);
		formulas.addAll(frames);

		return formulas;
	}

	public java.util.List<EqualAtom> extractEqualAtoms(Node context) {
		java.util.List<EqualAtom> equalAtoms = new ArrayList<EqualAtom>();

		if (context == null) {
			return equalAtoms;
		}

		NodeList equalNodes = queryNodeList(context, "rif:Equal");

		if (equalNodes == null) {
			return equalAtoms;
		}

		for (int i = 0; i < equalNodes.getLength(); i++) {
			Node equalNode = equalNodes.item(i);

			Node leftNode = queryNode(equalNode, "rif:left");
			java.util.List<Term> leftTerms = extractTerms(leftNode);

			Node rightNode = queryNode(equalNode, "rif:right");
			java.util.List<Term> rightTerms = extractTerms(rightNode);

			java.util.List<Term> terms = new ArrayList<Term>();
			terms.addAll(leftTerms);
			terms.addAll(rightTerms);

			EqualAtom equalAtom = new EqualAtom(terms);
			setMetadata(equalNode, equalAtom);
			equalAtoms.add(equalAtom);
		}

		return equalAtoms;
	}

	public java.util.List<MemberAtom> extractMemberAtoms(Node context) {
		java.util.List<MemberAtom> memberAtoms = new ArrayList<MemberAtom>();

		if (context == null) {
			return memberAtoms;
		}

		NodeList memberNodes = queryNodeList(context, "rif:Member");

		if (memberNodes == null) {
			return memberAtoms;
		}

		for (int i = 0; i < memberNodes.getLength(); i++) {
			Node memberNode = memberNodes.item(i);

			Node classNode = queryNode(memberNode, "rif:class");
			java.util.List<Term> classTerms = extractTerms(classNode);
			Term classTerm = classTerms.iterator().next();

			Node instanceNode = queryNode(memberNode, "rif:instance");
			java.util.List<Term> instanceTerms = extractTerms(instanceNode);
			Term instanceTerm = instanceTerms.iterator().next();

			MemberAtom memberAtom = new MemberAtom(instanceTerm, classTerm);
			setMetadata(memberNode, memberAtom);
			memberAtoms.add(memberAtom);
		}

		return memberAtoms;
	}

	public java.util.List<SubclassAtom> extractSubclassAtoms(Node context) {
		java.util.List<SubclassAtom> subclassAtoms = new ArrayList<SubclassAtom>();

		if (context == null) {
			return subclassAtoms;
		}

		NodeList subclassNodes = queryNodeList(context, "rif:Subclass");

		if (subclassNodes == null) {
			return subclassAtoms;
		}

		for (int i = 0; i < subclassNodes.getLength(); i++) {
			Node subclassNode = subclassNodes.item(i);

			Node superNode = queryNode(subclassNode, "rif:super");
			java.util.List<Term> superTerms = extractTerms(superNode);
			Term superTerm = superTerms.iterator().next();

			Node subNode = queryNode(subclassNode, "rif:sub");
			java.util.List<Term> subTerms = extractTerms(subNode);
			Term subTerm = subTerms.iterator().next();

			SubclassAtom subclassAtom = new SubclassAtom(subTerm, superTerm);
			setMetadata(subclassNode, subclassAtom);
			subclassAtoms.add(subclassAtom);
		}

		return subclassAtoms;
	}

	public java.util.List<Atom> extractAtoms(Node context) {
		java.util.List<Atom> atoms = new ArrayList<Atom>();

		if (context == null) {
			return atoms;
		}

		NodeList atomNodes = queryNodeList(context, "rif:Atom");

		if (atomNodes == null) {
			return atoms;
		}

		for (int i = 0; i < atomNodes.getLength(); i++) {
			Node atomNode = queryNode(context, "rif:Atom");

			if (atomNode == null) {
				return null;
			}

			Uniterm uniterm = extractUniterm(atomNode);

			Atom atom = new Atom(uniterm.getOperator(), uniterm.getArguments());
			setMetadata(atomNode, atom);
			atoms.add(atom);
		}

		return atoms;
	}

	public java.util.List<Variable> extractVariables(Node context) {
		return extractVariables(context, null);
	}

	public java.util.List<Variable> extractVariables(Node context,
			Map<Object, Integer> positions) {
		java.util.List<Variable> variables = new ArrayList<Variable>();

		if (context == null) {
			return variables;
		}

		NodeList variableNodes = queryNodeList(context, "rif:Var");
		for (int i = 0; i < variableNodes.getLength(); i++) {
			Node variableNode = variableNodes.item(i);

			String name = variableNode.getTextContent().trim();
			Variable variable = new Variable(name);

			setMetadata(variableNode, variable);
			setPosition(variableNode, variable, positions);

			variables.add(variable);
		}

		return variables;
	}

	public java.util.List<Formula> extractFormulas(Node context) {
		java.util.List<Formula> formulas = new ArrayList<Formula>();

		if (context == null) {
			return formulas;
		}

		java.util.List<AndFormula> andFormulas = extractAndFormulas(context);
		formulas.addAll(andFormulas);

		java.util.List<OrFormula> orFormulas = extractOrFormulas(context);
		formulas.addAll(orFormulas);

		java.util.List<AtomicFormula> atomicFormulas = extractAtomicFormulas(context);
		formulas.addAll(atomicFormulas);

		java.util.List<ExternalFormula> externalFormulas = extractExternalFormulas(context);
		formulas.addAll(externalFormulas);

		java.util.List<ExistsFormula> existsFormulas = extractExistsFormulas(context);
		formulas.addAll(existsFormulas);

		return formulas;
	}

	public java.util.List<AndFormula> extractAndFormulas(Node context) {
		java.util.List<AndFormula> andFormulas = new ArrayList<AndFormula>();

		if (context == null) {
			return andFormulas;
		}

		NodeList andNodes = queryNodeList(context, "rif:And");

		if (andNodes == null) {
			return andFormulas;
		}

		for (int i = 0; i < andNodes.getLength(); i++) {
			Node andNode = andNodes.item(i);

			java.util.List<Formula> formulas = new ArrayList<Formula>();

			NodeList formulaNodes = queryNodeList(andNode, "rif:formula");
			for (int j = 0; j < formulaNodes.getLength(); j++) {
				Node formulaNode = formulaNodes.item(j);
				formulas.addAll(extractFormulas(formulaNode));
			}

			AndFormula andFormula = new AndFormula(formulas);
			setMetadata(andNode, andFormula);
			andFormulas.add(andFormula);
		}

		return andFormulas;
	}

	public java.util.List<OrFormula> extractOrFormulas(Node context) {
		java.util.List<OrFormula> orFormulas = new ArrayList<OrFormula>();

		if (context == null) {
			return orFormulas;
		}

		NodeList orNodes = queryNodeList(context, "rif:Or");

		if (orNodes == null) {
			return orFormulas;
		}

		for (int i = 0; i < orNodes.getLength(); i++) {
			Node orNode = orNodes.item(i);

			java.util.List<Formula> formulas = new ArrayList<Formula>();

			NodeList formulaNodes = queryNodeList(orNode, "rif:formula");
			for (int j = 0; j < formulaNodes.getLength(); j++) {
				Node formulaNode = formulaNodes.item(j);
				formulas.addAll(extractFormulas(formulaNode));
			}

			OrFormula orFormula = new OrFormula(formulas);
			setMetadata(orNode, orFormula);
			orFormulas.add(orFormula);
		}

		return orFormulas;
	}

	public java.util.List<ExistsFormula> extractExistsFormulas(Node context) {
		java.util.List<ExistsFormula> existsFormulas = new ArrayList<ExistsFormula>();

		if (context == null) {
			return existsFormulas;
		}

		NodeList existsNodes = queryNodeList(context, "rif:Exists");

		if (existsNodes == null) {
			return existsFormulas;
		}

		for (int i = 0; i < existsNodes.getLength(); i++) {
			Node existsNode = existsNodes.item(i);

			java.util.List<Formula> formulas = new ArrayList<Formula>();

			NodeList formulaNodes = queryNodeList(existsNode, "rif:formula");
			for (int j = 0; j < formulaNodes.getLength(); j++) {
				Node formulaNode = formulaNodes.item(j);
				formulas.addAll(extractFormulas(formulaNode));
			}

			java.util.List<Variable> variables = new ArrayList<Variable>();

			NodeList declareNodes = queryNodeList(existsNode, "rif:declare");
			for (int j = 0; j < declareNodes.getLength(); j++) {
				Node declareNode = declareNodes.item(j);
				variables.addAll(extractVariables(declareNode));
			}

			ExistsFormula existsFormula = new ExistsFormula(variables, formulas);
			setMetadata(existsNode, existsFormula);
			existsFormulas.add(existsFormula);
		}

		return existsFormulas;
	}

	public java.util.List<ExternalFormula> extractExternalFormulas(Node context) {
		java.util.List<ExternalFormula> formulas = new ArrayList<ExternalFormula>();

		if (context == null) {
			return formulas;
		}

		NodeList externalNodes = queryNodeList(context, "rif:External");

		if (externalNodes == null) {
			return formulas;
		}

		for (int i = 0; i < externalNodes.getLength(); i++) {
			Node externalNode = externalNodes.item(i);
			Node contentNode = queryNode(externalNode, "rif:content");
			java.util.List<Atom> atoms = extractAtoms(contentNode);
			Atom atom = atoms.iterator().next();

			ExternalFormula formula = new ExternalFormula(atom);
			setMetadata(externalNode, formula);
			formulas.add(formula);
		}

		return formulas;
	}

	public java.util.List<ExternalExpression> extractExternalTerms(Node context) {
		return extractExternalTerms(context, null);
	}

	public java.util.List<ExternalExpression> extractExternalTerms(
			Node context, Map<Object, Integer> positions) {
		java.util.List<ExternalExpression> externalTerms = new ArrayList<ExternalExpression>();

		NodeList externalNodes = queryNodeList(context, "rif:External");
		for (int i = 0; i < externalNodes.getLength(); i++) {
			Node externalNode = externalNodes.item(i);
			Node contentNode = queryNode(externalNode, "rif:content");

			java.util.List<Expression> expressions = extractExpressions(contentNode);
			Expression expression = expressions.iterator().next();

			ExternalExpression externalTerm = new ExternalExpression(expression);

			setMetadata(externalNode, externalTerm);
			setPosition(externalNode, externalTerm, positions);

			externalTerms.add(externalTerm);
		}

		return externalTerms;
	}

	public java.util.List<List> extractLists(Node context) {
		return extractLists(context, null);
	}

	public java.util.List<List> extractLists(Node context,
			Map<Object, Integer> positions) {
		java.util.List<List> lists = new ArrayList<List>();

		NodeList listNodes = queryNodeList(context, "rif:List");

		if (listNodes == null) {
			return lists;
		}

		for (int i = 0; i < listNodes.getLength(); i++) {
			java.util.List<Term> terms = new ArrayList<Term>();

			Node listNode = listNodes.item(i);
			java.util.List<Term> listTerms = extractTerms(listNode);
			terms.addAll(listTerms);

			Node restNode = queryNode(listNode, "rif:rest");
			java.util.List<Term> restTerms = extractTerms(restNode);
			terms.addAll(restTerms);

			List list = new List(terms);

			setMetadata(listNode, list);
			setPosition(listNode, list, positions);

			lists.add(list);
		}

		return lists;
	}

	public java.util.List<Expression> extractExpressions(Node context) {
		return extractExpressions(context, null);
	}

	public java.util.List<Expression> extractExpressions(Node context,
			Map<Object, Integer> positions) {
		java.util.List<Expression> expressions = new ArrayList<Expression>();

		NodeList exprNodes = queryNodeList(context, "rif:Expr");

		for (int i = 0; i < exprNodes.getLength(); i++) {
			Node exprNode = exprNodes.item(i);

			Uniterm uniterm = extractUniterm(exprNode);

			Expression expression = new Expression(uniterm.getOperator(),
					uniterm.getArguments());

			setMetadata(exprNode, expression);
			setPosition(exprNode, expression, positions);

			expressions.add(expression);
		}

		return expressions;
	}

	public Uniterm extractUniterm(Node context) {
		Node argsNode = queryNode(context, "rif:args");
		java.util.List<Term> argsTerms = extractTerms(argsNode);

		java.util.List<Argument> arguments = new ArrayList<Argument>();

		// Convert each term to an unnamed argument.
		for (Term term : argsTerms) {
			Argument argument = new Argument(term);
			arguments.add(argument);
		}

		NodeList slotNodes = queryNodeList(context, "rif:slot");
		for (int j = 0; j < slotNodes.getLength(); j++) {
			Node slotNode = slotNodes.item(j);
			Argument argument = extractArgument(slotNode);

			if (argument != null) {
				arguments.add(argument);
			}
		}

		Node opNode = queryNode(context, "rif:op");
		Constant operator = extractConstant(opNode);

		return new SimpleUniterm(operator, arguments);
	}

	public Argument extractArgument(Node context) {
		if (context == null) {
			return null;
		}

		Node nameNode = queryNode(context, "rif:Name");

		if (nameNode == null) {
			return null;
		}

		String name = nameNode.getTextContent().trim();
		java.util.List<Term> terms = extractTerms(context);
		return new Argument(name, terms.iterator().next());
	}

	public java.util.List<Term> extractTerms(Node context) {
		java.util.List<Term> terms = new ArrayList<Term>();

		if (context == null) {
			return terms;
		}

		// Extract each Term type from this context preserving the order of the
		// terms.

		Node node = (Node) context;
		NodeList childNodes = node.getChildNodes();

		Map<Object, Integer> positions = new HashMap<Object, Integer>();

		Term[] orderedTerms = new Term[childNodes.getLength()];

		java.util.List<Variable> variables = extractVariables(context,
				positions);
		insertOrdered(orderedTerms, variables, positions);

		java.util.List<List> lists = extractLists(context, positions);
		insertOrdered(orderedTerms, lists, positions);

		java.util.List<Expression> expressions = extractExpressions(context,
				positions);
		insertOrdered(orderedTerms, expressions, positions);

		java.util.List<ExternalExpression> externalTerms = extractExternalTerms(
				context, positions);
		insertOrdered(orderedTerms, externalTerms, positions);

		java.util.List<Constant> constantTerms = extractConstants(context,
				positions);
		insertOrdered(orderedTerms, constantTerms, positions);

		for (Term orderedTerm : orderedTerms) {
			if (orderedTerm != null) {
				terms.add(orderedTerm);
			}
		}

		return terms;
	}

	private void insertOrdered(Object[] array, java.util.List<?> objects,
			Map<Object, Integer> positions) {
		for (Object object : objects) {
			Integer position = positions.get(object);

			if (position != null && position >= 0) {
				array[position] = object;
			}
		}
	}

	public Metadata extractMetadata(Node context) {
		Node idNode = queryNode(context, "rif:id");

		Constant id = null;
		if (idNode != null) {
			id = extractConstant(idNode);
		}

		java.util.List<Frame> allFrames = new ArrayList<Frame>();

		Node metaNode = queryNode(context, "rif:meta");

		if (metaNode != null) {
			NodeList formulaNodes = queryNodeList(metaNode,
					"rif:And/rif:formula");
			for (int i = 0; i < formulaNodes.getLength(); i++) {
				Node formulaNode = formulaNodes.item(i);

				java.util.List<Frame> frames = extractFrames(formulaNode);
				allFrames.addAll(frames);
			}

			java.util.List<Frame> frames = extractFrames(metaNode);
			allFrames.addAll(frames);
		}

		return new Metadata(id, allFrames);
	}

	public java.util.List<Frame> extractFrames(Node context) {
		java.util.List<Frame> frames = new ArrayList<Frame>();

		if (context == null) {
			return frames;
		}

		NodeList frameNodes = queryNodeList(context, "rif:Frame");

		if (frameNodes == null) {
			return frames;
		}

		for (int i = 0; i < frameNodes.getLength(); i++) {
			Node frameNode = frameNodes.item(i);

			Node objectNode = queryNode(frameNode, "rif:object");
			java.util.List<Term> objectTerms = extractTerms(objectNode);
			Term object = objectTerms.iterator().next();

			java.util.List<Attribute> attributes = new ArrayList<Attribute>();

			NodeList slotNodes = queryNodeList(frameNode, "rif:slot");
			if (slotNodes != null) {
				for (int j = 0; j < slotNodes.getLength(); j++) {
					Node slotNode = slotNodes.item(j);
					java.util.List<Term> slotTerms = extractTerms(slotNode);

					if (slotTerms.size() == 2) {
						Iterator<Term> iterator = slotTerms.iterator();
						Term name = iterator.next();
						Term value = iterator.next();

						Attribute attribute = new Attribute(name, value);
						attributes.add(attribute);
					}
				}
			}

			Frame frame = new Frame(object, attributes);
			frames.add(frame);
		}

		return frames;
	}

	public java.util.List<Import> extractImports(NodeList context) {
		java.util.List<Import> imports = new ArrayList<Import>();

		if (context == null) {
			return imports;
		}

		NodeList directiveList = (NodeList) context;
		for (int i = 0; i < directiveList.getLength(); i++) {
			Node directiveNode = directiveList.item(i);
			Import importt = extractImport(directiveNode);
			if (importt != null) {
				imports.add(importt);
			}
		}

		return imports;
	}

	public Import extractImport(Node context) {
		Node importNode = queryNode(context, "rif:Import");

		Node locationNode = queryNode(importNode, "rif:location");
		Constant location = extractConstant(locationNode);

		Node profileNode = queryNode(importNode, "rif:profile");
		Constant profile = extractConstant(profileNode);

		Import importt = new Import(location, profile);
		setMetadata(importNode, importt);

		return importt;
	}

	public java.util.List<Constant> extractConstants(Node context) {
		return extractConstants(context, null);
	}

	public java.util.List<Constant> extractConstants(Node context,
			Map<Object, Integer> positions) {
		java.util.List<Constant> constants = new ArrayList<Constant>();

		if (context == null) {
			return constants;
		}

		NodeList constantNodes = queryNodeList(context, "rif:Const");
		for (int i = 0; i < constantNodes.getLength(); i++) {
			Node constNode = constantNodes.item(i);

			String language = null;
			Node languageNode = constNode.getAttributes().getNamedItem("lang");
			if (languageNode != null) {
				language = languageNode.getTextContent().trim();
			}

			String type = null;
			Node typeNode = constNode.getAttributes().getNamedItem("type");
			if (typeNode != null) {
				type = typeNode.getTextContent().trim();
			}

			String text = queryString(constNode, "normalize-space(text())");

			Constant constant = new Constant(type, language, text);

			setMetadata(constNode, constant);
			setPosition(constNode, constant, positions);

			constants.add(constant);
		}

		return constants;
	}

	public Constant extractConstant(Node context) {
		if (context == null) {
			return null;
		}

		java.util.List<Constant> constants = extractConstants(context);
		if (constants.size() > 0) {
			return constants.iterator().next();
		}

		return null;
	}
}
