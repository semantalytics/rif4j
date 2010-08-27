package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

public class ITTranslateEqualityInConditionPremise extends AbstractITTranslate {

	@Test
	public void testVideoOnDemandCondition() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(1, "Equality_in_condition-premise.xml");
	}

}
