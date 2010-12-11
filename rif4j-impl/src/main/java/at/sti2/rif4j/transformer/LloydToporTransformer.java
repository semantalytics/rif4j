package at.sti2.rif4j.transformer;

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.condition.AndFormula;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.ExistsFormula;
import at.sti2.rif4j.condition.ExternalFormula;
import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.condition.FormulaVisitor;
import at.sti2.rif4j.condition.OrFormula;
import at.sti2.rif4j.rule.ImpliesFormula;

/**
 * <p>
 * Transforms {@link ImpliesFormula}s according to the following rules:
 * </p>
 * <table cellspacing="1" border="1">
 * <tr>
 * <td><b>Original rule</b></td>
 * <td><b>Simplified rule(s)</b></td>
 * </tr>
 * <tr>
 * <td>H<sub>1</sub> and ... and H<sub>n</sub> :- B</td>
 * <td>H<sub>1</sub> :- B, ..., H<sub>n</sub> :- B</td>
 * </tr>
 * <tr>
 * <td>H :- B<sub>1</sub> or ... or B<sub>n</sub></td>
 * <td>H :- B<sub>1</sub>, ..., H :- B<sub>n</sub></td>
 * </tr>
 * </table>
 * </p>
 * 
 * @author Adrian Marte
 */
public class LloydToporTransformer {

	public List<ImpliesFormula> transform(final ImpliesFormula implies) {
		// Create a new implies formula of the form H :- Bi for each atom Bi in
		// a OrFormula.
		BodyVisitor bodyVisitor = new BodyVisitor(implies);
		implies.getBody().accept(bodyVisitor);

		List<ImpliesFormula> eraseOrformulas = bodyVisitor.newImpliesFormulas;

		List<ImpliesFormula> eraseAndformulas = new ArrayList<ImpliesFormula>();

		for (ImpliesFormula impliesFormula : eraseOrformulas) {
			// Create a new implies formula of the form Hi :- B for each head
			// atom Hi.
			for (AtomicFormula headAtom : impliesFormula.getHead()) {
				ImpliesFormula newImplies = new ImpliesFormula(impliesFormula
						.getBody(), headAtom);
				eraseAndformulas.add(newImplies);
			}
		}

		return eraseAndformulas;
	}

	private static class BodyVisitor implements FormulaVisitor {

		public List<ImpliesFormula> newImpliesFormulas = new ArrayList<ImpliesFormula>();

		private ImpliesFormula impliesFormula;

		public BodyVisitor(ImpliesFormula impliesFormula) {
			this.impliesFormula = impliesFormula;
		}

		@Override
		public void visit(AndFormula andFormula) {
			// Do not change the rule.
			newImpliesFormulas.add(impliesFormula);
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

		@Override
		public void visit(ExistsFormula existsFormula) {
			// Do not change the rule.
			newImpliesFormulas.add(impliesFormula);
		}

		@Override
		public void visit(ExternalFormula externalFormula) {
			// Do not change the rule.
			newImpliesFormulas.add(impliesFormula);
		}

		@Override
		public void visit(AtomicFormula atomicFormula) {
			// Do not change the rule.
			newImpliesFormulas.add(impliesFormula);
		}

	}

}
