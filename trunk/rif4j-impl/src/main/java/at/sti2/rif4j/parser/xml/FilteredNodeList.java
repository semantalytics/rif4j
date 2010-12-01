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
