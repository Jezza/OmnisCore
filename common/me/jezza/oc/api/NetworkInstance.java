package me.jezza.oc.api;

import cpw.mods.fml.common.FMLCommonHandler;
import me.jezza.oc.api.NetworkResponse.NodeUpdated;
import me.jezza.oc.api.interfaces.IMessageProcessor;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.api.interfaces.INetworkNodeHandler;

import java.util.*;

import static me.jezza.oc.api.NetworkResponse.NodeAdded;
import static me.jezza.oc.api.NetworkResponse.NodeRemoved;

/**
 * This is the master instance.
 * Used to add and remove nodes to your network.
 */
public class NetworkInstance {

    public HashSet<INetworkNodeHandler> networks;
    private Class<? extends INetworkNodeHandler> nodeHandlerClazz;

    public NetworkInstance() {
        this(NetworkCore.class);
    }

    public NetworkInstance(Class<? extends INetworkNodeHandler> nodeHandlerClazz) {
        this.networks = new HashSet<>();
        this.nodeHandlerClazz = nodeHandlerClazz;
    }

    public NodeAdded addNetworkNode(INetworkNode node) throws IllegalAccessException, InstantiationException {
        List<INetworkNodeHandler> networksFound = new ArrayList<>();
        Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();

        if (!nearbyNodes.isEmpty())
            for (INetworkNode nearbyNode : nearbyNodes) {
                IMessageProcessor messageProcessor = nearbyNode.getIMessageProcessor();
                if (messageProcessor != null)
                    networksFound.add((INetworkNodeHandler) messageProcessor);
            }

        INetworkNodeHandler networkNodeHandler;
        NodeAdded response;
        switch (networksFound.size()) {
            case 0:
                response = NodeAdded.NETWORK_CREATION;
                networkNodeHandler = createNetworkNodeHandler();
                break;
            case 1:
                response = NodeAdded.NETWORK_JOIN;
                networkNodeHandler = networksFound.get(0);
                break;
            default:
                response = NodeAdded.NETWORK_MERGE;
                networkNodeHandler = networksFound.get(0);
                List<INetworkNodeHandler> nodes = networksFound.subList(1, networksFound.size());
                for (INetworkNodeHandler networkFound : nodes) {
                    networkNodeHandler.mergeNetwork(networkFound.getNodeMap());
                    removeNetworkNodeHandler(networkFound);
                }
        }

        boolean flag = networkNodeHandler.addNetworkNode(node);
        if (!flag) {
            if (response == NodeAdded.NETWORK_CREATION)
                removeNetworkNodeHandler(networkNodeHandler);
            response = NodeAdded.NETWORK_FAILED_TO_ADD;
        }
        return response;
    }

    public NodeRemoved removeNetworkNode(INetworkNode node) throws IllegalAccessException, InstantiationException {
        INetworkNodeHandler nodeHandler = null;
        for (INetworkNodeHandler networkNodeHandler : networks)
            if (networkNodeHandler.containsNode(node))
                nodeHandler = networkNodeHandler;

        if (nodeHandler == null)
            return NodeRemoved.NETWORK_FAILED_TO_REMOVE;

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();

        boolean removed = nodeHandler.removeNetworkNode(node);
        if (!removed)
            return NodeRemoved.NETWORK_FAILED_TO_REMOVE;

        int nodeHandlerSize = nodeHandler.size();
        switch (nodeHandlerSize) {
            case 0:
                removeNetworkNodeHandler(nodeHandler);
                return NodeRemoved.NETWORK_DESTROYED;
            case 1:
                return NodeRemoved.NETWORK_LEAVE;
            default:
                Collection<INetworkNode> nearbyNodes = nodeMap.remove(node);
                if (nearbyNodes.isEmpty())
                    return NodeRemoved.NETWORK_FAILED_TO_REMOVE;

                Iterator<INetworkNode> nextNode = nearbyNodes.iterator();
                Collection<INetworkNode> connectedNodes = breadthFirstSearchSpread(nextNode.next());

                int currentSize = connectedNodes.size();
                if (currentSize >= nodeHandlerSize)
                    return NodeRemoved.NETWORK_LEAVE;

                nodeHandler.retainAll(connectedNodes);

                while (currentSize < nodeHandlerSize) {
                    if (!nextNode.hasNext())
                        break;
                    connectedNodes = breadthFirstSearchSpread(nextNode.next());
                    currentSize += connectedNodes.size();
                    createNetworkNodeHandlerAndFill(connectedNodes);
                }
        }

        return NodeRemoved.NETWORK_SPLIT;
    }

    public NodeUpdated updateNetworkNode(INetworkNode node) {
        INetworkNodeHandler nodeHandler = null;
        for (INetworkNodeHandler networkNodeHandler : networks)
            if (networkNodeHandler.containsNode(node))
                nodeHandler = networkNodeHandler;

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();
        Collection<INetworkNode> cachedNodes = nodeMap.get(node);
        Collection<INetworkNode> currentNodes = node.getNearbyNodes();

        if (cachedNodes.equals(currentNodes))
            return NodeUpdated.NETWORK_NO_DELTA_DETECTED;

        NodeRemoved nodeRemoved;
        NodeAdded nodeAdded;
        try {
            nodeRemoved = removeNetworkNode(node);
            for (INetworkNode nearbyNode : node.getNearbyNodes())
                System.out.println(nearbyNode);
            nodeAdded = addNetworkNode(node);
        } catch (Exception e) {
            return NodeUpdated.NETWORK_FAILED_TO_UPDATE;
        }

        if (nodeRemoved == NodeRemoved.NETWORK_FAILED_TO_REMOVE)
            return NodeUpdated.NETWORK_FAILED_TO_UPDATE;
        if (nodeAdded == NodeAdded.NETWORK_FAILED_TO_ADD)
            return NodeUpdated.NETWORK_FAILED_TO_UPDATE;
        return NodeUpdated.NETWORK_UPDATED;
    }

    private INetworkNodeHandler createNetworkNodeHandler() throws InstantiationException, IllegalAccessException {
        INetworkNodeHandler nodeHandler = nodeHandlerClazz.newInstance();
        if (nodeHandler.requiresRegistration())
            FMLCommonHandler.instance().bus().register(nodeHandler);
        networks.add(nodeHandler);
        return nodeHandler;
    }

    private INetworkNodeHandler createNetworkNodeHandlerAndFill(Collection<INetworkNode> collection) throws InstantiationException, IllegalAccessException {
        INetworkNodeHandler nodeHandler = createNetworkNodeHandler();
        for (INetworkNode node : collection)
            nodeHandler.addNetworkNode(node);
        return nodeHandler;
    }

    private void removeNetworkNodeHandler(INetworkNodeHandler networkCore) {
        if (networkCore.requiresRegistration())
            FMLCommonHandler.instance().bus().unregister(networkCore);
        networks.remove(networkCore);
    }

    /**
     * @param startingNode
     * @return A collection of all connected nodes.
     */
    private Collection<INetworkNode> breadthFirstSearchSpread(INetworkNode startingNode) {
        HashSet<INetworkNode> visited = new HashSet<>();
        Queue<INetworkNode> queue = new LinkedList<>();
        queue.offer(startingNode);

        while (!queue.isEmpty()) {
            INetworkNode node = queue.poll();
            if (visited.contains(node))
                continue;
            visited.add(node);
            Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();
            if (nearbyNodes.isEmpty())
                continue;
            for (INetworkNode childNode : nearbyNodes) {
                if (visited.contains(childNode))
                    continue;
                queue.offer(childNode);
            }
        }
        return visited;
    }

}
