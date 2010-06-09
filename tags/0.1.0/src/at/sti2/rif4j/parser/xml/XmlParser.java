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
package at.sti2.rif4j.parser.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;

/**
 * Parses RIF BLD documents and formulas represented in the RIF XML syntax. This
 * parser uses the JAXP framework to create a DOM for the specified XML
 * documents and translates it to a corresponding object model. Therefore, an
 * appropriate XML parser API has to be present on the classpath.
 * 
 * @author Adrian Marte
 */
public class XmlParser {

	private boolean isValidating;

	/**
	 * Creates a new parser for RIF XML documents and formulas. By default the
	 * parser does not validate the XML documents against the XML schema of RIF
	 * XML.
	 */
	public XmlParser() {
		this(false);
	}

	/**
	 * Creates a new parser for RIF XML documents and formulas.
	 * 
	 * @param isValidating If set to <code>true</code>, the parser first
	 *            validates the specified XML document against the XML schemo of
	 *            RIF XML.
	 */
	public XmlParser(boolean isValidating) {
		this.isValidating = isValidating;
	}

	/**
	 * Parses a RIF Document using the specified reader.
	 * 
	 * @param reader The reader which serves the document.
	 * @return A Document object representing the specified RIF document.
	 * @throws SAXException If a SAX error or warning occurs.
	 * @throws IOException If an I/O error or waring occurs.
	 * @throws ParserConfigurationException Indicates a configuration error.
	 */
	public Document parseDocument(Reader reader) throws SAXException,
			IOException, ParserConfigurationException {
		org.w3c.dom.Document xmlDocument = parseXml(reader);

		if (isValidating) {
			validate(xmlDocument);
		}

		XmlExtractor extractor = new XmlExtractor();
		Document document = extractor.extractDocument(xmlDocument);

		return document;
	}

	/**
	 * Parses a RIF Formula using the specified reader.
	 * 
	 * @param reader The reader which serves the formula.
	 * @return A Formula object representing the specified RIF formula.
	 * @throws SAXException If a SAX error or warning occurs.
	 * @throws IOException If an I/O error or waring occurs.
	 * @throws ParserConfigurationException Indicates a configuration error.
	 */
	public Formula parseFormula(Reader reader)
			throws ParserConfigurationException, SAXException, IOException {
		org.w3c.dom.Document xmlDocument = parseXml(reader);

		if (isValidating) {
			validate(xmlDocument);
		}

		XmlExtractor extractor = new XmlExtractor();
		Collection<Formula> formulas = extractor.extractFormulas(xmlDocument);

		if (formulas.size() > 0) {
			return formulas.iterator().next();
		}

		return null;
	}

	private org.w3c.dom.Document parseXml(Reader reader)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);

		InputSource source = new InputSource(reader);

		DocumentBuilder builder = factory.newDocumentBuilder();
		org.w3c.dom.Document xmlDocument = builder.parse(source);

		return xmlDocument;
	}

	private void validate(org.w3c.dom.Document document) throws SAXException,
			IOException {
		// FIXME Validation does somehow not work yet.
		
		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		InputStream condSchemaInput = openSchema("BLDCond.xsd");
		InputStream ruleSchemaInput = openSchema("BLDRule.xsd");
		Source condSchema = new StreamSource(condSchemaInput);
		Source ruleSchema = new StreamSource(ruleSchemaInput);

		try {
			Schema schema = factory.newSchema(new Source[] { condSchema,
					ruleSchema });
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} finally {
			condSchemaInput.close();
			ruleSchemaInput.close();
		}
	}

	private InputStream openSchema(String fileName) {
		InputStream input = getClass().getResourceAsStream(fileName);
		return input;
	}

}
