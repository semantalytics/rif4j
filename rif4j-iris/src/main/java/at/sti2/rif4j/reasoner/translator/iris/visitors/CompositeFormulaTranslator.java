package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.CompositeFormulaVisitor;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.rule.ForallFormula;

public class CompositeFormulaTranslator implements CompositeFormulaVisitor {

	private List<ILiteral> literals;

	public CompositeFormulaTranslator() {
		reset();
	}

	public void reset() {
		literals = new ArrayList<ILiteral>();
	}

	public List<ILiteral> getLiterals() {
		return literals;
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

	@Override
	public void visit(ExistsFormula existsFormula) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ForallFormula forallFormula) {
		// TODO Auto-generated method stub

	}

}
