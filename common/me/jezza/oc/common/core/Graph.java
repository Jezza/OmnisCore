package me.jezza.oc.common.core;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

/**
 * This is the backend of the network.
 * No touchy.
 */
public class Graph<T> {

    private final TreeSet<T> EMPTY_SET = new TreeSet<T>();

    private HashMap<T, TreeSet<T>> nodeMap;
    private HashSet<T> visited;

    public Graph() {
        nodeMap = new HashMap<T, TreeSet<T>>();
        visited = new HashSet<T>();
    }

    public void addNode(T node) {
        if (hasNode(node))
            return;
        nodeMap.put(node, new TreeSet<T>());
    }

    public void removeNode(T node) {
        if (!hasNode(node))
            return;
        TreeSet<T> treeSet = nodeMap.remove(node);
        for (T adjacentNode : treeSet)
            nodeMap.get(adjacentNode).remove(node);
    }

    public void clear() {
        nodeMap = new HashMap<T, TreeSet<T>>();
    }

    public int size() {
        return nodeMap.size();
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

    public Iterable<T> getNodes() {
        return nodeMap.keySet();
    }

    /**
     * Return an iterator over the adjacent nodes.
     */
    public Iterable<T> adjacentTo(T node) {
        return !hasNode(node) ? EMPTY_SET : nodeMap.get(node);
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

    /**
     * As it's undirected it doesn't matter the order of the nodes.
     * The only difference is going to be a reversed path.
     *
     * @return An empty list if no path was found and a complete path with the current node as the first index.
     */
    public List<T> getPathFrom(T from, T to) {
        if (!(hasNode(from) && hasNode(to)))
            return Lists.newArrayList();
        visited.clear();
        List<T> path = depthFirstSearch(from, from, to);
        return Lists.reverse(path);
    }


    private List<T> depthFirstSearch(T parentNode, T from, T to) {
        if (from.equals(to))
            return Lists.newArrayList(to);
        List<T> result = null;
        for (T childNode : adjacentTo(from)) {
            if (childNode.equals(parentNode) || visited.contains(childNode))
                continue;
            visited.add(childNode);
            List<T> tempResult = depthFirstSearch(from, childNode, to);
            if (tempResult.isEmpty())
                continue;
            tempResult.add(from);
            if (result == null || tempResult.size() < result.size())
                result = tempResult;
        }
        return result != null ? result : Lists.<T>newArrayList();
    }

}
