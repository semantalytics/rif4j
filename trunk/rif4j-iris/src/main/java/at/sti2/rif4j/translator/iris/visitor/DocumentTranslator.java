package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;

import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.DocumentVisitor;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Import;
import at.sti2.rif4j.rule.Prefix;
import at.sti2.rif4j.rule.Rule;

public class DocumentTranslator implements DocumentVisitor {

	private Map<IPredicate, IRelation> facts;

	private List<IRule> rules;

	public DocumentTranslator() {
		reset();
	}

	private void reset() {
		facts = new HashMap<IPredicate, IRelation>();
		rules = new ArrayList<IRule>();
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public List<IRule> getRules() {
		return rules;
	}

	@Override
	public void visit(Document document) {
		// We can ignore the base URI, sinc the names of the predicates, etc.,
		// should already be absolute URIs.

		// Constant base = document.getBase();
		// if (base != null && base.getText() != null) {
		// try {
		// URI.create(base.getText());
		// } catch (IllegalArgumentException e) {
		// logger.error("Base URI is not a valid URI", e);
		// }
		// }

		// List<Import> imports = document.getImports();
		// for (Import import1 : imports) {
		// import1.accept(this);
		// }

		// We can ignore the metadata.
		// List<Frame> metadata = document.getMetadata();
		// for (Frame frame : metadata) {
		// frame.accept((AtomicFormulaVisitor) this);
		// }

		// List<Prefix> prefixes = document.getPrefixes();
		// for (Prefix prefix : prefixes) {
		// prefix.accept(this);
		// }

		Group group = document.getGroup();
		
		if (group != null) {
			group.accept(this);
		}
	}

	@Override
	public void visit(Group group) {
		// We retrieve all rules of the group, even those inside another group
		// in the group :), by calling Group#getAllRules.
		for (Rule rule : group.getAllRules()) {
			RuleTranslator ruleTranslator = new RuleTranslator();
			rule.accept(ruleTranslator);

			if (ruleTranslator.getRules() != null) {
				rules.addAll(ruleTranslator.getRules());
			}

			if (ruleTranslator.getFacts() != null) {
				RuleTranslator.mergeFacts(ruleTranslator.getFacts(), facts);
			}
		}
	}

	@Override
	public void visit(Import imprt) {
	}

	@Override
	public void visit(Prefix prefix) {
	}

}
