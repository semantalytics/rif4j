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
package at.sti2.rif4j.reasoner.iris.rdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.RdbKnowledgeBase;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.reasoner.AbstractReasoner;
import at.sti2.rif4j.reasoner.ReasoningException;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;
import at.sti2.rif4j.translator.iris.visitor.AtomicFormulaTranslator;
import at.sti2.rif4j.translator.iris.visitor.RuleTranslator;

/**
 * @author Adrian Marte
 */
public class IrisRdbRifReasoner extends AbstractReasoner {

	private static final Logger logger = LoggerFactory
			.getLogger(IrisRdbRifReasoner.class);

	private boolean hasChanged = true;

	private Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();

	private List<IRule> rules = new ArrayList<IRule>();

	private RdbKnowledgeBase knowledgeBase;

	@Override
	public void register(Document document) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(document);

		RuleTranslator.mergeFacts(translator.getFacts(), facts);
		rules.addAll(translator.getRules());

		hasChanged = true;
	}

	@Override
	public boolean entails(Formula query) throws ReasoningException {
		if (logger.isDebugEnabled()) {
			for (IRule rule : rules) {
				logger.debug(rule.toString());
			}

			logger.debug(facts.toString());
		}

		try {
			createKnowledgeBase();
		} catch (EvaluationException e) {
			logger.error("Failed to evaluate knowledge base", e);
			throw new ReasoningException(e);
		}

		try {
			List<IQuery> irisQueries = toQueries(query);

			for (IQuery irisQuery : irisQueries) {
				logger.debug(irisQuery.toString());

				IRelation relation = knowledgeBase.execute(irisQuery);
				logger.debug(relation.toString());

				if (relation.size() == 0) {
					return false;
				}

				return true;
			}
		} catch (EvaluationException e) {
			logger.error("Error evaluating knowledge base", e);
			throw new ReasoningException(e);
		}

		return false;
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

	protected RdbKnowledgeBase toKnowledgeBase(
			Map<IPredicate, IRelation> rawFacts, List<IRule> rules)
			throws EvaluationException {
		Configuration configuration = KnowledgeBaseFactory
				.getDefaultConfiguration();

		IFacts facts = new Facts(rawFacts, configuration.relationFactory);
		RdbKnowledgeBase knowledgeBase;
		try {
			knowledgeBase = new RdbKnowledgeBase(facts, rules, configuration);
		} catch (Exception e) {
			throw new EvaluationException(e.getMessage());
		}

		return knowledgeBase;
	}

	protected List<IQuery> toQueries(Formula formula) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(formula);

		List<IQuery> queries = translator.getQueries();

		return queries;
	}

	public void dispose() {
		if (knowledgeBase != null) {
			knowledgeBase.dispose();
		}
	}

}