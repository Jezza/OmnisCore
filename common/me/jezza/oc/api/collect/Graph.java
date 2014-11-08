package me.jezza.oc.api.collect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class Graph<T> {

    private final LinkedHashSet<T> EMPTY_SET = new LinkedHashSet<T>();

    private HashMap<T, LinkedHashSet<T>> nodeMap;

    public Graph() {
        nodeMap = new HashMap();
    }

    public void addNode(T node) {
        if (hasNode(node))
            return;
        nodeMap.put(node, new LinkedHashSet<T>());
    }

    public void removeNode(T node) {
        if (!hasNode(node))
            return;
        HashSet<T> set = nodeMap.remove(node);
        for (T adjacentNode : set)
            nodeMap.get(adjacentNode).remove(node);
    }

    public void clear() {
        nodeMap.clear();
    }

    public int size() {
        return nodeMap.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean hasNode(T node) {
        return nodeMap.containsKey(node);
    }

    /**
     * Are two nodes adjacent?
     * It's an undirected graph, so order doesn't matter.
     */
    public boolean isConnected(T from, T to) {
        if (!(hasNode(from) && hasNode(to)))
            return false;
        return nodeMap.get(from).contains(to);
    }

    public boolean addEdge(T from, T to) {
        if (from == to)
            return false;
        if (!(hasNode(from) && hasNode(to)))
            return false;

        if (!nodeMap.get(from).contains(to))
            nodeMap.get(from).add(to);
        if (!nodeMap.get(to).contains(from))
            nodeMap.get(to).add(from);
        return true;
    }

    /**
     * Add the entire given graph to this graph.
     *
     * @param graphToAdd the graph to add
     */
    public void addAll(Graph<T> graphToAdd) {
        nodeMap.putAll(graphToAdd.nodeMap);
    }

    /**
     * @return an iterable for the adjacent nodes.
     */
    public Iterable<T> adjacentTo(T node) {
        return !hasNode(node) ? EMPTY_SET : nodeMap.get(node);
    }

    /**
     * @return all nodes currently in the network.
     */
    public Iterable<T> getNodes() {
        return nodeMap.keySet();
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

    public static enum CollectionMethod {
        DFS, BFS;
    }

}
