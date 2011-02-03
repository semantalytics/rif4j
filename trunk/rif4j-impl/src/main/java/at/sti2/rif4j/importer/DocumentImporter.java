package at.sti2.rif4j.importer;

import java.net.URI;

import at.sti2.rif4j.rule.Document;

public interface DocumentImporter {

	public boolean supports(URI profile);

	public Document importDocument(URI uri, URI profile)
			throws ImportException, UnsupportedProfileException;

}
