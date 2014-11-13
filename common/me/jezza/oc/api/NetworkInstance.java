package me.jezza.oc.api;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.FMLCommonHandler;
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

    private List<INetworkNodeHandler> networks;
    private Class<? extends INetworkNodeHandler> nodeHandlerClazz;

    public NetworkInstance() {
        this(NetworkCore.class);
    }

    public NetworkInstance(Class<? extends INetworkNodeHandler> nodeHandlerClazz) {
        this.networks = Lists.newArrayList();
        this.nodeHandlerClazz = nodeHandlerClazz;
    }

    public NodeAdded addNetworkNode(INetworkNode node) throws IllegalAccessException, InstantiationException {
        List<INetworkNodeHandler> networksFound = Lists.newArrayList();

        for (INetworkNode nearbyNode : node.getNearbyNodes()) {
            IMessageProcessor messageProcessor = nearbyNode.getIMessageProcessor();
            if (messageProcessor != null)
                networksFound.add((INetworkNodeHandler) messageProcessor);
        }

        INetworkNodeHandler networkNodeHandler = null;
        NodeAdded response = null;
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
                int hit = 0;
                for (INetworkNodeHandler networkFound : networksFound) {
                    if (hit++ == 0)
                        continue;
                    networkNodeHandler.mergeNetwork(networkFound.getNodeMap());
                    removeNetworkNodeHandler(networkFound);
                }
        }
        boolean flag = networkNodeHandler.addNetworkNode(node);
        if (!flag)
            response = NodeAdded.NETWORK_FAILED_TO_ADD;
        return response;
    }

    public NodeRemoved removeNetworkNode(INetworkNode node) throws IllegalAccessException, InstantiationException {
        IMessageProcessor messageProcessor = node.getIMessageProcessor();
        if (messageProcessor == null)
            return NodeRemoved.NETWORK_FAILED_TO_REMOVE;

        INetworkNodeHandler nodeHandler = (INetworkNodeHandler) messageProcessor;
        Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();

        boolean flag = nodeHandler.removeNetworkNode(node);
        if (!flag)
            return NodeRemoved.NETWORK_FAILED_TO_REMOVE;

        switch (nearbyNodes.size()) {
            case 0:
                removeNetworkNodeHandler(nodeHandler);
                System.out.println("SIZE OF NETWORK: " + nodeHandler.size());
                return NodeRemoved.NETWORK_DESTROYED;
            case 1:
                return NodeRemoved.NETWORK_LEAVE;
            default:
                break;
        }

        switch (nodeHandler.size()) {
            case 0:
                removeNetworkNodeHandler(nodeHandler);
                return NodeRemoved.NETWORK_DESTROYED;
            case 1:
                return NodeRemoved.NETWORK_LEAVE;
            default:
                Map<? extends INetworkNode, ? extends Collection<INetworkNode>> nodeMap = nodeHandler.getNodeMap();
                HashSet<INetworkNode> masterVisited = new HashSet<>();
                HashSet<INetworkNode> subVisited = new HashSet<>();

                Queue<INetworkNode> queue = new PriorityQueue<>();
                for (INetworkNode key : nodeMap.keySet()) {
                    if (masterVisited.contains(key))
                        continue;
                    queue.offer(key);
                    break;
                }

                if (queue.isEmpty())
                    break;

                while (masterVisited.size() < nodeMap.size()) {
                    while (!queue.isEmpty()) {
                        INetworkNode networkNode = queue.poll();
                        subVisited.add(networkNode);
                        masterVisited.add(networkNode);
                        for (INetworkNode childNode : nodeMap.get(networkNode)) {
                            if (masterVisited.contains(childNode))
                                continue;
                            queue.offer(childNode);
                        }
                    }
                    INetworkNodeHandler createdNetwork = createNetworkNodeHandler();
                    LinkedHashMap<INetworkNode, Collection<INetworkNode>> hashMap = new LinkedHashMap<>();
                    for (INetworkNode nodeToAdd : subVisited)
                        hashMap.put(nodeToAdd, nodeMap.get(nodeToAdd));
                    createdNetwork.mergeNetwork(hashMap);
                    subVisited.clear();
                }
        }

        return NodeRemoved.NETWORK_SPLIT;
    }

    private INetworkNodeHandler createNetworkNodeHandler() throws InstantiationException, IllegalAccessException {
        INetworkNodeHandler nodeHandler = nodeHandlerClazz.newInstance();
        if (nodeHandler.requiresRegistration())
            FMLCommonHandler.instance().bus().register(nodeHandler);
        networks.add(nodeHandler);
        return nodeHandler;
    }

    private void removeNetworkNodeHandler(INetworkNodeHandler networkCore) {
        if (networkCore.requiresRegistration())
            FMLCommonHandler.instance().bus().unregister(networkCore);
        networks.remove(networkCore);
    }

}
