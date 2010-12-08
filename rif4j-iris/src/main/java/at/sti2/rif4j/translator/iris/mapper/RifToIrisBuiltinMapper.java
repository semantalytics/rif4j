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
package at.sti2.rif4j.translator.iris.mapper;

import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.factory.IBuiltinsFactory;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.RifBuiltIn;

/**
 * @author Adrian Marte
 */
public class RifToIrisBuiltinMapper {

	private static final Logger logger = LoggerFactory
			.getLogger(RifToIrisBuiltinMapper.class);

	public static IBuiltinAtom toIrisBuiltin(String rifBuiltinIri,
			ITerm... terms) {
		RifBuiltIn rifBuiltin = RifBuiltIn.from(rifBuiltinIri);

		if (rifBuiltin == null) {
			return null;
		}

		IBuiltinsFactory factory = Factory.BUILTIN;

		switch (rifBuiltin) {
		case ADD_DAYTIMEDURATION_TO_DATE:
			return factory.createAddDayTimeDurationToDate(terms);
		case ADD_DAYTIMEDURATION_TO_TIME:
			return factory.createAddDayTimeDurationToTime(terms);
		case ADD_DAYTIMEDURATION_TO_DATETIME:
			return factory.createAddDayTimeDurationToDateTime(terms);
		case ADD_DAYTIMEDURATIONS:
			return factory.createAddDayTimeDurations(terms);
		case ADD_YEARMONTHDURATION_TO_DATE:
			return factory.createAddYearMonthDurationToDate(terms);
		case ADD_YEARMONTHDURATION_TO_DATETIME:
			return factory.createAddYearMonthDurationToDateTime(terms);
		case ADD_YEARMONTHDURATIONS:
			return factory.createAddYearMonthDurations(terms);
		case APPEND:
			// TODO What to do?
			break;
		case BOOLEAN_EQUAL:
			return factory.createBooleanEqual(terms);
		case BOOLEAN_GREATER_THAN:
			return factory.createBooleanGreater(terms);
		case BOOLEAN_LESS_THAN:
			return factory.createBooleanLess(terms);
		case COMPARE:
			return factory.createStringCompare(terms);
		case CONCAT:
			return factory.createStringConcat(terms);
		case CONCATENATE:
			// TODO What to do?
			break;
		case CONTAINS:
			return factory.createStringContains(terms);
		case COUNT:
			// TODO What to do?
			break;
		case DATE_EQUAL:
			return factory.createDateEqual(terms);
		case DATE_GREATER_THAN:
			return factory.createDateGreater(terms);
		case DATE_GREATER_THAN_OR_EQUAL:
			return factory.createDateGreaterEqual(terms);
		case DATE_LESS_THAN:
			return factory.createDateLess(terms);
		case DATE_LESS_THAN_OR_EQUAL:
			return factory.createDateLessEqual(terms);
		case DATE_NOT_EQUAL:
			return factory.createDateNotEqual(terms);
		case DATETIME_EQUAL:
			return factory.createDateTimeEqual(terms);
		case DATETIME_GREATER_THAN:
			return factory.createDateTimeGreater(terms);
		case DATETIME_GREATER_THAN_OR_EQUAL:
			return factory.createDateTimeGreaterEqual(terms);
		case DATETIME_LESS_THAN:
			return factory.createDateTimeLess(terms);
		case DATETIME_LESS_THAN_OR_EQUAL:
			return factory.createDateTimeLessEqual(terms);
		case DATETIME_NOT_EQUAL:
			return factory.createDateTimeNotEqual(terms);
		case DAY_FROM_DATE:
			return factory.createDayFromDate(terms);
		case DAY_FROM_DATETIME:
			return factory.createDayFromDateTime(terms);
		case DAYS_FROM_DURATION:
			return factory.createDaysFromDuration(terms);
		case DAYTIMEDURATION_GREATER_THAN:
			return factory.createDayTimeDurationGreater(terms);
		case DAYTIMEDURATION_GREATER_THAN_OR_EQUAL:
			return factory.createDayTimeDurationGreaterEqual(terms);
		case DAYTIMEDURATION_LESS_THAN:
			return factory.createDayTimeDurationLess(terms);
		case DAYTIMEDURATION_LESS_THAN_OR_EQUAL:
			return factory.createDayTimeDurationLessEqual(terms);
		case DISTINCT_VALUES:
			// FIXME What to do?
			break;
		case DIVIDE_DAYTIMEDURATION:
			return factory.createDayTimeDurationDivide(terms);
		case DIVIDE_DAYTIMEDURATION_BY_DAYTIMEDURATION:
			return factory.createDayTimeDurationDivideByDayTimeDuration(terms);
		case DIVIDE_YEARMONTHDURATION:
			return factory.createYearMonthDurationDivide(terms);
		case DIVIDE_YEARMONTHDURATION_BY_YEARMONTHDURATION:
			return factory
					.createYearMonthDurationDivideByYearMonthDuration(terms);
		case DURATION_EQUAL:
			return factory.createDurationEqual(terms);
		case DURATION_NOT_EQUAL:
			return factory.createDurationNotEqual(terms);
		case ENCODE_FOR_URI:
			// FIXME IRIS built-in missing.
			break;
		case ENDS_WITH:
			return factory.createStringEndsWith(terms);
		case ESCAPE_HTML_URI:
			return factory.createStringEscapeHtmlUri(terms);
		case EXCEPT:
			// FIXME IRIS built-in missing.
			break;
		case GET:
			// FIXME IRIS built-in missing.
			break;
		case HOURS_FROM_DATETIME:
			return factory.createHoursFromDateTime(terms);
		case HOURS_FROM_DURATION:
			return factory.createHoursFromDuration(terms);
		case HOURS_FROM_TIME:
			return factory.createHoursFromTime(terms);
		case INDEX_OF:
			// FIXME IRIS built-in missing.
			break;
		case INSERT_BEFORE:
			// FIXME IRIS built-in missing.
			break;
		case INTERSECT:
			// FIXME IRIS built-in missing.
			break;
		case IRI_STRING:
			return factory.createIriString(terms);
		case IRI_TO_URI:
			return factory.createStringIriToUri(terms);
		case IS_LIST:
			// FIXME IRIS built-in missing.
			break;
		case IS_LITERAL_ANYURI:
			return factory.createIsAnyURI(terms);
		case IS_LITERAL_BASE64BINARY:
			return factory.createIsBase64Binary(terms);
		case IS_LITERAL_BOOLEAN:
			return factory.createIsBoolean(terms);
		case IS_LITERAL_BYTE:
			return factory.createIsByte(terms);
		case IS_LITERAL_DATE:
			return factory.createIsDate(terms);
		case IS_LITERAL_DATETIME:
			return factory.createIsDateTime(terms);
		case IS_LITERAL_DATETIMESTAMP:
			return factory.createIsDateTimeStamp(terms);
		case IS_LITERAL_DAYTIMEDURATION:
			return factory.createIsDayTimeDuration(terms);
		case IS_LITERAL_DECIMAL:
			return factory.createIsDecimal(terms);
		case IS_LITERAL_DOUBLE:
			return factory.createIsDouble(terms);
		case IS_LITERAL_FLOAT:
			return factory.createIsFloat(terms);
		case IS_LITERAL_HEXBINARY:
			return factory.createIsHexBinary(terms);
		case IS_LITERAL_INT:
			return factory.createIsInt(terms);
		case IS_LITERAL_INTEGER:
			return factory.createIsInteger(terms);
		case IS_LITERAL_LANGUAGE:
			return factory.createIsLanguage(terms);
		case IS_LITERAL_LONG:
			return factory.createIsLong(terms);
		case IS_LITERAL_NAME:
			return factory.createIsName(terms);
		case IS_LITERAL_NCNAME:
			return factory.createIsNCName(terms);
		case IS_LITERAL_NEGATIVEINTEGER:
			return factory.createIsNegativeInteger(terms);
		case IS_LITERAL_NMTOKEN:
			return factory.createIsNMTOKEN(terms);
		case IS_LITERAL_NONNEGATIVEINTEGER:
			return factory.createIsNonNegativeInteger(terms);
		case IS_LITERAL_NONPOSITIVEINTEGER:
			return factory.createIsNonPositiveInteger(terms);
		case IS_LITERAL_NORMALIZEDSTRING:
			return factory.createIsNormalizedString(terms);
		case IS_LITERAL_NOT_ANYURI:
			return factory.createIsNotAnyURI(terms);
		case IS_LITERAL_NOT_BASE64BINARY:
			return factory.createIsNotBase64Binary(terms);
		case IS_LITERAL_NOT_BOOLEAN:
			return factory.createIsNotBoolean(terms);
		case IS_LITERAL_NOT_BYTE:
			return factory.createIsNotByte(terms);
		case IS_LITERAL_NOT_DATE:
			return factory.createIsNotDate(terms);
		case IS_LITERAL_NOT_DATETIME:
			return factory.createIsNotDateTime(terms);
		case IS_LITERAL_NOT_DATETIMESTAMP:
			return factory.createIsNotDateTimeStamp(terms);
		case IS_LITERAL_NOT_DAYTIMEDURATION:
			return factory.createIsNotDayTimeDuration(terms);
		case IS_LITERAL_NOT_DECIMAL:
			return factory.createIsNotDecimal(terms);
		case IS_LITERAL_NOT_DOUBLE:
			return factory.createIsNotDouble(terms);
		case IS_LITERAL_NOT_FLOAT:
			return factory.createIsNotFloat(terms);
		case IS_LITERAL_NOT_HEXBINARY:
			return factory.createIsNotHexBinary(terms);
		case IS_LITERAL_NOT_INTEGER:
			return factory.createIsNotInteger(terms);
		case IS_LITERAL_NOT_LANGUAGE:
			return factory.createIsNotLanguage(terms);
		case IS_LITERAL_NOT_LONG:
			return factory.createIsNotLong(terms);
		case IS_LITERAL_NOT_NAME:
			return factory.createIsNotName(terms);
		case IS_LITERAL_NOT_NCNAME:
			return factory.createIsNotNCName(terms);
		case IS_LITERAL_NOT_NEGATIVEINTEGER:
			return factory.createIsNotNegativeInteger(terms);
		case IS_LITERAL_NOT_NMTOKEN:
			return factory.createIsNotNMTOKEN(terms);
		case IS_LITERAL_NOT_NONNEGATIVEINTEGER:
			return factory.createIsNotNonNegativeInteger(terms);
		case IS_LITERAL_NOT_NONPOSITIVEINTEGER:
			return factory.createIsNotNonPositiveInteger(terms);
		case IS_LITERAL_NOT_NORMALIZEDSTRING:
			return factory.createIsNotNormalizedString(terms);
		case IS_LITERAL_NOT_PLAINLITERAL:
			return factory.createIsNotPlainLiteral(terms);
		case IS_LITERAL_NOT_POSITIVEINTEGER:
			return factory.createIsNotPositiveInteger(terms);
		case IS_LITERAL_NOT_SHORT:
			return factory.createIsNotShort(terms);
		case IS_LITERAL_NOT_STRING:
			return factory.createIsNotString(terms);
		case IS_LITERAL_NOT_TIME:
			return factory.createIsNotTime(terms);
		case IS_LITERAL_NOT_TOKEN:
			return factory.createIsNotToken(terms);
		case IS_LITERAL_NOT_UNSIGNEDBYTE:
			return factory.createIsNotUnsignedByte(terms);
		case IS_LITERAL_NOT_UNSIGNEDINT:
			return factory.createIsNotUnsignedByte(terms);
		case IS_LITERAL_NOT_UNSIGNEDLONG:
			return factory.createIsNotUnsignedLong(terms);
		case IS_LITERAL_NOT_UNSIGNEDSHORT:
			return factory.createIsNotUnsignedShort(terms);
		case IS_LITERAL_NOT_XMLLITERAL:
			return factory.createIsNotXMLLiteral(terms);
		case IS_LITERAL_NOT_YEARMONTHDURATION:
			return factory.createIsNotYearMonthDuration(terms);
		case IS_LITERAL_PLAINLITERAL:
			return factory.createIsPlainLiteral(terms);
		case IS_LITERAL_POSITIVEINTEGER:
			return factory.createIsPositiveInteger(terms);
		case IS_LITERAL_SHORT:
			return factory.createIsShort(terms);
		case IS_LITERAL_STRING:
			return factory.createIsString(terms);
		case IS_LITERAL_TIME:
			return factory.createIsTime(terms);
		case IS_LITERAL_TOKEN:
			return factory.createIsToken(terms);
		case IS_LITERAL_UNSIGNEDBYTE:
			return factory.createIsUnsignedByte(terms);
		case IS_LITERAL_UNSIGNEDINT:
			return factory.createIsUnsignedInt(terms);
		case IS_LITERAL_UNSIGNEDLONG:
			return factory.createIsUnsignedLong(terms);
		case IS_LITERAL_UNSIGNEDSHORT:
			return factory.createIsUnsignedShort(terms);
		case IS_LITERAL_XMLLITERAL:
			return factory.createIsXMLLiteral(terms);
		case IS_LITERAL_YEARMONTHDURATION:
			return factory.createIsYearMonthDuration(terms);
		case LANG_FROM_PLAINLITERAL:
			// FIXME Rename method to createLangFromPlainLiteral.
			return factory.createLangFromText(terms);
		case LIST_CONTAINS:
			// FIXME IRIS built-in missing.
			break;
		case LITERAL_NOT_IDENTICAL:
			return factory.createNotExactEqual(terms);
		case LOWER_CASE:
			return factory.createStringToLower(terms);
		case MAKE_LISTS:
			// FIXME IRIS built-in missing.
			break;
		case MATCHES:
			return factory.createStringMatches(terms);
		case MATCHES_LANGUAGE_RANGE:
			// FIXME IRIS built-in missing.
			break;
		case MINUTES_FROM_DATETIME:
			return factory.createMinutesFromDateTime(terms);
		case MINUTES_FROM_DURATION:
			return factory.createMinutesFromDuration(terms);
		case MINUTES_FROM_TIME:
			return factory.createMinutesFromTime(terms);
		case MONTH_FROM_DATE:
			return factory.createMonthFromDate(terms);
		case MONTH_FROM_DATETIME:
			return factory.createMonthFromDateTime(terms);
		case MONTHS_FROM_DURATION:
			return factory.createMonthsFromDuration(terms);
		case MULTIPLY_DAYTIMEDURATION:
			return factory.createDayTimeDurationMultiply(terms);
		case MULTIPLY_YEARMONTHDURATION:
			return factory.createYearMonthDurationMultiply(terms);
		case NOT:
			return factory.createBooleanNot(terms);
		case NUMERIC_ADD:
			return factory.createNumericAdd(terms);
		case NUMERIC_DIVIDE:
			return factory.createNumericDivide(terms);
		case NUMERIC_EQUAL:
			return factory.createNumericEqual(terms);
		case NUMERIC_GREATER_THAN:
			return factory.createNumericGreater(terms);
		case NUMERIC_GREATER_THAN_OR_EQUAL:
			return factory.createNumericGreaterEqual(terms);
		case NUMERIC_INTEGER_DIVIDE:
			return factory.createNumericIntegerDivide(terms);
		case NUMERIC_LESS_THAN:
			return factory.createNumericLess(terms);
		case NUMERIC_LESS_THAN_OR_EQUAL:
			return factory.createNumericLessEqual(terms);
		case NUMERIC_MOD:
			return factory.createNumericModulus(terms);
		case NUMERIC_MULTIPLY:
			return factory.createNumericMultiply(terms);
		case NUMERIC_NOT_EQUAL:
			return factory.createNumericNotEqual(terms);
		case NUMERIC_SUBTRACT:
			return factory.createNumericSubtract(terms);
		case PLAINLITERAL_COMPARE:
			// FIXME Rename method.
			return factory.createTextCompare(terms);
		case PLAINLITERAL_FROM_STRING_LANG:
			// FIXME Rename method.
			return factory.createTextFromStringLang(terms);
		case PLAINLITERAL_LENGTH:
			// FIXME Rename method.
			return factory.createTextLength(terms);
		case REMOVE:
			// FIXME IRIS built-in missing.
			break;
		case REPLACE:
			return factory.createStringReplace(terms);
		case REVERSE:
			// FIXME IRIS built-in missing.
			break;
		case SECONDS_FROM_DATETIME:
			return factory.createSecondsFromDateTime(terms);
		case SECONDS_FROM_DURATION:
			return factory.createSecondsFromDuration(terms);
		case SECONDS_FROM_TIME:
			return factory.createSecondsFromTime(terms);
		case STARTS_WITH:
			return factory.createStringStartsWith(terms);
		case STRING_FROM_PLAINLITERAL:
			// FIXME Rename method.
			return factory.createStringFromText(terms);
		case STRING_JOIN:
			return factory.createStringJoin(terms);
		case STRING_LENGTH:
			return factory.createStringLength(terms);
		case SUBLIST:
			// FIXME IRIS built-in missing.
			break;
		case SUBSTRING:
			return factory.createStringSubstring(terms);
		case SUBSTRING_AFTER:
			return factory.createStringSubstringAfter(terms);
		case SUBSTRING_BEFORE:
			return factory.createStringSubstringBefore(terms);
		case SUBTRACT_DATES:
			return factory.createDateSubtract(terms);
		case SUBTRACT_DATETIMES:
			return factory.createDateTimeSubtract(terms);
		case SUBTRACT_DAYTIMEDURATION_FROM_DATE:
			return factory.createSubtractDayTimeDurationFromDate(terms);
		case SUBTRACT_DAYTIMEDURATION_FROM_DATETIME:
			return factory.createSubtractDayTimeDurationFromDateTime(terms);
		case SUBTRACT_DAYTIMEDURATION_FROM_TIME:
			return factory.createSubtractDayTimeDurationFromTime(terms);
		case SUBTRACT_DAYTIMEDURATIONS:
			return factory.createDayTimeDurationSubtract(terms);
		case SUBTRACT_TIMES:
			return factory.createTimeSubtract(terms);
		case SUBTRACT_YEARMONTHDURATION_FROM_DATE:
			return factory.createSubtractYearMonthDurationFromDate(terms);
		case SUBTRACT_YEARMONTHDURATION_FROM_DATETIME:
			return factory.createSubtractYearMonthDurationFromDateTime(terms);
		case SUBTRACT_YEARMONTHDURATIONS:
			return factory.createYearMonthDurationSubtract(terms);
		case TIME_EQUAL:
			return factory.createTimeEqual(terms);
		case TIME_GREATER_THAN:
			return factory.createTimeGreater(terms);
		case TIME_GREATER_THAN_OR_EQUAL:
			return factory.createTimeGreaterEqual(terms);
		case TIME_LESS_THAN:
			return factory.createTimeLess(terms);
		case TIME_LESS_THAN_OR_EQUAL:
			return factory.createTimeLessEqual(terms);
		case TIME_NOT_EQUAL:
			return factory.createTimeNotEqual(terms);
		case TIMEZONE_FROM_DATE:
			return factory.createTimezoneFromDate(terms);
		case TIMEZONE_FROM_DATETIME:
			return factory.createTimezoneFromDateTime(terms);
		case TIMEZONE_FROM_TIME:
			return factory.createTimezoneFromTime(terms);
		case UNION:
			// FIXME IRIS built-in missing.
			break;
		case UPPER_CASE:
			return factory.createStringToUpper(terms);
		case XMLLITERAL_EQUAL:
			return factory.createXMLLiteralEqual(terms);
		case XMLLITERAL_NOT_EQUAL:
			return factory.createXMLLiteralNotEqual(terms);
		case YEAR_FROM_DATE:
			return factory.createYearFromDate(terms);
		case YEAR_FROM_DATETIME:
			return factory.createYearFromDateTime(terms);
		case YEARMONTHDURATION_GREATER_THAN:
			return factory.createYearMonthDurationGreater(terms);
		case YEARMONTHDURATION_GREATER_THAN_OR_EQUAL:
			return factory.createYearMonthDurationGreaterEqual(terms);
		case YEARMONTHDURATION_LESS_THAN:
			return factory.createYearMonthDurationLess(terms);
		case YEARMONTHDURATION_LESS_THAN_OR_EQUAL:
			return factory.createYearMonthDurationLessEqual(terms);
		case YEARS_FROM_DURATION:
			return factory.createYearsFromDuration(terms);
		}

		return null;
	}

	public static boolean isBuiltin(String rifBuiltinIri) {
		boolean isBuiltIn = (RifBuiltIn.from(rifBuiltinIri) != null);
		return isBuiltIn;
	}

	public static boolean isPredicate(String rifBuiltinIri) {
		RifBuiltIn rifBuiltin = RifBuiltIn.from(rifBuiltinIri);

		if (rifBuiltin != null) {
			return rifBuiltin.isPredicate();
		}

		return false;
	}

	public static boolean isFunction(String rifBuiltinIri) {
		RifBuiltIn rifBuiltin = RifBuiltIn.from(rifBuiltinIri);

		if (rifBuiltin != null) {
			return rifBuiltin.isFunction();
		}

		return false;
	}

	public static boolean needsAuxiliaryVariable(String rifBuiltinIri) {
		RifBuiltIn rifBuiltin = RifBuiltIn.from(rifBuiltinIri);
		if (rifBuiltin == null) {
			return false;
		}

		switch (rifBuiltin) {
		case SUBTRACT_DATETIMES:
			return true;
		case DAYS_FROM_DURATION:
			return true;
		}

		return false;
	}
}
