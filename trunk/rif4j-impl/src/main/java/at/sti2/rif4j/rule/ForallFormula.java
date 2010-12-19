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
package at.sti2.rif4j.rule;

import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.condition.Variable;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
public class ForallFormula extends AbstractDescribable implements Rule {

	private List<Variable> variables;

	private Clause clause;

	public ForallFormula(List<Variable> variables, Clause clause) {
		Assertions.notNull("variables", variables);
		Assertions.notNull("clause", clause);

		this.variables = variables;
		this.clause = clause;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		Assertions.notNull("variables", variables);

		this.variables = variables;
	}

	public Clause getClause() {
		return clause;
	}

	public void setClause(Clause clause) {
		Assertions.notNull("clause", clause);

		this.clause = clause;
	}

	@Override
	public void accept(RuleVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept(serializer);
		return serializer.getString();
	}
	
}
