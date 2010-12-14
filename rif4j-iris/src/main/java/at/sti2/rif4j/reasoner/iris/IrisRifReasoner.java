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

import org.deri.iris.EvaluationException;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.storage.IRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.reasoner.AbstractRifReasoner;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;

/**
 * @author Adrian Marte
 */
public class IrisRifReasoner extends AbstractRifReasoner {

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

	private IKnowledgeBase toKnowledgeBase(Document document)
			throws EvaluationException {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(document);

		IKnowledgeBase knowledgeBase = translator.getKnowledgeBase();
		return knowledgeBase;
	}

	private List<IQuery> toQueries(Formula formula) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(formula);

		List<IQuery> queries = translator.getQueries();

		return queries;
	}

}
