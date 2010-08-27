package at.sti2.wsmo4j.translator.rif;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Test the translation of the RuleML2010 Challenge demo example.
 */
public class ITTranslateRuleML2010ChallengeDemo extends AbstractITTranslate {

	@Test
	public void testRuleML2010ChallengeDemo() throws SAXException, IOException,
			ParserConfigurationException {
		assertNumberOfExpressions(2, "RuleML2010_Challenge_Demo.xml");
	}

}
