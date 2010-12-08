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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.CompositeFormula;
import at.sti2.rif4j.condition.CompositeFormulaVisitor;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.condition.ExternalExpression;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.TermVisitor;
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
import at.sti2.rif4j.translator.iris.mapper.RifToIrisBuiltinMapper;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisConstantMapper;

/**
 * @author Iker Larizgoitia Abad
 * @author Adrian Marte
 */
public class RifToIrisVisitor implements DocumentVisitor, AtomicFormulaVisitor,
		RuleVisitor, FormulaVisitor, ClauseVisitor, TermVisitor,
		CompositeFormulaVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(RifToIrisVisitor.class);

	private Map<IPredicate, IRelation> facts;

	private List<IRule> rules;

	private List<ITerm> terms;

	private IAtom atom;

	public RifToIrisVisitor() {
		this(null, null);
	}

	private RifToIrisVisitor(URI baseUri, Map<String, URI> prefixes) {
		reset();
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public List<IRule> getRules() {
		return rules;
	}

	public List<ITerm> getTerms() {
		return terms;
	}

	public IAtom getAtom() {
		return atom;
	}

	private void reset() {
		facts = new HashMap<IPredicate, IRelation>();
		rules = new ArrayList<IRule>();
		terms = new ArrayList<ITerm>();
		atom = null;
	}

	private static ITerm[] toArray(List<ITerm> terms) {
		ITerm[] termArray = new ITerm[terms.size()];
		termArray = terms.toArray(termArray);

		return termArray;
	}

	private static ITuple toTuple(List<ITerm> terms) {
		ITerm[] termArray = toArray(terms);
		ITuple tuple = Factory.BASIC.createTuple(termArray);

		return tuple;
	}

	@Override
	public void visit(Document document) {
		// We can ignore the base URI, sinc the names of the predicates, etc.,
		// should already be absolute URIs.

		// Constant base = document.getBase();
		// if (base != null && base.getText() != null) {
		// try {
		// URI.create(base.getText());
		// } catch (IllegalArgumentException e) {
		// logger.error("Base URI is not a valid URI", e);
		// }
		// }

		List<Import> imports = document.getImports();
		for (Import import1 : imports) {
			import1.accept(this);
		}

		// We can ignore the metadata.
		// List<Frame> metadata = document.getMetadata();
		// for (Frame frame : metadata) {
		// frame.accept((AtomicFormulaVisitor) this);
		// }

		List<Prefix> prefixes = document.getPrefixes();
		for (Prefix prefix : prefixes) {
			prefix.accept(this);
		}

		Group group = document.getGroup();
		group.accept(this);
	}

	@Override
	public void visit(Group group) {
		RifToIrisVisitor visitor = new RifToIrisVisitor();

		for (Rule rule : group.getRules()) {
			rule.accept(visitor);

			if (visitor.getRules() != null) {
				rules.addAll(visitor.getRules());
			}

			if (visitor.getFacts() != null) {
				// TODO Merge facts.
			}

			visitor.reset();
		}
	}

	@Override
	public void visit(Import imprt) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Prefix prefix) {
		// We can ignore the prefixes, as the names of the predicates etc.
		// should already be absolute URIs.

		// if (prefixes == null) {
		// prefixes = new HashMap<String, URI>();
		// }
		//
		// String name = prefix.getName();
		// Constant iri = prefix.getIri();
		//
		// if (name != null && iri != null && iri.getText() != null) {
		// try {
		// URI uri = URI.create(iri.getText());
		// prefixes.put(name, uri);
		// } catch (IllegalArgumentException e) {
		// logger.error("Prefix URI is not a valid URI", e);
		// }
		// }
	}

	@Override
	public void visit(Atom atom) {
		logger.debug("Visiting atom");

		logger.debug("text: ");
		logger.debug(atom.getOperator().getText());

		RifToIrisVisitor visitor = new RifToIrisVisitor();
		for (Argument argument : atom.getArguments()) {
			// In IRIS, an argument can not be named, therefore, we can ignore
			// the name and only use the value.
			argument.getValue().accept(visitor);

			logger.debug("Argument value is " + argument.getValue().toString());
		}

		List<ITerm> irisTermList = visitor.getTerms();
		ITerm[] irisTerms = toArray(irisTermList);

		IAtom irisAtom = RifToIrisBuiltinMapper.toIrisBuiltin(atom
				.getOperator().getText(), irisTerms);

		// If it is not a built-in, then it is an ordinary predicate.
		if (irisAtom == null) {
			String predicateName = atom.getOperator().getText();

			IPredicate predicate = Factory.BASIC.createPredicate(predicateName,
					irisTerms.length);
			ITuple tuple = toTuple(irisTermList);
			irisAtom = Factory.BASIC.createAtom(predicate, tuple);

			// FIXME What about regular facts?
		}

		this.atom = irisAtom;
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		logger.debug("Visiting equalAtom");
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Frame frame) {
		logger.debug("Visiting frame");
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		logger.debug("Visiting member atom");
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		logger.debug("Visiting subclassAtom");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ForallFormula forallFormula) {
		logger.debug("Visiting forAll");

		RifToIrisVisitor visitor = new RifToIrisVisitor();

		List<Variable> vars = forallFormula.getVariables();
		for (Variable variable : vars) {
			variable.accept(visitor);
		}

		List<ITerm> irisVariables = visitor.getTerms();

		forallFormula.getClause().accept((ClauseVisitor) this);
	}

	@Override
	public void visit(Clause clause) {
		logger.debug("Visiting clause");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		logger.debug("Visiting exists");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		logger.debug("Visiting external");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		logger.debug("Visiting atomic");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CompositeFormula compositeFormula) {
		logger.debug("Visiting composite");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ImpliesFormula implies) {
		logger.debug("Visiting implies");

		implies.getBody().accept(this);

		List<AtomicFormula> formulas = implies.getHead();
		for (AtomicFormula atomicFormula : formulas) {
			atomicFormula.accept((AtomicFormulaVisitor) this);
		}
	}

	@Override
	public void visit(Constant constant) {
		if (constant == null) {
			logger.error("Constant is null");
			return;
		}

		RifToIrisConstantMapper mapper = new RifToIrisConstantMapper();

		// FIXME What about the language?
		ITerm irisTerm = mapper.toIrisTerm(constant.getType(),
				constant.getText());

		if (irisTerm != null) {
			terms.add(irisTerm);
		}
	}

	@Override
	public void visit(Expression expression) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(at.sti2.rif4j.condition.List list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Variable variable) {
		if (variable == null) {
			logger.error("Variable is null");
			return;
		}

		IVariable irisVariable = Factory.TERM
				.createVariable(variable.getName());

		if (irisVariable != null) {
			terms.add(irisVariable);
		}

	}

	@Override
	public void visit(AndFormula andFormula) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(OrFormula orFormula) {
		// TODO Auto-generated method stub

	}

}
