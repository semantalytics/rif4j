package at.sti2.rif4j.rule;

public interface DocumentVisitor {

	public void visit(Document document);

	public void visit(Group group);

	public void visit(Import imprt);

	public void visit(Prefix prefix);

}
