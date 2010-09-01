package at.sti2.rif4j.serializer.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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

import at.sti2.rif4j.Describable;
import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
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
import at.sti2.rif4j.serializer.Serializer;

public class XmlSerializer implements DocumentVisitor, TermVisitor,
ClauseVisitor, FormulaVisitor, AtomicFormulaVisitor,
CompositeFormulaVisitor, RuleVisitor, UnitermVisitor, Serializer {

	private static final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	private final DocumentBuilder builder;
	private org.w3c.dom.Document document;
	private final Stack<Element> elementStack;

	public XmlSerializer() throws ParserConfigurationException {
		elementStack = new Stack<Element>();
		
		builder = builderFactory.newDocumentBuilder();
		this.document = builder.newDocument();
	}
	
	public String getString() {
		assert elementStack.size() == 1 : "There must be exactly one element on stack representing the root node";
		document.appendChild(pop());
		
		Writer result = new StringWriter();
        Transformer xformer;
		try {
			xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.ENCODING, "UTF8");
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			/*
			 * Set standalone="no" since we can not be sure that the XML file is
			 * standalone unless we parse through it and look for outer references.
			 * 
			 * If the property is not set it is set to standalone="no" by
			 * default anyway (so this property is not omitted if not set).
			 */
			xformer.setOutputProperty(OutputKeys.STANDALONE, "no");
			
			xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
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
	public String toString() {
		return getString();
	}
	
	@Override
	public void visit(Document document) {
		/*
		 <xs:element name="Document">
		    <!--
		  Document  ::= IRIMETA? 'Document' '(' Base? Prefix* Import* Group? ')'
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="directive" minOccurs="0" maxOccurs="unbounded"/>
		        <xs:element ref="payload" minOccurs="0" maxOccurs="1"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="directive">
		    <!--
		  Base and Prefix represented directly in XML
		    -->
		  <xs:complexType>
		      <xs:sequence>
		        <xs:element ref="Import"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:element name="payload">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:element ref="Group"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		// create root node
		Element root = this.document.createElement("Document");
		push(root);

		// handle iri and meta data
		visitDescribable(document);
		
		
		// handle directive, i.e. imports
		for(Import rifImport : document.getImports()) {
			appendAndPush("directive");
			rifImport.accept(this);
			pop();
		}
		
		// TODO prefixes are not allowed in xml
		for(Prefix prefix : document.getPrefixes())
			prefix.accept(this);
		
		
		// handle payload
		appendAndPush("payload");
		
		document.getGroup().accept(this);
		pop();
		
		
		// final assertions
		assert elementStack.size() == 1;
		assert peek().equals(root);
	}

	private void visitDescribable(Describable describable) {
		/*
		  <xs:group name="IRIMETA">
		    <!--
		  IRIMETA   ::= '(*' IRICONST? (Frame | 'And' '(' Frame* ')')? '*)'
		    -->
		    <xs:sequence>
		      <xs:element ref="id" minOccurs="0" maxOccurs="1"/>
		      <xs:element ref="meta" minOccurs="0" maxOccurs="1"/>
		    </xs:sequence>
		  </xs:group>
		  
		  <xs:element name="id">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:element name="Const" type="IRICONST.type"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:element name="meta">
		    <xs:complexType>
		     <xs:choice>
		       <xs:element ref="Frame"/>
		       <xs:element name="And" type="And-meta.type"/>
		     </xs:choice>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:complexType name="And-meta.type">
		  <!-- sensitive to meta (Frame) context-->
		    <xs:sequence>
		      <xs:element name="formula" type="formula-meta.type" minOccurs="0" maxOccurs="unbounded"/>
		    </xs:sequence>
		  </xs:complexType>
		 
		  <xs:complexType name="formula-meta.type">
		    <!-- sensitive to meta (Frame) context-->
		    <xs:sequence>
		      <xs:element ref="Frame"/>
		    </xs:sequence>
		  </xs:complexType>
		  
		  <xs:complexType name="IRICONST.type" mixed="true">
		    <!-- sensitive to location/id context-->
		    <xs:sequence/>
		    <xs:attribute name="type" type="xs:anyURI" use="required" fixed="http://www.w3.org/2007/rif#iri"/>
		  </xs:complexType>
		 */
		
		Constant id;
		if ((id = describable.getId()) != null) {
			appendAndPush("id");
			id.accept(this);
			pop();
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
				pop();
			}
			pop();
		}
	}

	@Override
	public void visit(Group group) {
		/*
		  <xs:element name="Group">
		    <!--
		  Group     ::= IRIMETA? 'Group' '(' (RULE | Group)* ')'
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="sentence" minOccurs="0" maxOccurs="unbounded"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>

		  <xs:element name="sentence">
		   <xs:complexType>
		     <xs:choice>
		       <xs:group ref="RULE"/>
		       <xs:element ref="Group"/>
		     </xs:choice>
		   </xs:complexType>
		 </xs:element>
		 */
		
		appendAndPush("Group");
		
		visitDescribable(group);
		
		for (Rule rule : group.getRules()) {
			appendAndPush("sentence");
			rule.accept(this);
			pop();
		}
		
		for (Group subGroup : group.getGroups()) {
			appendAndPush("sentence");
			subGroup.accept(this);
			pop();
		}
		
		pop();
	}

	@Override
	public void visit(Import imprt) {
		/*
		  <xs:element name="Import">
		    <!--
		  Import    ::= IRIMETA? 'Import' '(' LOCATOR PROFILE? ')'
		  LOCATOR   ::= ANGLEBRACKIRI
		  PROFILE   ::= ANGLEBRACKIRI
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/> 
		        <xs:element ref="location"/>
		        <xs:element ref="profile" minOccurs="0" maxOccurs="1"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:element name="location" type="xs:anyURI"/>
		 
		  <xs:element name="profile" type="xs:anyURI"/>
		 */
		
		appendAndPush("Import");
			
			visitDescribable(imprt);
			
			appendAndPush("location");
				imprt.getLocation().accept(this);
			pop();
			
			appendAndPush("profile");
				imprt.getProfile().accept(this);
			pop();
			
		pop();
	}

	@Override
	public void visit(Prefix prefix) {
		throw new RuntimeException("Prefixes not allowed in XML serialization");
	}

	@Override
	public void visit(Constant constant) {
		/*
		  <xs:element name="Const">
		    <!--
		  Const          ::= '"' UNICODESTRING '"^^' SYMSPACE | CONSTSHORT
		    -->
		    <xs:complexType mixed="true">
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		      </xs:sequence>
		      <xs:attribute name="type" type="xs:anyURI" use="required"/> 
		      <xs:attribute ref="xml:lang"/>
		    </xs:complexType>
		  </xs:element>
		 */

		appendAndPush("Const");
		
			visitDescribable(constant);
			
			peek().setAttribute("type", constant.getType());
			String language = null;
			if (!(language = constant.getLanguage()).isEmpty())
				peek().setAttribute("xml:lang", language);
			peek().setTextContent(constant.getText());
		
		
		pop();
	}

	@Override
	public void visit(Expression expression) {
		/*
		  <xs:element name="Expr">
		    <!--
		  Expr           ::= UNITERM
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="UNITERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Expr");
		
			visitUniterm(expression);
		
		pop();
	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		/*
		  <xs:complexType name="External-FORMULA.type">
		    <!-- sensitive to FORMULA (Atom) context-->
		    <xs:sequence>
		      <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		      <xs:element name="content" type="content-FORMULA.type"/>
		    </xs:sequence>
		  </xs:complexType>
		  
		  <xs:complexType name="content-FORMULA.type">
		    <!-- sensitive to FORMULA (Atom) context-->
		    <xs:sequence>
		      <xs:element ref="Atom"/>
		    </xs:sequence>
		  </xs:complexType>
		 */
		
		appendAndPush("External");

			visitDescribable(externalExpression);
			
			appendAndPush("content");
				externalExpression.getExpression().accept(this);
			pop();
		
		pop();
	}

	@Override
	public void visit(List list) {
		/*
		  <xs:element name="List">  
		    <!--
		  List           ::= 'List' '(' TERM* ')' | 'List' '(' TERM+ '|' TERM ')'
		             rewritten as
		  List           ::= 'List' '(' LISTELEMENTS? ')'
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:group ref="LISTELEMENTS" minOccurs="0" maxOccurs="1"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		
		  <xs:group name="LISTELEMENTS">
		    <!--
		  LISTELEMENTS   ::= TERM+ ('|' TERM)?
		    -->
		    <xs:sequence>
		      <xs:element ref="items"/>
		      <xs:element ref="rest" minOccurs="0" maxOccurs="1"/>
		    </xs:sequence>
		  </xs:group>
		  
		  <xs:element name="items">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM" minOccurs="1" maxOccurs="unbounded"/>
		      </xs:sequence>
		      <xs:attribute name="ordered" type="xs:string" fixed="yes"/>
		    </xs:complexType>
		  </xs:element>
		
		  <xs:element name="rest">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("List");
		
			visitDescribable(list);
			
			appendAndPush("items");
			peek().setAttribute("ordered", "yes");
				for (Term item : list.getElements())
					item.accept(this);
			pop();
			

		// appendAndPush("rest");
		// // TODO how to model rest?
		// pop();
		
		pop();
	}

	@Override
	public void visit(Variable variable) {
		/*
		  <xs:element name="Var">
		    <!--
		  Var            ::= '?' Name
		    -->
		    <xs:complexType mixed="true">
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Var");
			
			visitDescribable(variable);
			
			peek().setTextContent(variable.getName());
			
		pop();
	}

	@Override
	public void visit(ImpliesFormula implies) {
		/*
		  <xs:element name="Implies">
		    <!--
		  Implies   ::= IRIMETA? (ATOMIC | 'And' '(' ATOMIC* ')') ':-' FORMULA
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="if"/>
		        <xs:element ref="then"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="if">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="FORMULA"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="then">
		    <xs:complexType>
		     <xs:choice>
		       <xs:group ref="ATOMIC"/>
		       <xs:element name="And" type="And-then.type"/>
		     </xs:choice>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:complexType name="And-then.type">
		    <!-- sensitive to then (ATOMIC) context-->
		    <xs:sequence>
		      <xs:element name="formula" type="formula-then.type" minOccurs="0" maxOccurs="unbounded"/>
		    </xs:sequence>
		  </xs:complexType>
		 
		  <xs:complexType name="formula-then.type">
		    <!-- sensitive to then (ATOMIC) context-->
		    <xs:sequence>
		      <xs:group ref="ATOMIC"/>
		    </xs:sequence>
		  </xs:complexType>
		 */
		
		appendAndPush("Implies");
		
			visitDescribable(implies);
			
			appendAndPush("if");
				implies.getBody().accept(this);
			pop();
			
			java.util.List<AtomicFormula> headFormulas = implies.getHead();
			
			appendAndPush("then");
			if (headFormulas.size() == 1) {
				headFormulas.get(0).accept((AtomicFormulaVisitor) this);
			} else if (headFormulas.size() > 1) {
				appendAndPush("And");
				for (AtomicFormula headFormula : headFormulas) {
					appendAndPush("formula");
					headFormula.accept((AtomicFormulaVisitor) this);
					pop();
				}
				pop();
			}
			pop();
			
		
		pop();
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		/*
		  <xs:group name="ATOMIC">
		    <!--
		  ATOMIC         ::= IRIMETA? (Atom | Equal | Member | Subclass | Frame)
		    -->
		    <xs:choice>
		      <xs:element ref="Atom"/>
		      <xs:element ref="Equal"/>
		      <xs:element ref="Member"/>
		      <xs:element ref="Subclass"/>
		      <xs:element ref="Frame"/>
		    </xs:choice>
		  </xs:group>
		 */
		atomicFormula.accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		/*
		  <xs:element name="Exists">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="declare" minOccurs="1" maxOccurs="unbounded"/>
		        <xs:element ref="formula"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="formula">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="FORMULA"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="declare">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:element ref="Var"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Exists");
		
			visitDescribable(existsFormula);
			
			appendAndPush("declare");
			pop();
			
			appendAndPush("formula");
				existsFormula.getFormulas().get(0).accept(this);
			pop();
		
		pop();
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		/*
		  <xs:complexType name="External-FORMULA.type">
		    <!-- sensitive to FORMULA (Atom) context-->
		    <xs:sequence>
		      <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		      <xs:element name="content" type="content-FORMULA.type"/>
		    </xs:sequence>
		  </xs:complexType>
		 */
		
		appendAndPush("External");

			visitDescribable(externalFormula);
			
			appendAndPush("content");
				externalFormula.getAtom().accept((AtomicFormulaVisitor) this);
			pop();
		
		pop();
	}

	@Override
	public void visit(ForallFormula forallFormula) {
		/*
		  <xs:group name="RULE">
		    <!--
		  RULE      ::= (IRIMETA? 'Forall' Var+ '(' CLAUSE ')') | CLAUSE
		    -->
		    <xs:choice>
		      <xs:element ref="Forall"/>
		      <xs:group ref="CLAUSE"/>
		    </xs:choice>
		  </xs:group>
		  
		  <xs:element name="Forall">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="declare" minOccurs="1" maxOccurs="unbounded"/>
		        <!-- different from formula in And, Or and Exists -->
		        <xs:element name="formula">
		          <xs:complexType>
		            <xs:group ref="CLAUSE"/>
		          </xs:complexType>
		        </xs:element>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:group name="CLAUSE">  
		    <!--
		  CLAUSE   ::= Implies | ATOMIC
		    -->
		    <xs:choice>
		      <xs:element ref="Implies"/>
		      <xs:group ref="ATOMIC"/>
		    </xs:choice>
		  </xs:group>
		  
		  <xs:element name="declare">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:element ref="Var"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		java.util.List<Variable> variables = forallFormula.getVariables();
		if (variables.size() > 0) { // Forall
			appendAndPush("Forall");
			
			visitDescribable(forallFormula);
			
			for(Variable variable : variables) {
				appendAndPush("declare");
				variable.accept(this);
				pop();
			}
				appendAndPush("formula");
					forallFormula.getClause().accept((ClauseVisitor) this);
				pop();
			
			pop();
		} else { // Clause
			forallFormula.getClause().accept((ClauseVisitor) this);
		}
		
	}

	@Override
	public void visit(CompositeFormula compositeFormula) {
		compositeFormula.accept((CompositeFormulaVisitor) this);
	}

	@Override
	public void visit(Atom atom) {
		/*
		  <xs:element name="Atom">
		    <!--
		  Atom           ::= UNITERM
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="UNITERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>  
		 */
		
		appendAndPush("Atom");
		
		visitUniterm(atom);

		pop();
	}

	private void visitUniterm(Uniterm atom) {
		/*
		 <xs:group name="UNITERM">
		    <!--
		  UNITERM        ::= Const '(' (TERM* | (Name '->' TERM)*) ')'
		    -->
		    <xs:sequence>
		      <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		      <xs:element ref="op"/>
		      <xs:choice>
		        <xs:element ref="args" minOccurs="0" maxOccurs="1"/>
		        <xs:element name="slot" type="slot-UNITERM.type" minOccurs="0" maxOccurs="unbounded"/>
		      </xs:choice>
		    </xs:sequence>
		  </xs:group>
		 
		  <xs:element name="op">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:element ref="Const"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="args">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM" minOccurs="1" maxOccurs="unbounded"/>
		      </xs:sequence>
		      <xs:attribute name="ordered" type="xs:string" fixed="yes"/>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:complexType name="slot-UNITERM.type">
		    <!-- sensitive to UNITERM (Name) context-->
		    <xs:sequence>
		      <xs:element ref="Name"/>
		      <xs:group ref="TERM"/>
		    </xs:sequence>
		    <xs:attribute name="ordered" type="xs:string" fixed="yes"/>
		  </xs:complexType>
		 */
		
		visitDescribable(atom);
		
		appendAndPush("op");
		atom.getOperator().accept(this);
		pop();
		
		java.util.List<Argument> arguments = atom.getArguments();
		if (!arguments.isEmpty()) {

			if (arguments.get(0).getName() == null) { // arguments
				appendAndPush("args");
				peek().setAttribute("ordered", "yes");
				
				for (Argument argument : arguments) {
					argument.getValue().accept(this);
				}
				
				pop();
			} else { // slots
				for (Argument argument : arguments) {
					appendAndPush("slot");
					peek().setAttribute("ordered", "yes");
					
					appendAndPush("Name");
					peek().setNodeValue(argument.getName());
					
					argument.getValue().accept(this);
					
					pop();
				}
			}
		}
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		/*
		  <xs:element name="Equal">
		    <!--
		  Equal          ::= TERM '=' TERM
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="left"/>
		        <xs:element ref="right"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:element name="left">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:element name="right">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Equal");
		
			visitDescribable(equalAtom);
			
			appendAndPush("left");
				equalAtom.getTerms().get(0).accept(this);
			pop();
			
			appendAndPush("right");
				equalAtom.getTerms().get(1).accept(this);
			pop();
		
		pop();
	}

	@Override
	public void visit(Frame frame) {
		/*
		  <xs:element name="Frame">
		    <!--
		  Frame          ::= TERM '[' (TERM '->' TERM)* ']'
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="object"/>
		        <xs:element name="slot" type="slot-Frame.type" minOccurs="0" maxOccurs="unbounded"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:element name="object">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 
		  <xs:complexType name="slot-Frame.type">
		    <!-- sensitive to Frame (TERM) context-->
		    <xs:sequence>
		      <xs:group ref="TERM"/>
		      <xs:group ref="TERM"/>
		    </xs:sequence>
		    <xs:attribute name="ordered" type="xs:string" fixed="yes"/>
		  </xs:complexType>
		 */
		
		appendAndPush("Frame");
		
		visitDescribable(frame);
		
		appendAndPush("object");
		frame.getObject().accept(this);
		pop();
		
		for (Attribute attribute : frame.getAttributes()) {
			appendAndPush("slot");
			peek().setAttribute("ordered", "yes");
			
			attribute.getName().accept(this);
			attribute.getValue().accept(this);
			
			pop();
		}
		
		pop();
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		/*
		  <xs:element name="Member">
		    <!--
		  Member         ::= TERM '#' TERM
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="instance"/>
		        <xs:element ref="class"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Member");
		
		visitDescribable(memberAtom);
		
		memberAtom.getInstance().accept(this);
		memberAtom.getClassOfInstance().accept(this);
		
		pop();
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		/*
		  <xs:element name="Subclass">
		    <!--
		  Subclass       ::= TERM '##' TERM
		    -->
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="sub"/>
		        <xs:element ref="super"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="sub">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		  
		  <xs:element name="super">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="TERM"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Subclass");
		
			visitDescribable(subclassAtom);
			
			appendAndPush("sub");
				subclassAtom.getSubClass().accept(this);
			pop();
			
			appendAndPush("super");
				subclassAtom.getSuperClass().accept(this);
			pop();
		
		pop();
	}

	@Override
	public void visit(AndFormula andFormula) {
		/*
		  <xs:element name="And">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="formula" minOccurs="0" maxOccurs="unbounded"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("And");
		
		visitDescribable(andFormula);
		java.util.List<Formula> formulas = andFormula.getFormulas();
		visitFormulas(formulas);
		
		pop();
	}

	private void visitFormulas(java.util.List<Formula> formulas) {
		for(Formula formula : formulas) {
			appendAndPush("formula");
			formula.accept(this);
			pop();
		}
	}

	@Override
	public void visit(OrFormula orFormula) {
		/*
		  <xs:element name="Or">
		    <xs:complexType>
		      <xs:sequence>
		        <xs:group ref="IRIMETA" minOccurs="0" maxOccurs="1"/>
		        <xs:element ref="formula" minOccurs="0" maxOccurs="unbounded"/>
		      </xs:sequence>
		    </xs:complexType>
		  </xs:element>
		 */
		
		appendAndPush("Or");
		
		visitDescribable(orFormula);
		java.util.List<Formula> formulas = orFormula.getFormulas();
		visitFormulas(formulas);
		
		pop();
	}

	@Override
	public void visit(Clause clause) {
		/*
		  <xs:group name="CLAUSE">  
		    <!--
		  CLAUSE   ::= Implies | ATOMIC
		    -->
		    <xs:choice>
		      <xs:element ref="Implies"/>
		      <xs:group ref="ATOMIC"/>
		    </xs:choice>
		  </xs:group>
		 */
		
		clause.accept((ClauseVisitor) this);
	}

	private void push(Element top) {
		elementStack.push(top);
	}
	
	private Element peek() {
		return elementStack.peek();
	}

	private Element pop() {
		return elementStack.pop();
	}
	
	private void appendAndPush(Element element) {
		peek().appendChild(element);
		push(element);
	}

	private void appendAndPush(String elementName) {
		appendAndPush(document.createElement(elementName));
	}


}
