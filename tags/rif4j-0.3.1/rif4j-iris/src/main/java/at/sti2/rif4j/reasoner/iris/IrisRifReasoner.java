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
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.importer.Profile;
import at.sti2.rif4j.manager.DocumentManager;
import at.sti2.rif4j.reasoner.AbstractReasoner;
import at.sti2.rif4j.reasoner.ReasoningException;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;
import at.sti2.rif4j.translator.iris.visitor.AtomicFormulaTranslator;
import at.sti2.rif4j.translator.iris.visitor.RuleTranslator;

/**
 * @author Adrian Marte
 */
public class IrisRifReasoner extends AbstractReasoner {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Profile highestProfile;

	private boolean hasChanged = true;

	private Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();

	private List<IRule> rules = new ArrayList<IRule>();

	private IKnowledgeBase knowledgeBase;

	@Override
	public void register(Document document) {
		if (document.getProfile() != null) {
			if (highestProfile == null
					|| highestProfile.ordinal() < document.getProfile()
							.ordinal()) {
				highestProfile = document.getProfile();
			}
		}

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

		// If the document manager supports the RDF profiles, we add the RDF
		// Simple Entailment axioms.
		if (highestProfile != null
				&& highestProfile.ordinal() <= Profile.RDFS.ordinal()
				&& (DocumentManager.supports(Profile.SIMPLE.toUri())
						|| DocumentManager.supports(Profile.RDF.toUri()) || DocumentManager
						.supports(Profile.RDFS.toUri()))) {
			newRules.addAll(RdfSimpleAxioms.getRules());
		}

		knowledgeBase = toKnowledgeBase(facts, newRules);
		hasChanged = false;
	}

	protected IKnowledgeBase toKnowledgeBase(Map<IPredicate, IRelation> facts,
			List<IRule> rules) throws EvaluationException {
		Configuration configuration = KnowledgeBaseFactory
				.getDefaultConfiguration();

		// Use an augmenting rule safety processor.
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
