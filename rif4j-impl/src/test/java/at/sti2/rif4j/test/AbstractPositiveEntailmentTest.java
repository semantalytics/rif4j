package at.sti2.rif4j.test;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import at.sti2.rif4j.test.suite.ObjectFactory;
import at.sti2.rif4j.test.suite.PositiveEntailmentTest;
import at.sti2.rif4j.test.suite.TestSuite;

@RunWith(Parameterized.class)
public abstract class AbstractPositiveEntailmentTest extends
		AbstractEntailmentTest {

	public AbstractPositiveEntailmentTest(String testName, URI premiseUri,
			URI conclusionUri, boolean expectedEvaluation) {
		super(testName, premiseUri, conclusionUri, expectedEvaluation);
	}

	public static final String RIF_BLD_URL = "http://www.w3.org/2005/rules/test/repository/BLDTests.xml";

	public static final boolean EXPECTED_EVALUATION = true;

	@Parameters
	public static Collection<Object[]> data() throws FileNotFoundException,
			JAXBException, MalformedURLException {
		List<Object[]> data = new ArrayList<Object[]>();

		JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		TestSuite suite = (TestSuite) unmarshaller.unmarshal(new URL(
				RIF_BLD_URL));

		List<Object> testCases = suite.getTestCase();

		for (Object testCase : testCases) {
			if (testCase instanceof PositiveEntailmentTest) {
				PositiveEntailmentTest positiveTestCase = (PositiveEntailmentTest) testCase;

				List<String> premiseUrls = positiveTestCase
						.getPremiseDocument().getNormative().getRemote();
				List<String> conclusionUrls = positiveTestCase
						.getConclusionDocument().getNormative().getRemote();

				if (premiseUrls.size() > 0 && conclusionUrls.size() > 0) {
					String testName = positiveTestCase.getId();
					URI premiseUrl = URI.create(premiseUrls.get(0));
					URI conclusionUrl = URI.create(conclusionUrls.get(0));

					data.add(new Object[] { testName, premiseUrl,
							conclusionUrl, EXPECTED_EVALUATION });
				}
			}
		}

		return data;
	}

}
