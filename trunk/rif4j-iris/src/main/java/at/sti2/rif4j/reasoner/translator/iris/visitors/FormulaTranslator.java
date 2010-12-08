package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;

import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.CompositeFormula;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.rule.ForallFormula;

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
	public void visit(ForallFormula forallFormula) {
		// We can ignore the variable definition.

		ClauseTranslator clauseTranslator = new ClauseTranslator();
		forallFormula.getClause().accept(clauseTranslator);

		rules.addAll(clauseTranslator.getRules());
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulaTranslator);

		literals.addAll(atomicFormulaTranslator.getLiterals());
	}

	@Override
	public void visit(CompositeFormula compositeFormula) {
		CompositeFormulaTranslator compositeFormulaExtractor = new CompositeFormulaTranslator();
		compositeFormula.accept(compositeFormulaExtractor);

		literals = compositeFormulaExtractor.getLiterals();
	}

}
