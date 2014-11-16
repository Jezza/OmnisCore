package me.jezza.oc.tests;

import me.jezza.oc.api.NetworkCore;
import me.jezza.oc.api.interfaces.INetworkNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class NetworkCoreTest {

    private NetworkCore networkCore;
    private TestNode[] networkNodes;

    @Before
    public void setUp() throws Exception {
        networkCore = new NetworkCore();

        int length = 5;
        networkNodes = new TestNode[length];
        for (int i = 0; i < length; i++)
            networkNodes[i] = new TestNode();
    }

    @Test
    public void testAddNetworkNode() throws Exception {
        networkCore.addNetworkNode(networkNodes[0]);
        Assert.assertEquals(1, networkCore.size());

        addNearbyNodes(networkNodes[1], networkNodes[0]);
        networkCore.addNetworkNode(networkNodes[1]);
        Assert.assertEquals(2, networkCore.size());

        networkCore.addNetworkNode(networkNodes[2]);
        Assert.assertEquals(3, networkCore.size());

        addNearbyNodes(networkNodes[3], networkNodes[2]);
        networkCore.addNetworkNode(networkNodes[3]);
        Assert.assertEquals(4, networkCore.size());

        addNearbyNodes(networkNodes[4], networkNodes[1]);
        addNearbyNodes(networkNodes[4], networkNodes[3]);
        networkCore.addNetworkNode(networkNodes[4]);
        Assert.assertEquals(5, networkCore.size());
    }

    @Test
    public void testRemoveNetworkNode() throws Exception {
        networkCore.addNetworkNode(networkNodes[0]);

        addNearbyNodes(networkNodes[1], networkNodes[0]);
        networkCore.addNetworkNode(networkNodes[1]);

        networkCore.addNetworkNode(networkNodes[2]);

        addNearbyNodes(networkNodes[3], networkNodes[2]);
        networkCore.addNetworkNode(networkNodes[3]);

        addNearbyNodes(networkNodes[4], networkNodes[1]);
        addNearbyNodes(networkNodes[4], networkNodes[3]);
        networkCore.addNetworkNode(networkNodes[4]);
    }

    @Test
    public void testMergeNetwork() throws Exception {
        networkCore.addNetworkNode(networkNodes[0]);

        addNearbyNodes(networkNodes[1], networkNodes[0]);
        networkCore.addNetworkNode(networkNodes[1]);

        NetworkCore otherNetwork = new NetworkCore();
        otherNetwork.addNetworkNode(networkNodes[2]);

        addNearbyNodes(networkNodes[3], networkNodes[2]);
        otherNetwork.addNetworkNode(networkNodes[3]);

        networkCore.mergeNetwork(otherNetwork.getNodeMap());

        addNearbyNodes(networkNodes[4], networkNodes[1]);
        addNearbyNodes(networkNodes[4], networkNodes[3]);
        networkCore.addNetworkNode(networkNodes[4]);

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = networkCore.getNodeMap();
        Assert.assertEquals(5, nodeMap.keySet().size());

        Assert.assertEquals(1, nodeMap.get(networkNodes[0]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[1]).size());
        Assert.assertEquals(1, nodeMap.get(networkNodes[2]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[3]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[4]).size());
    }

    @Test
    public void testGetNodeMap() throws Exception {
        networkCore.addNetworkNode(networkNodes[0]);

        addNearbyNodes(networkNodes[1], networkNodes[0]);
        networkCore.addNetworkNode(networkNodes[1]);

        networkCore.addNetworkNode(networkNodes[2]);

        addNearbyNodes(networkNodes[3], networkNodes[2]);
        networkCore.addNetworkNode(networkNodes[3]);

        addNearbyNodes(networkNodes[4], networkNodes[1]);
        addNearbyNodes(networkNodes[4], networkNodes[3]);
        networkCore.addNetworkNode(networkNodes[4]);

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = networkCore.getNodeMap();
        Assert.assertEquals(5, nodeMap.keySet().size());

        Assert.assertEquals(1, nodeMap.get(networkNodes[0]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[1]).size());
        Assert.assertEquals(1, nodeMap.get(networkNodes[2]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[3]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[4]).size());
    }

    private void addNearbyNodes(TestNode testNode1, TestNode testNode2) {
        testNode1.addNearbyNode(testNode2);
        testNode2.addNearbyNode(testNode1);
    }

    private void removeNearbyNodes(TestNode testNode1, TestNode testNode2) {
        testNode1.removeNearbyNode(testNode2);
        testNode2.removeNearbyNode(testNode1);
    }

}