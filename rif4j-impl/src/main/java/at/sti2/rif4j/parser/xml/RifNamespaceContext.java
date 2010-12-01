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
package at.sti2.rif4j.parser.xml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import at.sti2.rif4j.Namespaces;

/**
 * Resolves the namespaces for RIF XML documents.
 * 
 * @author Adrian Marte
 */
public class RifNamespaceContext implements NamespaceContext {

	private static Map<String, String> prefixes;

	private static Map<String, String> uris;

	static {
		prefixes = new HashMap<String, String>();
		uris = new HashMap<String, String>();

		prefixes.put("rif", Namespaces.RIF_NAMESPACE);
		uris.put(Namespaces.RIF_NAMESPACE, "rif");

		prefixes.put("xml", Namespaces.XML_NAMESPACE);
		uris.put(Namespaces.XML_NAMESPACE, "xml");
	}

	public String getNamespaceURI(String prefix) {
		return prefixes.get(prefix);
	}

	public String getPrefix(String namespaceURI) {
		return uris.get(namespaceURI);
	}

	public Iterator<Object> getPrefixes(String namespaceURI) {
		String prefix = uris.get(namespaceURI);

		if (prefix != null) {
			return Arrays.asList(new Object[] { prefix }).iterator();
		}

		return null;
	}

}
