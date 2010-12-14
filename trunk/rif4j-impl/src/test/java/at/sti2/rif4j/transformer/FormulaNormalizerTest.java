/*
 * Copyright 2010 STI Innsbruck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.sti2.rif4j.transformer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Atom;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.OrFormula;

/**
 * @author Adrian Marte
 */
// TODO Make some asserts in the tests.
public class FormulaNormalizerTest {

	private FormulaNormalizer normalizer;

	private Atom a;

	private Atom b;

	private Atom c;

	private Atom d;

	private Atom e;

	@Before
	public void setUp() {
		normalizer = new FormulaNormalizer();

		List<Argument> arguments = new ArrayList<Argument>();
		a = new Atom(createConstant("a"), arguments);
		b = new Atom(createConstant("b"), arguments);
		c = new Atom(createConstant("c"), arguments);
		d = new Atom(createConstant("d"), arguments);
		e = new Atom(createConstant("e"), arguments);
	}

	private Constant createConstant(String name) {
		return new Constant(null, null, name);
	}

	@Test
	public void testSimple() {
		List<Formula> formulas = new ArrayList<Formula>();
		formulas.add(a);
		formulas.add(b);

		// a or b
		OrFormula orFormula1 = new OrFormula(formulas);

		formulas = new ArrayList<Formula>();
		formulas.add(c);
		formulas.add(d);

		// c or d
		OrFormula orFormula2 = new OrFormula(formulas);

		formulas = new ArrayList<Formula>();
		formulas.add(orFormula1);
		formulas.add(orFormula2);

		// (a or b) and (c or d)
		AndFormula andFormula = new AndFormula(formulas);

		// Should result in the following DNF:
		// (a and c) or (b and c) or (a and d) or (b and d)
		Formula normalizedFormula = normalizer.normalize(andFormula);

		Assert.assertNotNull(normalizedFormula);
		Assert.assertFalse(andFormula.equals(normalizedFormula));
	}

	@Test
	public void testMoreComplex() {
		List<Formula> formulas = new ArrayList<Formula>();
		formulas.add(b);
		formulas.add(c);

		// b and c
		AndFormula andFormula1 = new AndFormula(formulas);

		formulas = new ArrayList<Formula>();
		formulas.add(a);
		formulas.add(andFormula1);

		// a or (b and c)
		OrFormula orFormula = new OrFormula(formulas);

		formulas = new ArrayList<Formula>();
		formulas.add(d);
		formulas.add(e);

		// d and e
		AndFormula andFormula2 = new AndFormula(formulas);

		formulas = new ArrayList<Formula>();
		formulas.add(orFormula);
		formulas.add(andFormula2);

		// (a or (b and c)) and (d and e)
		AndFormula andFormula3 = new AndFormula(formulas);

		// Should result in the following DNF:
		// (a and d and e) or (b and c and d and e)
		Formula normalizedFormula = normalizer.normalize(andFormula3);

		Assert.assertNotNull(normalizedFormula);
		Assert.assertFalse(andFormula3.equals(normalizedFormula));
	}

}
