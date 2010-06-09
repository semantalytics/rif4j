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

import at.sti2.rif4j.AbstractDescribable;

/**
 * @author Adrian Marte
 */
public class Constant extends AbstractDescribable implements Term {

	private String type;

	private String language;

	private String text;

	public Constant(String type, String language, String text) {
		if (type == null) {
			type = "";
		}
		this.type = type;
		
		if (language == null) {
			language = "";
		}
		this.language = language;
		
		if (text == null) {
			text = "";
		}
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void accept(TermVisitor visitor) {
		visitor.visit(this);
	}

}
