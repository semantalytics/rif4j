package at.sti2.rif4j.reasoner.iris;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.deri.iris.api.basics.IRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.manager.DocumentLoadingException;
import at.sti2.rif4j.manager.DocumentManager;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.translator.iris.RifToIrisTranslator;

/**
 * Loads the RDF Simple Entailment axioms as defined in
 * http://www.w3.org/2005/rules/wiki/SWC#Appendix:_Embeddings_.28Informative.29.
 */
public class RdfSimpleAxioms {

	private static final String AXIOMS_FILE = "simple-axioms.xml";

	private static final Logger logger = LoggerFactory
			.getLogger(RdfSimpleAxioms.class);

	private static Document document;

	static {
		URL axiomsFile = RdfSimpleAxioms.class.getClassLoader().getResource(
				AXIOMS_FILE);

		DocumentManager manager = new DocumentManager();

		try {
			document = manager.loadDocument(axiomsFile.toURI());
		} catch (DocumentLoadingException e) {
			logger.error("Failed to load document", e);
		} catch (URISyntaxException e) {
			logger.error("Found malformed URI", e);
		}
	}

	public static List<IRule> getRules() {
		if (document != null) {
			RifToIrisTranslator translator = new RifToIrisTranslator();
			translator.translate(document);

			return translator.getRules();
		}

		return null;
	}

}
