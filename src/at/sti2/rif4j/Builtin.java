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
 * Defines the list of built-ins from RIF DTB 1.0, except the built-ins for
 * datatype conversion and casting, which are basically identified by the XML
 * Schema and RDF datatype URIs defined in {@link XmlSchemaDatatype} and
 * {@link RdfDatatype}.
 * 
 * @author Adrian Marte
 * @see XmlSchemaDatatype
 * @see RdfDatatype
 */
public enum Builtin {

	// Comparison for literals.

	LITERAL_NOT_IDENTICAL(Namespaces.RIF_PRED, "literal-not-identical"),

	// Guard predicates for datatypes.

	IS_LITERAL_ANYURI(Namespaces.RIF_PRED, "is-literal-anyURI"),

	IS_LITERAL_BASE64BINARY(Namespaces.RIF_PRED, "is-literal-base64Binary"),

	IS_LITERAL_BOOLEAN(Namespaces.RIF_PRED, "is-literal-boolean"),

	IS_LITERAL_DATE(Namespaces.RIF_PRED, "is-literal-date"),

	IS_LITERAL_DATETIME(Namespaces.RIF_PRED, "is-literal-dateTime"),

	IS_LITERAL_DATETIMESTAMPE(Namespaces.RIF_PRED, "is-literal-dateTimeStamp"),

	IS_LITERAL_DOUBLE(Namespaces.RIF_PRED, "is-literal-double"),

	IS_LITERAL_FLOAT(Namespaces.RIF_PRED, "is-literal-float"),

	IS_LITERAL_HEXBINARY(Namespaces.RIF_PRED, "is-literal-hexBinary"),

	IS_LITERAL_DECIMAL(Namespaces.RIF_PRED, "is-literal-decimal"),

	IS_LITERAL_INTEGER(Namespaces.RIF_PRED, "is-literal-integer"),

	IS_LITERAL_LONG(Namespaces.RIF_PRED, "is-literal-long"),

	IS_LITERAL_SHORT(Namespaces.RIF_PRED, "is-literal-short"),

	IS_LITERAL_BYTE(Namespaces.RIF_PRED, "is-literal-byte"),

	IS_LITERAL_NONNEGATIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-nonNegativeInteger"),

	IS_LITERAL_POSITIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-positiveInteger"),

	IS_LITERAL_UNSIGNEDLONG(Namespaces.RIF_PRED, "is-literal-unsignedLong"),

	IS_LITERAL_UNSIGNEDINT(Namespaces.RIF_PRED, "is-literal-unsignedInt"),

	IS_LITERAL_UNSIGNEDSHORT(Namespaces.RIF_PRED, "is-literal-unsignedShort"),

	IS_LITERAL_UNSIGNEDBYTE(Namespaces.RIF_PRED, "is-literal-unsignedByte"),

	IS_LITERAL_NONPOSITIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-nonPositiveInteger"),

	IS_LITERAL_NEGATIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-negativeInteger"),

	IS_LITERAL_PLAINLITERAL(Namespaces.RIF_PRED, "is-literal-PlainLiteral"),

	IS_LITERAL_STRING(Namespaces.RIF_PRED, "is-literal-string"),

	IS_LITERAL_NORMALIZEDSTRING(Namespaces.RIF_PRED,
			"is-literal-normalizedString"),

	IS_LITERAL_TOKEN(Namespaces.RIF_PRED, "is-literal-token"),

	IS_LITERAL_LANGUAGE(Namespaces.RIF_PRED, "is-literal-language"),

	IS_LITERAL_NAME(Namespaces.RIF_PRED, "is-literal-Name"),

	IS_LITERAL_NCNAME(Namespaces.RIF_PRED, "is-literal-NCName"),

	IS_LITERAL_NMTOKEN(Namespaces.RIF_PRED, "is-literal-NMTOKEN"),

	IS_LITERAL_TIME(Namespaces.RIF_PRED, "is-literal-time"),

	IS_LITERAL_DAYTIMEDURATION(Namespaces.RIF_PRED,
			"is-literal-dayTimeDuration"),

	IS_LITERAL_YEARMONTHDURATION(Namespaces.RIF_PRED,
			"is-literal-yearMonthDuration"),

	IS_LITERAL_XMLLITERAL(Namespaces.RIF_PRED, "is-literal-XMLLiteral"),

	// Negative guard predicates for datatypes.

	IS_LITERAL_NOT_ANYURI(Namespaces.RIF_PRED, "is-literal-not-anyURI"),

	IS_LITERAL_NOT_BASE64BINARY(Namespaces.RIF_PRED,
			"is-literal-not-base64Binary"),

	IS_LITERAL_NOT_BOOLEAN(Namespaces.RIF_PRED, "is-literal-not-boolean"),

	IS_LITERAL_NOT_DATE(Namespaces.RIF_PRED, "is-literal-not-date"),

	IS_LITERAL_NOT_DATETIME(Namespaces.RIF_PRED, "is-literal-not-dateTime"),

	IS_LITERAL_NOT_DATETIMESTAMPE(Namespaces.RIF_PRED,
			"is-literal-not-dateTimeStamp"),

	IS_LITERAL_NOT_DOUBLE(Namespaces.RIF_PRED, "is-literal-not-double"),

	IS_LITERAL_NOT_FLOAT(Namespaces.RIF_PRED, "is-literal-not-float"),

	IS_LITERAL_NOT_HEXBINARY(Namespaces.RIF_PRED, "is-literal-not-hexBinary"),

	IS_LITERAL_NOT_DECIMAL(Namespaces.RIF_PRED, "is-literal-not-decimal"),

	IS_LITERAL_NOT_INTEGER(Namespaces.RIF_PRED, "is-literal-not-integer"),

	IS_LITERAL_NOT_LONG(Namespaces.RIF_PRED, "is-literal-not-long"),

	IS_LITERAL_NOT_SHORT(Namespaces.RIF_PRED, "is-literal-not-short"),

	IS_LITERAL_NOT_BYTE(Namespaces.RIF_PRED, "is-literal-not-byte"),

	IS_LITERAL_NOT_NONNEGATIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-not-nonNegativeInteger"),

	IS_LITERAL_NOT_POSITIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-not-positiveInteger"),

	IS_LITERAL_NOT_UNSIGNEDLONG(Namespaces.RIF_PRED,
			"is-literal-not-unsignedLong"),

	IS_LITERAL_NOT_UNSIGNEDINT(Namespaces.RIF_PRED,
			"is-literal-not-unsignedInt"),

	IS_LITERAL_NOT_UNSIGNEDSHORT(Namespaces.RIF_PRED,
			"is-literal-not-unsignedShort"),

	IS_LITERAL_NOT_UNSIGNEDBYTE(Namespaces.RIF_PRED,
			"is-literal-not-unsignedByte"),

	IS_LITERAL_NOT_NONPOSITIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-not-nonPositiveInteger"),

	IS_LITERAL_NOT_NEGATIVEINTEGER(Namespaces.RIF_PRED,
			"is-literal-not-negativeInteger"),

	IS_LITERAL_NOT_PLAINLITERAL(Namespaces.RIF_PRED,
			"is-literal-not-PlainLiteral"),

	IS_LITERAL_NOT_STRING(Namespaces.RIF_PRED, "is-literal-not-string"),

	IS_LITERAL_NOT_NORMALIZEDSTRING(Namespaces.RIF_PRED,
			"is-literal-not-normalizedString"),

	IS_LITERAL_NOT_TOKEN(Namespaces.RIF_PRED, "is-literal-not-token"),

	IS_LITERAL_NOT_LANGUAGE(Namespaces.RIF_PRED, "is-literal-not-language"),

	IS_LITERAL_NOT_NAME(Namespaces.RIF_PRED, "is-literal-not-Name"),

	IS_LITERAL_NOT_NCNAME(Namespaces.RIF_PRED, "is-literal-not-NCName"),

	IS_LITERAL_NOT_NMTOKEN(Namespaces.RIF_PRED, "is-literal-not-NMTOKEN"),

	IS_LITERAL_NOT_TIME(Namespaces.RIF_PRED, "is-literal-not-time"),

	IS_LITERAL_NOT_DAYTIMEDURATION(Namespaces.RIF_PRED,
			"is-literal-not-dayTimeDuration"),

	IS_LITERAL_NOT_YEARMONTHDURATION(Namespaces.RIF_PRED,
			"is-literal-not-yearMonthDuration"),

	IS_LITERAL_NOT_XMLLITERAL(Namespaces.RIF_PRED, "is-literal-not-XMLLiteral"),

	IRI_STRING(Namespaces.RIF_PRED, "iri-string"),

	// Numeric functions.

	NUMERIC_ADD(Namespaces.RIF_FUNC, "numeric-add"),

	NUMERIC_SUBTRACT(Namespaces.RIF_FUNC, "numeric-subtract"),

	NUMERIC_MULTIPLY(Namespaces.RIF_FUNC, "numeric-multiply"),

	NUMERIC_DIVIDE(Namespaces.RIF_FUNC, "numeric-divide"),

	NUMERIC_INTEGER_DIVIDE(Namespaces.RIF_FUNC, "numeric-integer-divide"),

	NUMERIC_MOD(Namespaces.RIF_FUNC, "numeric-mod"),

	// Numeric predicates.

	NUMERIC_EQUAL(Namespaces.RIF_PRED, "numeric-equal"),

	NUMERIC_LESS_THAN(Namespaces.RIF_PRED, "numeric-less-than"),

	NUMERIC_GREATER_THAN(Namespaces.RIF_PRED, "numeric-greater-than"),

	NUMERIC_NOT_EQUAL(Namespaces.RIF_PRED, "numeric-not-equal"),

	NUMERIC_LESS_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"numeric-less-than-or-equal"),

	NUMERIC_GREATER_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"numeric-greater-than-or-equal"),

	// Functions on boolean values.

	NOT(Namespaces.RIF_FUNC, "not"),

	// Predicates on boolean values.

	BOOLEAN_EQUAL(Namespaces.RIF_PRED, "boolean-equal"),

	BOOLEAN_LESS_THAN(Namespaces.RIF_PRED, "boolean-less-than"),

	BOOLEAN_GREATER_THAN(Namespaces.RIF_PRED, "boolean-greater-than"),

	// Functions on strings.

	COMPARE(Namespaces.RIF_FUNC, "compare"),

	CONCAT(Namespaces.RIF_FUNC, "concat"),

	STRING_JOIN(Namespaces.RIF_FUNC, "string-join"),

	SUBSTRING(Namespaces.RIF_FUNC, "substring"),

	STRING_LENGTH(Namespaces.RIF_FUNC, "string-length"),

	UPPER_CASE(Namespaces.RIF_FUNC, "upper-case"),

	LOWER_CASE(Namespaces.RIF_FUNC, "lower-case"),

	ENCODE_FOR_URI(Namespaces.RIF_FUNC, "encode-for-uri"),

	IRI_TO_URI(Namespaces.RIF_FUNC, "iri-to-uri"),

	ESCAPE_HTML_URI(Namespaces.RIF_FUNC, "escape-html-uri"),

	SUBSTRING_BEFORE(Namespaces.RIF_FUNC, "substring-before"),

	SUBSTRING_AFTER(Namespaces.RIF_FUNC, "substring-after"),

	REPLACE(Namespaces.RIF_FUNC, "replace"),

	// Predicates on strings.

	CONTAINS(Namespaces.RIF_PRED, "contains"),

	STARTS_WITH(Namespaces.RIF_PRED, "starts-with"),

	ENDS_WITH(Namespaces.RIF_PRED, "ends-with"),

	MATCHES(Namespaces.RIF_PRED, "matches"),

	YEAR_FROM_DATETIME(Namespaces.RIF_FUNC, "year-from-dateTime"),

	MONTH_FROM_DATETIME(Namespaces.RIF_FUNC, "month-from-dateTime"),

	DAY_FROM_DATETIME(Namespaces.RIF_FUNC, "day-from-dateTime"),

	HOURS_FROM_DATETIME(Namespaces.RIF_FUNC, "hours-from-dateTime"),

	MINUTES_FROM_DATETIME(Namespaces.RIF_FUNC, "minutes-from-dateTime"),

	SECONDS_FROM_DATETIME(Namespaces.RIF_FUNC, "seconds-from-dateTime"),

	// Functions on dates, times and durations.

	YEAR_FROM_DATE(Namespaces.RIF_FUNC, "year-from-date"),

	MONTH_FROM_DATE(Namespaces.RIF_FUNC, "month-from-date"),

	DAY_FROM_DATE(Namespaces.RIF_FUNC, "day-from-date"),

	HOURS_FROM_TIME(Namespaces.RIF_FUNC, "hours-from-time"),

	MINUTES_FROM_TIME(Namespaces.RIF_FUNC, "minutes-from-time"),

	SECONDS_FROM_TIME(Namespaces.RIF_FUNC, "seconds-from-time"),

	YEARS_FROM_DURATION(Namespaces.RIF_FUNC, "years-from-duration"),

	MONTHS_FROM_DURATION(Namespaces.RIF_FUNC, "months-from-duration"),

	DAYS_FROM_DURATION(Namespaces.RIF_FUNC, "days-from-duration"),

	HOURS_FROM_DURATION(Namespaces.RIF_FUNC, "hours-from-duration"),

	MINUTES_FROM_DURATION(Namespaces.RIF_FUNC, "minutes-from-duration"),

	SECONDS_FROM_DURATION(Namespaces.RIF_FUNC, "seconds-from-duration"),

	TIMEZONE_FROM_DATETIME(Namespaces.RIF_FUNC, "timezone-from-dateTime"),

	TIMEZONE_FROM_DATE(Namespaces.RIF_FUNC, "timezone-from-date"),

	TIMEZONE_FROM_TIME(Namespaces.RIF_FUNC, "timezone-from-time"),

	SUBTRACT_DATETIMES(Namespaces.RIF_FUNC, "subtract-dateTimes"),

	SUBTRACT_DATES(Namespaces.RIF_FUNC, "subtract-dates"),

	SUBTRACT_TIMES(Namespaces.RIF_FUNC, "subtract-times"),

	ADD_YEARMONTHDURATIONS(Namespaces.RIF_FUNC, "add-yearMonthDurations"),

	SUBTRACT_YEARMONTHDURATIONS(Namespaces.RIF_FUNC,
			"subtract-yearMonthDurations"),

	MULTIPLY_YEARMONTHDURATION(Namespaces.RIF_FUNC,
			"multiply-yearMonthDuration"),

	DIVIDE_YEARMONTHDURATION(Namespaces.RIF_FUNC, "divide-yearMonthDuration"),

	DIVIDE_YEARMONTHDURATION_BY_YEARMONTHDURATION(Namespaces.RIF_FUNC,
			"divide-yearMonthDuration-by-yearMonthDuration"),

	ADD_DAYTIMEDURATIONS(Namespaces.RIF_FUNC, "add-dayTimeDurations"),

	SUBTRACT_DAYTIMEDURATIONS(Namespaces.RIF_FUNC, "subtract-dayTimeDurations"),

	MULTIPLY_DAYTIMEDURATION(Namespaces.RIF_FUNC, "multiply-dayTimeDuration"),

	DIVIDE_DAYTIMEDURATION(Namespaces.RIF_FUNC, "divide-dayTimeDuration"),

	DIVIDE_DAYTIMEDURATION_BY_DAYTIMEDURATION(Namespaces.RIF_FUNC,
			"divide-dayTimeDuration-by-dayTimeDuration"),

	ADD_YEARMONTHDURATION_TO_DATETIME(Namespaces.RIF_FUNC,
			"add-yearMonthDuration-to-dateTime"),

	ADD_YEARMONTHDURATION_TO_DATE(Namespaces.RIF_FUNC,
			"add-yearMonthDuration-to-date"),

	ADD_DAYTIMEDURATION_TO_DATETIME(Namespaces.RIF_FUNC,
			"add-dayTimeDuration-to-dateTime"),

	ADD_DAYTIMEDURATION_TO_DATE(Namespaces.RIF_FUNC,
			"add-dayTimeDuration-to-date"),

	ADD_DAYTIMEDURATION_TO_TIME(Namespaces.RIF_FUNC,
			"add-dayTimeDuration-to-time"),

	SUBTRACT_YEARMONTHDURATION_FROM_DATETIME(Namespaces.RIF_FUNC,
			"subtract-yearMonthDuration-from-dateTime"),

	SUBTRACT_YEARMONTHDURATION_FROM_DATE(Namespaces.RIF_FUNC,
			"subtract-yearMonthDuration-from-date"),

	SUBTRACT_DAYTIMEDURATION_FROM_DATETIME(Namespaces.RIF_FUNC,
			"subtract-dayTimeDuration-from-dateTime"),

	SUBTRACT_DAYTIMEDURATION_FROM_DATE(Namespaces.RIF_FUNC,
			"subtract-dayTimeDuration-from-date"),

	SUBTRACT_DAYTIMEDURATION_FROM_TIME(Namespaces.RIF_FUNC,
			"subtract-dayTimeDuration-from-time"),

	// Predicates on dates, times and durations.

	DATETIME_EQUAL(Namespaces.RIF_PRED, "dateTime-equal"),

	DATETIME_LESS_THAN(Namespaces.RIF_PRED, "dateTime-less-than"),

	DATETIME_GREATER_THAN(Namespaces.RIF_PRED, "dateTime-greater-than"),

	DATE_EQUAL(Namespaces.RIF_PRED, "date-equal"),

	DATE_LESS_THAN(Namespaces.RIF_PRED, "date-less-than"),

	DATE_GREATER_THAN(Namespaces.RIF_PRED, "date-greater-than"),

	TIME_EQUAL(Namespaces.RIF_PRED, "time-equal"),

	TIME_LESS_THAN(Namespaces.RIF_PRED, "time-less-than"),

	TIME_GREATER_THAN(Namespaces.RIF_PRED, "time-greater-than"),

	DURATION_EQUAL(Namespaces.RIF_PRED, "duration-equal"),

	DAYTIMEDURATION_LESS_THAN(Namespaces.RIF_PRED, "dayTimeDuration-less-than"),

	DAYTIMEDURATION_GREATER_THAN(Namespaces.RIF_PRED,
			"dayTimeDuration-greater-than"),

	YEARMONTHDURATION_LESS_THAN(Namespaces.RIF_PRED,
			"yearMonthDuration-less-than"),

	YEARMONTHDURATION_GREATER_THAN(Namespaces.RIF_PRED,
			"yearMonthDuration-greater-than"),

	DATETIME_NOT_EQUAL(Namespaces.RIF_PRED, "dateTime-not-equal"),

	DATETIME_LESS_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"dateTime-less-than-or-equal"),

	DATETIME_GREATER_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"dateTime-greater-than-or-equal"),

	DATE_NOT_EQUAL(Namespaces.RIF_PRED, "date-not-equal"),

	DATE_LESS_THAN_OR_EQUAL(Namespaces.RIF_PRED, "date-less-than-or-equal"),

	DATE_GREATER_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"date-greater-than-or-equal"),

	TIME_NOT_EQUAL(Namespaces.RIF_PRED, "time-not-equal"),

	TIME_LESS_THAN_OR_EQUAL(Namespaces.RIF_PRED, "time-less-than-or-equal"),

	TIME_GREATER_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"time-greater-than-or-equal"),

	DURATION_NOT_EQUAL(Namespaces.RIF_PRED, "duration-not-equal"),

	DAYTIMEDURATION_LESS_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"dayTimeDuration-less-than-or-equal"),

	DAYTIMEDURATION_GREATER_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"dayTimeDuration-greater-than-or-equal"),

	YEARMONTHDURATION_LESS_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"yearMonthDuration-less-than-or-equal"),

	YEARMONTHDURATION_GREATER_THAN_OR_EQUAL(Namespaces.RIF_PRED,
			"yearMonthDuration-greater-than-or-equal"),

	// Functions and predicates on rdf:XMLLiterals.

	XMLLITERAL_EQUAL(Namespaces.RIF_PRED, "XMLLiteral-equal"),

	XMLLITERAL_NOT_EQUAL(Namespaces.RIF_PRED, "XMLLiteral-not-equal"),

	// Functions on rdf:PlainLiteral.

	PLAINLITERAL_FROM_STRING_LANG(Namespaces.RIF_FUNC,
			"PlainLiteral-from-string-lang"),

	STRING_FROM_PLAINLITERAL(Namespaces.RIF_FUNC, "string-from-PlainLiteral"),

	LANG_FROM_PLAINLITERAL(Namespaces.RIF_FUNC, "lang-from-PlainLiteral"),

	PLAINLITERAL_COMPARE(Namespaces.RIF_FUNC, "PlainLiteral-compare"),

	PLAINLITERAL_LENGTH(Namespaces.RIF_FUNC, "PlainLiteral-length"),

	// Predicates on rdf:PlainLiteral.

	MATCHES_LANGUAGE_RANGE(Namespaces.RIF_PRED, "matches-language-range"),

	// Predicates on RIF lists.

	IS_LIST(Namespaces.RIF_PRED, "is-list"),

	LIST_CONTAINS(Namespaces.RIF_PRED, "list-contains"),

	// Functions on RIF lists.

	MAKE_LISTS(Namespaces.RIF_FUNC, "make-list"),

	COUNT(Namespaces.RIF_FUNC, "count"),

	GET(Namespaces.RIF_FUNC, "get"),

	SUBLIST(Namespaces.RIF_FUNC, "sublist"),

	APPEND(Namespaces.RIF_FUNC, "append"),

	CONCATENATE(Namespaces.RIF_FUNC, "concatenate"),

	INSERT_BEFORE(Namespaces.RIF_FUNC, "insert-before"),

	REMOVE(Namespaces.RIF_FUNC, "remove"),

	REVERSE(Namespaces.RIF_FUNC, "reverse"),

	INDEX_OF(Namespaces.RIF_FUNC, "index-of"),

	UNION(Namespaces.RIF_FUNC, "union"),

	DISTINCT_VALUES(Namespaces.RIF_FUNC, "distinct-values"),

	INTERSECT(Namespaces.RIF_FUNC, "intersect"),

	EXCEPT(Namespaces.RIF_FUNC, "except");

	/**
	 * The namespace of the built-in.
	 */
	private String namespace;

	/**
	 * The name of the built-in.
	 */
	private String name;

	private Builtin(String namespace, String name) {
		this.namespace = namespace;
		this.name = name;
	}

	/**
	 * Returns the namespace of the built-in.
	 * 
	 * @return The namespace of the built-in.
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Returns the name of the built-in.
	 * 
	 * @return The name of the built-in.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the URI of the built-in which is the concatenation of the
	 * namespace and the name of the built-in.
	 * 
	 * @return The URI of the built-in which is the concatenation of the
	 *         namespace and the name of the built-in.
	 */
	public String getUri() {
		return getNamespace() + getName();
	}

	/**
	 * Returns <code>true</code> if this built-in represents a functional
	 * built-in, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this built-in represents a functional
	 *         built-in, <code>false</code> otherwise.
	 */
	public boolean isFunction() {
		return namespace.equals(Namespaces.RIF_FUNC);
	}

	/**
	 * Returns <code>true</code> if this built-in represents a predicate
	 * built-in, <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if this built-in represents a predicate
	 *         built-in, <code>false</code> otherwise.
	 */
	public boolean isPredicate() {
		return namespace.equals(Namespaces.RIF_PRED);
	}

	/**
	 * Finds the built-in for the specified URI.
	 * 
	 * @param uri The URI of the built-in to look for.
	 * @return The corresponding built-in for the specified URI, or
	 *         <code>null</code> if no such built-in can be found.
	 */
	public static Builtin from(String uri) {
		return Lookup.table.get(uri);
	}

	/**
	 * Checks if the specified URI represents a built-in.
	 * 
	 * @param uri The URI to check if it represents a built-in.
	 * @return true if the specified URI represents a built-in, false otherwise.
	 */
	public static boolean isBuiltIn(String uri) {
		return from(uri) != null;
	}

	public static boolean isDatatypeCast(String uri) {
		return RdfDatatype.isDatatype(uri) || XmlSchemaDatatype.isDatatype(uri);
	}

	/**
	 * Maps the URI of each built-in to the built-in itself.
	 */
	private static class Lookup {

		private static final Map<String, Builtin> table;

		static {
			table = new HashMap<String, Builtin>();

			for (Builtin builtin : Builtin.values()) {
				table.put(builtin.getUri(), builtin);
			}
		}

	}
}
