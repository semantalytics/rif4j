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
	 * @param operator The operator of the uniterm.
	 * @param arguments The arguments of the uniterm.
	 */
	public SimpleUniterm(Constant operator, List<Argument> arguments) {
		this.operator = operator;
		this.arguments = arguments;
	}

	public Constant getOperator() {
		return operator;
	}

	public void setOperator(Constant operator) {
		this.operator = operator;
	}

	public List<Argument> getArguments() {
		return arguments;
	}

	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

}
