package me.jezza.oc.tests;

import me.jezza.oc.api.NetworkCore;
import me.jezza.oc.api.NetworkInstance;
import me.jezza.oc.api.NetworkResponse.NodeUpdated;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.api.interfaces.INetworkNodeHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static me.jezza.oc.api.NetworkResponse.NodeAdded;
import static me.jezza.oc.api.NetworkResponse.NodeRemoved;

public class NetworkInstanceTest {

    private NetworkInstance networkInstance;
    private TestNode[] networkNodes;

    @Before
    public void setUp() throws Exception {
        networkInstance = new NetworkInstance();
    }

    @Test
    public void testAddNetworkNode() throws Exception {
        createNodes(5);
        // Method: 0, then join 1, 2 then join 3, 4 and join 3 and 1

        // 0
        NodeAdded nodeAdded = networkInstance.addNetworkNode(networkNodes[0]);
        Assert.assertTrue(nodeAdded == NodeAdded.NETWORK_CREATION);

        // 1 => 0
        // 0 1
        addNearbyNodes(networkNodes[0], networkNodes[1]);
        nodeAdded = networkInstance.addNetworkNode(networkNodes[1]);
        Assert.assertTrue(nodeAdded == NodeAdded.NETWORK_JOIN);

        // 0 1 | 2
        nodeAdded = networkInstance.addNetworkNode(networkNodes[2]);
        Assert.assertTrue(nodeAdded == NodeAdded.NETWORK_CREATION);

        // 3 => 2
        // 0 1 | 3 2
        addNearbyNodes(networkNodes[2], networkNodes[3]);
        nodeAdded = networkInstance.addNetworkNode(networkNodes[3]);
        Assert.assertTrue(nodeAdded == NodeAdded.NETWORK_JOIN);

        // 4 => 3
        // 4 => 1
        // 0 1 4 3 2
        addNearbyNodes(networkNodes[4], networkNodes[3]);
        addNearbyNodes(networkNodes[4], networkNodes[1]);
        nodeAdded = networkInstance.addNetworkNode(networkNodes[4]);
        Assert.assertTrue(nodeAdded == NodeAdded.NETWORK_MERGE);

        HashSet<INetworkNodeHandler> hashSet = networkInstance.networks;
        Assert.assertEquals(1, hashSet.size());

        Iterator<INetworkNodeHandler> iterator = hashSet.iterator();
        INetworkNodeHandler nodeHandler = iterator.next();

        NetworkCore networkCore = (NetworkCore) nodeHandler;
        Assert.assertEquals(5, networkCore.size());
        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = networkCore.getNodeMap();
        Assert.assertEquals(5, nodeMap.keySet().size());

        Assert.assertEquals(1, nodeMap.get(networkNodes[0]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[1]).size());
        Assert.assertEquals(1, nodeMap.get(networkNodes[2]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[3]).size());
        Assert.assertEquals(2, nodeMap.get(networkNodes[4]).size());
    }

    @Test(timeout = 50)
    public void testRemoveNetworkNode() throws Exception {
        createNodes(5);
        networkInstance.addNetworkNode(networkNodes[0]);

        addNearbyNodes(networkNodes[1], networkNodes[0]);
        networkInstance.addNetworkNode(networkNodes[1]);

        networkInstance.addNetworkNode(networkNodes[2]);

        addNearbyNodes(networkNodes[3], networkNodes[2]);
        networkInstance.addNetworkNode(networkNodes[3]);

        addNearbyNodes(networkNodes[4], networkNodes[3]);
        addNearbyNodes(networkNodes[4], networkNodes[1]);
        networkInstance.addNetworkNode(networkNodes[4]);

        removeNearbyNodes(networkNodes[0], networkNodes[1]);
        NodeRemoved nodeRemoved = networkInstance.removeNetworkNode(networkNodes[0]);
        Assert.assertTrue(nodeRemoved == NodeRemoved.NETWORK_LEAVE);

        removeNearbyNodes(networkNodes[2], networkNodes[3]);
        nodeRemoved = networkInstance.removeNetworkNode(networkNodes[2]);
        Assert.assertTrue(nodeRemoved == NodeRemoved.NETWORK_LEAVE);

        removeNearbyNodes(networkNodes[4], networkNodes[3]);
        removeNearbyNodes(networkNodes[4], networkNodes[1]);
        nodeRemoved = networkInstance.removeNetworkNode(networkNodes[4]);
        Assert.assertTrue(nodeRemoved == NodeRemoved.NETWORK_SPLIT);

        nodeRemoved = networkInstance.removeNetworkNode(networkNodes[3]);
        Assert.assertTrue(nodeRemoved == NodeRemoved.NETWORK_DESTROYED);

        nodeRemoved = networkInstance.removeNetworkNode(networkNodes[1]);
        Assert.assertTrue(nodeRemoved == NodeRemoved.NETWORK_DESTROYED);

    }

    @Test
    public void testUpdateNetworkNode() throws Exception {
        createNodes(4);

        //   2
        // 0 1
        //   3

        networkInstance.addNetworkNode(networkNodes[0]);

        addNearbyNodes(networkNodes[1], networkNodes[0]);
        networkInstance.addNetworkNode(networkNodes[1]);

        addNearbyNodes(networkNodes[2], networkNodes[1]);
        networkInstance.addNetworkNode(networkNodes[2]);

        addNearbyNodes(networkNodes[3], networkNodes[1]);
        networkInstance.addNetworkNode(networkNodes[3]);

        Assert.assertEquals(1, networkInstance.networks.size());

        removeNearbyNodes(networkNodes[2], networkNodes[1]);
        NodeUpdated nodeUpdated = networkInstance.updateNetworkNode(networkNodes[2]);

        Assert.assertTrue(nodeUpdated == NodeUpdated.NETWORK_UPDATED);
        Assert.assertEquals(2, networkInstance.networks.size());

        removeNearbyNodes(networkNodes[3], networkNodes[1]);
        nodeUpdated = networkInstance.updateNetworkNode(networkNodes[3]);

        Assert.assertTrue(nodeUpdated == NodeUpdated.NETWORK_UPDATED);
        Assert.assertEquals(3, networkInstance.networks.size());

        nodeUpdated = networkInstance.updateNetworkNode(networkNodes[3]);

        Assert.assertTrue(nodeUpdated == NodeUpdated.NETWORK_NO_DELTA_DETECTED);
        Assert.assertEquals(3, networkInstance.networks.size());
    }

    private void createNodes(int count) {
        networkNodes = new TestNode[count];
        for (int i = 0; i < count; i++)
            networkNodes[i] = new TestNode();
    }

    private void printNodes() {
        for (int i = 0; i < networkNodes.length; i++)
            System.out.println(i + ": " + networkNodes[i]);
        System.out.println();
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