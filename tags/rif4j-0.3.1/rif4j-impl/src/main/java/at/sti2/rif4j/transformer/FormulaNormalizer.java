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
import java.util.Collections;
import java.util.List;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.OrFormula;

/**
 * @author Adrian Marte
 */
// FIXME What about external formulas, exists formulas?
public class FormulaNormalizer {

	/**
	 * Transforms the specified formula to an equivalent formula in disjunctive
	 * normal form (DNF). In this process, {@link AndFormula}s and
	 * {@link OrFormula}s are simplified using
	 * {@link FormulaSimplifier#simplify(Formula)}.
	 * 
	 * @param formula
	 *            The formula to transform to an equivalent formula in DNF.
	 * @return An equivalent formula in disjunctive normal form (DNF).
	 */
	public Formula normalize(Formula formula) {
		FormulaSimplifier simplifier = new FormulaSimplifier();
		Formula simplifiedFormula = simplifier.simplify(formula);
		
		Formula newFormula;

		newFormula = normalizeAndFormula(simplifiedFormula);

		if (newFormula != null) {
			return newFormula;
		}

		newFormula = normalizeOrFormula(simplifiedFormula);

		if (newFormula != null) {
			return newFormula;
		}

		// t[p1->v1 ... pn->vn] => And(t[p1->v1] ... t[pn->vn])
		newFormula = normalizeFrame(simplifiedFormula);

		if (newFormula != null) {
			return newFormula;
		}

		return simplifiedFormula;
	}

	private Formula normalizeOrFormula(Formula formula) {
		if (!(formula instanceof OrFormula)) {
			return null;
		}

		OrFormula orFormula = (OrFormula) formula;
		List<Formula> newDisjuncts = new ArrayList<Formula>();

		for (Formula disjunct : orFormula.getFormulas()) {
			Formula newFormula = normalize(disjunct);
			newDisjuncts.add(newFormula);
		}

		if (orFormula.getFormulas().equals(newDisjuncts)) {
			return orFormula;
		}

		// Normalize again, in order to simplify any Or(Or(a b) c).
		return normalize(new OrFormula(newDisjuncts));
	}

	private Formula normalizeFrame(Formula formula) {
		if (!(formula instanceof Frame)) {
			return null;
		}

		Frame frame = (Frame) formula;

		if (frame.getAttributes().size() > 1) {
			List<Formula> newFrames = new ArrayList<Formula>();

			for (Attribute attribute : frame.getAttributes()) {
				Frame newFrame = new Frame(frame.getObject(),
						Collections.singletonList(attribute));
				newFrames.add(newFrame);
			}

			return new AndFormula(newFrames);
		}

		return frame;
	}

	private Formula normalizeAndFormula(Formula formula) {
		if (!(formula instanceof AndFormula)) {
			return null;
		}

		AndFormula andFormula = (AndFormula) formula;

		Formula resultingFormula = null;

		// Apply the distribute law on formulas of the form (a and (b or c)).
		resultingFormula = applyLeftDistributiveLaw(andFormula);

		if (resultingFormula != null) {
			return resultingFormula;
		}

		// Apply the distribute law on formulas of the form ((a or b) and c).
		resultingFormula = applyRightDistributiveLaw(andFormula);

		if (resultingFormula != null) {
			return resultingFormula;
		}

		List<Formula> newConjuncts = new ArrayList<Formula>();

		for (Formula conjunct : andFormula.getFormulas()) {
			Formula newFormula = normalize(conjunct);
			newConjuncts.add(newFormula);
		}

		if (andFormula.getFormulas().equals(newConjuncts)) {
			return andFormula;
		}

		return new AndFormula(newConjuncts);
	}

	private Formula applyLeftDistributiveLaw(AndFormula andFormula) {
		List<Formula> newConjuncts = new ArrayList<Formula>();
		List<Formula> conjuncts = andFormula.getFormulas();

		boolean hasChanged = false;

		// And(F1 ... Fn)
		for (int i = 0; i < conjuncts.size(); i++) {
			int j = i + 1;
			Formula conjunct = conjuncts.get(i);

			if (j < conjuncts.size()) {
				// And(F1 F2 ... Fi Or(G1, ..., Gm) Fi+2 Fi+3 ... Fn)
				if (conjuncts.get(j) instanceof OrFormula) {
					OrFormula orConjunct = (OrFormula) conjuncts.get(j);

					List<Formula> andFormulas = new ArrayList<Formula>();

					// And(Fi G1) ... And(Fi Gm)
					for (Formula disjunct : orConjunct.getFormulas()) {
						List<Formula> formulas = new ArrayList<Formula>();
						formulas.add(normalize(conjunct));
						formulas.add(normalize(disjunct));

						AndFormula temp = new AndFormula(formulas);
						andFormulas.add(temp);
					}

					// Or(And(Fi G1) ... And(Fi Gm))
					Formula orFormula = new OrFormula(andFormulas);

					// normalize(Or(And(Fi G1) ... And(Fi Gm)))
					newConjuncts.add(normalize(orFormula));

					hasChanged = true;

					i++;
				} else {
					newConjuncts.add(normalize(conjunct));
				}
			}
		}

		if (hasChanged) {
			if (newConjuncts.size() > 1) {
				// normalize(And(...)) is either an atomic, an or formula or a
				// and formula.
				return normalize(new AndFormula(newConjuncts));
			} else {
				// Either an atomic formula or an or formula.
				return newConjuncts.get(0);
			}
		}

		return null;
	}

	private Formula applyRightDistributiveLaw(AndFormula andFormula) {
		List<Formula> newConjuncts = new ArrayList<Formula>();
		List<Formula> conjuncts = andFormula.getFormulas();

		boolean hasChanged = false;

		for (int i = conjuncts.size() - 1; i > 0; i--) {
			Formula conjunct = conjuncts.get(i);
			int j = i - 1;

			if (j >= 0) {
				if (conjuncts.get(j) instanceof OrFormula) {
					OrFormula orConjunct = (OrFormula) conjuncts.get(j);

					List<Formula> andFormulas = new ArrayList<Formula>();

					for (Formula disjunct : orConjunct.getFormulas()) {
						List<Formula> formulas = new ArrayList<Formula>();
						formulas.add(normalize(disjunct));
						formulas.add(normalize(conjunct));

						AndFormula temp = new AndFormula(formulas);
						andFormulas.add(temp);
					}

					Formula orFormula = new OrFormula(andFormulas);
					newConjuncts.add(0, normalize(orFormula));

					hasChanged = true;

					i--;
				} else {
					newConjuncts.add(0, normalize(conjunct));
				}
			}
		}

		if (hasChanged) {
			if (newConjuncts.size() > 1) {
				return normalize(new AndFormula(newConjuncts));
			} else {
				return newConjuncts.get(0);
			}
		}

		return null;
	}

}
