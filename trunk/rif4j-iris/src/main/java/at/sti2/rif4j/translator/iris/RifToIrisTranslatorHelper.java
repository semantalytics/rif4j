package at.sti2.rif4j.translator.iris;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.terms.TermFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.condition.SubclassAtom;

public class RifToIrisTranslatorHelper implements AtomicFormulaVisitor,
		FormulaVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(RifToIrisTranslatorHelper.class);

	private List<ILiteral> literalList = new ArrayList<ILiteral>();
	private ILiteral literal = null;

	public ILiteral translateAtomicFormula(AtomicFormula atomicFormula) {
		atomicFormula.accept((AtomicFormulaVisitor) this);
		return this.literal;
	}

	public List<ILiteral> translateFormula(Formula formula) {
		formula.accept(this);
		return literalList;
	}

	@Override
	public void visit(Atom atom) {

		String symbol = atom.getOperator().getText();
		int arity = atom.getArguments().size();

		IPredicate predicate = BasicFactory.getInstance().createPredicate(
				symbol, arity);

		List<ITerm> terms = new ArrayList<ITerm>();

		List<Argument> args = atom.getArguments();

		for (Argument argument : args) {
			terms.add(TermFactory.getInstance().createVariable(
					argument.getValue().toString().replace("?", "")));
		}

		ITuple tuple = BasicFactory.getInstance().createTuple(terms);

		// TODO isPositive implications?
		this.literal = BasicFactory.getInstance().createLiteral(true,
				predicate, tuple);

		logger.debug("IRIS atom translated --> " + this.literal.toString());
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		// TODO Auto-generated method stub
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

	@Override
	public void visit(ExistsFormula existsFormula) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		atomicFormula.accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(AndFormula andFormula) {

		for (Formula formula : andFormula.getFormulas()) {
			this.literal = null;

			formula.accept(this);

			if (literal != null)
				this.literalList.add(this.literal);
		}
	}

	@Override
	public void visit(OrFormula orFormula) {
		// TODO Auto-generated method stub
	}
}
