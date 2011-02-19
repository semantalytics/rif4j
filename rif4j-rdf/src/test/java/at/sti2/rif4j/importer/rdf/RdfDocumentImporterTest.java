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
package at.sti2.rif4j.importer.rdf;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.sti2.rif4j.importer.DocumentImporter;
import at.sti2.rif4j.importer.ImportException;

public class RdfDocumentImporterTest {

	private DocumentImporter importer;

	@Before
	public void setUp() throws Exception {
		importer = new RdfDocumentImporter();
	}

	@Test
	public void testImportDocument() throws URISyntaxException, ImportException {
		URL resource = RdfDocumentImporter.class.getClassLoader().getResource(
				"RDF_example.rdf");
		URI uri = resource.toURI();

		// If there is no error, we assume the import works correctly.
		// FIXME Implement a real test.
		importer.importDocument(uri, RdfDocumentImporter.RDFS_PROFILE);
	}

	@Test
	public void testSupports() {
		Assert.assertTrue(importer.supports(RdfDocumentImporter.SIMPLE_PROFILE));
		Assert.assertTrue(importer.supports(RdfDocumentImporter.RDF_PROFILE));
		Assert.assertTrue(importer.supports(RdfDocumentImporter.RDFS_PROFILE));
	}

}
