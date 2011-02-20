package at.sti2.rif4j.importer;

import java.net.URI;

import at.sti2.rif4j.manager.DocumentLoadingException;
import at.sti2.rif4j.manager.DocumentManager;
import at.sti2.rif4j.rule.Document;

/**
 * Imports RIF-BLD documents in the RIF XML syntax. Uses {@link DocumentManager}
 * to transitively import other documents.
 * 
 * @author Adrian Marte
 */
public class DefaultDocumentImporter implements DocumentImporter {

	@Override
	public boolean supports(URI profile) {
		return profile == null;
	}

	@Override
	public Document importDocument(URI location, URI profile)
			throws DocumentImportException, UnsupportedProfileException {
		DocumentManager manager = new DocumentManager();

		try {
			return manager.loadDocument(location);
		} catch (DocumentLoadingException e) {
			throw new DocumentImportException(e);
		}
	}

}
