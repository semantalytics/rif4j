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
import at.sti2.rif4j.Describable;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * A group consists of rules and groups. To retrieve all rules contained in this
 * group and even those contained in the groups within this group, you can use
 * the <code>getAllRules</code> method of this class.
 * 
 * @author Adrian Marte
 */
public class Group extends AbstractDescribable implements Describable {

	private List<Rule> rules;

	private List<Group> groups;

	/**
	 * Creates a new group with an empty list of rules and groups.
	 */
	public Group() {
		this(new ArrayList<Rule>(), new ArrayList<Group>());
	}

	/**
	 * Creates a new group with the specified list of rules and with an empty
	 * list of groups.
	 * 
	 * @param rules
	 *            The list of rules of the group.
	 * @throws NullPointerException
	 *             If the <code>rules</code> parameter is <code>null</code>.
	 */
	public Group(List<Rule> rules) {
		this(rules, new ArrayList<Group>());
	}

	/**
	 * Creates a new group with the specified list of rules and groups.
	 * 
	 * @param rules
	 *            The list of rules of the group.
	 * @param groups
	 *            The list of groups of the group.
	 * @throws NullPointerException
	 *             If the <code>rules</code> or <code>groups</code> parameter is
	 *             <code>null</code>.
	 */
	public Group(List<Rule> rules, List<Group> groups) {
		Assertions.notNull("rules", rules);
		Assertions.notNull("groups", rules);

		this.rules = rules;
		this.groups = groups;
	}

	/**
	 * Returns the list of rules of this group. Note that the rules of the
	 * groups contained in this group are not included in this list.
	 * 
	 * @return The rules of this group, excluding the rules of the groups
	 *         contained in this group.
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * Sets list of rules of this group.
	 * 
	 * @param rules
	 *            The list of rules of this group.
	 * @throws NullPointerException
	 *             If the <code>rules</code> parameter is <code>null</code>.
	 */
	public void setRules(List<Rule> rules) {
		Assertions.notNull("rules", rules);

		this.rules = rules;
	}

	/**
	 * Returns the list of groups of this group.
	 * 
	 * @return The list of groups of this group.
	 */
	public List<Group> getGroups() {
		return groups;
	}

	/**
	 * Sets the list of groups for this group.
	 * 
	 * @param groups
	 *            The list of groups to set for this group.
	 * @throws NullPointerException
	 *             If the <code>groups</code> parameter is <code>null</code>.
	 */
	public void setGroups(List<Group> groups) {
		Assertions.notNull("groups", groups);

		this.groups = groups;
	}

	/**
	 * Returns all rules from this group and recursively all rules from the
	 * groups contained in this group. If no rules or groups are contained in
	 * this group, an empty list is returned.
	 * 
	 * @return All rules from this and the groups contained in this group.
	 */
	public List<Rule> getAllRules() {
		List<Rule> allRules = new ArrayList<Rule>();

		addRulesFromGroup(this, allRules);

		return allRules;
	}

	private void addRulesFromGroup(Group group, List<Rule> rules) {
		rules.addAll(group.getRules());

		for (Group childGroup : group.getGroups()) {
			addRulesFromGroup(childGroup, rules);
		}
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
