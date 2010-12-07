package at.sti2.rif4j.reasoner.translator.iris.visitors;

import java.util.ArrayList;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Expression;
import at.sti2.rif4j.condition.ExternalExpression;
import at.sti2.rif4j.condition.List;
import at.sti2.rif4j.condition.TermVisitor;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.translator.iris.mapper.RifToIrisBuiltinMapper;

public class TermTranslator implements TermVisitor {

	private static final Logger logger = LoggerFactory
	.getLogger(TermTranslator.class);
	
	private ITerm term;
	private IAtom irisAtom;
	private IAtom extraAtom;

	public ITerm getTerm() {
		return term;
	}
	
	public IAtom getExtraAtom()
	{
		return extraAtom;
	}
	
	@Override
	public void visit(Constant constant) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Expression expression) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(ExternalExpression externalExpression) {
		
		//TODO generate proper aux var names, is always necessary, considering this is an ExternalExpression?
		ITerm auxVariable = Factory.TERM.createVariable("auxVar");
		
		//Arguments
		java.util.List<ITerm> irisTermList = new ArrayList<ITerm>();		
		for (Argument argument : externalExpression.getExpression().getArguments()) {
			// In IRIS, an argument can not be named, therefore, we can ignore
			// the name and only use the value.			
			TermTranslator termTranslator = new TermTranslator();
			argument.getValue().accept(termTranslator);			
			irisTermList.add(termTranslator.getTerm());
		}
		
		ITerm[] irisTerms = AtomicFormulaTranslator.toArray(irisTermList);
		
		//Operator
		String operator = externalExpression.getExpression().getOperator().getText();
		if (RifToIrisBuiltinMapper.isBuiltin(operator)) {			
			
			if (RifToIrisBuiltinMapper.needsAuxiliaryVariable(operator))
			{								
				irisTermList.add(auxVariable);
				irisTerms = AtomicFormulaTranslator.toArray(irisTermList);
			}
			
			term = auxVariable;			
			extraAtom = RifToIrisBuiltinMapper.toIrisBuiltin(externalExpression.getExpression().getOperator().getText(), irisTerms);			
		}
		else {
			//TODO Is this code reachable?
//			String predicateName = externalExpression.getExpression().getOperator().getText();
//			IPredicate predicate = Factory.BASIC.createPredicate(predicateName,irisTerms.length);
//			ITuple tuple = Factory.BASIC.createTuple(irisTerms);
//			
//			extraAtom = Factory.BASIC.createAtom(predicate, tuple);
//			
//			// FIXME What about regular facts?
		}
	}

	@Override
	public void visit(List list) {
		// TODO Auto-generated method stub
	}

	@Override
	public void visit(Variable variable) {
		term = Factory.TERM.createVariable(variable.getName());
	}
}
