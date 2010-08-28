/*
 * Copyright (c) 2009, STI Innsbruck
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.sti2.rif4j.rule;

import at.sti2.rif4j.AbstractDescribable;
import at.sti2.rif4j.Assertions;
import at.sti2.rif4j.Describable;
import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.serializer.presentation.PresentationSerializer;

/**
 * @author Adrian Marte
 */
public class Import extends AbstractDescribable implements Describable {

	private Constant location;

	private Constant profile;

	public Import(Constant location, Constant profile) {
		this.location = location;
		this.profile = profile;
	}

	public Constant getLocation() {
		return location;
	}

	public void setLocation(Constant location) {
		Assertions.notNull("location", location);

		this.location = location;
	}

	public Constant getProfile() {
		return profile;
	}

	public void setProfile(Constant profile) {
		Assertions.notNull("profile", profile);

		this.profile = profile;
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
