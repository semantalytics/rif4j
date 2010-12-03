package at.sti2.rif4j.translator.iris;

import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;

import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.CompositeFormula;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.ClauseVisitor;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.DocumentVisitor;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.ImpliesFormula;
import at.sti2.rif4j.rule.Import;
import at.sti2.rif4j.rule.Prefix;
import at.sti2.rif4j.rule.Rule;
import at.sti2.rif4j.rule.RuleVisitor;

/**
 * @author Iker Larizgoitia Abad
 */
public class RifToIrisVisitor implements DocumentVisitor, AtomicFormulaVisitor,
		RuleVisitor, FormulaVisitor, ClauseVisitor {

	private Map<IPredicate, IRelation> facts;

	private List<IRule> rules;

	public RifToIrisVisitor(Map<IPredicate, IRelation> facts, List<IRule> rules) {
		assert facts != null;
		assert rules != null;

		this.facts = facts;
		this.rules = rules;
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public List<IRule> getRules() {
		return rules;
	}

	@Override
	public void visit(Document document) {
		Constant base = document.getBase();

		Group group = document.getGroup();
		group.accept(this);

		List<Import> imports = document.getImports();
		for (Import import1 : imports) {
			import1.accept(this);
		}

		List<Frame> metadata = document.getMetadata();
		for (Frame frame : metadata) {
			// frame.accept(this);
		}

		List<Prefix> prefixes = document.getPrefixes();
		for (Prefix prefix : prefixes) {
			prefix.accept(this);
		}
	}

	@Override
	public void visit(Group group) {
		for (Rule rule : group.getRules()) {
			rule.accept(this);
		}
	}

	@Override
	public void visit(Import imprt) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Prefix prefix) {
		// TODO Auto-generated method stub
		System.out.println("Visiting prefix");
	}

	@Override
	public void visit(Atom atom) {
		System.out.println("Visiting atom");

		System.out.print("text: ");
		System.out.println(atom.getOperator().getText());

		List<Argument> args = atom.getArguments();
		for (Argument argument : args) {
			// System.out.println(argument.getName());
			System.out.println(argument.getValue().toString());
		}
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		System.out.println("Visiting equalAtom");
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Frame frame) {
		System.out.println("Visiting frame");
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		System.out.println("Visiting member atom");
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		System.out.println("Visiting subclassAtom");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ForallFormula forallFormula) {
		System.out.println("Visiting forAll");

		List<Variable> vars = forallFormula.getVariables();
		for (Variable variable : vars) {
		}

		forallFormula.getClause().accept((ClauseVisitor) this);
	}

	@Override
	public void visit(Clause clause) {
		System.out.println("Visiting clause");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExistsFormula existsFormula) {
		System.out.println("Visiting exists");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		System.out.println("Visiting external");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		System.out.println("Visiting atomic");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CompositeFormula compositeFormula) {
		System.out.println("Visiting composite");
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ImpliesFormula implies) {
		System.out.println("Visiting implies");

		implies.getBody().accept(this);

		List<AtomicFormula> formulas = implies.getHead();
		for (AtomicFormula atomicFormula : formulas) {
			atomicFormula.accept((AtomicFormulaVisitor) this);
		}
	}

}