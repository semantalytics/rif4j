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
package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.transformer.FormulaNormalizer;

public class AtomicFormulaTranslator implements AtomicFormulaVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(AtomicFormulaTranslator.class);

	public static IPredicate SUBCLASS_PREDICATE = Factory.BASIC
			.createPredicate("RIF_SUBCLASS_OF", 2);

	public static IPredicate MEMBER_PREDICATE = Factory.BASIC.createPredicate(
			"RIF_MEMBER_OF", 2);

	public static IPredicate FRAME_PREDICATE = Factory.BASIC.createPredicate(
			"RIF_HAS_VALUE", 3);

	private static List<IRule> metaLevelRules;

	private java.util.List<ITerm> terms;

	private java.util.List<ILiteral> literals;

	public AtomicFormulaTranslator() {
		reset();
	}

	static {
		metaLevelRules = new ArrayList<IRule>();

		IVariable a = Factory.TERM.createVariable("A");
		IVariable x = Factory.TERM.createVariable("X");
		IVariable y = Factory.TERM.createVariable("Y");
		IVariable z = Factory.TERM.createVariable("Z");

		// subClassOf(x,z) :- subClassOf(x,y), subClassOf(y,z).
		ITuple headTuple = Factory.BASIC.createTuple(x, z);
		ILiteral head = Factory.BASIC.createLiteral(true, SUBCLASS_PREDICATE,
				headTuple);

		ITuple bodyTuple1 = Factory.BASIC.createTuple(x, y);
		ILiteral body1 = Factory.BASIC.createLiteral(true, SUBCLASS_PREDICATE,
				bodyTuple1);

		ITuple bodyTuple2 = Factory.BASIC.createTuple(y, z);
		ILiteral body2 = Factory.BASIC.createLiteral(true, SUBCLASS_PREDICATE,
				bodyTuple2);

		List<ILiteral> body = new ArrayList<ILiteral>();
		body.add(body1);
		body.add(body2);

		IRule rule = Factory.BASIC.createRule(Collections.singletonList(head),
				body);
		metaLevelRules.add(rule);

		// memberOf(a,y) :- memberOf(a,x), subClassOf(y,x).
		headTuple = Factory.BASIC.createTuple(a, y);
		head = Factory.BASIC.createLiteral(true, MEMBER_PREDICATE, headTuple);

		bodyTuple1 = Factory.BASIC.createTuple(a, x);
		body1 = Factory.BASIC.createLiteral(true, MEMBER_PREDICATE, bodyTuple1);

		bodyTuple2 = Factory.BASIC.createTuple(y, x);
		body2 = Factory.BASIC.createLiteral(true, SUBCLASS_PREDICATE,
				bodyTuple2);
	}

	public void reset() {
		terms = new ArrayList<ITerm>();
		literals = new ArrayList<ILiteral>();
	}

	public java.util.List<ITerm> getTerms() {
		return terms;
	}

	public java.util.List<ILiteral> getLiterals() {
		return literals;
	}

	public static List<IRule> getMetaLevelRules() {
		return metaLevelRules;
	}

	@Override
	public void visit(Atom atom) {
		ExpressionFlattener flattener = new ExpressionFlattener();
		flattener.flatten(atom);

		literals.addAll(flattener.getLiterals());
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		List<Term> terms = equalAtom.getTerms();

		Term leftTerm = terms.get(0);
		TermTranslator leftTermTranslator = new TermTranslator();
		leftTerm.accept(leftTermTranslator);

		ITerm leftIrisTerm;

		if (leftTermTranslator.getTerms().size() == 1) {
			leftIrisTerm = leftTermTranslator.getTerms().get(0);
		} else {
			logger.error("Found more than one term for the left side of an equality relation");
			return;
		}

		Term rightTerm = terms.get(1);
		TermTranslator rightTermTranslator = new TermTranslator();
		rightTerm.accept(rightTermTranslator);

		ITerm rightIrisTerm;

		if (rightTermTranslator.getTerms().size() == 1) {
			rightIrisTerm = rightTermTranslator.getTerms().get(0);
		} else {
			logger.error("Found more than one term for the right side of an equality relation");
			return;
		}

		IBuiltinAtom equalBuiltin = Factory.BUILTIN.createEqual(leftIrisTerm,
				rightIrisTerm);
		ILiteral literal = Factory.BASIC.createLiteral(true, equalBuiltin);

		literals.add(literal);
		literals.addAll(leftTermTranslator.getLiterals());
		literals.addAll(rightTermTranslator.getLiterals());
	}

	@Override
	public void visit(Frame frame) {
		FormulaNormalizer normalizer = new FormulaNormalizer();
		Formula newFormula = normalizer.normalize(frame);

		List<Frame> frames = new ArrayList<Frame>();

		if (newFormula instanceof AndFormula) {
			for (Formula conjunct : ((AndFormula) newFormula).getFormulas()) {
				if (conjunct instanceof Frame) {
					frames.add((Frame) frame);
				} else {
					logger.error("Conjunct of the normalized frame was not a frame: "
							+ conjunct);
				}
			}
		} else {
			frames.add(frame);
		}

		for (Frame newFrame : frames) {
			TermTranslator translator = new TermTranslator();
			newFrame.getObject().accept(translator);
			ITerm object = translator.getTerms().get(0);
			literals.addAll(translator.getLiterals());
			translator.reset();

			List<Attribute> attributes = newFrame.getAttributes();

			if (attributes.size() != 1) {
				logger.error("Found more than one attribute for a normalized frame");
				continue;
			}

			Attribute attribute = attributes.get(0);
			attribute.getName().accept(translator);
			ITerm name = translator.getTerms().get(0);
			literals.addAll(translator.getLiterals());
			translator.reset();

			attribute.getValue().accept(translator);
			ITerm value = translator.getTerms().get(0);
			literals.addAll(translator.getLiterals());

			ITuple tuple = Factory.BASIC.createTuple(object, name, value);
			IAtom atom = Factory.BASIC.createAtom(FRAME_PREDICATE, tuple);
			ILiteral literal = Factory.BASIC.createLiteral(true, atom);
			literals.add(literal);
		}
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		TermTranslator translator = new TermTranslator();

		memberAtom.getInstance().accept(translator);
		ITerm instance = translator.getTerms().get(0);
		literals.addAll(translator.getLiterals());
		translator.reset();

		memberAtom.getClassOfInstance().accept(translator);
		ITerm classOfInstance = translator.getTerms().get(0);
		literals.addAll(translator.getLiterals());
		translator.reset();

		ITuple tuple = Factory.BASIC.createTuple(instance, classOfInstance);
		IAtom atom = Factory.BASIC.createAtom(MEMBER_PREDICATE, tuple);
		ILiteral literal = Factory.BASIC.createLiteral(true, atom);
		literals.add(literal);
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		TermTranslator translator = new TermTranslator();

		subclassAtom.getSubClass().accept(translator);
		ITerm subClass = translator.getTerms().get(0);
		literals.addAll(translator.getLiterals());
		translator.reset();

		subclassAtom.getSuperClass().accept(translator);
		ITerm superClass = translator.getTerms().get(0);
		literals.addAll(translator.getLiterals());
		translator.reset();

		ITuple tuple = Factory.BASIC.createTuple(subClass, superClass);
		IAtom atom = Factory.BASIC.createAtom(SUBCLASS_PREDICATE, tuple);
		ILiteral literal = Factory.BASIC.createLiteral(true, atom);
		literals.add(literal);
	}
}
