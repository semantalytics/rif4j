package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.HashMap;
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

	public RuleTranslator() {
		reset();
	}

	public void reset() {
		rules = new ArrayList<IRule>();
		facts = new HashMap<IPredicate, IRelation>();
	}

	public List<IRule> getRules() {
		return rules;
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public static void mergeFacts(Map<IPredicate, IRelation> source,
			Map<IPredicate, IRelation> target) {
		for (IPredicate predicate : source.keySet()) {
			IRelation sourceRelation = source.get(predicate);
			IRelation targetRelation = target.get(predicate);

			if (targetRelation == null) {
				target.put(predicate, sourceRelation);
			} else {
				for (int i = 0; i < sourceRelation.size(); i++) {
					targetRelation.add(sourceRelation.get(i));
				}
			}
		}
	}

	@Override
	public void visit(Clause clause) {
		ClauseTranslator clauseTranslator = new ClauseTranslator();
		clause.accept(clauseTranslator);

		mergeFacts(clauseTranslator.getFacts(), facts);

		rules.addAll(clauseTranslator.getRules());
	}

	@Override
	public void visit(ForallFormula forallFormula) {
		// We can ignore the variable definition.

		ClauseTranslator clauseTranslator = new ClauseTranslator();
		forallFormula.getClause().accept(clauseTranslator);

		rules.addAll(clauseTranslator.getRules());
	}
}