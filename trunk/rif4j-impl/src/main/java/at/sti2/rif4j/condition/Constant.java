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

import java.math.BigDecimal;
import java.math.BigInteger;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.RdfDatatype;
import at.sti2.rif4j.RifDatatype;
import at.sti2.rif4j.XmlSchemaDatatype;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
public class Constant extends AbstractDescribable implements Term {

	private String type;

	private String language;

	private String text;

	public Constant(String type, String language, String text) {
		this.text = text;
		this.language = language;
		this.type = type;

		this.type = determineType();
	}

	private String determineType() {
		if (text == null) {
			text = "";
		}

		if (type != null && !type.isEmpty()) {
			return type;
		}

		if (!text.isEmpty()) {
			if (language == null || language.isEmpty()) {
				int index = text.indexOf("@");

				if (index > 0) {
					text = text.substring(0, index);

					if ((index + 1) < text.length()) {
						language = text.substring(index + 1);
					}

					return RdfDatatype.PLAIN_LITERAL.getUri();
				}
			} else {
				return RdfDatatype.PLAIN_LITERAL.getUri();
			}

			try {
				new BigInteger(text);
				return XmlSchemaDatatype.INTEGER.getUri();
			} catch (NumberFormatException e) {
				// Ignore.
			}

			try {
				Double.valueOf(text);
				return XmlSchemaDatatype.DOUBLE.getUri();
			} catch (NumberFormatException e) {
				// Ignore.
			}

			try {
				new BigDecimal(text);
				return XmlSchemaDatatype.DECIMAL.getUri();
			} catch (NumberFormatException e) {
				// Ignore.
			}

			if (text.startsWith("_")) {
				return RifDatatype.LOCAL.getUri();
			}
		}

		return XmlSchemaDatatype.STRING.getUri();
	}

	/**
	 * Returns the datatype URI of the RIF constant, e.g.
	 * http://www.w3.org/2001/XMLSchema#double.
	 * 
	 * @return The datatype URI of the RIF constant.
	 */
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

	/**
	 * Returns the value of the RIF constant in canonical string representation,
	 * e.g. 0.1337 for a xsd:double.
	 * 
	 * @return The value of the RIF constant in canonical string representation
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void accept(TermVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept(serializer);
		return serializer.getString();
	}

}
