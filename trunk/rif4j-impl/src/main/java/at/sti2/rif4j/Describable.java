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
package at.sti2.rif4j;

import java.util.List;

import at.sti2.rif4j.condition.Constant;
import at.sti2.rif4j.condition.Frame;

/**
 * RIF-BLD allows every term and formula to be optionally described by an
 * annotation consisting of an identifier and meta data, where the identifier is
 * a RIF constant and the meta data is a frame formula or a conjunction of frame
 * formulas.
 * 
 * @author Adrian Marte
 */
public interface Describable {

	/**
	 * Returns the identifier of this RIF element, or <code>null</code> if no
	 * identifier is set.
	 * 
	 * @return The identifier of this RIF element, or <code>null</code> if no
	 *         identifier is set.
	 */
	public Constant getId();

	/**
	 * Sets the identifier of this RIF element. <code>null</code> values are
	 * interpreted as non-present identifiers.
	 * 
	 * @param id
	 *            The identifier of this RIF element.
	 */
	public void setId(Constant id);

	/**
	 * Returns the meta data of this RIF element represented as a list of Frames
	 * interpreted as a conjunction of Frames, or <code>null</code> if no meta
	 * data is set.
	 * 
	 * @return The meta data of this RIF element represented as a list of
	 *         Frames, or <code>null</code> if no meta data is set.
	 */
	public List<Frame> getMetadata();

	/**
	 * Sets the meta data of this RIF element represented as a list of Frames
	 * interpreted as a conjunction of Frames. <code>null</code> values are
	 * interpreted as non-present meta data.
	 * 
	 * @param metadata
	 *            The meta data of this RIF element represented as a list of
	 *            Frames.
	 */
	public void setMetadata(List<Frame> metadata);

}
