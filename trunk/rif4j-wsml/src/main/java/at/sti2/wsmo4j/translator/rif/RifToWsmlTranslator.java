/*
 * Copyright (c) 2009, STI Innsbruck
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.wsmo4j.translator.rif;

import java.util.ArrayList;
import java.util.List;

import org.omwg.logicalexpression.LogicalExpression;
import org.sti2.wsmo4j.factory.WsmlFactoryContainer;
import org.wsmo.factory.FactoryContainer;

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

		if (transformer.getExpressions() != null) {
			wsmlRules.addAll(transformer.getExpressions());
		}

		return wsmlRules;
	}

}
