package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;

import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.RuleVisitor;

public class RuleTranslator implements RuleVisitor {

	private List<IRule> rules;
	private Map<IPredicate, IRelation> facts;

	public List<IRule> getRules() {
		return rules;
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}
	
	@Override
	public void visit(Clause clause) {
		ClauseTranslator clauseTranslator = new ClauseTranslator();
		clause.accept(clauseTranslator);
	}	

	@Override
	public void visit(ForallFormula forallFormula) {
		
		//Ignored
		//forallFormula.getVariables();
		
		ClauseTranslator clauseTranslator = new ClauseTranslator();
		forallFormula.getClause().accept(clauseTranslator);		
		
		rules = clauseTranslator.getRules();
	}
}