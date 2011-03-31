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

	private String namespace;

	/**
	 * Creates a new node filter.
	 * 
	 * @param context
	 *            The node whose child nodes are to be filtered.
	 * @param localName
	 *            The local name of the child nodes to filter.
	 */
	public NodeFilter(Node context, String localName) {
		this(context, localName, null);
	}

	/**
	 * Creates a new namespace-aware node filter.
	 * 
	 * @param context
	 *            The node whose child nodes are to be filtered.
	 * @param localName
	 *            The local name of the child nodes to filter.
	 * @param namespace
	 *            The namespace of the child nodes to filter.
	 */
	public NodeFilter(Node context, String localName, String namespace) {
		this.context = context;
		this.localName = localName;
		this.namespace = namespace;
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
			// Do one iteration in order to initialize the first "next" node.
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
					if (namespace != null
							&& !namespace.equals(childNode.getNamespaceURI())) {
						continue;
					}

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
