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

import at.sti2.rif4j.Assertions;

/**
 * This class represents (named) arguments for Atoms and Expressions. An
 * argument consists of an optional name and a term value.
 * 
 * @author Adrian Marte
 * @see Atom
 * @see Expression
 */
public class Argument {

	private String name;

	private Term value;

	/**
	 * Creates a new unnamed argument with the specified term value.
	 * 
	 * @param value
	 *            The term value of the argument.
	 * @throws NullPointerException
	 *             If the term value is <code>null</code>.
	 */
	public Argument(Term value) {
		this(null, value);
	}

	/**
	 * Creates a new named argument with the specified name and term value. If
	 * name is
	 * <code>null</null> the argument is interpreted as an unnamed argument.
	 * 
	 * @param name
	 *            The name of the argument.
	 * @param value
	 *            The term value of the argument.
	 * @throws NullPointerException
	 *             If the term value is <code>null</code>.
	 */
	public Argument(String name, Term value) {
		Assertions.notNull("value", value);

		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name of this argument, or <code>null</code> if this is not a
	 * named argument.
	 * 
	 * @return The name of this argument, or <code>null</code> if this is not a
	 *         named argument.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this argument. If a <code>null</code> value is provided,
	 * this argument becomes an unnamed argument.
	 * 
	 * @param name
	 *            The name of this argument.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the term value of this argument.
	 * 
	 * @return The term value of this argument.
	 */
	public Term getValue() {
		return value;
	}

	/**
	 * Sets the term value of this argument.
	 * 
	 * @param value
	 *            The term value of this argument.
	 * @throws NullPointerException
	 *             If the term value is <code>null</code>.
	 */
	public void setValue(Term value) {
		Assertions.notNull("value", value);

		this.value = value;
	}

}
