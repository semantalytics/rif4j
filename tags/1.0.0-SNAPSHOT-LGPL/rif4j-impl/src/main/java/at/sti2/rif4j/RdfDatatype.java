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
package at.sti2.rif4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Adrian Marte
 */
public enum RdfDatatype {

	PLAIN_LITERAL("PlainLiteral"),

	XML_LITERAL("XMLLiteral");

	private String uri;

	private RdfDatatype(String name) {
		this(Namespaces.RDF_NAMESPACE, name);
	}

	private RdfDatatype(String namespace, String name) {
		this.uri = namespace + name;
	}

	public String getUri() {
		return this.uri;
	}

	public boolean isSameDatatype(String iri) {
		return getUri().equals(iri);
	}

	@Override
	public String toString() {
		return getUri();
	}

	public static RdfDatatype forUri(String uri) {
		return Lookup.table.get(uri);
	}

	public static boolean isDatatype(String uri) {
		return Lookup.table.containsKey(uri);
	}

	/**
	 * Maps the URI of each datatype to the datatype itself.
	 */
	private static class Lookup {

		private static final Map<String, RdfDatatype> table;

		static {
			table = new HashMap<String, RdfDatatype>();

			for (RdfDatatype builtin : RdfDatatype.values()) {
				table.put(builtin.getUri(), builtin);
			}
		}

	}

}
