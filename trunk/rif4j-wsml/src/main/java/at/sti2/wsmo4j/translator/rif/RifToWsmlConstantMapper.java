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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.XmlSchemaDataType;
import org.sti2.wsmo4j.factory.WsmlFactoryContainer;
import org.wsmo.common.IRI;
import org.wsmo.factory.DataFactory;
import org.wsmo.factory.FactoryContainer;
import org.wsmo.factory.WsmoFactory;

import at.sti2.rif4j.RdfDatatype;
import at.sti2.rif4j.RifDatatype;
import at.sti2.rif4j.condition.Constant;

/**
 * Maps RIF constants to WSML data values and vice versa.
 * 
 * @author Adrian Marte
 */
public class RifToWsmlConstantMapper {

	protected FactoryContainer factories;

	private DataFactory dataFactory;

	private WsmoFactory wsmoFactory;

	public RifToWsmlConstantMapper() {
		this(new WsmlFactoryContainer());
	}

	public RifToWsmlConstantMapper(FactoryContainer factories) {
		this.factories = factories;
		dataFactory = factories.getXmlDataFactory();
		wsmoFactory = factories.getWsmoFactory();
	}

	/**
	 * Maps a RIF constant to a WSML term.
	 * 
	 * @param constant The RIF constant to transform into a WSML term.
	 * @return The corresponding WSML term for the specific RIF constant, or
	 *         <code>null</code> if there is no corresponding WSML term.
	 */
	public Term toWsmlTerm(Constant constant) {
		String type = constant.getType();
		String value = constant.getText();

		return toWsmlTerm(type, value);
	}

	/**
	 * Maps a RIF constant, represented by a type and a value, to a WSML term.
	 * 
	 * @param type The type URI of the RIF constant, e.g.
	 *            http://www.w3.org/2001/XMLSchema#double.
	 * @param value The value of the RIF constant in canonical string
	 *            representation, e.g. 0.1337 for a XSD double value.
	 * @return The corresponding WSML term for the specific RIF constant, or
	 *         <code>null</code> if there is no corresponding WSML term.
	 */
	public Term toWsmlTerm(String type, String value) {
		if (type.equals(XmlSchemaDataType.XSD_BASE64BINARY)) {
			return dataFactory.createBase64Binary(value.getBytes());
		} else if (type.equals(XmlSchemaDataType.XSD_BOOLEAN)) {
			return dataFactory.createBoolean(value);
		} else if (type.equals(XmlSchemaDataType.XSD_DATE)) {
			DateTime date = DateTime.parseDate(value);

			if (date != null) {
				return dataFactory.createDate(date.year, date.month, date.day,
						String.valueOf(date.sign), date.tzHour, date.tzMinute);
			}
		} else if (type.equals(XmlSchemaDataType.XSD_DATETIME)) {
			DateTime dateTime = DateTime.parseDateTime(value);

			if (dateTime != null) {
				return dataFactory.createDateTime(dateTime.year,
						dateTime.month, dateTime.day, dateTime.hour,
						dateTime.minute, dateTime.second, String
								.valueOf(dateTime.sign), dateTime.tzHour,
						dateTime.tzMinute);
			}
		} else if (type.equals(XmlSchemaDataType.XSD_DAYTIMEDURATION)) {
			Duration duration = createDuration(value);

			if (duration != null) {
				return dataFactory.createDayTimeDuration(duration.getSign(),
						duration.getDays(), duration.getHours(), duration
								.getMinutes(), duration.getSeconds());
			}
		} else if (type.equals(XmlSchemaDataType.XSD_DECIMAL)) {
			return dataFactory.createDecimal(value);
		} else if (type.equals(XmlSchemaDataType.XSD_DOUBLE)) {
			return dataFactory.createDouble(value);
		} else if (type.equals(XmlSchemaDataType.XSD_DURATION)) {
			Duration duration = createDuration(value);

			if (duration != null) {
				return dataFactory.createDuration(duration.getSign(), duration
						.getYears(), duration.getMonths(), duration.getDays(),
						duration.getHours(), duration.getMinutes(), duration
								.getSeconds());
			}
		} else if (type.equals(XmlSchemaDataType.XSD_FLOAT)) {
			return dataFactory.createFloat(value);
		} else if (type.equals(XmlSchemaDataType.XSD_GDAY)) {
			String[] gregorian = extractGregorian(value);
			return dataFactory.createGregorianDay(gregorian[0]);
		} else if (type.equals(XmlSchemaDataType.XSD_GMONTH)) {
			String[] gregorian = extractGregorian(value);
			return dataFactory.createGregorianMonth(gregorian[0]);
		} else if (type.equals(XmlSchemaDataType.XSD_GMONTHDAY)) {
			String[] gregorian = extractGregorian(value);
			return dataFactory.createGregorianMonthDay(gregorian[0],
					gregorian[1]);
		} else if (type.equals(XmlSchemaDataType.XSD_GYEAR)) {
			String[] gregorian = extractGregorian(value);
			return dataFactory.createGregorianYear(gregorian[0]);
		} else if (type.equals(XmlSchemaDataType.XSD_GYEARMONTH)) {
			String[] gregorian = extractGregorian(value);
			return dataFactory.createGregorianYearMonth(gregorian[0],
					gregorian[1]);
		} else if (type.equals(XmlSchemaDataType.XSD_HEXBINARY)) {
			return dataFactory.createHexBinary(value.getBytes());
		} else if (type.equals(XmlSchemaDataType.XSD_INTEGER)) {
			int index = value.indexOf(".");

			// Ignore fractional part if it exists.
			if (index >= 0) {
				value = value.substring(0, index);
			}

			return dataFactory.createInteger(value);
		} else if (type.equals(XmlSchemaDataType.XSD_STRING)) {
			return dataFactory.createString(value);
		} else if (type.equals(XmlSchemaDataType.XSD_TIME)) {
			DateTime time = DateTime.parseTime(value);

			if (time != null) {
				return dataFactory.createTime(time.hour, time.minute,
						time.second, String.valueOf(time.sign), time.tzHour,
						time.tzMinute);
			}
		} else if (type.equals(XmlSchemaDataType.XSD_YEARMONTHDURATION)) {
			Duration duration = createDuration(value);

			if (duration != null) {
				return dataFactory.createYearMonthDuration(duration.getSign(),
						duration.getYears(), duration.getMonths());
			}
		} else if (RdfDatatype.PLAIN_LITERAL.isSameDatatype(type)) {
			int position = value.lastIndexOf("@");

			if (position >= 0) {
				String text = value.substring(0, position);
				String lang = "";

				if (position < (value.length() - 1)) {
					lang = value.substring(position + 1);
				}

				return dataFactory.createPlainLiteral(text, lang);
			}
		} else if (RdfDatatype.XML_LITERAL.isSameDatatype(type)) {
			// FIXME Set language.
			return dataFactory.createXMLLiteral(value, "");
		} else if (RifDatatype.IRI.isSameDatatype(type)) {
			IRI iri = wsmoFactory.createIRI(value);
			return iri;
		}

		return null;
	}

	private Duration createDuration(String value) {
		DatatypeFactory foo;
		try {
			foo = DatatypeFactory.newInstance();
			Duration duration = foo.newDuration(value);
			return duration;
		} catch (DatatypeConfigurationException e) {
		} catch (IllegalArgumentException e) {
		}

		return null;
	}

	private String[] extractGregorian(String value) {
		String[] values = value.split("Z");

		value = values[0].substring(2);
		return value.split("-");
	}

	/**
	 * A helper class to parse the lexical representation of the date and time
	 * data types.
	 * 
	 * @author Adrian Marte
	 */
	private static class DateTime {
		public String year;

		public String month;

		public String day;

		public String hour;

		public String minute;

		public String second;

		public String tzHour;

		public String tzMinute;

		// Per default 1, indicating positive time/timezone.
		public int sign = 1;

		public static DateTime parseDateTime(String value) {
			DateTime timezone = extractTimezone(value);
			value = removeTimezone(value);
			int sign = value.startsWith("-") ? -1 : 1;

			String[] parts = value.split("T");

			if (parts.length >= 2) {
				DateTime date = parseDate(parts[0]);
				DateTime time = parseTime(parts[1]);
				timezone.sign = sign;

				if (date != null && time != null) {
					timezone.year = date.year;
					timezone.month = date.month;
					timezone.day = date.day;
					timezone.hour = time.hour;
					timezone.minute = time.minute;
					timezone.second = time.second;

					return timezone;
				}
			}

			return null;
		}

		public static DateTime parseDate(String value) {
			DateTime timezone = extractTimezone(value);
			value = removeTimezone(value);

			int sign = value.startsWith("-") ? -1 : 1;

			String[] parts = value.split("-");

			if (parts.length == 3) {
				timezone.sign = sign;
				timezone.year = parts[0];
				timezone.month = parts[1];
				timezone.day = parts[2];

				return timezone;
			}

			return null;
		}

		public static DateTime parseTime(String value) {
			DateTime timezone = extractTimezone(value);
			value = removeTimezone(value);

			int sign = value.startsWith("-") ? -1 : 1;

			String[] parts = value.split(":");

			if (parts.length == 3) {
				timezone.sign = sign;
				timezone.hour = parts[0];
				timezone.minute = parts[1];
				timezone.second = parts[2];

				return timezone;
			}

			return null;
		}

		public static DateTime extractTimezone(String value) {
			if (value.endsWith("Z")) {
				value = value.substring(0, value.length() - 1);
			}

			DateTime dateTime = new DateTime();
			dateTime.tzHour = "0";
			dateTime.tzMinute = "0";

			int indexOfPlus = value.lastIndexOf("+");
			int indexOfMinus = value.lastIndexOf("-");

			String timezonePart = null;
			
			// FIXME Prepend sign to values?
			String sign = "";

			int theSign = 1;

			if (indexOfPlus >= 0) {
				timezonePart = value.substring(indexOfPlus + 1);
			} else if (indexOfMinus >= 0) {
				timezonePart = value.substring(indexOfMinus + 1);
				theSign = -1;
				sign = "-";
			} else {
				return dateTime;
			}

			String[] parts = timezonePart.split(":");
			dateTime.sign = theSign;

			if (parts.length == 2) {
				dateTime.tzHour = sign + parts[0];
				dateTime.tzMinute = sign + parts[1];
			}

			return dateTime;
		}

		public static String removeTimezone(String value) {
			if (value.endsWith("Z")) {
				return value.substring(0, value.length() - 1);
			}

			int indexOfPlus = value.lastIndexOf("+");
			int indexOfMinus = value.lastIndexOf("-");

			String timezonePart = null;

			if (indexOfPlus >= 0) {
				timezonePart = value.substring(indexOfPlus + 1);
			} else if (indexOfMinus >= 0) {
				timezonePart = value.substring(indexOfMinus + 1);
			} else {
				return value;
			}

			String[] parts = timezonePart.split(":");

			if (parts.length == 2) {
				int position = value.lastIndexOf(timezonePart);
				return value.substring(0, position - 1);
			}

			return value;
		}
	}
}
