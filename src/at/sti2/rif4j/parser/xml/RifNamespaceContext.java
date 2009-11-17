/*
 * Copyright (c) 2009, STI Innsbruck
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
