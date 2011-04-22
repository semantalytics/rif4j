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

import java.util.List;
import java.util.Map;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.RdbKnowledgeBase;
import org.deri.iris.storage.IRelation;

import at.sti2.rif4j.reasoner.iris.IrisRifReasoner;

/**
 * @author Adrian Marte
 */
public class IrisRdbRifReasoner extends IrisRifReasoner {

	private RdbKnowledgeBase knowledgeBase;

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

	public void dispose() {
		if (knowledgeBase != null) {
			knowledgeBase.dispose();
		}
	}

}
