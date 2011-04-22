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
package at.sti2.rif4j.parser.xml;

import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.condition.Argument;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Uniterm;

/**
 * A simple implementation of Uniterm. Note that Uniterm is not a real RIF
 * element. This class has been introduced in order to reduce redundancy when
 * parsing Atoms and Expressions.
 * 
 * @author Adrian Marte
 */
class SimpleUniterm extends AbstractDescribable implements Uniterm {

	private Constant operator;

	private List<Argument> arguments;

	/**
	 * Creates a new uniterm with the specified operator and arguments.
	 * 
	 * @param operator
	 *            The operator of the uniterm.
	 * @param arguments
	 *            The arguments of the uniterm.
	 */
	public SimpleUniterm(Constant operator, List<Argument> arguments) {
		this.operator = operator;
		this.arguments = arguments;
	}

	@Override
	public Constant getOperator() {
		return operator;
	}

	@Override
	public void setOperator(Constant operator) {
		this.operator = operator;
	}

	@Override
	public List<Argument> getArguments() {
		return arguments;
	}

	@Override
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

}
