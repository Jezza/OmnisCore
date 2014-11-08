package me.jezza.oc.tests;

import me.jezza.oc.api.NetworkInstance;
import org.junit.Before;
import org.junit.Test;

public class NetworkInstanceTest extends NetworkInstance {

    private NetworkInstance networkInstance;
    private TestNode[] networkNodes;

    @Before
    public void setUp() throws Exception {
        networkInstance = new NetworkInstance();

        int length = 5;
        networkNodes = new TestNode[length];
        for (int i = 0; i < length; i++)
            networkNodes[i] = new TestNode();
    }

    @Test
    public void testAddNetworkNode() throws Exception {
        // 0 1 4 3 2
        // Order of adding:
        // 0, then join 1, 2 then join 3, 4 and join 3 and 1
        System.out.println("Adding networkNode[0]");
        networkInstance.addNetworkNode(networkNodes[0]);

        System.out.println("Adding networkNode[1]");
        addNearbyNodes(networkNodes[0], networkNodes[1]);
        networkInstance.addNetworkNode(networkNodes[1]);

        System.out.println("Adding networkNode[2]");
        networkInstance.addNetworkNode(networkNodes[2]);

        System.out.println("Adding networkNode[3]");
        addNearbyNodes(networkNodes[2], networkNodes[3]);
        networkInstance.addNetworkNode(networkNodes[3]);

        System.out.println("Adding networkNode[4]");
        addNearbyNodes(networkNodes[4], networkNodes[3]);
        addNearbyNodes(networkNodes[4], networkNodes[1]);
        networkInstance.addNetworkNode(networkNodes[4]);
    }

    private void addNearbyNodes(TestNode testNode1, TestNode testNode2) {
        testNode1.addNearbyNode(testNode2);
        testNode2.addNearbyNode(testNode1);
    }

    @Test
    public void testRemoveNetworkNode() throws Exception {
    }

    @Test
    public void testPostMessage() throws Exception {

    }
}