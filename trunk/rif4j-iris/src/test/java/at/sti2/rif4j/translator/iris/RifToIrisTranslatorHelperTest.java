package at.sti2.rif4j.translator.iris;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import at.sti2.rif4j.TestUtils;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.ForallFormula;
import at.sti2.rif4j.rule.ImpliesFormula;

public class RifToIrisTranslatorHelperTest {

	Document rifDocument = null;
	RifToIrisTranslatorHelper helper = null;
	
	@Before
	public void setUp() throws Exception {
		String rifXmlFileName = "core/document.rif";

		Reader rifXmlFileReader = TestUtils.getFileReader(rifXmlFileName);
		assertNotNull("Test file " + rifXmlFileName + " could not be found",
				rifXmlFileReader);

		XmlParser parser = new XmlParser(true);
		rifDocument = parser.parseDocument(rifXmlFileReader);
		assertNotNull(rifDocument);
		
		helper = new RifToIrisTranslatorHelper();
		
	}

	@After
	public void tearDown() throws Exception {
		rifDocument = null;
		helper = null;
	}

	@Test
	public void testTranslateHead() throws SAXException, IOException, ParserConfigurationException
	{		
		ForallFormula formula = (ForallFormula) rifDocument.getGroup().getAllRules().get(0);
		assertNotNull(formula);
		
		ImpliesFormula implies = (ImpliesFormula) formula.getClause();		
		
		// Head
		ArrayList<ILiteral> irisHead = new ArrayList<ILiteral>();
		for (AtomicFormula atomicFormula : implies.getHead())
		{
			ILiteral irisLiteral = helper.translateAtomicFormula(atomicFormula);
			irisHead.add(irisLiteral);
		}
		
		String expectedHead = "http://example.org/concepts#reject(?store, ?item)";		
		assertEquals(expectedHead,irisHead.get(0).toString());		
	}
	
	@Test
	public void testTranslateBody() throws SAXException, IOException, ParserConfigurationException
	{		
		ForallFormula formula = (ForallFormula) rifDocument.getGroup().getAllRules().get(0);
		assertNotNull(formula);
		
		ImpliesFormula implies = (ImpliesFormula) formula.getClause();		
		
		// Body
		List<ILiteral> irisBody = helper.translateFormula(implies.getBody());
				
		ArrayList<String> expectedBody = new ArrayList<String>();
		expectedBody.add("http://example.org/concepts#perishable(?item)");
		expectedBody.add("http://example.org/concepts#delivered(?item, ?deliverydate, ?store)");
		expectedBody.add("http://example.org/concepts#scheduled(?item, ?scheduledate)");
		expectedBody.add("IS_DATETIME(?deliverydate)");
		assertEquals(expectedBody.toString(),irisBody.toString());		
	}
	
}
