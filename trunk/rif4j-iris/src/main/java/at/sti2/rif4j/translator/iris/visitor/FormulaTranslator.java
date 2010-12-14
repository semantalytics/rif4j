package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.factory.Factory;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.OrFormula;

public class FormulaTranslator implements FormulaVisitor {

	private List<ILiteral> literals;

	private List<IQuery> queries;

	public FormulaTranslator() {
		reset();
	}

	private void reset() {
		literals = new ArrayList<ILiteral>();
		queries = new ArrayList<IQuery>();
	}

	public List<ILiteral> getLiterals() {
		return literals;
	}

	public List<IQuery> getQueries() {
		return queries;
	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		existsFormula.getFormula().accept(this);
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		externalFormula.getAtom().accept(atomicFormulaTranslator);

		literals.addAll(atomicFormulaTranslator.getLiterals());

		IQuery query = Factory.BASIC.createQuery(literals);
		queries.add(query);
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulaTranslator);

		literals.addAll(atomicFormulaTranslator.getLiterals());

		IQuery query = Factory.BASIC.createQuery(literals);
		queries.add(query);
	}

	@Override
	public void visit(AndFormula andFormula) {
		for (Formula formula : andFormula.getFormulas()) {
			FormulaTranslator formulaTranslator = new FormulaTranslator();
			formula.accept(formulaTranslator);

			literals.addAll(formulaTranslator.getLiterals());
		}

		IQuery query = Factory.BASIC.createQuery(literals);
		queries.add(query);
	}

	@Override
	public void visit(OrFormula orFormula) {
		for (Formula formula : orFormula.getFormulas()) {
			FormulaTranslator formulaTranslator = new FormulaTranslator();
			formula.accept(formulaTranslator);

			List<ILiteral> disjunctLiterals = formulaTranslator.getLiterals();

			IQuery query = Factory.BASIC.createQuery(disjunctLiterals);
			queries.add(query);

			literals.addAll(formulaTranslator.getLiterals());
		}
	}

}
