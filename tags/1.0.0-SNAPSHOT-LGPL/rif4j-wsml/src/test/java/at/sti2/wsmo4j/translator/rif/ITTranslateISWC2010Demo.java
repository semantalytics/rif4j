package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Test the translation of the RuleML2010 Challenge demo example.
 */
public class ITTranslateISWC2010Demo extends AbstractITTranslate {

	@Test
	public void testVideoOnDemandCondition() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(6, "ISWC2010_Demo.xml");
	}

}
