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

import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.rule.ImpliesFormula;

/**
 * <p>
 * Transforms {@link ImpliesFormula}s according to Lloyd-Topor transformation
 * function <i>t</i> defined below:
 * </p>
 * <table cellspacing="1" border="1">
 * <tr>
 * <td><b>Original rule</b></td>
 * <td><b>Simplified rule(s)</b></td>
 * </tr>
 * <tr>
 * <td>t(H<sub>1</sub> and ... and H<sub>n</sub> :- B .)</td>
 * <td>t(H<sub>1</sub> :- B .), ..., t(H<sub>n</sub> :- B .)</td>
 * </tr>
 * <tr>
 * <td>t(H :- B<sub>1</sub> or ... or B<sub>n</sub> .)</td>
 * <td>t(H :- B<sub>1</sub> .), ..., t(H :- B<sub>n</sub> .)</td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author Adrian Marte
 */
public class LloydToporTransformer {

	public List<ImpliesFormula> transform(final ImpliesFormula implies) {
		List<ImpliesFormula> eraseOrformulas = eraseOrFormulaInBody(implies);

		if (eraseOrformulas.size() > 0) {
			return transform(eraseOrformulas);
		}

		List<ImpliesFormula> eraseAndformulas = eraseAndFormulaInHead(implies);

		if (eraseAndformulas.size() > 0) {
			return transform(eraseAndformulas);
		}

		return Collections.singletonList(implies);
	}

	public List<ImpliesFormula> transform(List<ImpliesFormula> implies) {
		List<ImpliesFormula> resultingFormulas = new ArrayList<ImpliesFormula>();

		for (ImpliesFormula formula : implies) {
			resultingFormulas.addAll(transform(formula));
		}

		return resultingFormulas;
	}

	private static List<ImpliesFormula> eraseAndFormulaInHead(
			ImpliesFormula impliesFormula) {
		List<ImpliesFormula> newFormulas = new ArrayList<ImpliesFormula>();

		if (impliesFormula.getHead().size() > 1) {
			// Create a new implies formula of the form Hi :- B for each head
			// atom Hi.
			for (AtomicFormula headAtom : impliesFormula.getHead()) {
				ImpliesFormula newImplies = new ImpliesFormula(
						impliesFormula.getBody(), headAtom);
				newFormulas.add(newImplies);
			}
		}

		return newFormulas;
	}

	private static List<ImpliesFormula> eraseOrFormulaInBody(
			ImpliesFormula impliesFormula) {
		// Create a new implies formula of the form H :- Bi for each atom Bi in
		// a OrFormula.
		BodyVisitor bodyVisitor = new BodyVisitor(impliesFormula);
		impliesFormula.getBody().accept(bodyVisitor);

		List<ImpliesFormula> eraseOrformulas = bodyVisitor.newImpliesFormulas;
		return eraseOrformulas;
	}

	private static class BodyVisitor extends DefaultFormulaVisitor {

		public List<ImpliesFormula> newImpliesFormulas = new ArrayList<ImpliesFormula>();

		private ImpliesFormula impliesFormula;

		public BodyVisitor(ImpliesFormula impliesFormula) {
			this.impliesFormula = impliesFormula;
		}

		@Override
		public void visit(OrFormula orFormula) {
			List<AtomicFormula> head = impliesFormula.getHead();

			for (Formula bodyFormula : orFormula.getFormulas()) {
				ImpliesFormula newImplies = new ImpliesFormula(bodyFormula,
						head);
				newImpliesFormulas.add(newImplies);
			}
		}

	}

}
