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
package at.sti2.rif4j.reasoner;

import java.util.Collections;

import at.sti2.rif4j.condition.Formula;
import at.sti2.rif4j.rule.Document;
import at.sti2.rif4j.rule.Group;
import at.sti2.rif4j.rule.Rule;

/**
 * Subclasses of this class only need to implement
 * {@link Reasoner#entails(Document, Document)} and
 * {@link Reasoner#query(Document, Rule)}.
 * 
 * @author Adrian Marte
 */
public abstract class AbstractReasoner implements Reasoner {

	@Override
	public boolean entails(Group phi, Formula psi) {
		Document phiDocument = toDocument(phi);

		return entails(phiDocument, psi);
	}

	@Override
	public boolean entails(Rule phi, Formula psi) {
		Document phiDocument = toDocument(phi);

		return entails(phiDocument, psi);
	}

	protected Document toDocument(Group group) {
		Document document = new Document();
		document.setGroup(group);

		return document;
	}

	protected Document toDocument(Rule rule) {
		Document document = new Document();
		Group group = new Group(Collections.singletonList(rule));
		document.setGroup(group);
		return document;
	}

}
