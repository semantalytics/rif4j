package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.OrFormula;

public class FormulaTranslator implements FormulaVisitor {

	private List<ILiteral> literals;

	private List<IRule> rules;

	public FormulaTranslator() {
		reset();
	}

	private void reset() {
		literals = new ArrayList<ILiteral>();
		rules = new ArrayList<IRule>();
	}

	public List<ILiteral> getLiterals() {
		return literals;
	}

	public List<IRule> getRules() {
		return rules;
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		externalFormula.getAtom().accept(atomicFormulaTranslator);

		literals.addAll(atomicFormulaTranslator.getLiterals());
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulaTranslator);

		literals.addAll(atomicFormulaTranslator.getLiterals());
	}

	@Override
	public void visit(AndFormula andFormula) {
		for (Formula formula : andFormula.getFormulas()) {
			FormulaTranslator formulaTranslator = new FormulaTranslator();
			formula.accept(formulaTranslator);

			literals.addAll(formulaTranslator.getLiterals());
		}
	}

	@Override
	public void visit(OrFormula orFormula) {
		// TODO Auto-generated method stub

	}

}