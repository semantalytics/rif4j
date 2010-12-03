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

import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.CompositeFormula;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.SubclassAtom;
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

/**
 * @author Iker Larizgoitia Abad
 */
public class RifToIrisVisitor implements DocumentVisitor, AtomicFormulaVisitor,
		RuleVisitor, FormulaVisitor, ClauseVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(RifToIrisVisitor.class);

	private Map<IPredicate, IRelation> facts;

	private List<IRule> rules;

	public RifToIrisVisitor(Map<IPredicate, IRelation> facts, List<IRule> rules) {
		assert facts != null;
		assert rules != null;

		this.facts = facts;
		this.rules = rules;
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public List<IRule> getRules() {
		return rules;
	}

	@Override
	public void visit(Document document) {
		Constant base = document.getBase();

		Group group = document.getGroup();
		group.accept(this);

		List<Import> imports = document.getImports();
		for (Import import1 : imports) {
			import1.accept(this);
		}

		List<Frame> metadata = document.getMetadata();
		for (Frame frame : metadata) {
			// frame.accept(this);
		}

		List<Prefix> prefixes = document.getPrefixes();
		for (Prefix prefix : prefixes) {
			prefix.accept(this);
		}
	}

	@Override
	public void visit(Group group) {
		for (Rule rule : group.getRules()) {
			rule.accept(this);
		}
	}

	@Override
	public void visit(Import imprt) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Prefix prefix) {
		// TODO Auto-generated method stub
		logger.debug("Visiting prefix");
	}

	@Override
	public void visit(Atom atom) {
		logger.debug("Visiting atom");

		logger.debug("text: ");
		logger.debug(atom.getOperator().getText());

		List<Argument> args = atom.getArguments();
		for (Argument argument : args) {
			// System.out.println(argument.getName());
			logger.debug(argument.getValue().toString());
		}
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

		List<Variable> vars = forallFormula.getVariables();
		for (Variable variable : vars) {
		}

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

}
