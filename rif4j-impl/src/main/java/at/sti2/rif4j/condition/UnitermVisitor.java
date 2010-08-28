package at.sti2.rif4j.condition;

public interface UnitermVisitor {

	public void visit(Expression expression);

	public void visit(Atom atom);

}
