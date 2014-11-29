package me.jezza.oc.api.network;

import cpw.mods.fml.common.FMLCommonHandler;
import me.jezza.oc.api.network.interfaces.INetworkNode;
import me.jezza.oc.api.network.interfaces.INetworkNodeHandler;

import java.util.*;

/**
 * This is the master instance.
 * Used to add and remove nodes to your network.
 */
public class NetworkInstance {

    private LinkedHashSet<INetworkNodeHandler> networks;
    private Class<? extends INetworkNodeHandler> nodeHandlerClazz;

    public NetworkInstance() {
        this(NetworkCore.class);
    }

    public NetworkInstance(Class<? extends INetworkNodeHandler> nodeHandlerClazz) {
        this.networks = new LinkedHashSet<>();
        this.nodeHandlerClazz = nodeHandlerClazz;
    }

    public NetworkResponse.NodeAdded addNetworkNode(INetworkNode node) throws IllegalAccessException, InstantiationException {
        List<INetworkNodeHandler> networksFound = new LinkedList<>();
        Collection<INetworkNode> nearbyNodes;
        nearbyNodes = node.getNearbyNodes();

        if (!nearbyNodes.isEmpty())
            nodeIterator:
                    for (INetworkNode nearbyNode : nearbyNodes)
                        for (INetworkNodeHandler networkFound : networks)
                            if (!networksFound.contains(networkFound))
                                if (networkFound.containsNode(nearbyNode)) {
                                    networksFound.add(networkFound);
                                    continue nodeIterator;
                                }

        INetworkNodeHandler networkNodeHandler;
        NetworkResponse.NodeAdded response;
        switch (networksFound.size()) {
            case 0:
                response = NetworkResponse.NodeAdded.NETWORK_CREATION;
                networkNodeHandler = createNetworkNodeHandler();
                break;
            case 1:
                response = NetworkResponse.NodeAdded.NETWORK_JOIN;
                networkNodeHandler = networksFound.get(0);
                break;
            default:
                response = NetworkResponse.NodeAdded.NETWORK_MERGE;
                networkNodeHandler = networksFound.get(0);
                List<INetworkNodeHandler> nodes = networksFound.subList(1, networksFound.size());
                for (INetworkNodeHandler networkFound : nodes) {
                    networkNodeHandler.mergeNetwork(networkFound.getNodeMap());
                    removeNetworkNodeHandler(networkFound);
                }
        }

        boolean flag = networkNodeHandler.addNetworkNode(node);
        if (!flag) {
            if (response == NetworkResponse.NodeAdded.NETWORK_CREATION)
                removeNetworkNodeHandler(networkNodeHandler);
            response = NetworkResponse.NodeAdded.NETWORK_FAILED_TO_ADD;
        }
        return response;
    }

    public NetworkResponse.NodeRemoved removeNetworkNode(INetworkNode node) throws IllegalAccessException, InstantiationException {
        INetworkNodeHandler nodeHandler = null;
        for (INetworkNodeHandler networkNodeHandler : networks) {
            if (networkNodeHandler.containsNode(node)) {
                nodeHandler = networkNodeHandler;
                break;
            }
        }

        if (nodeHandler == null)
            return NetworkResponse.NodeRemoved.NETWORK_FAILED_TO_REMOVE;

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();

        boolean removed = nodeHandler.removeNetworkNode(node);
        if (!removed)
            return NetworkResponse.NodeRemoved.NETWORK_FAILED_TO_REMOVE;

        Collection<INetworkNode> nearbyNodes = nodeMap.get(node);
        switch (nearbyNodes.size()) {
            case 0:
                removeNetworkNodeHandler(nodeHandler);
                return NetworkResponse.NodeRemoved.NETWORK_DESTROYED;
            case 1:
                return NetworkResponse.NodeRemoved.NETWORK_LEAVE;
            default:
                Iterator<INetworkNode> nextNode = nearbyNodes.iterator();

                INetworkNode networkNode = nextNode.next();
                Collection<INetworkNode> connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);

                int nodeHandlerSize = nodeHandler.size();
                if (connectedNodes.size() == nodeHandlerSize)
                    return NetworkResponse.NodeRemoved.NETWORK_LEAVE;

                HashSet<INetworkNode> visited = new HashSet<>(connectedNodes);
                visited.add(node);
                nodeHandler.retainAll(connectedNodes);

                Set<? extends INetworkNode> keySet = nodeMap.keySet();
                while (!visited.containsAll(keySet)) {
                    if (!nextNode.hasNext())
                        break;
                    networkNode = nextNode.next();
                    if (visited.contains(networkNode))
                        continue;
                    connectedNodes = breadthFirstSearchSpread(networkNode, nodeMap);
                    visited.addAll(connectedNodes);
                    createNetworkNodeHandlerAndFill(connectedNodes);
                }
        }

        return NetworkResponse.NodeRemoved.NETWORK_SPLIT;
    }

    public NetworkResponse.NodeUpdated updateNetworkNode(INetworkNode node) {
        INetworkNodeHandler nodeHandler = null;
        for (INetworkNodeHandler networkNodeHandler : networks)
            if (networkNodeHandler.containsNode(node))
                nodeHandler = networkNodeHandler;

        if (nodeHandler == null)
            return NetworkResponse.NodeUpdated.NETWORK_FAILED_TO_UPDATE;

        Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();
        Collection<INetworkNode> cachedNodes = nodeMap.get(node);
        Collection<INetworkNode> currentNodes = node.getNearbyNodes();

        if (cachedNodes.equals(currentNodes))
            return NetworkResponse.NodeUpdated.NETWORK_NO_DELTA_DETECTED;

        NetworkResponse.NodeRemoved nodeRemoved;
        NetworkResponse.NodeAdded nodeAdded;
        try {
            nodeRemoved = removeNetworkNode(node);
            nodeAdded = addNetworkNode(node);
        } catch (Exception e) {
            return NetworkResponse.NodeUpdated.NETWORK_FAILED_TO_UPDATE;
        }

        if (nodeRemoved == NetworkResponse.NodeRemoved.NETWORK_FAILED_TO_REMOVE || nodeAdded == NetworkResponse.NodeAdded.NETWORK_FAILED_TO_ADD)
            return NetworkResponse.NodeUpdated.NETWORK_FAILED_TO_UPDATE;
        return NetworkResponse.NodeUpdated.NETWORK_UPDATED;
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
    public Collection<INetworkNode> breadthFirstSearchSpread(INetworkNode startingNode, Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap) {
        HashSet<INetworkNode> visited = new HashSet<>();
        Queue<INetworkNode> queue = new LinkedList<>();
        queue.offer(startingNode);

        while (!queue.isEmpty()) {
            INetworkNode node = queue.poll();
            visited.add(node);
            Collection<INetworkNode> nearbyNodes = nodeMap.get(node);
            for (INetworkNode childNode : nearbyNodes) {
                if (visited.contains(childNode))
                    continue;
                queue.offer(childNode);
            }
        }
        return visited;
    }

}
