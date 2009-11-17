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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A node filter can be used to filter the child nodes (XML elements) of a node.
 * 
 * @author Adrian Marte
 */
public class NodeFilter implements Iterable<Node> {

	private Node context;

	private String localName;

	/**
	 * Creates a new node filter.
	 * 
	 * @param context The node whose child nodes are to be filtered.
	 * @param localName The local name of the child nodes to filter.
	 */
	public NodeFilter(Node context, String localName) {
		this.context = context;
		this.localName = localName;
	}

	/**
	 * Returns the first child node with the specific local name.
	 * 
	 * @return The first child node with the specific local name, or
	 *         <code>null</code> if no such child node exists.
	 */
	public Node first() {
		Iterator<Node> iterator = iterator();

		if (iterator.hasNext()) {
			return iterator.next();
		}

		return null;
	}

	/**
	 * Returns an iterator over the child nodes with the specific local name.
	 */
	public Iterator<Node> iterator() {
		return new NodeIterator();
	}

	private class NodeIterator implements Iterator<Node> {

		private Node next = null;

		private int currentIndex = 0;

		public NodeIterator() {
			// Do one iteration in order to set the next node.
			try {
				next();
			} catch (NoSuchElementException e) {
			}
		}

		public boolean hasNext() {
			return next != null;
		}

		public Node next() {
			if (next == null && currentIndex > 0) {
				throw new NoSuchElementException(
						"Iteration has no more elements");
			}

			Node current = next;
			next = null;

			NodeList childNodes = context.getChildNodes();

			while (currentIndex < childNodes.getLength()) {
				Node childNode = childNodes.item(currentIndex);
				currentIndex++;

				if (childNode.getNodeType() == Node.ELEMENT_NODE
						&& childNode.getLocalName().equals(localName)) {
					next = childNode;
					break;
				}
			}

			if (current != null) {
				return current;
			}

			throw new NoSuchElementException("Iteration has no more elements");
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"Remove is not supported by this iterator");
		}

	}

}
