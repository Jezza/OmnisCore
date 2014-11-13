package me.jezza.oc.api.collect;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class Graph<T> {

    private final LinkedHashSet<T> EMPTY_SET = new LinkedHashSet<T>();

    private LinkedHashMap<T, Collection<T>> nodeMap;

    public Graph() {
        nodeMap = new LinkedHashMap();
    }

    public boolean addNode(T node) {
        if (containsNode(node))
            return false;
        nodeMap.put(node, new LinkedHashSet<T>());
        return containsNode(node);
    }

    public boolean removeNode(T node) {
        if (!containsNode(node))
            return false;
        Collection<T> nodes = nodeMap.remove(node);
        for (T adjacentNode : nodes)
            nodeMap.get(adjacentNode).remove(node);
        return !containsNode(node);
    }

    public boolean containsNode(T node) {
        return nodeMap.containsKey(node);
    }

    public boolean isAdjacent(T from, T to) {
        if (!(containsNode(from) && containsNode(to)))
            return false;
        return nodeMap.get(from).contains(to);
    }

    public boolean addEdge(T from, T to) {
        if (from == to)
            return false;
        if (!(containsNode(from) && containsNode(to)))
            return false;

        if (!nodeMap.get(from).contains(to))
            nodeMap.get(from).add(to);
        if (!nodeMap.get(to).contains(from))
            nodeMap.get(to).add(from);
        return true;
    }

    public void clear() {
        nodeMap.clear();
    }

    public int size() {
        return nodeMap.size();
    }

    public boolean isEmpty() {
        return nodeMap.isEmpty();
    }

    /**
     * Add the entire given graph to this graph.
     */
    public void addAll(Map<? extends T, ? extends Collection<T>> map) {
        nodeMap.putAll(map);
    }

    /**
     * @return an iterable for the adjacent nodes.
     */
    public Collection<T> adjacentTo(T node) {
        return !containsNode(node) ? EMPTY_SET : nodeMap.get(node);
    }

    /**
     * @return all nodes currently in the network.
     */
    public Collection<T> getNodes() {
        return nodeMap.keySet();
    }


    // TODO Should I return an immutable copy of the map?
    public Map<? extends T, ? extends Collection<T>> asImmutableMap() {
        return ImmutableMap.copyOf(nodeMap);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Total Nodes: " + nodeMap.size());
        stringBuilder.append(System.lineSeparator());
        for (T node : nodeMap.keySet()) {
            stringBuilder.append(node.toString());
            stringBuilder.append(": ");
            for (T adjacentNode : nodeMap.get(node)) {
                stringBuilder.append(adjacentNode.toString());
                stringBuilder.append(" ");
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
