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

import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.Rule;

/**
 * @author Adrian Marte
 */
public class RuleNormalizer {

	public Rule normalize(Rule rule) {
		if (rule instanceof ForallFormula) {
			ForallFormula forallFormula = (ForallFormula) rule;
			return normalize(forallFormula);
		} else if (rule instanceof Clause) {
			Clause clause = (Clause) rule;
			return normalize(clause);
		}

		return rule;
	}

	public ForallFormula normalize(ForallFormula forallFormula) {
		Clause normalizedClause = normalize(forallFormula.getClause());

		if (!forallFormula.getClause().equals(normalizedClause)) {
			// TODO Use clone method.
			return new ForallFormula(forallFormula.getVariables(),
					normalizedClause);
		}

		return forallFormula;
	}

	private Clause normalize(Clause clause) {
		ClauseNormalizer clauseNormalizer = new ClauseNormalizer();
		Clause normalizedClause = clauseNormalizer.normalize(clause);

		return normalizedClause;
	}

}
