package at.sti2.rif4j.translator.iris.visitor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.rule.ImpliesFormula;

public class LloydToporTransformerTest {

	private LloydToporTransformer transformer;

	@Before
	public void setUp() {
		transformer = new LloydToporTransformer();
	}

	@Test
	public void testOrFormula() {
		AtomicFormula head = new Atom(new Constant("sometype", "", "head"));

		List<Formula> bodyAtoms = new ArrayList<Formula>();
		Atom body1 = new Atom(new Constant("sometype", "", "body1"));
		Atom body2 = new Atom(new Constant("sometype", "", "body2"));
		bodyAtoms.add(body1);
		bodyAtoms.add(body2);

		OrFormula body = new OrFormula(bodyAtoms);
		ImpliesFormula implies = new ImpliesFormula(body, head);

		List<ImpliesFormula> newImpliesFormulas = transformer
				.transform(implies);

		Assert.assertEquals(2, newImpliesFormulas.size());

		ImpliesFormula firstFormula = newImpliesFormulas.get(0);

		Assert.assertEquals(1, firstFormula.getHead().size());
		Assert.assertFalse(firstFormula.getBody() instanceof OrFormula);

		ImpliesFormula secondFormula = newImpliesFormulas.get(1);

		Assert.assertEquals(1, secondFormula.getHead().size());
		Assert.assertFalse(secondFormula.getBody() instanceof OrFormula);
	}
	
	@Test
	public void testAndFormula() {
		List<AtomicFormula> head = new ArrayList<AtomicFormula>();

		Atom head1 = new Atom(new Constant("sometype", "", "head1"));
		Atom head2 = new Atom(new Constant("sometype", "", "head2"));
		head.add(head1);
		head.add(head2);

		List<Formula> bodyAtoms = new ArrayList<Formula>();
		Atom body1 = new Atom(new Constant("sometype", "", "body1"));
		bodyAtoms.add(body1);

		OrFormula body = new OrFormula(bodyAtoms);
		ImpliesFormula implies = new ImpliesFormula(body, head);

		List<ImpliesFormula> newImpliesFormulas = transformer
				.transform(implies);
		
		Assert.assertEquals(2, newImpliesFormulas.size());
		
		ImpliesFormula firstFormula = newImpliesFormulas.get(0);

		Assert.assertEquals(1, firstFormula.getHead().size());
		Assert.assertFalse(firstFormula.getBody() instanceof OrFormula);

		ImpliesFormula secondFormula = newImpliesFormulas.get(1);

		Assert.assertEquals(1, secondFormula.getHead().size());
		Assert.assertFalse(secondFormula.getBody() instanceof OrFormula);
	}

	@Test
	public void testAndOrFormula() {
		List<AtomicFormula> head = new ArrayList<AtomicFormula>();

		Atom head1 = new Atom(new Constant("sometype", "", "head1"));
		Atom head2 = new Atom(new Constant("sometype", "", "head2"));
		head.add(head1);
		head.add(head2);

		List<Formula> bodyAtoms = new ArrayList<Formula>();
		Atom body1 = new Atom(new Constant("sometype", "", "body1"));
		Atom body2 = new Atom(new Constant("sometype", "", "body2"));
		bodyAtoms.add(body1);
		bodyAtoms.add(body2);

		OrFormula body = new OrFormula(bodyAtoms);
		ImpliesFormula implies = new ImpliesFormula(body, head);

		List<ImpliesFormula> newImpliesFormulas = transformer
				.transform(implies);
		
		Assert.assertEquals(4, newImpliesFormulas.size());
	}

}
