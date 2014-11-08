package me.jezza.oc.tests;

import me.jezza.oc.api.collect.Graph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

public class GraphTest {

    private Graph<String> graph;

    @Before
    public void setUp() throws Exception {
        graph = new Graph<>();
    }

    @Test
    public void testAddNode() throws Exception {
        graph.addNode("TestNode1");
        Assert.assertEquals(graph.size(), 1);
        graph.addNode("TestNode1");
        Assert.assertEquals(graph.size(), 1);
        graph.addNode("TestNode2");
        Assert.assertEquals(graph.size(), 2);
    }

    @Test
    public void testRemoveNode() throws Exception {
        graph.addNode("TestNode1");
        graph.removeNode("TestNode2");
        Assert.assertEquals(graph.size(), 1);
        graph.removeNode("TestNode1");
        Assert.assertEquals(graph.size(), 0);
    }

    @Test
    public void testClear() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.clear();
        Assert.assertEquals(graph.size(), 0);
        graph.addNode("TestNode1");
        graph.clear();
        graph.addNode("TestNode2");
        Assert.assertEquals(graph.size(), 1);
    }

    @Test
    public void testSize() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.addNode("TestNode3");
        int size = graph.size();
        Assert.assertEquals(size, 3);
    }

    @Test
    public void testHasNode() throws Exception {
        graph.addNode("TestNode1");
        Assert.assertTrue(graph.hasNode("TestNode1"));
        Assert.assertFalse(graph.hasNode("TestNode2"));
    }

    @Test
    public void testIsConnected() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.addNode("TestNode3");
        graph.addEdge("TestNode1", "TestNode2");
        Assert.assertTrue(graph.isConnected("TestNode1", "TestNode2"));
        Assert.assertFalse(graph.isConnected("TestNode2", "TestNode2"));
        Assert.assertFalse(graph.isConnected("TestNode2", "TestNode3"));
        Assert.assertFalse(graph.isConnected("TestNode1", "TestNode3"));
        Assert.assertTrue(graph.isConnected("TestNode2", "TestNode1"));
    }

    @Test
    public void testAddEdge() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.addNode("TestNode3");
        graph.addEdge("TestNode1", "TestNode2");
        graph.addEdge("TestNode1", "TestNode3");
        Assert.assertTrue(graph.isConnected("TestNode1", "TestNode2"));
        Assert.assertFalse(graph.isConnected("TestNode2", "TestNode2"));
        Assert.assertFalse(graph.isConnected("TestNode2", "TestNode3"));
        Assert.assertTrue(graph.isConnected("TestNode1", "TestNode3"));
    }

    @Test
    public void testGetNodes() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.addNode("TestNode3");
        Iterable<String> iterable = graph.getNodes();
        int size = sizeOfIterator(iterable);
        Assert.assertEquals(size, 3);
    }

    @Test
    public void testAdjacentTo() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.addNode("TestNode3");
        graph.addEdge("TestNode1", "TestNode2");
        graph.addEdge("TestNode1", "TestNode3");
        int size = sizeOfIterator(graph.adjacentTo("TestNode1"));
        Assert.assertEquals(size, 2);
        size = sizeOfIterator(graph.adjacentTo("TestNode2"));
        Assert.assertEquals(size, 1);

    }

    private <T> int sizeOfIterator(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            iterator.next();
            index++;
        }
        return index;
    }
}