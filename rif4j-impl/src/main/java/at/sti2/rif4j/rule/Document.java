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

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.importer.Profile;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * Represents a RIF document. A document consists of a base path, prefixes,
 * imports and a group.
 * 
 * @author Adrian Marte
 * @see Prefix
 * @see Import
 * @see Group
 */
public class Document extends AbstractDescribable {

	private Constant base;

	private List<Prefix> prefixes = new ArrayList<Prefix>();

	private List<Import> imports = new ArrayList<Import>();

	private Group group;

	public Constant getBase() {
		return base;
	}

	public void setBase(Constant base) {
		this.base = base;
	}

	public List<Prefix> getPrefixes() {
		return prefixes;
	}

	public void setPrefixes(List<Prefix> prefixes) {
		Assertions.notNull("prefixes", prefixes);

		this.prefixes = prefixes;
	}

	public List<Import> getImports() {
		return imports;
	}

	public void setImports(List<Import> imports) {
		Assertions.notNull("imports", imports);

		this.imports = imports;
	}

	/**
	 * Returns the group of this document.
	 * 
	 * @return The group of this document.
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Sets the group of this document.
	 * 
	 * @param group
	 *            The group to set for this document.
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * <p>
	 * Returns the profile of the document, where the profile is either
	 * <code>null</code> for a RIF-BLD document, or the highest profile of
	 * imported documents, where the order of the profiles is as follows.
	 * </p>
	 * <p>
	 * Simple &lt; RDF &lt; RDFS &lt; D &lt; OWL RDF-Based
	 * </p>
	 * 
	 * @return Returns the profile of the document.
	 */
	public Profile getProfile() {
		Profile profile = null;

		for (Import imprt : imports) {
			if (imprt.getProfile() != null) {
				Profile imprtProfile = Profile.forUri(imprt.getProfile());

				if (imprtProfile != null) {
					if (profile == null
							|| (imprtProfile.ordinal() > profile.ordinal())) {
						profile = imprtProfile;
					}
				}
			}
		}

		return profile;
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
