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
package at.sti2.rif4j.serializer.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import at.sti2.rif4j.Describable;
import at.sti2.rif4j.Namespaces;
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

/**
 * @author Daniel Winkler
 * @author Iker Larizgoitia Abad
 */
public class XmlSerializer extends XmlHandlerBase implements RifSerializer,
		DocumentVisitor, TermVisitor, ClauseVisitor, FormulaVisitor,
		AtomicFormulaVisitor, RuleVisitor, UnitermVisitor {

	private org.w3c.dom.Document xmlDocument;

	private final Stack<Element> elementStack;

	public XmlSerializer() {
		super();
		elementStack = new Stack<Element>();
	}

	public XmlSerializer(boolean useValidation) {
		super(useValidation);
		elementStack = new Stack<Element>();
	}

	@Override
	public String serialize(Document rifDocument) {
		String serializedDocument = null;
		try {
			xmlDocument = this.getXmlDocument(rifDocument);
			serializedDocument = serializeXmlDocument(xmlDocument);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serializedDocument;
	}

	private org.w3c.dom.Document getXmlDocument(Document rifDocument)
			throws ParserConfigurationException, SAXException, IOException {
		xmlDocument = this.getNewDocument();
		rifDocument.accept(this);

		assert elementStack.size() == 1 : "There must be exactly one element on stack representing the root node";
		Element root = elementStack.pop();
		// TODO assert that xmlns is not used with other NS
		root.setAttribute("xmlns", Namespaces.RIF_NAMESPACE);

		xmlDocument.appendChild(root);

		if (useValidation) {
			validate(xmlDocument, BLD_RULE_XSD);
		}

		return xmlDocument;
	}

	private String serializeXmlDocument(org.w3c.dom.Document document) {
		Writer result = new StringWriter();
		try {

			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");

			/*
			 * Set standalone="no" since we can not be sure that the XML file is
			 * standalone unless we parse through it and look for outer
			 * references.
			 * 
			 * If the property is not set it is set to standalone="no" by
			 * default anyway (so this property is not omitted if not set).
			 */
			xformer.setOutputProperty(OutputKeys.STANDALONE, "no");

			xformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			xformer.transform(new DOMSource(document), new StreamResult(result));
			result.flush();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Error when serializing the XML DOM", e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new RuntimeException("Error when serializing the XML DOM", e);
		} catch (TransformerException e) {
			throw new RuntimeException("Error when serializing the XML DOM", e);
		} catch (IOException e) {
			throw new RuntimeException("Error when serializing the XML DOM", e);
		}

		return result.toString();
	}

	@Override
	public void visit(Document rifDocument) {
		/*
		 * <xs:element name="Document"> <!-- Document ::= IRIMETA? 'Document'
		 * '(' Base? Prefix* Import* Group? ')' --> <xs:complexType>
		 * <xs:sequence> <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		 * <xs:element ref="directive" minOccurs="0" maxOccurs="unbounded"/>
		 * <xs:element ref="payload" minOccurs="0" maxOccurs="1"/>
		 * </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="directive"> <!-- Base and Prefix represented
		 * directly in XML --> <xs:complexType> <xs:sequence> <xs:element
		 * ref="Import"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="payload"> <xs:complexType> <xs:sequence>
		 * <xs:element ref="Group"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 */

		// create root node
		Element root = xmlDocument.createElementNS(Namespaces.RIF_NAMESPACE,
				"Document");
		elementStack.push(root);

		// handle iri and meta data
		visitDescribable(rifDocument);

		// handle directive, i.e. imports
		for (Import rifImport : rifDocument.getImports()) {
			appendAndPush("directive");
			rifImport.accept(this);
			elementStack.pop();
		}

		// TODO prefixes are not allowed in xml
		for (Prefix prefix : rifDocument.getPrefixes())
			prefix.accept(this);

		if (rifDocument.getGroup() != null) {
			appendAndPush("payload");
			rifDocument.getGroup().accept(this);
			elementStack.pop();
		}

		// final assertions
		assert elementStack.size() == 1;
		assert elementStack.peek().equals(root);
	}

	private void visitDescribable(Describable describable) {
		/*
		 * <xs:group name="IRIMETA"> <!-- IRIMETA ::= '(*' IRICONST? (Frame |
		 * 'And' '(' Frame* ')')? '*)' --> <xs:sequence> <xs:element ref="id"
		 * minOccurs="0" maxOccurs="1"/> <xs:element ref="meta" minOccurs="0"
		 * maxOccurs="1"/> </xs:sequence> </xs:group>
		 * 
		 * <xs:element name="id"> <xs:complexType> <xs:sequence> <xs:element
		 * name="Const" type="IRICONST.type"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 * 
		 * <xs:element name="meta"> <xs:complexType> <xs:choice> <xs:element
		 * ref="Frame"/> <xs:element name="And" type="And-meta.type"/>
		 * </xs:choice> </xs:complexType> </xs:element>
		 * 
		 * <xs:complexType name="And-meta.type"> <!-- sensitive to meta (Frame)
		 * context--> <xs:sequence> <xs:element name="formula"
		 * type="formula-meta.type" minOccurs="0" maxOccurs="unbounded"/>
		 * </xs:sequence> </xs:complexType>
		 * 
		 * <xs:complexType name="formula-meta.type"> <!-- sensitive to meta
		 * (Frame) context--> <xs:sequence> <xs:element ref="Frame"/>
		 * </xs:sequence> </xs:complexType>
		 * 
		 * <xs:complexType name="IRICONST.type" mixed="true"> <!-- sensitive to
		 * location/id context--> <xs:sequence/> <xs:attribute name="type"
		 * type="xs:anyURI" use="required"
		 * fixed="http://www.w3.org/2007/rif#iri"/> </xs:complexType>
		 */

		Constant id;
		if ((id = describable.getId()) != null) {
			appendAndPush("id");
			id.accept(this);
			elementStack.pop();
		}

		java.util.List<Frame> metadataList = describable.getMetadata();
		if (!metadataList.isEmpty()) {
			appendAndPush("meta");
			if (metadataList.size() == 1) {
				metadataList.get(0).accept((AtomicFormulaVisitor) this);
			} else { // (metadataList.size() > 1)
				appendAndPush("And");
				for (Frame metaData : metadataList) {
					metaData.accept((AtomicFormulaVisitor) this);
				}
				elementStack.pop();
			}
			elementStack.pop();
		}
	}

	@Override
	public void visit(Group group) {
		/*
		 * <xs:element name="Group"> <!-- Group ::= IRIMETA? 'Group' '(' (RULE |
		 * Group)* ')' --> <xs:complexType> <xs:sequence> <xs:group
		 * ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element
		 * ref="sentence" minOccurs="0" maxOccurs="unbounded"/> </xs:sequence>
		 * </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="sentence"> <xs:complexType> <xs:choice> <xs:group
		 * ref="RULE"/> <xs:element ref="Group"/> </xs:choice> </xs:complexType>
		 * </xs:element>
		 */

		appendAndPush("Group");

		visitDescribable(group);

		for (Rule rule : group.getRules()) {
			appendAndPush("sentence");
			rule.accept(this);
			elementStack.pop();
		}

		for (Group subGroup : group.getGroups()) {
			appendAndPush("sentence");
			subGroup.accept(this);
			elementStack.pop();
		}

		elementStack.pop();
	}

	@Override
	public void visit(Import imprt) {
		/*
		 * <xs:element name="Import"> <!-- Import ::= IRIMETA? 'Import' '('
		 * LOCATOR PROFILE? ')' LOCATOR ::= ANGLEBRACKIRI PROFILE ::=
		 * ANGLEBRACKIRI --> <xs:complexType> <xs:sequence> <xs:group
		 * ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element
		 * ref="location"/> <xs:element ref="profile" minOccurs="0"
		 * maxOccurs="1"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="location" type="xs:anyURI"/>
		 * 
		 * <xs:element name="profile" type="xs:anyURI"/>
		 */

		appendAndPush("Import");

		visitDescribable(imprt);

		appendAndPush("location");
		elementStack.peek().setTextContent(imprt.getLocation());
		// imprt.getLocation().accept(this);
		elementStack.pop();

		appendAndPush("profile");
		// imprt.getProfile().accept(this);
		elementStack.peek().setTextContent(imprt.getProfile());
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(Prefix prefix) {
		throw new RuntimeException("Prefixes not allowed in XML serialization");
	}

	@Override
	public void visit(Constant constant) {
		/*
		 * <xs:element name="Const"> <!-- Const ::= '"' UNICODESTRING '"^^'
		 * SYMSPACE | CONSTSHORT --> <xs:complexType mixed="true"> <xs:sequence>
		 * <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/> </xs:sequence>
		 * <xs:attribute name="type" type="xs:anyURI" use="required"/>
		 * <xs:attribute ref="xml:lang"/> </xs:complexType> </xs:element>
		 */

		appendAndPush("Const");

		visitDescribable(constant);

		elementStack.peek().setAttribute("type", constant.getType());
		String language = null;
		if (!(language = constant.getLanguage()).isEmpty())
			elementStack.peek().setAttribute("xml:lang", language);
		elementStack.peek().setTextContent(constant.getText());

		elementStack.pop();
	}

	@Override
	public void visit(Expression expression) {
		/*
		 * <xs:element name="Expr"> <!-- Expr ::= UNITERM --> <xs:complexType>
		 * <xs:sequence> <xs:group ref="UNITERM"/> </xs:sequence>
		 * </xs:complexType> </xs:element>
		 */

		appendAndPush("Expr");

		visitUniterm(expression);

		elementStack.pop();
	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		/*
		 * <xs:complexType name="External-FORMULA.type"> <!-- sensitive to
		 * FORMULA (Atom) context--> <xs:sequence> <xs:group ref="IRIMETA"
		 * minOccurs="0" maxOccurs="1"/> <xs:element name="content"
		 * type="content-FORMULA.type"/> </xs:sequence> </xs:complexType>
		 * 
		 * <xs:complexType name="content-FORMULA.type"> <!-- sensitive to
		 * FORMULA (Atom) context--> <xs:sequence> <xs:element ref="Atom"/>
		 * </xs:sequence> </xs:complexType>
		 */

		appendAndPush("External");

		visitDescribable(externalExpression);

		appendAndPush("content");
		externalExpression.getExpression().accept(this);
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(List list) {
		/*
		 * <xs:element name="List"> <!-- List ::= 'List' '(' TERM* ')' | 'List'
		 * '(' TERM+ '|' TERM ')' rewritten as List ::= 'List' '(' LISTELEMENTS?
		 * ')' --> <xs:complexType> <xs:sequence> <xs:group ref="IRIMETA"
		 * minOccurs="0" maxOccurs="1"/> <xs:group ref="LISTELEMENTS"
		 * minOccurs="0" maxOccurs="1"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 * 
		 * <xs:group name="LISTELEMENTS"> <!-- LISTELEMENTS ::= TERM+ ('|'
		 * TERM)? --> <xs:sequence> <xs:element ref="items"/> <xs:element
		 * ref="rest" minOccurs="0" maxOccurs="1"/> </xs:sequence> </xs:group>
		 * 
		 * <xs:element name="items"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM" minOccurs="1" maxOccurs="unbounded"/> </xs:sequence>
		 * <xs:attribute name="ordered" type="xs:string" fixed="yes"/>
		 * </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="rest"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM"/> </xs:sequence> </xs:complexType> </xs:element>
		 */

		appendAndPush("List");

		visitDescribable(list);

		// This attribute is optional and fixed so no need to add it and better
		// for testing purposes
		// elementStack.peek().setAttribute("ordered", "yes");

		if (list.getElements().size() > 0) {
			appendAndPush("items");
			for (Term item : list.getElements())
				item.accept(this);
			elementStack.pop();
		}

		if (list.getRestElements().size() > 0) {
			appendAndPush("rest");
			for (Term item : list.getRestElements())
				item.accept(this);
			elementStack.pop();
		}

		elementStack.pop();
	}

	@Override
	public void visit(Variable variable) {
		/*
		 * <xs:element name="Var"> <!-- Var ::= '?' Name --> <xs:complexType
		 * mixed="true"> <xs:sequence> <xs:group ref="IRIMETA" minOccurs="0"
		 * maxOccurs="1"/> </xs:sequence> </xs:complexType> </xs:element>
		 */

		appendAndPush("Var");

		visitDescribable(variable);

		elementStack.peek().setTextContent(variable.getName());

		elementStack.pop();
	}

	@Override
	public void visit(ImpliesFormula implies) {
		/*
		 * <xs:element name="Implies"> <!-- Implies ::= IRIMETA? (ATOMIC | 'And'
		 * '(' ATOMIC* ')') ':-' FORMULA --> <xs:complexType> <xs:sequence>
		 * <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element
		 * ref="if"/> <xs:element ref="then"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 * 
		 * <xs:element name="if"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="FORMULA"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="then"> <xs:complexType> <xs:choice> <xs:group
		 * ref="ATOMIC"/> <xs:element name="And" type="And-then.type"/>
		 * </xs:choice> </xs:complexType> </xs:element>
		 * 
		 * <xs:complexType name="And-then.type"> <!-- sensitive to then (ATOMIC)
		 * context--> <xs:sequence> <xs:element name="formula"
		 * type="formula-then.type" minOccurs="0" maxOccurs="unbounded"/>
		 * </xs:sequence> </xs:complexType>
		 * 
		 * <xs:complexType name="formula-then.type"> <!-- sensitive to then
		 * (ATOMIC) context--> <xs:sequence> <xs:group ref="ATOMIC"/>
		 * </xs:sequence> </xs:complexType>
		 */

		appendAndPush("Implies");

		visitDescribable(implies);

		appendAndPush("if");
		implies.getBody().accept(this);
		elementStack.pop();

		java.util.List<AtomicFormula> headFormulas = implies.getHead();

		appendAndPush("then");
		if (headFormulas.size() == 1) {
			headFormulas.get(0).accept((AtomicFormulaVisitor) this);
		} else if (headFormulas.size() > 1) {
			appendAndPush("And");
			for (AtomicFormula headFormula : headFormulas) {
				appendAndPush("formula");
				headFormula.accept((AtomicFormulaVisitor) this);
				elementStack.pop();
			}
			elementStack.pop();
		}
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		/*
		 * <xs:group name="ATOMIC"> <!-- ATOMIC ::= IRIMETA? (Atom | Equal |
		 * Member | Subclass | Frame) --> <xs:choice> <xs:element ref="Atom"/>
		 * <xs:element ref="Equal"/> <xs:element ref="Member"/> <xs:element
		 * ref="Subclass"/> <xs:element ref="Frame"/> </xs:choice> </xs:group>
		 */
		atomicFormula.accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		/*
		 * <xs:element name="Exists"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element ref="declare"
		 * minOccurs="1" maxOccurs="unbounded"/> <xs:element ref="formula"/>
		 * </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="formula"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="FORMULA"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="declare"> <xs:complexType> <xs:sequence>
		 * <xs:element ref="Var"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 */

		appendAndPush("Exists");

		visitDescribable(existsFormula);

		appendAndPush("declare");
		elementStack.pop();

		appendAndPush("formula");
		existsFormula.getFormula().accept(this);
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		/*
		 * <xs:complexType name="External-FORMULA.type"> <!-- sensitive to
		 * FORMULA (Atom) context--> <xs:sequence> <xs:group ref="IRIMETA"
		 * minOccurs="0" maxOccurs="1"/> <xs:element name="content"
		 * type="content-FORMULA.type"/> </xs:sequence> </xs:complexType>
		 */

		appendAndPush("External");

		visitDescribable(externalFormula);

		appendAndPush("content");
		externalFormula.getAtom().accept((AtomicFormulaVisitor) this);
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(ForallFormula forallFormula) {
		/*
		 * <xs:group name="RULE"> <!-- RULE ::= (IRIMETA? 'Forall' Var+ '('
		 * CLAUSE ')') | CLAUSE --> <xs:choice> <xs:element ref="Forall"/>
		 * <xs:group ref="CLAUSE"/> </xs:choice> </xs:group>
		 * 
		 * <xs:element name="Forall"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element ref="declare"
		 * minOccurs="1" maxOccurs="unbounded"/> <!-- different from formula in
		 * And, Or and Exists --> <xs:element name="formula"> <xs:complexType>
		 * <xs:group ref="CLAUSE"/> </xs:complexType> </xs:element>
		 * </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:group name="CLAUSE"> <!-- CLAUSE ::= Implies | ATOMIC -->
		 * <xs:choice> <xs:element ref="Implies"/> <xs:group ref="ATOMIC"/>
		 * </xs:choice> </xs:group>
		 * 
		 * <xs:element name="declare"> <xs:complexType> <xs:sequence>
		 * <xs:element ref="Var"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 */

		java.util.List<Variable> variables = forallFormula.getVariables();
		if (variables.size() > 0) { // Forall

			appendAndPush("Forall");
			visitDescribable(forallFormula);

			for (Variable variable : variables) {
				appendAndPush("declare");
				variable.accept(this);
				elementStack.pop();
			}

			appendAndPush("formula");
			forallFormula.getClause().accept((ClauseVisitor) this);
			elementStack.pop();

			elementStack.pop();
		} else { // Clause
			forallFormula.getClause().accept((ClauseVisitor) this);
		}
	}

	@Override
	public void visit(Atom atom) {
		/*
		 * <xs:element name="Atom"> <!-- Atom ::= UNITERM --> <xs:complexType>
		 * <xs:sequence> <xs:group ref="UNITERM"/> </xs:sequence>
		 * </xs:complexType> </xs:element>
		 */

		appendAndPush("Atom");

		visitUniterm(atom);

		elementStack.pop();
	}

	private void visitUniterm(Uniterm atom) {
		/*
		 * <xs:group name="UNITERM"> <!-- UNITERM ::= Const '(' (TERM* | (Name
		 * '->' TERM)*) ')' --> <xs:sequence> <xs:group ref="IRIMETA"
		 * minOccurs="0" maxOccurs="1"/> <xs:element ref="op"/> <xs:choice>
		 * <xs:element ref="args" minOccurs="0" maxOccurs="1"/> <xs:element
		 * name="slot" type="slot-UNITERM.type" minOccurs="0"
		 * maxOccurs="unbounded"/> </xs:choice> </xs:sequence> </xs:group>
		 * 
		 * <xs:element name="op"> <xs:complexType> <xs:sequence> <xs:element
		 * ref="Const"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="args"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM" minOccurs="1" maxOccurs="unbounded"/> </xs:sequence>
		 * <xs:attribute name="ordered" type="xs:string" fixed="yes"/>
		 * </xs:complexType> </xs:element>
		 * 
		 * <xs:complexType name="slot-UNITERM.type"> <!-- sensitive to UNITERM
		 * (Name) context--> <xs:sequence> <xs:element ref="Name"/> <xs:group
		 * ref="TERM"/> </xs:sequence> <xs:attribute name="ordered"
		 * type="xs:string" fixed="yes"/> </xs:complexType>
		 */

		visitDescribable(atom);

		appendAndPush("op");
		atom.getOperator().accept(this);
		elementStack.pop();

		java.util.List<Argument> arguments = atom.getArguments();
		if (!arguments.isEmpty()) {

			if (arguments.get(0).getName() == null) { // arguments
				appendAndPush("args");
				// elementStack.peek().setAttribute("ordered", "yes");

				for (Argument argument : arguments) {
					argument.getValue().accept(this);
				}

				elementStack.pop();
			} else { // slots
				for (Argument argument : arguments) {
					appendAndPush("slot");

					appendAndPush("Name");
					elementStack.peek().setTextContent(argument.getName());
					elementStack.pop();

					argument.getValue().accept(this);
					elementStack.pop();
				}
			}
		}
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		/*
		 * <xs:element name="Equal"> <!-- Equal ::= TERM '=' TERM -->
		 * <xs:complexType> <xs:sequence> <xs:group ref="IRIMETA" minOccurs="0"
		 * maxOccurs="1"/> <xs:element ref="left"/> <xs:element ref="right"/>
		 * </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="left"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="right"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM"/> </xs:sequence> </xs:complexType> </xs:element>
		 */

		appendAndPush("Equal");

		visitDescribable(equalAtom);

		appendAndPush("left");
		equalAtom.getTerms().get(0).accept(this);
		elementStack.pop();

		appendAndPush("right");
		equalAtom.getTerms().get(1).accept(this);
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(Frame frame) {
		/*
		 * <xs:element name="Frame"> <!-- Frame ::= TERM '[' (TERM '->' TERM)*
		 * ']' --> <xs:complexType> <xs:sequence> <xs:group ref="IRIMETA"
		 * minOccurs="0" maxOccurs="1"/> <xs:element ref="object"/> <xs:element
		 * name="slot" type="slot-Frame.type" minOccurs="0"
		 * maxOccurs="unbounded"/> </xs:sequence> </xs:complexType>
		 * </xs:element>
		 * 
		 * <xs:element name="object"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:complexType name="slot-Frame.type"> <!-- sensitive to Frame
		 * (TERM) context--> <xs:sequence> <xs:group ref="TERM"/> <xs:group
		 * ref="TERM"/> </xs:sequence> <xs:attribute name="ordered"
		 * type="xs:string" fixed="yes"/> </xs:complexType>
		 */

		appendAndPush("Frame");

		visitDescribable(frame);

		appendAndPush("object");
		frame.getObject().accept(this);
		elementStack.pop();

		for (Attribute attribute : frame.getAttributes()) {
			appendAndPush("slot");
			// elementStack.peek().setAttribute("ordered", "yes");

			attribute.getName().accept(this);
			attribute.getValue().accept(this);

			elementStack.pop();
		}

		elementStack.pop();
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		/*
		 * <xs:element name="Member"> <!-- Member ::= TERM '#' TERM -->
		 * <xs:complexType> <xs:sequence> <xs:group ref="IRIMETA" minOccurs="0"
		 * maxOccurs="1"/> <xs:element ref="instance"/> <xs:element
		 * ref="class"/> </xs:sequence> </xs:complexType> </xs:element>
		 */

		appendAndPush("Member");

		visitDescribable(memberAtom);

		appendAndPush("instance");
		memberAtom.getInstance().accept(this);
		elementStack.pop();

		appendAndPush("class");
		memberAtom.getClassOfInstance().accept(this);
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		/*
		 * <xs:element name="Subclass"> <!-- Subclass ::= TERM '##' TERM -->
		 * <xs:complexType> <xs:sequence> <xs:group ref="IRIMETA" minOccurs="0"
		 * maxOccurs="1"/> <xs:element ref="sub"/> <xs:element ref="super"/>
		 * </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="sub"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM"/> </xs:sequence> </xs:complexType> </xs:element>
		 * 
		 * <xs:element name="super"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="TERM"/> </xs:sequence> </xs:complexType> </xs:element>
		 */

		appendAndPush("Subclass");

		visitDescribable(subclassAtom);

		appendAndPush("sub");
		subclassAtom.getSubClass().accept(this);
		elementStack.pop();

		appendAndPush("super");
		subclassAtom.getSuperClass().accept(this);
		elementStack.pop();

		elementStack.pop();
	}

	@Override
	public void visit(AndFormula andFormula) {
		/*
		 * <xs:element name="And"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element ref="formula"
		 * minOccurs="0" maxOccurs="unbounded"/> </xs:sequence>
		 * </xs:complexType> </xs:element>
		 */

		appendAndPush("And");

		visitDescribable(andFormula);
		java.util.List<Formula> formulas = andFormula.getFormulas();
		visitFormulas(formulas);

		elementStack.pop();
	}

	private void visitFormulas(java.util.List<Formula> formulas) {
		for (Formula formula : formulas) {
			appendAndPush("formula");
			formula.accept(this);
			elementStack.pop();
		}
	}

	@Override
	public void visit(OrFormula orFormula) {
		/*
		 * <xs:element name="Or"> <xs:complexType> <xs:sequence> <xs:group
		 * ref="IRIMETA" minOccurs="0" maxOccurs="1"/> <xs:element ref="formula"
		 * minOccurs="0" maxOccurs="unbounded"/> </xs:sequence>
		 * </xs:complexType> </xs:element>
		 */

		appendAndPush("Or");

		visitDescribable(orFormula);
		java.util.List<Formula> formulas = orFormula.getFormulas();
		visitFormulas(formulas);

		elementStack.pop();
	}

	@Override
	public void visit(Clause clause) {
		/*
		 * <xs:group name="CLAUSE"> <!-- CLAUSE ::= Implies | ATOMIC -->
		 * <xs:choice> <xs:element ref="Implies"/> <xs:group ref="ATOMIC"/>
		 * </xs:choice> </xs:group>
		 */

		clause.accept((ClauseVisitor) this);
	}

	private void appendAndPush(Element element) {
		elementStack.peek().appendChild(element);
		elementStack.push(element);
	}

	private void appendAndPush(String elementName) {
		appendAndPush(xmlDocument.createElementNS(Namespaces.RIF_NAMESPACE,
				elementName));
	}

}
