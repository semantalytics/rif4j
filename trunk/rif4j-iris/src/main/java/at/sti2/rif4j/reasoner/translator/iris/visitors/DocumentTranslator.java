package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import at.sti2.rif4j.translator.iris.RifToIrisVisitor;

public class DocumentTranslator implements DocumentVisitor {

	private Map<IPredicate, IRelation> facts;
	private List<IRule> rules;

	public DocumentTranslator()
	{
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

//		List<Import> imports = document.getImports();
//		for (Import import1 : imports) {
//			import1.accept(this);
//		}

		// We can ignore the metadata.
		// List<Frame> metadata = document.getMetadata();
		// for (Frame frame : metadata) {
		// frame.accept((AtomicFormulaVisitor) this);
		// }

//		List<Prefix> prefixes = document.getPrefixes();
//		for (Prefix prefix : prefixes) {
//			prefix.accept(this);
//		}
		
		Group group = document.getGroup();
		group.accept(this);
	}

	@Override
	public void visit(Group group) {
			
		//TODO Subgroups?		
		for (Rule rule : group.getRules()) 
		{
			RuleTranslator ruleTranslator = new RuleTranslator();			
			rule.accept(ruleTranslator);

			if (ruleTranslator.getRules() != null) {
				rules.addAll(ruleTranslator.getRules());
			}
			
			if (ruleTranslator.getFacts() != null) {
				// TODO Merge facts.
				this.addFacts(ruleTranslator.getFacts());
			}
		}
	}

	private void addFacts(Map<IPredicate, IRelation> newFacts) 
	{
		for (Iterator<IPredicate> iterator = newFacts.keySet().iterator(); iterator.hasNext();) {
			IPredicate predicate = (IPredicate) iterator.next();
			facts.put(predicate, newFacts.get(predicate));
		}		
	}

	@Override
	public void visit(Import imprt) {
	}

	@Override
	public void visit(Prefix prefix) {
	}


}
