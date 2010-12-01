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
