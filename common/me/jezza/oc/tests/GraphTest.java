package me.jezza.oc.tests;

import me.jezza.oc.common.core.Graph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

    @Test(timeout = 50)
    public void testGetPathFrom() throws Exception {
        int length = 1000;
        ArrayList<String> pathResult = new ArrayList<>(length);
        for (int i = 1; i <= length; i++) {
            String node = "TestNode" + i;
            pathResult.add(node);
            graph.addNode(node);
            if (i > 1)
                graph.addEdge("TestNode" + (i - 1), node);
            Assert.assertEquals(graph.size(), i);
        }

        int randomConnections = 1;
        Random rand = new Random();
        for (int i = 1; i < length; i++) {
            int start = (rand.nextInt(length) + 1);
            int finish = (rand.nextInt(length) + 1);
            boolean flag = graph.addEdge("TestNode" + start, "TestNode" + finish);
            if (flag)
                randomConnections++;
        }
        System.out.println("Random Connections: " + randomConnections);

        int first = rand.nextInt(length);
        int second = rand.nextInt(length);
        List<String> pathTo = graph.getPathFrom("TestNode" + first, "TestNode" + second);

        Assert.assertTrue(pathTo.size() <= length);
        Assert.assertEquals(pathResult.size(), length);

    }
}