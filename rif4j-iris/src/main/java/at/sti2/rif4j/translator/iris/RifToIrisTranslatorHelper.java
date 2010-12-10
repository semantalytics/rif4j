package at.sti2.rif4j.translator.iris;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.AndFormula;
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
import at.sti2.rif4j.translator.iris.visitor.AtomicFormulaTranslator;
import at.sti2.rif4j.translator.iris.visitor.FormulaTranslator;

public class RifToIrisTranslatorHelper implements AtomicFormulaVisitor,
		FormulaVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(RifToIrisTranslatorHelper.class);

	private List<ILiteral> literals = new ArrayList<ILiteral>();

	public ILiteral translateAtomicFormula(AtomicFormula atomicFormula) {
		atomicFormula.accept((AtomicFormulaVisitor) this);
		return this.literals.get(0);
	}

	public List<ILiteral> translateFormula(Formula formula) {
		formula.accept(this);
		return literals;
	}

	@Override
	public void visit(Atom atom) {
		AtomicFormulaTranslator translator = new AtomicFormulaTranslator();
		atom.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		AtomicFormulaTranslator translator = new AtomicFormulaTranslator();
		equalAtom.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(Frame frame) {
		AtomicFormulaTranslator translator = new AtomicFormulaTranslator();
		frame.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		AtomicFormulaTranslator translator = new AtomicFormulaTranslator();
		memberAtom.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		AtomicFormulaTranslator translator = new AtomicFormulaTranslator();
		subclassAtom.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		FormulaTranslator translator = new FormulaTranslator();
		existsFormula.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		FormulaTranslator translator = new FormulaTranslator();
		externalFormula.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		atomicFormula.accept((AtomicFormulaVisitor) this);
	}

	@Override
	public void visit(AndFormula andFormula) {
		FormulaTranslator translator = new FormulaTranslator();
		andFormula.accept(translator);

		literals.addAll(translator.getLiterals());
	}

	@Override
	public void visit(OrFormula orFormula) {
		FormulaTranslator translator = new FormulaTranslator();
		orFormula.accept(translator);

		literals.addAll(translator.getLiterals());
	}
}
