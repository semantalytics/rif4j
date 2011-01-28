package at.sti2.rif4j.importer.rdf;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ontoware.aifbcommons.collection.ClosableIterator;
import org.ontoware.rdf2go.RDF2Go;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdf2go.model.Model;
import org.ontoware.rdf2go.model.Statement;
import org.ontoware.rdf2go.model.node.DatatypeLiteral;
import org.ontoware.rdf2go.model.node.LanguageTagLiteral;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.Variable;
import org.ontoware.rdf2go.vocabulary.RDF;
import org.ontoware.rdf2go.vocabulary.RDFS;

import at.sti2.rif4j.RdfDatatype;
import at.sti2.rif4j.RifDatatype;
import at.sti2.rif4j.condition.AtomicFormula;
import at.sti2.rif4j.condition.Attribute;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Frame;
import at.sti2.rif4j.condition.MemberAtom;
import at.sti2.rif4j.condition.SubclassAtom;
import at.sti2.rif4j.condition.Term;
import at.sti2.rif4j.importer.DocumentImporter;
import at.sti2.rif4j.importer.ImportException;
import at.sti2.rif4j.importer.UnsupportedProfileException;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Rule;

public class RdfDocumentImporter implements DocumentImporter {

	private static final Set<URI> PROFILES;

	public static final URI RDFS_PROFILE = URI
			.create("http://www.w3.org/2007/rif-import-profile#RDFS");

	public static final URI RDF_PROFILE = URI
			.create("http://www.w3.org/2007/rif-import-profile#RDF");

	public static final URI SIMPLE_PROFILE = URI
			.create("http://www.w3.org/ns/entailment/Simple");

	static {
		PROFILES = new HashSet<URI>();

		PROFILES.add(SIMPLE_PROFILE);
		PROFILES.add(RDF_PROFILE);
		PROFILES.add(RDFS_PROFILE);
	}

	@Override
	public Document importDocument(URI uri, URI profile) throws ImportException {
		if (profile == null) {
			throw new UnsupportedProfileException(
					"Default RIF profile is not supported");
		}

		if (!supports(profile)) {
			throw new UnsupportedProfileException(profile + " is not supported");
		}

		Model model = RDF2Go.getModelFactory().createModel(Reasoning.none);
		model.open();

		try {
			InputStream uriStream = uri.toURL().openStream();
			model.readFrom(uriStream);
		} catch (MalformedURLException e) {
			throw new ImportException(e);
		} catch (IOException e) {
			throw new ImportException(e);
		}

		Group group = new Group();
		List<Rule> rules = new ArrayList<Rule>();

		ClosableIterator<Statement> statements = model.findStatements(
				Variable.ANY, Variable.ANY, Variable.ANY);

		while (statements.hasNext()) {
			Statement statement = statements.next();
			AtomicFormula atomicFormula = toAtomicFormula(statement, profile);
			rules.add(atomicFormula);
		}

		statements.close();
		model.close();

		Document document = new Document();
		group.setRules(rules);
		document.setGroup(group);

		return document;
	}

	private AtomicFormula toAtomicFormula(Statement statement, URI profile) {
		Resource subject = statement.getSubject();
		org.ontoware.rdf2go.model.node.URI predicate = statement.getPredicate();
		Node object = statement.getObject();

		Constant instance = new Constant(RifDatatype.IRI.toString(), null,
				subject.toString());
		Constant name = new Constant(RifDatatype.IRI.toString(), null,
				predicate.toString());
		Term value = toTerm(object);

		AtomicFormula atomicFormula = null;

		if (profile.equals(RDFS_PROFILE)) {
			if (predicate.equals(RDF.type)) {
				atomicFormula = new MemberAtom(instance, value);
			} else if (predicate.equals(RDFS.subClassOf)) {
				atomicFormula = new SubclassAtom(instance, value);
			}
		}

		if (atomicFormula == null) {
			Attribute attribute = new Attribute(name, value);
			Frame frame = new Frame(instance,
					Collections.singletonList(attribute));
			atomicFormula = frame;
		}

		// FIXME Handle variables correctly.
		if (value instanceof at.sti2.rif4j.condition.Variable) {
			// at.sti2.rif4j.condition.Variable variable =
			// (at.sti2.rif4j.condition.Variable) value;
			// atomicFormula = new ExistsFormula(
			// Collections.singletonList(variable), atomicFormula);
		}

		return atomicFormula;
	}

	private Term toTerm(Node node) {
		// FIXME Handle blank nodes correctly.
		// if (node instanceof BlankNode) {
		// return new at.sti2.rif4j.condition.Variable("X");
		// }

		String language = null;
		String text = node.toString();
		String type = null;

		if (node instanceof LanguageTagLiteral) {
			LanguageTagLiteral literal = (LanguageTagLiteral) node;
			language = literal.getLanguageTag();
			text = literal.getValue();
		}

		if (node instanceof DatatypeLiteral) {
			DatatypeLiteral literal = (DatatypeLiteral) node;
			type = literal.getDatatype().toString();
			text = literal.getValue();
		}

		if (type == null || type.isEmpty()) {
			type = RdfDatatype.PLAIN_LITERAL.getUri();
		}

		Constant constant = new Constant(type, language, text);
		return constant;
	}

	@Override
	public boolean supports(URI profile) {
		return PROFILES.contains(profile);
	}

	public static void main(String[] args) throws ImportException,
			URISyntaxException {
		URL resource = RdfDocumentImporter.class.getClassLoader().getResource(
				"RDF_example.rdf");
		URI uri = resource.toURI();

		RdfDocumentImporter importer = new RdfDocumentImporter();

		Document document = importer.importDocument(uri,
				URI.create("http://www.w3.org/ns/entailment/RDFS"));
		System.out.println(document);
	}

}

