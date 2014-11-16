package me.jezza.oc.api.collect;

import java.util.*;

public class Graph<T> {

    private final LinkedHashSet<T> CACHE = new LinkedHashSet<T>();

    private LinkedHashMap<T, Collection<T>> nodeMap;
    private int keySize = 0;

    public Graph() {
        nodeMap = new LinkedHashMap();
    }

    public boolean addNode(T node) {
        if (containsNode(node))
            return false;
        keySize++;
        nodeMap.put(node, cloneNewHashSet());
        return containsNode(node);
    }

    public boolean removeNode(T node) {
        if (!containsNode(node))
            return false;
        keySize--;
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

    /**
     * Add a connection/edge between two nodes.
     * The graph has to contain both.
     *
     * @param from
     * @param to
     * @return true if the edge was added.
     */
    public boolean addEdge(T from, T to) {
        if (from == to)
            return false;
        if (!(containsNode(from) && containsNode(to)))
            return false;

        Collection<T> fromCollection = nodeMap.get(from);
        if (!fromCollection.contains(to))
            fromCollection.add(to);

        Collection<T> toCollection = nodeMap.get(to);
        if (!toCollection.contains(from))
            toCollection.add(from);
        return true;
    }

    private LinkedHashSet<T> cloneNewHashSet() {
        return (LinkedHashSet<T>) CACHE.clone();
    }

    public void clear() {
        keySize = 0;
        nodeMap.clear();
    }

    /**
     * @return number of nodes
     */
    public int size() {
        return keySize;
    }

    /**
     * @param node The node in question.
     * @return The number of adjacent nodes.
     */
    public int connectionSize(T node) {
        if (!containsNode(node))
            return 0;
        return nodeMap.get(node).size();
    }

    /**
     * @return size == 0;........
     */
    public boolean isEmpty() {
        return keySize == 0;
    }

    /**
     * Add the given map to this graph.
     */
    public void addAll(Map<? extends T, ? extends Collection<T>> map) {
        nodeMap.putAll(map);
        keySize = nodeMap.keySet().size();
    }

    public boolean removeAll(Collection<? extends T> nodes) {
        return batchRemove(nodes, false);
    }

    public boolean retainAll(Collection<? extends T> nodes) {
        return batchRemove(nodes, true);
    }

    private boolean batchRemove(Collection<? extends T> nodes, boolean compliment) {
        Collection<T> keySet = nodeMap.keySet();
        Iterator<T> iterator = keySet.iterator();
        boolean modified = false;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (nodes.contains(t) != compliment)
                iterator.remove();
        }
        keySize = keySet.size();
        return modified;
    }

    /**
     * @return all nodes currently in the network.
     */
    public Collection<T> getNodes() {
        return nodeMap.keySet();
    }

    /**
     * @return an iterable for the adjacent nodes.
     */
    public Collection<T> adjacentTo(T node) {
        if (!containsNode(node))
            return cloneNewHashSet();
        return nodeMap.get(node);
    }

    /**
     * Changes will NOT reflect.
     *
     * @return A clone of the map.
     */
    public Map<T, Collection<T>> asMap() {
        return (Map<T, Collection<T>>) nodeMap.clone();
    }

    @Override
    public String toString() {
        return nodeMap.toString();
    }
}
