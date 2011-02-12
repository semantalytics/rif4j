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
package at.sti2.rif4j;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;

/**
 * This class needs to be marked with {@link Ignore}, otherwise the Maven
 * Surefire plug-in handles this class like a JUnit test, due to its "Test"
 * prefix.
 * 
 * @author Iker Larizgoitia Abad
 */
@Ignore
public class TestUtils {

	private static Logger logger = LoggerFactory.getLogger(TestUtils.class);

	public static URI getFileUri(String fileName) {
		try {
			// Check if this is already a URL.
			URL url = new URL(fileName);
			return url.toURI();
		} catch (MalformedURLException e) {
		} catch (URISyntaxException e) {
		}

		URL url = TestUtils.class.getClassLoader().getResource(fileName);

		if (url == null) {
			logger.error("Could not find " + fileName);
			return null;
		}

		try {
			return url.toURI();
		} catch (URISyntaxException e) {
			logger.error("Invalid URI", e);
		}

		return null;
	}

	public static Reader getFileReader(String fileName) {
		URI uri = getFileUri(fileName);

		if (uri == null) {
			return null;
		}
		
		try {
			InputStream input = uri.toURL().openStream();
			return new InputStreamReader(input);
		} catch (IOException e) {
			logger.error("Could not load " + fileName);
		}

		return null;
	}

	public static String[] getRIFTestFiles(String path) {
		File dir = new File(path);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith("rif"));
			}
		};

		return dir.list(filter);
	}

	public static Document parseDocument(String fileName) {
		XmlParser parser = new XmlParser();

		Reader reader = getFileReader(fileName);
		Document document = null;

		try {
			document = parser.parseDocument(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return document;
	}

	public static Rule parseRule(String fileName) {
		XmlParser parser = new XmlParser();

		Reader reader = getFileReader(fileName);
		Rule rule = null;

		try {
			rule = parser.parseRule(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rule;
	}

	public static Formula parseFormula(String fileName) {
		XmlParser parser = new XmlParser();

		Reader reader = getFileReader(fileName);
		Formula formula = null;

		try {
			formula = parser.parseFormula(reader);
		} catch (Exception e) {
			logger.error("Failed to parse " + fileName, e);
		}

		return formula;
	}

}