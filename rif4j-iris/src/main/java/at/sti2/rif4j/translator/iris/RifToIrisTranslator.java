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

/**
 * @author Iker Larizgoitia Abad
 */
public class RifToIrisTranslator {

	Map<IPredicate, IRelation> facts;

	List<IRule> rules;

	public void translate(Reader rifXmlFileReader) {
		XmlParser parser = new XmlParser(true);
		Document rifDocument = null;

		try {
			rifDocument = parser.parseDocument(rifXmlFileReader);
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

		if (rifDocument == null)
			return; // TODO Treat error

		facts = new HashMap<IPredicate, IRelation>();
		rules = new ArrayList<IRule>();

		RifToIrisVisitor visitor = new RifToIrisVisitor(facts, rules);
		rifDocument.accept(visitor);

		this.facts = visitor.getFacts();
		this.rules = visitor.getRules();
	}

	public Map<IPredicate, IRelation> getFacts() {
		return facts;
	}

	public List<IRule> getRules() {
		return rules;
	}

}
