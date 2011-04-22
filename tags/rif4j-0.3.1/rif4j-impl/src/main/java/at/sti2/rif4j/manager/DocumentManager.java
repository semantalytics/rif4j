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
package at.sti2.rif4j.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.importer.DocumentImportException;
import at.sti2.rif4j.importer.DocumentImporter;
import at.sti2.rif4j.importer.DocumentImporterLoader;
import at.sti2.rif4j.importer.UnsupportedProfileException;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Import;
import at.sti2.rif4j.rule.Rule;

/**
 * A {@link DocumentManager} loads a RIF-BLD document and all documents it
 * imports using the {@link DocumentImporter}s present on the classpath.
 * 
 * @author Adrian Marte
 */
public class DocumentManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final Set<DocumentImporter> importers;

	static {
		DocumentImporterLoader loader = new DocumentImporterLoader();
		importers = loader.loadAll();
	}

	public static boolean supports(URI profile) {
		// null represents the default RIF-BLD profile.
		if (profile == null) {
			return true;
		}

		for (DocumentImporter importer : importers) {
			if (importer.supports(profile)) {
				return true;
			}
		}

		return false;
	}

	public Formula loadFormula(URI uri) throws DocumentLoadingException {
		try {
			InputStreamReader reader = new InputStreamReader(uri.toURL()
					.openStream());

			XmlParser parser = new XmlParser(true);
			return parser.parseFormula(reader);
		} catch (MalformedURLException e) {
			throw new DocumentLoadingException(e);
		} catch (IOException e) {
			throw new DocumentLoadingException(e);
		} catch (ParserConfigurationException e) {
			throw new DocumentLoadingException(e);
		} catch (SAXException e) {
			throw new DocumentLoadingException(e);
		}
	}

	public Document loadDocument(URI uri) throws DocumentLoadingException {
		try {
			InputStreamReader reader = new InputStreamReader(uri.toURL()
					.openStream());
			return loadDocument(reader);
		} catch (MalformedURLException e) {
			throw new DocumentLoadingException(e);
		} catch (IOException e) {
			throw new DocumentLoadingException(e);
		}
	}

	public Document loadDocument(File file) throws DocumentLoadingException {
		try {
			FileReader reader = new FileReader(file);
			return loadDocument(reader);
		} catch (FileNotFoundException e) {
			throw new DocumentLoadingException(e);
		}
	}

	public Document loadDocument(Reader reader) throws DocumentLoadingException {
		XmlParser parser = new XmlParser(true);
		Document document = null;

		try {
			// Parse the document.
			document = parser.parseDocument(reader);
		} catch (SAXException e) {
			throw new DocumentLoadingException(e);
		} catch (IOException e) {
			throw new DocumentLoadingException(e);
		} catch (ParserConfigurationException e) {
			throw new DocumentLoadingException(e);
		}

		List<Group> importedGroups = new ArrayList<Group>();
		List<Import> imports = document.getImports();

		// Try to import all imported documents.
		for (Import imprt : imports) {
			// Create a URI for the location.
			String locationString = imprt.getLocation();
			URI location = URI.create(locationString);

			// Create a URI for the profile.
			String profileString = imprt.getProfile();
			URI profile = profileString != null ? URI.create(profileString)
					: null;

			try {
				boolean foundImporter = false;

				for (DocumentImporter importer : importers) {
					// Check if the importer supports the profile.
					if (importer.supports(profile)) {
						try {
							logger.debug("Importing " + location);

							// Import/load the imported document.
							Document importedDocument = importer
									.importDocument(location, profile);

							Group group = importedDocument.getGroup();

							// If the imported document contains a group
							// formula, we add it to the list of imported
							// groups.
							if (group != null) {
								importedGroups.add(group);
							}

							// We have found a document importer capable of
							// loading the imported document. Therefore, we can
							// stop looking for an importer.
							foundImporter = true;
							break;
						} catch (UnsupportedProfileException e) {
							throw new DocumentLoadingException(
									"Profile of imported document is not supported",
									e);
						} catch (DocumentImportException e) {
							throw new DocumentLoadingException("Document ("
									+ location + ") could not be imported", e);
						}
					}
				}

				if (!foundImporter && logger.isDebugEnabled()) {
					logger.warn("Could not find document importer for "
							+ location + " with profile " + profile);
				}
			} catch (IllegalArgumentException e) {

			}
		}

		if (importedGroups.size() > 0) {
			if (document.getGroup() == null) {
				document.setGroup(new Group(new ArrayList<Rule>(),
						importedGroups));
			} else {
				document.getGroup().getGroups().addAll(importedGroups);
			}
		}

		return document;
	}

}
