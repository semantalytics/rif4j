package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.storage.IRelation;

import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.ImpliesFormula;

public class ClauseTranslator implements ClauseVisitor {

	private List<IRule> rules = new ArrayList<IRule>();
	private Map<IPredicate, IRelation> facts;
	
	public List<IRule> getRules() {
		return rules;
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	@Override
	public void visit(ImpliesFormula implies) {
		
		List<ILiteral> headLiterals = new ArrayList<ILiteral>();
		
		for ( AtomicFormula atomicFormula : implies.getHead())
		{
			AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
			atomicFormula.accept(atomicFormulaTranslator);
			
			headLiterals.add(atomicFormulaTranslator.getLiteral());
		}
		
		FormulaTranslator formulaTranslator = new FormulaTranslator();
		implies.getBody().accept(formulaTranslator);
		List<ILiteral> bodyLiterals = formulaTranslator.getLiterals();
		
		rules.add(BasicFactory.getInstance().createRule(headLiterals, bodyLiterals));
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulatTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulatTranslator);		
	}

}
