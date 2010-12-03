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

import at.sti2.rif4j.reasoner.AbstractRifReasoner;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;

/**
 * @author Adrian Marte
 */
public class IrisRifReasoner extends AbstractRifReasoner {

	@Override
	public boolean entails(Document phi, Rule psi) {
		try {
			IKnowledgeBase irisKnowledgeBase = toKnowledgeBase(phi);
			List<IQuery> irisQueries = toQueries(psi);

			for (IQuery irisQuery : irisQueries) {
				IRelation relation = irisKnowledgeBase.execute(irisQuery);

				if (relation.size() == 0) {
					return false;
				}
			}
		} catch (EvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean query(Document document, Rule query) {
		return entails(document, query);
	}

	private IKnowledgeBase toKnowledgeBase(Document document)
			throws EvaluationException {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(document);

		IKnowledgeBase knowledgeBase = translator.getKnowledgeBase();
		return knowledgeBase;
	}

	private List<IQuery> toQueries(Rule rule) {
		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(rule);

		List<IQuery> queries = translator.getQueries();
		return queries;
	}

}
