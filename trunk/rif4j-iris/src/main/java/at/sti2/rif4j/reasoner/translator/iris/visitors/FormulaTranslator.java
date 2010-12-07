package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.basics.BasicFactory;

import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.CompositeFormula;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.rule.ForallFormula;

public class FormulaTranslator implements FormulaVisitor {

	private List<ILiteral> literalList = new ArrayList<ILiteral>();

	public List<ILiteral> getLiterals() {		
		return literalList;
	}
	
	@Override
	public void visit(ExistsFormula existsFormula) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExternalFormula externalFormula) {
		 
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		externalFormula.getAtom().accept(atomicFormulaTranslator);
		
		literalList.add(atomicFormulaTranslator.getLiteral());
	}

	@Override
	public void visit(ForallFormula forallFormula) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(AtomicFormula atomicFormula) {
		AtomicFormulaTranslator atomicFormulaTranslator = new AtomicFormulaTranslator();
		atomicFormula.accept(atomicFormulaTranslator);

		//This should not be null when all the builtins and predicates are implemented
		if (atomicFormulaTranslator.getLiteral() != null)
			//TODO change to addAll to support the extra atoms.
			literalList.add(atomicFormulaTranslator.getLiteral());
	}

	@Override
	public void visit(CompositeFormula compositeFormula) {
		CompositeFormulaTranslator compositeFormulaExtractor = new CompositeFormulaTranslator();
		compositeFormula.accept(compositeFormulaExtractor);
		
		literalList = compositeFormulaExtractor.getLiterals();
	}

}
