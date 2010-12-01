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

	STRING("string"),

	BOOLEAN("boolean"),

	DECIMAL("decimal"),

	FLOAT("float"),

	DOUBLE("double"),

	DURATION("duration"),

	DAYTIMEDURATION("dayTimeDuration"),

	YEARMONTHDURATION("yearMonthDuration"),

	DATETIME("dateTime"),

	DATETIMESTAMP("dateTimeStamp"),

	TIME("time"),

	DATE("date"),

	GYEARMONTH("gYearMonth"),

	GYEAR("gYear"),

	GMONTHDAY("gMonthDay"),

	GDAY("gDay"),

	GMONTH("gMonth"),

	HEXBINARY("hexBinary"),

	BASE64BINARY("base64Binary"),

	ANYURI("anyUri"),

	QNAME("QName"),

	NOTATION("NOTATION"),

	// Derived data types.

	NORMALIZED_STRING("normalizedString"),

	TOKEN("token"),

	LANGUAGE("language"),

	NMTOKEN("NMTOKEN"),

	NMTOKENS("NMTOKENS"),

	NAME("Name"),

	NCNAME("NCName"),

	ID("ID"),

	IDREF("IDREF"),

	IDREFS("IDREFS"),

	ENTITY("ENTITY"),

	ENTITIES("ENTITIES"),

	INTEGER("integer"),

	NON_POSITIVE_INTEGER("nonPositiveInteger"),

	NEGATIVE_INTEGER("negativeInteger"),

	LONG("long"),

	INT("int"),

	SHORT("short"),

	BYTE("byte"),

	NON_NEGATIVE_INTEGER("nonNegativeInteger"),

	UNSIGNED_LONG("unsignedLong"),

	UNSIGNED_INT("unsignedInt"),

	UNSIGNED_SHORT("unsignedShort"),

	UNSIGNED_BYTE("unsignedByte"),

	POSITIVE_INTEGER("positiveInteger");

	private String uri;

	private XmlSchemaDatatype(String name) {
		this(Namespaces.XSD_NAMESPACE, name);
	}

	private XmlSchemaDatatype(String namespace, String name) {
		this.uri = namespace + name;
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
