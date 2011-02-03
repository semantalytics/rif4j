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

/**
 * @author Adrian Marte
 */
public class IrisRifReasoner extends AbstractReasoner {

	private static final Logger logger = LoggerFactory
			.getLogger(IrisRifReasoner.class);

	@Override
	public boolean entails(Document phi, Formula psi) {
		try {
			IKnowledgeBase irisKnowledgeBase = toKnowledgeBase(phi);
			List<IQuery> irisQueries = toQueries(psi);

			for (IQuery irisQuery : irisQueries) {
				IRelation relation = irisKnowledgeBase.execute(irisQuery);

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
	public boolean query(Document document, Formula query) {
		return entails(document, query);
	}

	protected IKnowledgeBase toKnowledgeBase(Document document)
			throws EvaluationException {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(document);

		Map<IPredicate, IRelation> facts = translator.getFacts();
		List<IRule> rules = translator.getRules();

		// Add the meta-level rules.
		rules.addAll(0, AtomicFormulaTranslator.getMetaLevelRules());

		Configuration configuration = KnowledgeBaseFactory
				.getDefaultConfiguration();

		// Use well-founded evaluation.
		configuration.evaluationStrategyFactory = new WellFoundedEvaluationStrategyFactory();
		configuration.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

		IKnowledgeBase irisKnowledgeBase = KnowledgeBaseFactory
				.createKnowledgeBase(facts, rules, configuration);

		return irisKnowledgeBase;
	}

	protected List<IQuery> toQueries(Formula formula) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(formula);

		List<IQuery> queries = translator.getQueries();

		return queries;
	}

}
