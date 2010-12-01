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
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
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
import at.sti2.rif4j.rule.ImpliesFormula;
import at.sti2.rif4j.rule.Import;
import at.sti2.rif4j.rule.Rule;

/**
 * Extracts and translates RIF XML elements to a corresponding object model for
 * RIF.
 * 
 * @author Adrian Marte
 * @author Iker Larizgoitia Abad
 */
// FIXME This class has huge optimization potential.
class XmlExtractor {

	public static final String RIF_VAR = "rif:Var";

	public static final String RIF_ATOM = "rif:Atom";

	public static final String RIF_SUB = "rif:sub";

	public static final String RIF_SUPER = "rif:super";

	public static final String RIF_SUBCLASS = "rif:Subclass";

	public static final String RIF_CLASS = "rif:class";

	public static final String RIF_INSTANCE = "rif:instance";

	public static final String RIF_MEMBER = "rif:Member";

	public static final String RIF_RIGHT = "rif:right";

	public static final String RIF_LEFT = "rif:left";

	public static final String RIF_EQUAL = "rif:Equal";

	public static final String RIF_THEN = "rif:then";

	public static final String RIF_IF = "rif:if";

	public static final String RIF_IMPLIES = "rif:Implies";

	public static final String RIF_FORALL = "rif:Forall";

	public static final String RIF_SENTENCE = "rif:sentence";

	public static final String RIF_GROUP = "rif:Group";

	public static final String RIF_DIRECTIVE = "rif:directive";

	public static final String RIF_PAYLOAD = "rif:payload";

	public static final String RIF_DOCUMENT = "rif:Document";

	public static final String RIF_CONST = "rif:Const";

	public static final String RIF_PROFILE = "rif:profile";

	public static final String RIF_LOCATION = "rif:location";

	public static final String RIF_IMPORT = "rif:Import";

	public static final String RIF_OBJECT = "rif:object";

	public static final String RIF_FRAME = "rif:Frame";

	public static final String RIF_META = "rif:meta";

	public static final String RIF_ID = "rif:id";

	public static final String RIF_NAME = "rif:Name";

	public static final String RIF_OP = "rif:op";

	public static final String RIF_SLOT = "rif:slot";

	public static final String RIF_ARGS = "rif:args";

	public static final String RIF_EXPR = "rif:Expr";

	public static final String RIF_REST = "rif:rest";

	public static final String RIF_LIST = "rif:List";

	public static final String RIF_CONTENT = "rif:content";

	public static final String RIF_EXTERNAL = "rif:External";

	public static final String RIF_DECLARE = "rif:declare";

	public static final String RIF_EXISTS = "rif:Exists";

	public static final String RIF_OR = "rif:Or";

	public static final String RIF_FORMULA = "rif:formula";

	public static final String RIF_AND = "rif:And";

	public static final String RIF_ITEMS = "rif:items";

	private static XPathFactory factory;

	private static XPath xPath;

	private static NamespaceContext namespaceContext;

	static {
		factory = XPathFactory.newInstance();
		xPath = factory.newXPath();
		namespaceContext = new RifNamespaceContext();
		xPath.setNamespaceContext(namespaceContext);
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
			int index = query.indexOf(":");

			String namespace = null;
			String localName = query;

			if (index >= 0) {
				localName = query.substring(index + 1);

				String prefix = query.substring(0, index);
				namespace = namespaceContext.getNamespaceURI(prefix);
			}

			NodeFilter filter = new NodeFilter(context, localName, namespace);
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
		Node documentNode = queryNode(context, RIF_DOCUMENT);

		DocumentType doctype = documentNode.getOwnerDocument().getDoctype();
		NamedNodeMap entities = doctype.getEntities();

		for (int i = 0; i < entities.getLength(); i++) {
			// TODO Extract the prefixes.
		}

		Node payloadNode = queryNode(documentNode, RIF_PAYLOAD);
		NodeList directiveNodes = queryNodeList(documentNode, RIF_DIRECTIVE);

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

		Node groupNode = queryNode(context, RIF_GROUP);

		if (groupNode == null) {
			return null;
		}

		java.util.List<Rule> rules = new ArrayList<Rule>();
		java.util.List<Group> groups = new ArrayList<Group>();

		NodeList sentenceNodes = queryNodeList(groupNode, RIF_SENTENCE);

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

		NodeList forallNodes = queryNodeList(context, RIF_FORALL);

		if (forallNodes == null) {
			return forallFormulas;
		}

		for (int i = 0; i < forallNodes.getLength(); i++) {
			Node forallNode = forallNodes.item(i);

			Node formulaNode = queryNode(forallNode, RIF_FORMULA);
			Clause clause = extractClause(formulaNode);

			java.util.List<Variable> variables = new ArrayList<Variable>();

			NodeList declareNodes = queryNodeList(forallNode, RIF_DECLARE);
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

		ImpliesFormula implies = extractImplies(context);
		if (implies != null) {
			return implies;
		}

		java.util.List<AtomicFormula> atomicFormula = extractAtomicFormulas(context);
		if (atomicFormula.size() > 0) {
			return atomicFormula.iterator().next();
		}

		return null;
	}

	public ImpliesFormula extractImplies(Node context) {
		if (context == null) {
			return null;
		}

		Node impliesNode = queryNode(context, RIF_IMPLIES);

		if (impliesNode == null) {
			return null;
		}

		Node ifNode = queryNode(impliesNode, RIF_IF);
		java.util.List<Formula> ifFormulas = extractFormulas(ifNode);
		Formula ifFormula = ifFormulas.iterator().next();

		java.util.List<AtomicFormula> thenFormulas = new ArrayList<AtomicFormula>();

		Node thenNode = queryNode(impliesNode, RIF_THEN);

		NodeList formulaNodes = queryNodeList(impliesNode, RIF_THEN + "/"
				+ RIF_AND + "/" + RIF_FORMULA);
		for (int i = 0; i < formulaNodes.getLength(); i++) {
			Node formulaNode = formulaNodes.item(i);
			thenFormulas.addAll(extractAtomicFormulas(formulaNode));
		}

		thenFormulas.addAll(extractAtomicFormulas(thenNode));

		ImpliesFormula implies = new ImpliesFormula(ifFormula, thenFormulas);

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

		NodeList equalNodes = queryNodeList(context, RIF_EQUAL);

		if (equalNodes == null) {
			return equalAtoms;
		}

		for (int i = 0; i < equalNodes.getLength(); i++) {
			Node equalNode = equalNodes.item(i);

			Node leftNode = queryNode(equalNode, RIF_LEFT);
			java.util.List<Term> leftTerms = extractTerms(leftNode);

			Node rightNode = queryNode(equalNode, RIF_RIGHT);
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

		NodeList memberNodes = queryNodeList(context, RIF_MEMBER);

		if (memberNodes == null) {
			return memberAtoms;
		}

		for (int i = 0; i < memberNodes.getLength(); i++) {
			Node memberNode = memberNodes.item(i);

			Node instanceNode = queryNode(memberNode, RIF_INSTANCE);
			java.util.List<Term> instanceTerms = extractTerms(instanceNode);
			Term instanceTerm = instanceTerms.get(0);

			Node classNode = queryNode(memberNode, RIF_CLASS);
			java.util.List<Term> classTerms = extractTerms(classNode);
			Term classTerm = classTerms.get(0);

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

		NodeList subclassNodes = queryNodeList(context, RIF_SUBCLASS);

		if (subclassNodes == null) {
			return subclassAtoms;
		}

		for (int i = 0; i < subclassNodes.getLength(); i++) {
			Node subclassNode = subclassNodes.item(i);

			Node superNode = queryNode(subclassNode, RIF_SUPER);
			java.util.List<Term> superTerms = extractTerms(superNode);
			Term superTerm = superTerms.iterator().next();

			Node subNode = queryNode(subclassNode, RIF_SUB);
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

		NodeList atomNodes = queryNodeList(context, RIF_ATOM);

		if (atomNodes == null) {
			return atoms;
		}

		for (int i = 0; i < atomNodes.getLength(); i++) {
			Node atomNode = atomNodes.item(i);

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

		NodeList variableNodes = queryNodeList(context, RIF_VAR);
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

		NodeList andNodes = queryNodeList(context, RIF_AND);

		if (andNodes == null) {
			return andFormulas;
		}

		for (int i = 0; i < andNodes.getLength(); i++) {
			Node andNode = andNodes.item(i);

			java.util.List<Formula> formulas = new ArrayList<Formula>();

			NodeList formulaNodes = queryNodeList(andNode, RIF_FORMULA);
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

		NodeList orNodes = queryNodeList(context, RIF_OR);

		if (orNodes == null) {
			return orFormulas;
		}

		for (int i = 0; i < orNodes.getLength(); i++) {
			Node orNode = orNodes.item(i);

			java.util.List<Formula> formulas = new ArrayList<Formula>();

			NodeList formulaNodes = queryNodeList(orNode, RIF_FORMULA);
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

		NodeList existsNodes = queryNodeList(context, RIF_EXISTS);

		if (existsNodes == null) {
			return existsFormulas;
		}

		for (int i = 0; i < existsNodes.getLength(); i++) {
			Node existsNode = existsNodes.item(i);

			java.util.List<Formula> formulas = new ArrayList<Formula>();

			NodeList formulaNodes = queryNodeList(existsNode, RIF_FORMULA);
			for (int j = 0; j < formulaNodes.getLength(); j++) {
				Node formulaNode = formulaNodes.item(j);
				formulas.addAll(extractFormulas(formulaNode));
			}

			java.util.List<Variable> variables = new ArrayList<Variable>();

			NodeList declareNodes = queryNodeList(existsNode, RIF_DECLARE);
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

		NodeList externalNodes = queryNodeList(context, RIF_EXTERNAL);

		if (externalNodes == null) {
			return formulas;
		}

		for (int i = 0; i < externalNodes.getLength(); i++) {
			Node externalNode = externalNodes.item(i);
			Node contentNode = queryNode(externalNode, RIF_CONTENT);
			java.util.List<Atom> atoms = extractAtoms(contentNode);
			Atom atom = atoms.get(0);

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

		NodeList externalNodes = queryNodeList(context, RIF_EXTERNAL);
		for (int i = 0; i < externalNodes.getLength(); i++) {
			Node externalNode = externalNodes.item(i);
			Node contentNode = queryNode(externalNode, RIF_CONTENT);

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

		NodeList listNodes = queryNodeList(context, RIF_LIST);

		if (listNodes == null) {
			return lists;
		}

		for (int i = 0; i < listNodes.getLength(); i++) {
			Node listNode = listNodes.item(i);

			Node itemsNode = queryNode(listNode, RIF_ITEMS);
			java.util.List<Term> listTerms = extractTerms(itemsNode);

			Node restNode = queryNode(listNode, RIF_REST);
			java.util.List<Term> restTerms = extractTerms(restNode);

			List list = new List();
			list.setElements(listTerms);
			list.setRestElements(restTerms);

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

		NodeList exprNodes = queryNodeList(context, RIF_EXPR);

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
		Node argsNode = queryNode(context, RIF_ARGS);
		java.util.List<Term> argsTerms = extractTerms(argsNode);

		java.util.List<Argument> arguments = new ArrayList<Argument>();

		// Convert each term to an unnamed argument.
		for (Term term : argsTerms) {
			Argument argument = new Argument(term);
			arguments.add(argument);
		}

		NodeList slotNodes = queryNodeList(context, RIF_SLOT);
		for (int j = 0; j < slotNodes.getLength(); j++) {
			Node slotNode = slotNodes.item(j);
			Argument argument = extractArgument(slotNode);

			if (argument != null) {
				arguments.add(argument);
			}
		}

		Node opNode = queryNode(context, RIF_OP);
		Constant operator = extractConstant(opNode);

		return new SimpleUniterm(operator, arguments);
	}

	public Argument extractArgument(Node context) {
		if (context == null) {
			return null;
		}

		Node nameNode = queryNode(context, RIF_NAME);

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
		Node idNode = queryNode(context, RIF_ID);

		Constant id = null;
		if (idNode != null) {
			id = extractConstant(idNode);
		}

		java.util.List<Frame> allFrames = new ArrayList<Frame>();

		Node metaNode = queryNode(context, RIF_META);

		if (metaNode != null) {
			NodeList formulaNodes = queryNodeList(metaNode, RIF_AND + "/"
					+ RIF_FORMULA);
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

		NodeList frameNodes = queryNodeList(context, RIF_FRAME);

		if (frameNodes == null) {
			return frames;
		}

		for (int i = 0; i < frameNodes.getLength(); i++) {
			Node frameNode = frameNodes.item(i);

			Node objectNode = queryNode(frameNode, RIF_OBJECT);
			java.util.List<Term> objectTerms = extractTerms(objectNode);
			Term object = objectTerms.iterator().next();

			java.util.List<Attribute> attributes = new ArrayList<Attribute>();

			NodeList slotNodes = queryNodeList(frameNode, RIF_SLOT);
			if (slotNodes != null) {
				for (int j = 0; j < slotNodes.getLength(); j++) {
					Node slotNode = slotNodes.item(j);
					java.util.List<Term> slotTerms = extractTerms(slotNode);

					if (slotTerms.size() == 2) {
						Term name = slotTerms.get(0);
						Term value = slotTerms.get(1);

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
		Node importNode = queryNode(context, RIF_IMPORT);

		Node locationNode = queryNode(importNode, RIF_LOCATION);
		String location = locationNode.getTextContent().trim();
		// Constant location = new Constant(RIF_LOCATION, "", locationText);

		Node profileNode = queryNode(importNode, RIF_PROFILE);
		String profile = profileNode.getTextContent().trim();
		// Constant profile = new Constant(RIF_PROFILE,"", profileText);

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

		NodeList constantNodes = queryNodeList(context, RIF_CONST);
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
