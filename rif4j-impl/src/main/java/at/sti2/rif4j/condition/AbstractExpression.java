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
package at.sti2.rif4j.condition;

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
abstract class AbstractExpression extends AbstractDescribable implements Term,
		Uniterm {

	private Constant operator;

	private List<Argument> arguments;

	public AbstractExpression(Constant operator) {
		this(operator, new ArrayList<Argument>());
	}

	public AbstractExpression(Constant operator, List<Argument> arguments) {
		Assertions.notNull("operator", operator);
		Assertions.notNull("arguments", arguments);

		this.operator = operator;
		this.arguments = arguments;
	}

	@Override
	public Constant getOperator() {
		return operator;
	}

	@Override
	public void setOperator(Constant operator) {
		Assertions.notNull("operator", operator);

		this.operator = operator;
	}

	@Override
	public List<Argument> getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(List<Argument> arguments) {
		Assertions.notNull("arguments", arguments);

		this.arguments = arguments;
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept(serializer);
		return serializer.getString();
	}

}
