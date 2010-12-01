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
package at.sti2.rif4j.rule;

import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
public class Prefix {

	private String name;

	private Constant iri;

	public Prefix(String name, Constant iri) {
		Assertions.notNull("name", name);
		Assertions.notNull("iri", iri);

		this.name = name;
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Assertions.notNull("name", name);

		this.name = name;
	}

	public Constant getIri() {
		return iri;
	}

	public void setIri(Constant iri) {
		Assertions.notNull("iri", iri);

		this.iri = iri;
	}

	public void accept(DocumentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		PresentationSerializer serializer = new PresentationSerializer();
		accept(serializer);
		return serializer.getString();
	}

}
