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
package at.sti2.rif4j.reasoner.iris;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.reasoner.AbstractReasoner;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;
import at.sti2.rif4j.translator.iris.visitor.AtomicFormulaTranslator;
import at.sti2.rif4j.translator.iris.visitor.RuleTranslator;

/**
 * @author Adrian Marte
 */
public class IrisRifReasoner extends AbstractReasoner {

	private static final Logger logger = LoggerFactory
			.getLogger(IrisRifReasoner.class);

	private boolean hasChanged = true;

	private Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();

	private List<IRule> rules = new ArrayList<IRule>();

	private IKnowledgeBase knowledgeBase;

	@Override
	public void register(Document document) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(document);

		RuleTranslator.mergeFacts(translator.getFacts(), facts);
		rules.addAll(translator.getRules());

		hasChanged = true;
	}

	@Override
	public boolean entails(Formula query) {
		try {
			createKnowledgeBase();
		} catch (EvaluationException e) {
			logger.error("Failed to evaluate knowledge base", e);
			return false;
		}

		try {
			List<IQuery> irisQueries = toQueries(query);

			for (IQuery irisQuery : irisQueries) {
				IRelation relation = knowledgeBase.execute(irisQuery);

				if (relation.size() == 0) {
					return false;
				}

				return true;
			}
		} catch (EvaluationException e) {
			logger.error("Error evaluating knowledge base", e);
		}

		return false;
	}

	@Override
	public boolean query(Formula query) {
		return entails(query);
	}

	private void createKnowledgeBase() throws EvaluationException {
		if (!hasChanged) {
			return;
		}

		List<IRule> newRules = new ArrayList<IRule>();

		// Add the program rules.
		newRules.addAll(this.rules);

		// Add the meta-level rules.
		newRules.addAll(0, AtomicFormulaTranslator.getMetaLevelRules());

		knowledgeBase = toKnowledgeBase(facts, newRules);
		hasChanged = false;
	}

	protected IKnowledgeBase toKnowledgeBase(Map<IPredicate, IRelation> facts,
			List<IRule> rules) throws EvaluationException {
		Configuration configuration = KnowledgeBaseFactory
				.getDefaultConfiguration();

		// Use well-founded evaluation.
		configuration.evaluationStrategyFactory = new WellFoundedEvaluationStrategyFactory();
		configuration.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

		IKnowledgeBase knowledgeBase = KnowledgeBaseFactory
				.createKnowledgeBase(facts, rules, configuration);

		return knowledgeBase;
	}

	protected List<IQuery> toQueries(Formula formula) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(formula);

		List<IQuery> queries = translator.getQueries();

		return queries;
	}

}