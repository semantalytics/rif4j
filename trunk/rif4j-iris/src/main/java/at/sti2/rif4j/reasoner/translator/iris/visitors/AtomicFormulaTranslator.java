package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.factory.Factory;

import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormulaVisitor;
import at.sti2.rif4j.condition.EqualAtom;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisBuiltinMapper;

public class AtomicFormulaTranslator implements AtomicFormulaVisitor {

	private IAtom irisAtom;
	private IAtom extraAtom;

	public ILiteral getLiteral() {
		return BasicFactory.getInstance().createLiteral(true, irisAtom);
	}
	
	public ILiteral getExtraLiteral()
	{
		return Factory.BASIC.createLiteral(true,extraAtom);
	}
	
	public static ITerm[] toArray(List<ITerm> terms) {
		ITerm[] termArray = new ITerm[terms.size()];
		termArray = terms.toArray(termArray);
		return termArray;
	}
	
	@Override
	public void visit(Atom atom) {
		
		//Arguments
		List<ITerm> irisTermList = new ArrayList<ITerm>();
		
		for (Argument argument : atom.getArguments()) {
			// In IRIS, an argument can not be named, therefore, we can ignore
			// the name and only use the value.			
			TermTranslator termTranslator = new TermTranslator();
			argument.getValue().accept(termTranslator);			
			irisTermList.add(termTranslator.getTerm());
		}
		
		ITerm[] irisTerms = toArray(irisTermList);
							
		if (RifToIrisBuiltinMapper.isBuiltin(atom.getOperator().getText())) {			
			irisAtom = RifToIrisBuiltinMapper.toIrisBuiltin(atom.getOperator().getText(), irisTerms);		
		}
		else {
			String predicateName = atom.getOperator().getText();
			IPredicate predicate = Factory.BASIC.createPredicate(predicateName,irisTerms.length);
			ITuple tuple = Factory.BASIC.createTuple(irisTerms);
			
			irisAtom = Factory.BASIC.createAtom(predicate, tuple);		
			// FIXME What about regular facts?
		}
	}

	@Override
	public void visit(EqualAtom equalAtom) {
		List<Term> terms = equalAtom.getTerms();
		
		Term leftTerm = terms.get(0);
		TermTranslator leftTermTranslator = new TermTranslator();
		leftTerm.accept(leftTermTranslator);
		
		Term rightTerm = terms.get(1);		
		TermTranslator rightTermTranslator = new TermTranslator();
		rightTerm.accept(rightTermTranslator);
		
		irisAtom = Factory.BUILTIN.createEqual(leftTermTranslator.getTerm(), rightTermTranslator.getTerm());	
		extraAtom = rightTermTranslator.getExtraAtom();		
	}

	@Override
	public void visit(Frame frame) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(MemberAtom memberAtom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SubclassAtom subclassAtom) {
		// TODO Auto-generated method stub

	}
}
