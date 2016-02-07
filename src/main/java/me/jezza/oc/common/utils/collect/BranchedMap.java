package me.jezza.oc.common.utils.collect;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A spanning tree based off the array. Each value of an array defines an edge.
 * Any branched generated from a part of an array will not have a value, unless the option is chosen during construction.
 *
 * @author Jezza
 */
public class BranchedMap<K, V> {
	private final boolean inheritParentValue;
	private final boolean fallback;
	private final Node root;

	/**
	 * Constructs a basic BranchedMap.
	 */
	public BranchedMap() {
		this(null, false, false);
	}

	/**
	 * Constructs a basic tree map with a rootValue.
	 * Whenever an empty or null array is passed in, this value is returned.
	 *
	 * @param rootValue - The default value to return on an empty or null array key.
	 */
	public BranchedMap(V rootValue) {
		this(rootValue, false, false);
	}

	/**
	 * @param rootValue          - The default value to return on an empty or null array key.
	 * @param inheritParentValue - If any generated edges inherit their parent's value. By default, this is not the case.
	 * @param fallback           - On a failed retrieval, this option will cause the path to be generated, and returned. Typically used with inheritParentValue.
	 */
	public BranchedMap(V rootValue, boolean inheritParentValue, boolean fallback) {
		root = new Node();
		root.value = rootValue;
		this.inheritParentValue = inheritParentValue;
		this.fallback = fallback;
	}

	/**
	 * Places the value along the given path.
	 * Any edges or nodes that need to be generated will be generated, with the options passed in during construction.
	 *
	 * @param keys  - The path that the value should be placed at.
	 * @param value - The value that should be stored.
	 * @return - Any old value that was previously stored at the path, null otherwise.
	 */
	public V put(K[] keys, V value) {
		if (keys == null || keys.length == 0)
			return null;
		Node base = node(keys, true);
		if (base == null)
			return null;
		V oldValue = base.value;
		base.value = value;
		return oldValue;
	}

	/**
	 * Retrieve the value that is defined by the given path.
	 *
	 * @param keys - The path to the value.
	 * @return - The value, if found.
	 */
	public V get(K[] keys) {
		if (keys == null || keys.length == 0)
			return root.value;
		Node base = node(keys, false);
		return base != null ? base.value : null;
	}

	/**
	 * Removes the node that is at the given path, this also means all of its children.
	 *
	 * @param keys - The path to the value.
	 * @return - The value of the node that was removed.
	 */
	public V remove(K[] keys) {
		if (keys == null || keys.length == 0)
			return null;
		Node base = node(keys, false);
		if (base == null)
			return null;
		V value = base.value;
		Iterator<Edge> it = base.parent.children.iterator();
		while (it.hasNext()) {
			Edge edge = it.next();
			if (edge.node == base) {
				it.remove();
				break;
			}
		}
		destroyBranch(base);
		return value;
	}

	/**
	 * Purges the map.
	 * Removes all data, and makes it completely fresh.
	 */
	public void clear() {
		List<Edge> children = root.children;
		if (!children.isEmpty()) {
			Iterator<Edge> it = children.iterator();
			while (it.hasNext()) {
				Edge edge = it.next();
				destroyBranch(edge.node);
				edge.link = null;
				edge.node = null;
				it.remove();
			}
		}
	}

	/**
	 * Internal method, used to grab the node at the path, potentially generating nodes as it goes, if the boolean is true.
	 *
	 * @param keys   - The path of the node to get.
	 * @param create - If the path should be generated. (This means that the result is never null)
	 * @return - The node at the path.
	 */
	private Node node(K[] keys, boolean create) {
		Node base = root;
		for (K link : keys) {
			base = nextNode(base, link, create);
			if (base == null)
				return null;
		}
		return base;
	}

	/**
	 * Internal method, used to get or created the next node in the chain.
	 *
	 * @param check  - The node that should be searched for the next link.
	 * @param link   - The link, or edge. The value that defines the connection.
	 * @param create - If true, the necessary nodes will be generated.
	 * @return - The node that should be the next one in line. Can be null.
	 */
	private Node nextNode(Node check, K link, boolean create) {
		if (link == null)
			throw new NullPointerException("link");
		List<Edge> children = check.children;
		if (!children.isEmpty()) {
			for (Edge edge : children) {
				if (edge.link.equals(link))
					return edge.node;
			}
		}
		return create ? createChild(check, link) : null;
	}

	/**
	 * Does the dirty work of actual creation and linking of the child node to the parent.
	 *
	 * @param parent - The parent of the node to be created.
	 * @param link   - The connection between the two nodes.
	 * @return - The created child node.
	 */
	private Node createChild(Node parent, K link) {
		Node child = new Node();
		child.parent = parent;
		Edge edge = new Edge();
		edge.link = link;
		edge.node = child;
		parent.children.add(edge);
		if (inheritParentValue)
			child.value = parent.value;
		return child;
	}

	/**
	 * Obliterates any nodes that are children of the node, and then destroys the node itself.
	 * Note: Doesn't remove the node from the parent of the node in question, so do that before calling this.
	 *
	 * @param node - The node to destroy.
	 */
	private void destroyBranch(Node node) {
		List<Edge> children = node.children;
		if (!children.isEmpty()) {
			Iterator<Edge> it = children.iterator();
			while (it.hasNext()) {
				Edge edge = it.next();
				destroyBranch(edge.node);
				edge.link = null;
				edge.node = null;
				it.remove();
			}
		}
		node.children = null;
		node.value = null;
		node.parent = null;
	}

	@Override
	public String toString() {
		return toString0(root, new StringBuilder(), 0).toString();
	}

	/**
	 * Builds a recursive representation of the map.
	 * Typically, you should just call toString();
	 *
	 * @param node    - The starting node.
	 * @param builder - The StringBuilder to add the information to.
	 * @param depth   - How deep the traversal has gone so far.
	 * @return - A StringBuilder that contains the representation of the map.
	 */
	public StringBuilder toString0(Node node, StringBuilder builder, int depth) {
		List<Edge> children = node.children;
		builder.append('"').append(node.value).append('"').append(", ").append(children.size()).append('\n');
		for (Edge child : children) {
			for (int i = 0; i < depth; i++)
				builder.append('-');
			builder.append('|').append('"').append(child.link).append('"').append("=");
			toString0(child.node, builder, depth + 1);
		}
		return builder;
	}

	/**
	 * Internal representation of a node.
	 */
	private class Node {
		private Node parent;
		private List<Edge> children = new LinkedList<>();
		private V value;
	}

	/**
	 * Internal representation of a connection between two nodes.
	 */
	private class Edge {
		private Node node;
		private K link;
	}
}
