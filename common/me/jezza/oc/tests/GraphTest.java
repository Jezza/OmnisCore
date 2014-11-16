package me.jezza.oc.tests;

import me.jezza.oc.api.collect.Graph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class GraphTest {

    private Graph<String> graph;

    @Before
    public void setUp() throws Exception {
        graph = new Graph<>();
    }

    @Test
    public void testAddNode() throws Exception {
        graph.addNode("TestNode1");
        Assert.assertEquals(1, graph.size());
        graph.addNode("TestNode1");
        Assert.assertEquals(1, graph.size());
        graph.addNode("TestNode2");
        Assert.assertEquals(2, graph.size());
    }

    @Test
    public void testRemoveNode() throws Exception {
        graph.addNode("TestNode1");
        graph.removeNode("TestNode2");
        Assert.assertEquals(1, graph.size());
        graph.removeNode("TestNode1");
        Assert.assertEquals(0, graph.size());
    }

    @Test
    public void testClear() throws Exception {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.clear();
        Assert.assertEquals(0, graph.size());
        graph.addNode("TestNode1");
        graph.clear();
        graph.addNode("TestNode2");
        Assert.assertEquals(1, graph.size());
    }

    @Test
    public void testSize() throws Exception {
        addNodes(3);
        Assert.assertEquals(3, graph.size());
        graph.clear();
        addNodes(6);
        Assert.assertEquals(6, graph.size());
        graph.clear();
        addNodes(53);
        for (int i = 1; i <= 20; i++)
            graph.removeNode("TestNode" + i);
        Assert.assertEquals(33, graph.size());
    }

    @Test
    public void testAddAll() {
        graph.addNode("TestNode1");
        graph.addNode("TestNode2");
        graph.addEdge("TestNode1", "TestNode2");

        Graph<String> stringGraph = new Graph<>();
        stringGraph.addNode("TestNode4");
        stringGraph.addNode("TestNode5");
        stringGraph.addEdge("TestNode4", "TestNode5");

        graph.addAll(stringGraph.asMap());

        graph.addNode("TestNode3");
        graph.addEdge("TestNode3", "TestNode2");
        graph.addEdge("TestNode3", "TestNode4");

        Assert.assertEquals(5, graph.size());
        Assert.assertEquals(1, graph.adjacentTo("TestNode1").size());
        Assert.assertEquals(2, graph.adjacentTo("TestNode2").size());
        Assert.assertEquals(2, graph.adjacentTo("TestNode3").size());
        Assert.assertEquals(2, graph.adjacentTo("TestNode4").size());
        Assert.assertEquals(1, graph.adjacentTo("TestNode5").size());
    }

    @Test
    public void testHasNode() throws Exception {
        graph.addNode("TestNode1");
        Assert.assertTrue(graph.containsNode("TestNode1"));
        Assert.assertFalse(graph.containsNode("TestNode2"));
    }

    @Test
    public void testIsConnected() throws Exception {
        addNodes(3);
        graph.addEdge("TestNode1", "TestNode2");
        Assert.assertTrue(graph.isAdjacent("TestNode1", "TestNode2"));
        Assert.assertFalse(graph.isAdjacent("TestNode2", "TestNode2"));
        Assert.assertFalse(graph.isAdjacent("TestNode2", "TestNode3"));
        Assert.assertFalse(graph.isAdjacent("TestNode1", "TestNode3"));
        Assert.assertTrue(graph.isAdjacent("TestNode2", "TestNode1"));
    }

    @Test
    public void testAddEdge() throws Exception {
        addNodes(3);
        graph.addEdge("TestNode1", "TestNode2");
        graph.addEdge("TestNode1", "TestNode3");
        Assert.assertTrue(graph.isAdjacent("TestNode1", "TestNode2"));
        Assert.assertFalse(graph.isAdjacent("TestNode2", "TestNode2"));
        Assert.assertFalse(graph.isAdjacent("TestNode2", "TestNode3"));
        Assert.assertTrue(graph.isAdjacent("TestNode1", "TestNode3"));
    }

    @Test
    public void testGetNodes() throws Exception {
        addNodes(3);
        Collection<String> nodes = graph.getNodes();
        Assert.assertEquals(3, nodes.size());
        Assert.assertTrue(nodes.contains("TestNode1"));
        Assert.assertTrue(nodes.contains("TestNode2"));
        Assert.assertTrue(nodes.contains("TestNode3"));
    }

    @Test
    public void testAdjacentTo() throws Exception {
        // Method: 0, then join 1, 2 then join 3, 4 and join 3 and 1
        // 0
        // 1 => 0
        // 0 1
        // 0 1 | 2
        // 3 => 2
        // 0 1 | 3 2
        // 4 => 3
        // 4 => 1
        // 0 1 4 3 2

        graph.addNode("TestNode0");

        graph.addNode("TestNode1");
        graph.addEdge("TestNode1", "TestNode0");

        graph.addNode("TestNode2");

        graph.addNode("TestNode3");
        graph.addEdge("TestNode3", "TestNode2");

        graph.addNode("TestNode4");
        graph.addEdge("TestNode4", "TestNode1");
        graph.addEdge("TestNode4", "TestNode3");

        Assert.assertEquals(5, graph.size());
        Assert.assertEquals(1, graph.adjacentTo("TestNode0").size());
        Assert.assertEquals(2, graph.adjacentTo("TestNode1").size());
        Assert.assertEquals(1, graph.adjacentTo("TestNode2").size());
        Assert.assertEquals(2, graph.adjacentTo("TestNode3").size());
        Assert.assertEquals(2, graph.adjacentTo("TestNode4").size());


    }

    @Test
    public void testAsMap() {
        // Method: 0, then join 1, 2 then join 3, 4 and join 3 and 1
        // 0
        // 1 => 0
        // 0 1
        // 0 1 | 2
        // 3 => 2
        // 0 1 | 3 2
        // 4 => 3
        // 4 => 1
        // 0 1 4 3 2

        graph.addNode("TestNode0");

        graph.addNode("TestNode1");
        graph.addEdge("TestNode1", "TestNode0");

        graph.addNode("TestNode2");

        graph.addNode("TestNode3");
        graph.addEdge("TestNode3", "TestNode2");

        graph.addNode("TestNode4");
        graph.addEdge("TestNode4", "TestNode1");
        graph.addEdge("TestNode4", "TestNode3");

        Map<String, Collection<String>> nodeMap = graph.asMap();
        Assert.assertEquals(nodeMap.get("TestNode0").size(), 1);
        Assert.assertEquals(nodeMap.get("TestNode1").size(), 2);
        Assert.assertEquals(nodeMap.get("TestNode2").size(), 1);
        Assert.assertEquals(nodeMap.get("TestNode3").size(), 2);
        Assert.assertEquals(nodeMap.get("TestNode4").size(), 2);
    }

    private void addNodes(int num) {
        for (int i = 1; i <= num; i++)
            graph.addNode("TestNode" + i);
    }
}