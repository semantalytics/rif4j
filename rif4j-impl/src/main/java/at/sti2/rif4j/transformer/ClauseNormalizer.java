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

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.ImpliesFormula;

/**
 * @author Adrian Marte
 */
public class ClauseNormalizer {

	public Clause normalize(Clause clause) {
		if (clause instanceof ImpliesFormula) {
			ImpliesFormula impliesFormula = (ImpliesFormula) clause;
			return normalize(impliesFormula);
		}

		// An AtomicFormula does not need to be normalized.
		return clause;
	}

	public ImpliesFormula normalize(ImpliesFormula impliesFormula) {
		// Normalize the body of an ImpliesFormula.
		Formula body = impliesFormula.getBody();

		FormulaNormalizer normalizer = new FormulaNormalizer();
		Formula normalizedBody = normalizer.normalize(body);

		if (!normalizedBody.equals(body)) {
			// TODO Use clone method.
			return new ImpliesFormula(normalizedBody, impliesFormula.getHead());
		}

		return impliesFormula;
	}

}
