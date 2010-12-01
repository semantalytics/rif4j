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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An implementation of NodeList that servers nodes given by a NodeFilter.
 * 
 * @see NodeFilter
 * @see NodeList
 * 
 * @author Adrian Marte
 */
class FilteredNodeList implements NodeList {

	private java.util.List<Node> nodes;

	/**
	 * Creates a new FilteredNodeList.
	 * 
	 * @param filter
	 *            The filter whose elements are served by this node list.
	 */
	public FilteredNodeList(NodeFilter filter) {
		if (filter == null) {
			throw new NullPointerException("Filter must not be null");
		}

		nodes = new ArrayList<Node>();

		for (Node node : filter) {
			nodes.add(node);
		}
	}

	public int getLength() {
		return nodes.size();
	}

	public Node item(int index) {
		return nodes.get(index);
	}

}
