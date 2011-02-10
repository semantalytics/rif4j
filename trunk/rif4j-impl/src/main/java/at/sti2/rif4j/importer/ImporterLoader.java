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
public class ImporterLoader {

	private ServiceLoader<DocumentImporter> loader;

	/**
	 * Creates a new {@link ImporterLoader}, that loads the classes using it's
	 * own {@link ClassLoader}.
	 */
	public ImporterLoader() {
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
