package at.sti2.rif4j.serializer.xml;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Iker Larizgoitia Abad
 */
public class XmlHandlerBase {

	protected boolean useValidation = false;

	protected static final String BLD_RULE_XSD = "BLDRule.xsd";

	protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	protected static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	protected static final String RIF_BLD_SCHEMA = "BLDRule.xsd";

	protected XmlHandlerBase() {
	}

	protected XmlHandlerBase(boolean useValidation) {
		this.useValidation = useValidation;
	}

	protected void validate(org.w3c.dom.Document document,
			String schemaResourceName) throws SAXException, IOException {
		URL schemaUrl = this.getClass().getClassLoader()
				.getResource(schemaResourceName);

		SchemaFactory factory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = factory.newSchema(schemaUrl);

		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(document));
	}

	protected DocumentBuilder getDocumentBuilder()
			throws ParserConfigurationException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();

		builderFactory.setNamespaceAware(true);
		builderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

		return builderFactory.newDocumentBuilder();
	}

	protected Document getNewDocument() throws ParserConfigurationException {
		DocumentBuilder builder = this.getDocumentBuilder();
		return builder.newDocument();
	}
	
}