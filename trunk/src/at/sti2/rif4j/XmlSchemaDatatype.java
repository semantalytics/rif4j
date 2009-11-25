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
package at.sti2.rif4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the list of XML Schema datatypes.
 * 
 * @author Adrian Marte
 */
public enum XmlSchemaDatatype {

	// Primitive data types.

	STRING(Namespaces.XSD_NAMESPACE + "string"),

	BOOLEAN(Namespaces.XSD_NAMESPACE + "boolean"),

	DECIMAL(Namespaces.XSD_NAMESPACE + "decimal"),

	FLOAT(Namespaces.XSD_NAMESPACE + "float"),

	DOUBLE(Namespaces.XSD_NAMESPACE + "double"),

	DURATION(Namespaces.XSD_NAMESPACE + "duration"),

	DAYTIMEDURATION(Namespaces.XSD_NAMESPACE + "dayTimeDuration"),

	YEARMONTHDURATION(Namespaces.XSD_NAMESPACE + "yearMonthDuration"),

	DATETIME(Namespaces.XSD_NAMESPACE + "dateTime"),
	
	DATETIMESTAMP(Namespaces.XSD_NAMESPACE + "dateTimeStamp"),

	TIME(Namespaces.XSD_NAMESPACE + "time"),

	DATE(Namespaces.XSD_NAMESPACE + "date"),

	GYEARMONTH(Namespaces.XSD_NAMESPACE + "gYearMonth"),

	GYEAR(Namespaces.XSD_NAMESPACE + "gYear"),

	GMONTHDAY(Namespaces.XSD_NAMESPACE + "gMonthDay"),

	GDAY(Namespaces.XSD_NAMESPACE + "gDay"),

	GMONTH(Namespaces.XSD_NAMESPACE + "gMonth"),

	HEXBINARY(Namespaces.XSD_NAMESPACE + "hexBinary"),

	BASE64BINARY(Namespaces.XSD_NAMESPACE + "base64Binary"),

	ANYURI(Namespaces.XSD_NAMESPACE + "anyUri"),

	QNAME(Namespaces.XSD_NAMESPACE + "QName"),

	NOTATION(Namespaces.XSD_NAMESPACE + "NOTATION"),

	// Derived data types.

	NORMALIZED_STRING(Namespaces.XSD_NAMESPACE + "normalizedString"),

	TOKEN(Namespaces.XSD_NAMESPACE + "token"),

	LANGUAGE(Namespaces.XSD_NAMESPACE + "language"),

	NMTOKEN(Namespaces.XSD_NAMESPACE + "NMTOKEN"),

	NMTOKENS(Namespaces.XSD_NAMESPACE + "NMTOKENS"),

	NAME(Namespaces.XSD_NAMESPACE + "Name"),

	NCNAME(Namespaces.XSD_NAMESPACE + "NCName"),

	ID(Namespaces.XSD_NAMESPACE + "ID"),

	IDREF(Namespaces.XSD_NAMESPACE + "IDREF"),

	IDREFS(Namespaces.XSD_NAMESPACE + "IDREFS"),

	ENTITY(Namespaces.XSD_NAMESPACE + "ENTITY"),

	ENTITIES(Namespaces.XSD_NAMESPACE + "ENTITIES"),

	INTEGER(Namespaces.XSD_NAMESPACE + "integer"),

	NON_POSITIVE_INTEGER(Namespaces.XSD_NAMESPACE + "nonPositiveInteger"),

	NEGATIVE_INTEGER(Namespaces.XSD_NAMESPACE + "negativeInteger"),

	LONG(Namespaces.XSD_NAMESPACE + "long"),

	INT(Namespaces.XSD_NAMESPACE + "int"),

	SHORT(Namespaces.XSD_NAMESPACE + "short"),

	BYTE(Namespaces.XSD_NAMESPACE + "byte"),

	NON_NEGATIVE_INTEGER(Namespaces.XSD_NAMESPACE + "nonNegativeInteger"),

	UNSIGNED_LONG(Namespaces.XSD_NAMESPACE + "unsignedLong"),

	UNSIGNED_INT(Namespaces.XSD_NAMESPACE + "unsignedInt"),

	UNSIGNED_SHORT(Namespaces.XSD_NAMESPACE + "unsignedShort"),

	UNSIGNED_BYTE(Namespaces.XSD_NAMESPACE + "unsignedByte"),

	POSITIVE_INTEGER(Namespaces.XSD_NAMESPACE + "positiveInteger");

	private String uri;

	private XmlSchemaDatatype(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return this.uri;
	}

	public static XmlSchemaDatatype forUri(String uri) {
		return Lookup.table.get(uri);
	}

	public static boolean isDatatype(String uri) {
		return Lookup.table.containsKey(uri);
	}

	/**
	 * Maps the URI of each datatype to the datatype itself.
	 */
	private static class Lookup {

		private static final Map<String, XmlSchemaDatatype> table;

		static {
			table = new HashMap<String, XmlSchemaDatatype>();

			for (XmlSchemaDatatype builtin : XmlSchemaDatatype.values()) {
				table.put(builtin.getUri(), builtin);
			}
		}

	}

}
