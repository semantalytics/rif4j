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
package at.sti2.rif4j.importer;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Loads all classes on the classpath that implement {@link DocumentImporter}.
 * The JAR files containing these classes need to be registered by creating a
 * provider configuration file in the JAR file's META-INF/services directory, as
 * described in
 * http://java.sun.com/developer/technicalArticles/javase/extensible/index.html.
 * 
 * @author Adrian Marte
 */
public class DocumentImporterLoader {

	private ServiceLoader<DocumentImporter> loader;

	/**
	 * Creates a new {@link DocumentImporterLoader}, that loads the classes using it's
	 * own {@link ClassLoader}.
	 */
	public DocumentImporterLoader() {
		loader = ServiceLoader.load(DocumentImporter.class, getClass()
				.getClassLoader());
	}

	/**
	 * Loads all classes on the classpath that implement
	 * {@link DocumentImporter}.
	 * 
	 * @return Instances of all the classes on the classpath that implement
	 *         {@link DocumentImporter}.
	 */
	public Set<DocumentImporter> loadAll() {
		Set<DocumentImporter> importers = new HashSet<DocumentImporter>();

		for (DocumentImporter importer : loader) {
			importers.add(importer);
		}

		return importers;
	}

}
