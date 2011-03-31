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

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.OrFormula;

/**
 * @author Adrian Marte
 */
public class FormulaSimplifier {

	public Formula simplify(Formula formula) {
		if (formula instanceof OrFormula) {
			return simplify((OrFormula) formula);
		} else if (formula instanceof AndFormula) {
			return simplify((AndFormula) formula);
		}

		return formula;
	}

	private Formula simplify(OrFormula orFormula) {
		List<Formula> newDisjuncts = new ArrayList<Formula>();
		
		// Or(F1 ... Fn)
		for (Formula disjunct : orFormula.getFormulas()) {
			Formula newDisjunct = simplify(disjunct);

			// Or(s(F1) ... s(Fi) Or(G1 ... Gm) s(Fi+2) ... s(Fn))
			if (newDisjunct instanceof OrFormula) {
				/// Or(s(F1) ... s(Fi) G1 ... Gm s(Fi+2) ... s(Fn))
				newDisjuncts.addAll(((OrFormula) newDisjunct).getFormulas());
			} else {
				newDisjuncts.add(newDisjunct);
			}
		}

		// Or(F1)
		if (orFormula.getFormulas().size() == 1) {
			// F1
			return orFormula.getFormulas().get(0);
		}

		if (!orFormula.getFormulas().equals(newDisjuncts)) {
			return new OrFormula(newDisjuncts);
		}

		return orFormula;
	}

	private Formula simplify(AndFormula andFormula) {
		List<Formula> newConjuncts = new ArrayList<Formula>();
		
		// And(F1 ... Fn)
		for (Formula conjunct : andFormula.getFormulas()) {
			Formula newConjunct = simplify(conjunct);

			// And(s(F1) ... s(Fi) And(G1 ... Gm) s(Fi+2) ... s(Fn))
			if (newConjunct instanceof AndFormula) {
				/// And(s(F1) ... s(Fi) G1 ... Gm s(Fi+2) ... s(Fn))
				newConjuncts.addAll(((AndFormula) newConjunct).getFormulas());
			} else {
				newConjuncts.add(newConjunct);
			}
		}

		// And(F1)
		if (andFormula.getFormulas().size() == 1) {
			// F1
			return andFormula.getFormulas().get(0);
		}

		if (!andFormula.getFormulas().equals(newConjuncts)) {
			return new AndFormula(newConjuncts);
		}

		return andFormula;
	}

}
