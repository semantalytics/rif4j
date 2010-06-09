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
package at.sti2.rif4j.rule;

import java.util.ArrayList;
import java.util.List;


import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.condition.Constant;

/**
 * Represents a RIF document. A document consists of a base path, prefixes,
 * imports and a group.
 * 
 * @author Adrian Marte
 * @see Prefix
 * @see Import
 * @see Group
 */
public class Document extends AbstractDescribable {

	private Constant base;

	private List<Prefix> prefixes = new ArrayList<Prefix>();

	private List<Import> imports = new ArrayList<Import>();

	private Group group;

	public Constant getBase() {
		return base;
	}

	public void setBase(Constant base) {
		this.base = base;
	}

	public List<Prefix> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(List<Prefix> prefixes) {
		Assertions.notNull("prefixes", prefixes);

		this.prefixes = prefixes;
	}

	public List<Import> getImports() {
		return imports;
	}

	public void setImports(List<Import> imports) {
		Assertions.notNull("prefixes", prefixes);

		this.imports = imports;
	}

	/**
	 * Returns the group of this document.
	 * 
	 * @return The group of this document.
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets the group of this document.
	 * 
	 * @param group The group to set for this document.
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

}
