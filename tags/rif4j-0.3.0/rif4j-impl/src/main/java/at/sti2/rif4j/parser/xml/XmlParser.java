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
package at.sti2.rif4j.parser.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;
import at.sti2.rif4j.serializer.xml.XmlHandlerBase;

/**
 * Parses RIF BLD documents and formulas represented in the RIF XML syntax. This
 * parser uses the JAXP framework to create a DOM for the specified XML document
 * and translates it to a corresponding object model. Therefore, an appropriate
 * XML parser API has to be present on the classpath.
 * 
 * @author Adrian Marte
 * @author Iker Larizgoitia Abad
 */
public class XmlParser extends XmlHandlerBase {

	/**
	 * Creates a new parser for RIF XML documents and formulas. By default the
	 * parser does not validate the XML documents against the XML schema of RIF
	 * XML.
	 */
	public XmlParser() {
		super();
	}

	/**
	 * Creates a new parser for RIF XML documents and formulas. Depending on the
	 * value of <code>isValidating</code> the parser also validates the XML
	 * document against the XML schemo of the RIF XML syntax.
	 * 
	 * @param useValidation
	 *            If set to <code>true</code>, the parser first validates the
	 *            specified XML document against the XML schemo of RIF XML.
	 */
	public XmlParser(boolean useValidation) {
		super(useValidation);
	}

	/**
	 * Parses a RIF Document using the specified reader.
	 * 
	 * @param reader
	 *            The reader which serves the document.
	 * @return A Document object representing the specified RIF document.
	 * @throws SAXException
	 *             If a SAX error or warning occurs.
	 * @throws IOException
	 *             If an I/O error or waring occurs.
	 * @throws ParserConfigurationException
	 *             Indicates a configuration error.
	 */
	public Document parseDocument(Reader reader) throws SAXException,
			IOException, ParserConfigurationException {
		org.w3c.dom.Document xmlDocument = parseXml(reader);

		XmlExtractor extractor = new XmlExtractor();
		Document document = extractor.extractDocument(xmlDocument);

		return document;
	}

	/**
	 * Parses a RIF Formula using the specified reader.
	 * 
	 * @param reader
	 *            The reader which serves the formula.
	 * @return A Formula object representing the specified RIF formula.
	 * @throws SAXException
	 *             If a SAX error or warning occurs.
	 * @throws IOException
	 *             If an I/O error or waring occurs.
	 * @throws ParserConfigurationException
	 *             Indicates a configuration error.
	 */
	public Formula parseFormula(Reader reader)
			throws ParserConfigurationException, SAXException, IOException {
		org.w3c.dom.Document xmlDocument = parseXml(reader);

		XmlExtractor extractor = new XmlExtractor();
		Collection<Formula> formulas = extractor.extractFormulas(xmlDocument);

		if (formulas.size() > 0) {
			return formulas.iterator().next();
		}

		return null;
	}

	/**
	 * Parses a RIF rule using the specified reader.
	 * 
	 * @param reader
	 *            The reader which serves the rule.
	 * @return A Rule object representing the specified RIF rule.
	 * @throws SAXException
	 *             If a SAX error or warning occurs.
	 * @throws IOException
	 *             If an I/O error or waring occurs.
	 * @throws ParserConfigurationException
	 *             Indicates a configuration error.
	 */
	public Rule parseRule(Reader reader) throws ParserConfigurationException,
			SAXException, IOException {
		org.w3c.dom.Document xmlDocument = parseXml(reader);

		XmlExtractor extractor = new XmlExtractor();
		Rule rule = extractor.extractRule(xmlDocument);

		return rule;
	}

	public org.w3c.dom.Document parseXml(Reader reader)
			throws ParserConfigurationException, SAXException, IOException {
		InputSource source = new InputSource(reader);

		DocumentBuilder builder = this.getDocumentBuilder();
		org.w3c.dom.Document xmlDocument = builder.parse(source);

		if (useValidation) {
			this.validate(xmlDocument, BLD_RULE_XSD);
		}

		this.removeOrderedArgsAttribute(xmlDocument);

		return xmlDocument;
	}

	/**
	 * Removes the attribute ordered from xml elements in a given xml document
	 * This attribute is optional and has a fixed value, so it is not necessary
	 * and it is simpler not to have it (and better for testing purposes)
	 * 
	 * @param xmlDocument
	 *            The xml document from which to remove the ordered attribute in
	 *            the args element
	 */
	private void removeOrderedArgsAttribute(org.w3c.dom.Document xmlDocument) {
		NodeList nodeList = xmlDocument.getElementsByTagName("args");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			NamedNodeMap attributes = node.getAttributes();
			if (attributes.getNamedItem("ordered") != null)
				attributes.removeNamedItem("ordered");
		}
	}

}
