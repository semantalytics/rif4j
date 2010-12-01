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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import at.sti2.rif4j.parser.xml.XmlParser;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Rule;

/**
 * @author Iker Larizgoitia Abad
 */
public class TestUtils {

	public static Reader getFileReader(String fileName) {
		return new InputStreamReader(TestUtils.class.getClassLoader()
				.getResourceAsStream(fileName));

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

	public static URI getFileURI(String fileName) {
		try {
			return TestUtils.class.getClassLoader().getResource(fileName)
					.toURI();
		} catch (NullPointerException npe) {
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
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

}