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
package at.sti2.wsmo4j.translator.rif;

import java.util.ArrayList;
import java.util.List;

import org.omwg.logicalexpression.LogicalExpression;
import org.sti2.wsmo4j.factory.WsmlFactoryContainer;
import org.wsmo.factory.FactoryContainer;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Rule;

/**
 * @author Adrian Marte
 */
public class RifToWsmlTranslator {

	private FactoryContainer factories;

	public RifToWsmlTranslator() {
		factories = new WsmlFactoryContainer();
	}

	public List<LogicalExpression> translate(Document document) {
		Group group = document.getGroup();
		return translate(group);
	}

	public List<LogicalExpression> translate(Group group) {
		List<LogicalExpression> wsmlRules = new ArrayList<LogicalExpression>();

		for (Rule rule : group.getAllRules()) {
			wsmlRules.addAll(translate(rule));
		}

		return wsmlRules;
	}

	public List<LogicalExpression> translate(Rule rule) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);
		rule.accept(transformer);

		List<LogicalExpression> wsmlRules = new ArrayList<LogicalExpression>();

		if (transformer.getExpression() != null) {
			wsmlRules.add(transformer.getExpression());
		}

		return wsmlRules;
	}
	
	public List<LogicalExpression> translate(Formula formula) {
		RifEntityTransformer transformer = new RifEntityTransformer(factories);
		formula.accept(transformer);

		List<LogicalExpression> wsmlRules = new ArrayList<LogicalExpression>();

		// TODO Check if terms are also needed.

		if (transformer.getExpression() != null) {
			wsmlRules.add(transformer.getExpression());
		}

		return wsmlRules;
	}

}
