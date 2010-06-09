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
package at.sti2.rif4j.parser.xml;

import java.util.ArrayList;
import java.util.List;

import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Frame;

/**
 * A helper class to represent meta data. Note that this class does not
 * represent any real entity in RIF.
 * 
 * @author Adrian Marte
 */
class Metadata {

	private Constant id;

	private List<Frame> data;

	public Metadata(Constant id) {
		this(id, new ArrayList<Frame>());
	}

	public Metadata(List<Frame> data) {
		this(null, data);
	}

	public Metadata(Constant id, List<Frame> data) {
		this.id = id;
		this.data = data;
	}

	public Constant getId() {
		return id;
	}

	public void setId(Constant id) {
		this.id = id;
	}

	public List<Frame> getData() {
		return data;
	}

	public void setData(List<Frame> data) {
		this.data = data;
	}

}
