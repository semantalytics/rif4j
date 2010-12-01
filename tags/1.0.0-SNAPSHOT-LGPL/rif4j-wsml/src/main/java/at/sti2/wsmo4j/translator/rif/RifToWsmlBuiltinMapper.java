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
package at.sti2.wsmo4j.translator.rif;

import java.util.HashMap;
import java.util.Map;

import org.wsmo.common.BuiltIn;

import at.sti2.rif4j.RdfDatatype;
import at.sti2.rif4j.XmlSchemaDatatype;

/**
 * @author Adrian Marte
 */
public class RifToWsmlBuiltinMapper {

	private static Map<String, String> wsmlToRif;

	private static Map<String, String> rifToWsml;

	public RifToWsmlBuiltinMapper() {
	}

	static {
		wsmlToRif = new HashMap<String, String>();
		rifToWsml = new HashMap<String, String>();

		// Establish mappings according to D3.1.4, section 3.2.2.1.2. Updated
		// names of RIF built-ins according to current version of RIF DTB (1.0
		// as of October 21st, 2009).

		// Obsolete since WSMO4J does directly use the RIF Built-in IRIs.
		//
		// addMapping(BuiltIn.STRING_COMPARE, RifBuiltIn.COMPARE);
		// addMapping(BuiltIn.STRING_CONCAT, RifBuiltIn.CONCAT);
		// addMapping(BuiltIn.STRING_JOIN, RifBuiltIn.STRING_JOIN);
		// addMapping(BuiltIn.STRING_SUBSTRING, RifBuiltIn.SUBSTRING);
		// addMapping(BuiltIn.STRING_LENGTH, RifBuiltIn.STRING_LENGTH);
		// addMapping(BuiltIn.STRING_TO_UPPER, RifBuiltIn.UPPER_CASE);
		// addMapping(BuiltIn.STRING_TO_LOWER, RifBuiltIn.LOWER_CASE);
		// addMapping(BuiltIn.STRING_URI_ENCODE, RifBuiltIn.ENCODE_FOR_URI);
		// addMapping(BuiltIn.STRING_IRI_TO_URI, RifBuiltIn.IRI_TO_URI);
		// addMapping(BuiltIn.STRING_ESCAPE_HTML_URI,
		// RifBuiltIn.ESCAPE_HTML_URI);
		// addMapping(BuiltIn.STRING_SUBSTRING_BEFORE,
		// RifBuiltIn.SUBSTRING_BEFORE);
		// addMapping(BuiltIn.STRING_SUBSTRING_AFTER,
		// RifBuiltIn.SUBSTRING_AFTER);
		// addMapping(BuiltIn.STRING_REPLACE, RifBuiltIn.REPLACE);
		// addMapping(BuiltIn.STRING_CONTAINS, RifBuiltIn.CONTAINS);
		// addMapping(BuiltIn.STRING_STARTS_WITH, RifBuiltIn.STARTS_WITH);
		// addMapping(BuiltIn.STRING_ENDS_WITH, RifBuiltIn.ENDS_WITH);
		// addMapping(BuiltIn.STRING_MATCHES, RifBuiltIn.MATCHES);
		// addMapping(BuiltIn.YEAR_PART, RifBuiltIn.YEAR_FROM_DATETIME);
		// addMapping(BuiltIn.YEAR_PART, RifBuiltIn.YEAR_FROM_DATE);
		// addMapping(BuiltIn.YEAR_PART, RifBuiltIn.YEARS_FROM_DURATION);
		// addMapping(BuiltIn.MONTH_PART, RifBuiltIn.MONTH_FROM_DATETIME);
		// addMapping(BuiltIn.MONTH_PART, RifBuiltIn.MONTH_FROM_DATE);
		// addMapping(BuiltIn.MONTH_PART, RifBuiltIn.MONTHS_FROM_DURATION);
		// addMapping(BuiltIn.DAY_PART, RifBuiltIn.DAY_FROM_DATETIME);
		// addMapping(BuiltIn.DAY_PART, RifBuiltIn.DAY_FROM_DATE);
		// addMapping(BuiltIn.DAY_PART, RifBuiltIn.DAYS_FROM_DURATION);
		// addMapping(BuiltIn.HOUR_PART, RifBuiltIn.HOURS_FROM_DATETIME);
		// addMapping(BuiltIn.HOUR_PART, RifBuiltIn.HOURS_FROM_DURATION);
		// addMapping(BuiltIn.HOUR_PART, RifBuiltIn.HOURS_FROM_TIME);
		// addMapping(BuiltIn.MINUTE_PART, RifBuiltIn.MINUTES_FROM_DATETIME);
		// addMapping(BuiltIn.MINUTE_PART, RifBuiltIn.MINUTES_FROM_DURATION);
		// addMapping(BuiltIn.MINUTE_PART, RifBuiltIn.MINUTES_FROM_TIME);
		// addMapping(BuiltIn.SECOND_PART, RifBuiltIn.SECONDS_FROM_DATETIME);
		// addMapping(BuiltIn.SECOND_PART, RifBuiltIn.SECONDS_FROM_DURATION);
		// addMapping(BuiltIn.SECOND_PART, RifBuiltIn.SECONDS_FROM_TIME);
		// addMapping(BuiltIn.TIMEZONE_PART, RifBuiltIn.TIMEZONE_FROM_DATETIME);
		// addMapping(BuiltIn.TIMEZONE_PART, RifBuiltIn.TIMEZONE_FROM_DATE);
		// addMapping(BuiltIn.TIMEZONE_PART, RifBuiltIn.TIMEZONE_FROM_TIME);
		//
		// addMapping(BuiltIn.STRING_FROM_TEXT,
		// RifBuiltIn.STRING_FROM_PLAINLITERAL);
		// addMapping(BuiltIn.LANG_FROM_TEXT,
		// RifBuiltIn.LANG_FROM_PLAINLITERAL);
		// addMapping(BuiltIn.TEXT_COMPARE, RifBuiltIn.PLAINLITERAL_COMPARE);
		// addMapping(BuiltIn.TEXT_LENGTH, RifBuiltIn.PLAINLITERAL_LENGTH);
		// addMapping(BuiltIn.TEXT_FROM_STRING_LANG,
		// RifBuiltIn.PLAINLITERAL_FROM_STRING_LANG);
		//
		// addMapping(BuiltIn.NUMERIC_MODULUS, RifBuiltIn.NUMERIC_MOD);

		// addMapping(BuiltIn.NUMERIC_GREATER_THAN,
		// RifBuiltIn.NUMERIC_GREATER_THAN);
		// addMapping(BuiltIn.NUMERIC_LESS_THAN, RifBuiltIn.NUMERIC_LESS_THAN);
		// addMapping(BuiltIn.GREATER_EQUAL,
		// RifBuiltIn.NUMERIC_GREATER_THAN_OR_EQUAL);
		// addMapping(BuiltIn.LESS_EQUAL,
		// RifBuiltIn.NUMERIC_LESS_THAN_OR_EQUAL);
		// addMapping(BuiltIn.NUMERIC_ADD, RifBuiltIn.NUMERIC_ADD);
		// addMapping(BuiltIn.NUMERIC_SUBTRACT, RifBuiltIn.NUMERIC_SUBTRACT);
		// addMapping(BuiltIn.NUMERIC_MULTIPLY, RifBuiltIn.NUMERIC_MULTIPLY);
		// addMapping(BuiltIn.NUMERIC_DIVIDE, RifBuiltIn.NUMERIC_DIVIDE);
		// addMapping(BuiltIn.NUMERIC_EQUAL, RifBuiltIn.NUMERIC_EQUAL);
		// addMapping(BuiltIn.NUMERIC_INEQUAL, RifBuiltIn.NUMERIC_NOT_EQUAL);
		// addMapping(BuiltIn.DATETIME_INEQUAL, RifBuiltIn.DATETIME_NOT_EQUAL);
		// addMapping(BuiltIn.DATETIME_EQUAL, RifBuiltIn.DATETIME_EQUAL);
		// addMapping(BuiltIn.DATETIME_LESS_THAN,
		// RifBuiltIn.DATETIME_LESS_THAN);
		// addMapping(BuiltIn.DATETIME_GREATER_THAN,
		// RifBuiltIn.DATETIME_GREATER_THAN);
		// addMapping(BuiltIn.DATE_INEQUAL, RifBuiltIn.DATE_NOT_EQUAL);
		// addMapping(BuiltIn.DATE_EQUAL, RifBuiltIn.DATE_EQUAL);
		// addMapping(BuiltIn.DATE_LESS_THAN, RifBuiltIn.DATE_LESS_THAN);
		// addMapping(BuiltIn.DATE_GREATER_THAN, RifBuiltIn.DATE_GREATER_THAN);
		// addMapping(BuiltIn.TIME_INEQUAL, RifBuiltIn.TIME_NOT_EQUAL);
		// addMapping(BuiltIn.TIME_EQUAL, RifBuiltIn.TIME_EQUAL);
		// addMapping(BuiltIn.TIME_LESS_THAN, RifBuiltIn.TIME_LESS_THAN);
		// addMapping(BuiltIn.TIME_GREATER_THAN, RifBuiltIn.TIME_GREATER_THAN);
		// addMapping(BuiltIn.DURATION_EQUAL, RifBuiltIn.DURATION_EQUAL);
		// addMapping(BuiltIn.DURATION_INEQUAL, RifBuiltIn.DURATION_NOT_EQUAL);
		// addMapping(BuiltIn.DAYTIMEDURATION_LESS_THAN,
		// RifBuiltIn.DAYTIMEDURATION_LESS_THAN);
		// addMapping(BuiltIn.DAYTIMEDURATION_GREATER_THAN,
		// RifBuiltIn.DAYTIMEDURATION_GREATER_THAN);
		// addMapping(BuiltIn.YEARMONTHDURATION_LESS_THAN,
		// RifBuiltIn.YEARMONTHDURATION_LESS_THAN);
		// addMapping(BuiltIn.YEARMONTHDURATION_GREATER_THAN,
		// RifBuiltIn.YEARMONTHDURATION_GREATER_THAN);
		// addMapping(BuiltIn.LESS_EQUAL,
		// RifBuiltIn.DATETIME_LESS_THAN_OR_EQUAL);
		// addMapping(BuiltIn.GREATER_EQUAL,
		// RifBuiltIn.DATETIME_GREATER_THAN_OR_EQUAL);
		// addMapping(BuiltIn.LESS_EQUAL, RifBuiltIn.DATE_LESS_THAN_OR_EQUAL);
		// addMapping(BuiltIn.GREATER_EQUAL,
		// RifBuiltIn.DATE_GREATER_THAN_OR_EQUAL);
		// addMapping(BuiltIn.LESS_EQUAL, RifBuiltIn.TIME_LESS_THAN_OR_EQUAL);
		// addMapping(BuiltIn.GREATER_EQUAL,
		// RifBuiltIn.TIME_GREATER_THAN_OR_EQUAL);
		// addMapping(BuiltIn.LESS_EQUAL,
		// RifBuiltIn.DAYTIMEDURATION_LESS_THAN_OR_EQUAL);
		// addMapping(BuiltIn.GREATER_EQUAL,
		// RifBuiltIn.DAYTIMEDURATION_GREATER_THAN_OR_EQUAL);
		// addMapping(BuiltIn.LESS_EQUAL,
		// RifBuiltIn.YEARMONTHDURATION_LESS_THAN_OR_EQUAL);
		// addMapping(BuiltIn.GREATER_EQUAL,
		// RifBuiltIn.YEARMONTHDURATION_GREATER_THAN_OR_EQUAL);
		//
		// addMapping(BuiltIn.EQUAL, RifBuiltIn.XMLLITERAL_EQUAL);
		// addMapping(BuiltIn.INEQUAL, RifBuiltIn.XMLLITERAL_NOT_EQUAL);
		//
		// addMapping(BuiltIn.EQUAL, RifBuiltIn.BOOLEAN_EQUAL);

		// FIXME Complete the list of data type cast mappings.
		// Establish data type cast mappings.

		addMapping(null, XmlSchemaDatatype.ANYURI);
		addMapping(BuiltIn.TO_BASE64, XmlSchemaDatatype.BASE64BINARY);
		addMapping(BuiltIn.TO_BOOLEAN, XmlSchemaDatatype.BOOLEAN);
		addMapping(BuiltIn.TO_DATE, XmlSchemaDatatype.DATE);
		addMapping(BuiltIn.TO_DATETIME, XmlSchemaDatatype.DATETIME);
		addMapping(null, XmlSchemaDatatype.DATETIMESTAMP);
		addMapping(BuiltIn.TO_DOUBLE, XmlSchemaDatatype.DOUBLE);
		addMapping(BuiltIn.TO_FLOAT, XmlSchemaDatatype.FLOAT);
		addMapping(BuiltIn.TO_HEXBINARY, XmlSchemaDatatype.HEXBINARY);
		addMapping(BuiltIn.TO_DECIMAL, XmlSchemaDatatype.DECIMAL);
		addMapping(BuiltIn.TO_INTEGER, XmlSchemaDatatype.INTEGER);
		addMapping(null, XmlSchemaDatatype.LONG);
		addMapping(BuiltIn.TO_INTEGER, XmlSchemaDatatype.INT);
		addMapping(null, XmlSchemaDatatype.SHORT);
		addMapping(null, XmlSchemaDatatype.BYTE);
		addMapping(null, XmlSchemaDatatype.NON_NEGATIVE_INTEGER);
		addMapping(null, XmlSchemaDatatype.POSITIVE_INTEGER);
		addMapping(null, XmlSchemaDatatype.UNSIGNED_LONG);
		addMapping(null, XmlSchemaDatatype.UNSIGNED_INT);
		addMapping(null, XmlSchemaDatatype.UNSIGNED_SHORT);
		addMapping(null, XmlSchemaDatatype.UNSIGNED_BYTE);
		addMapping(null, XmlSchemaDatatype.NON_POSITIVE_INTEGER);
		addMapping(null, XmlSchemaDatatype.NEGATIVE_INTEGER);
		addMapping(BuiltIn.TO_STRING, XmlSchemaDatatype.STRING);
		addMapping(null, XmlSchemaDatatype.NORMALIZED_STRING);
		addMapping(null, XmlSchemaDatatype.TOKEN);
		addMapping(null, XmlSchemaDatatype.LANGUAGE);
		addMapping(null, XmlSchemaDatatype.NAME);
		addMapping(null, XmlSchemaDatatype.NCNAME);
		addMapping(null, XmlSchemaDatatype.NMTOKEN);
		addMapping(BuiltIn.TO_TIME, XmlSchemaDatatype.TIME);
		addMapping(BuiltIn.TO_DAYTIMEDURATION,
				XmlSchemaDatatype.DAYTIMEDURATION);
		addMapping(BuiltIn.TO_YEARMONTHDURATION,
				XmlSchemaDatatype.YEARMONTHDURATION);
		addMapping(BuiltIn.TO_TEXT, RdfDatatype.PLAIN_LITERAL);
		addMapping(BuiltIn.TO_XMLLITERAL, RdfDatatype.XML_LITERAL);

		// "Optional" datatype casts.

		addMapping(BuiltIn.TO_DURATION, XmlSchemaDatatype.DURATION);
		addMapping(BuiltIn.TO_GDAY, XmlSchemaDatatype.GDAY);
		addMapping(BuiltIn.TO_GMONTH, XmlSchemaDatatype.GMONTH);
		addMapping(BuiltIn.TO_GMONTHDAY, XmlSchemaDatatype.GMONTHDAY);
		addMapping(BuiltIn.TO_GYEAR, XmlSchemaDatatype.GYEAR);
		addMapping(BuiltIn.TO_GYEARMONTH, XmlSchemaDatatype.GYEARMONTH);

		// FIXME What are the guard predicates?
		// Guard predicates for datatypes.
	}

	private static void addMapping(BuiltIn wsmlBuiltin, RdfDatatype rdfDatatype) {
		addMapping(wsmlBuiltin.getFullName(), rdfDatatype.getUri());
	}

	private static void addMapping(BuiltIn wsmlBuiltin,
			XmlSchemaDatatype xsdDatatype) {
		if (wsmlBuiltin != null && xsdDatatype != null) {
			addMapping(wsmlBuiltin.getFullName(), xsdDatatype.getUri());
		}
	}

	// private static void addMapping(BuiltIn wsmlBuiltin, RifBuiltIn
	// rifBuiltin) {
	// addMapping(wsmlBuiltin.getFullName(), rifBuiltin.getUri());
	// }

	private static void addMapping(String wsmlBuiltin, String rifBuiltin) {
		wsmlToRif.put(wsmlBuiltin, rifBuiltin);
		rifToWsml.put(rifBuiltin, wsmlBuiltin);
	}

	public String toWsmlBuiltin(String rifBuiltinIri) {
		return rifToWsml.get(rifBuiltinIri);
	}

	public String toRifBuiltin(String wsmlBuiltinIri) {
		return wsmlToRif.get(wsmlBuiltinIri);
	}

}
