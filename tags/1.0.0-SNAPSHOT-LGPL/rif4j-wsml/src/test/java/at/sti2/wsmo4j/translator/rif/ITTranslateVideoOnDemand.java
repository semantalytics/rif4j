package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Tests translation of RIF encoded WSML logical expression embedded in example
 * for WSMO-Lite specification.
 */
public class ITTranslateVideoOnDemand extends AbstractITTranslate {

	@Test
	public void testVideoOnDemandCondition() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressionsOfFormula(1, "VideoOnDemand_Condition.xml");
	}

	@Test
	public void testVideoOnDemandEffect() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressionsOfFormula(1, "VideoOnDemand_Effect.xml");
	}

}
