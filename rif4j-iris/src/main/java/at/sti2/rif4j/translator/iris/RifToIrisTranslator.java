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
package at.sti2.rif4j.translator.iris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.evaluation.wellfounded.WellFoundedEvaluationStrategyFactory;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.storage.IRelation;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;
import at.sti2.rif4j.transformer.DocumentNormalizer;
import at.sti2.rif4j.transformer.FormulaNormalizer;
import at.sti2.rif4j.transformer.RuleNormalizer;
import at.sti2.rif4j.translator.iris.visitor.AtomicFormulaTranslator;
import at.sti2.rif4j.translator.iris.visitor.DocumentTranslator;
import at.sti2.rif4j.translator.iris.visitor.FormulaTranslator;
import at.sti2.rif4j.translator.iris.visitor.RuleTranslator;

/**
 * @author Iker Larizgoitia Abad
 * @author Adrian Marte
 */
public class RifToIrisTranslator {

	private Map<IPredicate, IRelation> facts;

	private List<IRule> rules;

	private List<IQuery> queries;

	public RifToIrisTranslator() {
		reset();
	}

	private void reset() {
		facts = new HashMap<IPredicate, IRelation>();
		rules = new ArrayList<IRule>();
		queries = new ArrayList<IQuery>();
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public List<IRule> getRules() {
		return rules;
	}

	public List<IQuery> getQueries() {
		return queries;
	}

	public void translate(Document document) {
		if (document == null) {
			throw new IllegalArgumentException("document must not be null");
		}

		DocumentNormalizer normalizer = new DocumentNormalizer();
		Document normalizedDocument = normalizer.normalize(document);

		DocumentTranslator documentTranslator = new DocumentTranslator();
		normalizedDocument.accept(documentTranslator);

		facts = documentTranslator.getFacts();
		rules = documentTranslator.getRules();
	}

	public void translate(Rule rule) {
		if (rule == null) {
			throw new IllegalArgumentException("Rule must not be null");
		}

		RuleNormalizer normalizer = new RuleNormalizer();
		Rule normalizedRule = normalizer.normalize(rule);

		RuleTranslator translator = new RuleTranslator();
		normalizedRule.accept(translator);

		rules.addAll(translator.getRules());
		facts.putAll(translator.getFacts());
	}

	public void translate(Formula formula) {
		if (formula == null) {
			throw new IllegalArgumentException("Formula must not be null");
		}

		FormulaNormalizer normalizer = new FormulaNormalizer();
		Formula normalizedFormula = normalizer.normalize(formula);

		FormulaTranslator translator = new FormulaTranslator();
		normalizedFormula.accept(translator);

		queries.addAll(translator.getQueries());
	}

	public IKnowledgeBase getKnowledgeBase() throws EvaluationException {
		if (facts != null && rules != null) {
			Configuration configuration = KnowledgeBaseFactory
					.getDefaultConfiguration();
			
			// Use well-founded evaluation.
			configuration.evaluationStrategyFactory = new WellFoundedEvaluationStrategyFactory();
			configuration.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

			// Add the meta-level rules.
			rules.addAll(0, AtomicFormulaTranslator.getMetaLevelRules());

			IKnowledgeBase irisKnowledgeBase = KnowledgeBaseFactory
					.createKnowledgeBase(facts, rules, configuration);

			return irisKnowledgeBase;
		}

		return null;
	}

}
