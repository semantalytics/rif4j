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
package at.sti2.rif4j.condition;

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;

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

	public Constant getOperator() {
		return operator;
	}

	public void setOperator(Constant operator) {
		Assertions.notNull("operator", operator);

		this.operator = operator;
	}

	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		Assertions.notNull("arguments", arguments);

		this.arguments = arguments;
	}

}
