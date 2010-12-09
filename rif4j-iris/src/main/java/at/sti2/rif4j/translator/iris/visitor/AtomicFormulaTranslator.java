package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Term;

public class AtomicFormulaTranslator implements AtomicFormulaVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(AtomicFormulaTranslator.class);

	private java.util.List<ITerm> terms;

	private java.util.List<ILiteral> literals;

	public AtomicFormulaTranslator() {
		reset();
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
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		// TODO Auto-generated method stub

	}
}
