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

import at.sti2.rif4j.rule.Clause;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Rule;

/**
 * @author Adrian Marte
 */
public class DocumentNormalizer {

	public Document normalize(Document document) {
		if (document.getGroup() == null) {
			return document;
		}
		
		Group normalizedGroup = normalize(document.getGroup());
		
		if (!document.getGroup().equals(normalizedGroup)) {
			Document normalizedDocument = new Document();
			normalizedDocument.setGroup(normalizedGroup);
			normalizedDocument.setBase(document.getBase());
			normalizedDocument.setId(document.getId());
			normalizedDocument.setImports(document.getImports());
			normalizedDocument.setMetadata(document.getMetadata());
			normalizedDocument.setPrefixes(document.getPrefixes());

			return normalizedDocument;
		}

		return document;
	}

	public Group normalize(Group group) {
		List<Rule> rules = new ArrayList<Rule>();

		for (Rule rule : group.getAllRules()) {
			RuleNormalizer normalizer = new RuleNormalizer();
			Rule normalizedRule = normalizer.normalize(rule);
			rules.add(normalizedRule);
		}

		if (!group.getAllRules().equals(rules)) {
			return new Group(rules);
		}

		return group;
	}

	public ForallFormula normalize(ForallFormula forallFormula) {
		Clause normalizedClause = normalize(forallFormula.getClause());

		return new ForallFormula(forallFormula.getVariables(), normalizedClause);
	}

	private Clause normalize(Clause clause) {
		ClauseNormalizer clauseNormalizer = new ClauseNormalizer();
		Clause normalizedClause = clauseNormalizer.normalize(clause);

		return normalizedClause;
	}

}
