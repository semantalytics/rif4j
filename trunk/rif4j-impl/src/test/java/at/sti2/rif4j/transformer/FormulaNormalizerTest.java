package at.sti2.rif4j.transformer;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.condition.Variable;

public class FormulaNormalizerTest {

	private FormulaNormalizer normalizer;

	private int counter;

	@Before
	public void setUp() {
		normalizer = new FormulaNormalizer();
		counter = 1;
	}

	private Variable createVariable() {
		return new Variable("var" + (counter++));
	}

	private Constant createConstant() {
		return new Constant("string", "eng", "Anna is awesome" + (counter++));
	}

	@Test
	public void testSimple() {
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(new Argument(createVariable()));
		arguments.add(new Argument(createVariable()));

		Atom atom = new Atom(createConstant(), arguments);

		List<Formula> formulas = new ArrayList<Formula>();
		formulas.add(atom);
		formulas.add(atom);

		OrFormula orFormula = new OrFormula(formulas);

		atom = new Atom(createConstant(), arguments);

		formulas = new ArrayList<Formula>();
		formulas.add(orFormula);
		formulas.add(orFormula);

		AndFormula andFormula = new AndFormula(formulas);

		Formula normalizedFormula = normalizer.normalize(andFormula);
		System.out.println(andFormula);
		System.out.println(normalizedFormula);
	}

	@Test
	public void testMoreComplex() {
		List<Argument> arguments = new ArrayList<Argument>();
		arguments.add(new Argument(createVariable()));
		arguments.add(new Argument(createVariable()));

		Atom atom = new Atom(createConstant(), arguments);

		List<Formula> formulas = new ArrayList<Formula>();
		formulas.add(atom);
		formulas.add(atom);

		AndFormula andFormula = new AndFormula(formulas);

		atom = new Atom(createConstant(), arguments);

		formulas = new ArrayList<Formula>();
		formulas.add(atom);
		formulas.add(andFormula);

		OrFormula orFormula = new OrFormula(formulas);
		
		formulas = new ArrayList<Formula>();
		formulas.add(orFormula);
		formulas.add(andFormula);
		
		andFormula = new AndFormula(formulas);

		Formula normalizedFormula = normalizer.normalize(andFormula);
		System.out.println(andFormula);
		System.out.println(normalizedFormula);
	}

}
