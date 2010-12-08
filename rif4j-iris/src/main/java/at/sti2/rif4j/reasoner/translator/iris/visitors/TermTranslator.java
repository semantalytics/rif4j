package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.factory.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.condition.ExternalExpression;
import at.sti2.rif4j.condition.List;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.condition.TermVisitor;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisConstantMapper;

public class TermTranslator implements TermVisitor {

	private static final Logger logger = LoggerFactory
			.getLogger(TermTranslator.class);

	private java.util.List<ITerm> terms;

	private java.util.List<ILiteral> literals;

	public TermTranslator() {
		reset();
	}

	public static ITuple toTuple(java.util.List<ITerm> terms) {
		ITerm[] termArray = toArray(terms);
		ITuple tuple = Factory.BASIC.createTuple(termArray);

		return tuple;
	}

	public static ITerm[] toArray(java.util.List<ITerm> terms) {
		ITerm[] termArray = new ITerm[terms.size()];
		termArray = terms.toArray(termArray);
		return termArray;
	}

	public java.util.List<ITerm> getTerms() {
		return terms;
	}

	public java.util.List<ILiteral> getLiterals() {
		return literals;
	}

	public void reset() {
		terms = new ArrayList<ITerm>();
		literals = new ArrayList<ILiteral>();
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
		ExpressionFlattener flattener = new ExpressionFlattener();
		flattener.flatten(expression);

		terms.add(flattener.getVariable());
		literals.addAll(flattener.getLiterals());
	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		externalExpression.getExpression().accept(this);
	}

	@Override
	public void visit(List list) {
		java.util.List<ITerm> irisTerms = new ArrayList<ITerm>();
		java.util.List<ILiteral> irisLiterals = new ArrayList<ILiteral>();

		for (Term element : list.getElements()) {
			TermTranslator translator = new TermTranslator();
			element.accept(translator);

			irisTerms.addAll(translator.getTerms());
			irisLiterals.addAll(translator.getLiterals());
		}

		// FIXME Create a list with all terms from irisTerms.
		IList irisList = new org.deri.iris.terms.concrete.List();

		terms.addAll(irisList);
		literals.addAll(irisLiterals);
	}

	@Override
	public void visit(Variable variable) {
		terms.add(Factory.TERM.createVariable(variable.getName()));
	}
}
