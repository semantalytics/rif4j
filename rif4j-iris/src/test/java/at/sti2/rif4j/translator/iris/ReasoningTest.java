package at.sti2.rif4j.translator.iris;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;

public class ReasoningTest {

	Configuration configuration = null;

	@Before
	public void setUp() throws Exception {
		configuration = org.deri.iris.KnowledgeBaseFactory
				.getDefaultConfiguration();
	}

	@After
	public void tearDown() throws Exception {
		configuration = null;
	}

	@Test
	public void testReasoning() {
		String rifXmlFileName = "Class_Membership.xml";

		Reader rifXmlFileReader = TestUtils.getFileReader(rifXmlFileName);
		assertNotNull("Test file " + rifXmlFileName + " could not be found",
				rifXmlFileReader);

		RifToIrisTranslator translator = new RifToIrisTranslator();
		translator.translate(rifXmlFileReader);

		assertTrue("Rules were not found", translator.getRules().size() > 0);
		assertTrue("Factos were not found", translator.getFacts().size() > 0);

		try {
			Configuration configuration = org.deri.iris.KnowledgeBaseFactory
					.getDefaultConfiguration();

			Map<IPredicate, IRelation> facts = translator.getFacts();
			List<IRule> rules = translator.getRules();

			IKnowledgeBase irisKnowledgeBase = KnowledgeBaseFactory
					.createKnowledgeBase(facts, rules, configuration);

			IQuery query = Factory.BASIC.createQuery(createLiteral(
					"http://example.com/concepts#reject",
					"http://example.com/instances#John",
					"http://example.com/instances#Milk"));
			IRelation relation = irisKnowledgeBase.execute(query);
			assertNotNull(relation);

			relation.toString();
		} catch (EvaluationException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Creates a positive literal out of a predicate name and a set of variable
	 * strings.
	 * 
	 * @param pred
	 *            the predicate name
	 * @param vars
	 *            the variable names
	 * @return the constructed literal
	 * @throws NullPointerException
	 *             if the predicate name or the set of variable names is
	 *             {@code null}
	 * @throws NullPointerException
	 *             if the set of variable names contains {@code null}
	 * @throws IllegalArgumentException
	 *             if the name of the predicate is 0 characters long
	 */
	private static ILiteral createLiteral(final String pred,
			final String... vars) {
		if ((pred == null) || (vars == null)) {
			throw new NullPointerException(
					"The predicate and the vars must not be null");
		}
		if (pred.length() <= 0) {
			throw new IllegalArgumentException(
					"The predicate name must be longer than 0 chars");
		}
		if (Arrays.asList(vars).contains(null)) {
			throw new NullPointerException("The vars must not contain null");
		}

		return BASIC.createLiteral(true,
				BASIC.createPredicate(pred, vars.length),
				BASIC.createTuple(new ArrayList<ITerm>(createVarList(vars))));
	}

	/**
	 * Creates a list of IVariables out of a list of strings.
	 * 
	 * @param vars
	 *            the variable names
	 * @return the list of correspoinding variables
	 * @throws NullPointerException
	 *             if the vars is null, or contains null
	 */
	private static List<IVariable> createVarList(final String... vars) {
		if ((vars == null) || Arrays.asList(vars).contains(null)) {
			throw new NullPointerException(
					"The vars must not be null and must not contain null");
		}
		final List<IVariable> v = new ArrayList<IVariable>(vars.length);
		for (final String var : vars) {
			v.add(TERM.createVariable(var));
		}
		return v;
	}

	@Ignore
	@Test
	public void testPresentationForm() {
		XmlParser parser = new XmlParser(true);
		Document rifDocument = null;

		String rifXmlFileName = "Class_Membership.xml";
		Reader rifXmlFileReader = TestUtils.getFileReader(rifXmlFileName);

		try {
			rifDocument = parser.parseDocument(rifXmlFileReader);
			// System.out.println(rifDocument.toString());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}