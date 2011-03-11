package at.sti2.rif4j.importer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the list of profiles
 * 
 * @author adrmar
 * 
 */
public enum Profile {

	SIMPLE("Simple"),

	RDF("RDF"),

	RDFS("RDFS"),

	D("D"),

	OWL_DIRECT("OWL Direct", "OWL-Direct"),

	OWL_RDF_BASED("OWL RDF-Based", "OWL-RDF-Based");

	private static final String PREFIX = "http://www.w3.org/ns/entailment/";

	private String name;

	private String uri;

	private Profile(String name) {
		this(name, name);
	}

	private Profile(String name, String suffix) {
		this.name = name;
		this.uri = PREFIX + suffix;
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}

	public URI toUri() {
		return URI.create(uri);
	}

	public static Profile forUri(String uri) {
		return Lookup.table.get(uri);
	}

	/**
	 * Maps the URI of a profile to the profile itself.
	 */
	private static class Lookup {

		private static final Map<String, Profile> table;

		static {
			table = new HashMap<String, Profile>();

			for (Profile profile : Profile.values()) {
				table.put(profile.getUri(), profile);
			}
		}

	}

}
