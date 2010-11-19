package at.sti2.rif4j.translator.iris;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;
import org.xml.sax.SAXException;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;

public class RifToIrisTranslator
{
	Map<IPredicate, IRelation> facts;
	List<IRule> rules;
	 
	public void translate(Reader rifXmlFileReader) 
	{		
		XmlParser parser = new XmlParser(true);
		Document rifDocument =null;
		
		try
		{
			rifDocument = parser.parseDocument(rifXmlFileReader);
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (rifDocument == null)
			return;  //TODO Treat error
		
		
		facts = new HashMap<IPredicate, IRelation>();
		rules = new ArrayList<IRule>();
		
		RifToIrisVisitor visitor = new RifToIrisVisitor(facts, rules);				
		rifDocument.accept(visitor);				
		
		this.facts = visitor.getFacts();
		this.rules = visitor.getRules();				
	}

	public Map<IPredicate, IRelation> getFacts()
	{
		return facts;
	}

	public List<IRule> getRules()
	{
		return rules;
	}
}
