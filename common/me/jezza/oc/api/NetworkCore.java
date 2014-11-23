package me.jezza.oc.api;

import com.google.common.collect.LinkedHashMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.jezza.oc.api.NetworkResponse.MessageResponse;
import me.jezza.oc.api.collect.Graph;
import me.jezza.oc.api.interfaces.IMessageProcessor;
import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.api.interfaces.INetworkNodeHandler;

import java.util.*;

public class NetworkCore implements INetworkNodeHandler, IMessageProcessor {

    /**
     * Backing data structure for the network.
     * This maintains links/connections between nodes.
     * Something of my own design, will probably change through time.
     */
    private Graph<INetworkNode> graph;

    /**
     * Messages that are posted to the network are placed in this map.
     * Each server tick they are promoted through {@link me.jezza.oc.api.NetworkCore.Phase}
     * <p/>
     * O(1) across the board for a lot of actions.
     * Fastest, but not necessarily the best.
     */
    private LinkedHashMultimap<Phase, INetworkMessage> messageMap;

    /**
     * Used to keep track of what nodes would like to be notified of messages being posted to the system.
     * Useful if you wish to have a "brain" of the network.
     */
    private LinkedHashSet<INetworkNode> messageNodesOverride;

    public NetworkCore() {
        graph = new Graph<>();
        messageMap = LinkedHashMultimap.create();
        messageNodesOverride = new LinkedHashSet<>();
    }

    @Override
    public boolean addNetworkNode(INetworkNode node) {
        boolean flag = graph.addNode(node);
        Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();
        if (!nearbyNodes.isEmpty())
            for (INetworkNode nearbyNode : nearbyNodes)
                graph.addEdge(node, nearbyNode);
        node.setIMessageProcessor(this);
        if (node.registerMessagePostedOverride())
            messageNodesOverride.add(node);
        return flag;
    }

    @Override
    public boolean removeNetworkNode(INetworkNode node) {
        if (node.registerMessagePostedOverride())
            messageNodesOverride.remove(node);
        return graph.removeNode(node);
    }

    @Override
    public boolean retainAll(Collection<? extends INetworkNode> nodes) {
        return graph.retainAll(nodes);
    }

    @Override
    public void mergeNetwork(Map<? extends INetworkNode, ? extends Collection<INetworkNode>> networkNodeMap) {
        graph.addAll(networkNodeMap);
        for (INetworkNode node : graph.getNodes())
            node.setIMessageProcessor(this);
    }

    @Override
    public Map<? extends INetworkNode, ? extends Collection<INetworkNode>> getNodeMap() {
        return graph.asMap();
    }

    @Override
    public boolean requiresRegistration() {
        return true;
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public boolean isEmpty() {
        return graph.isEmpty();
    }

    @Override
    public boolean containsNode(INetworkNode node) {
        return graph.containsNode(node);
    }

    @Override
    public boolean postMessage(INetworkMessage message) {
        return message.getOwner() != null && messageMap.put(Phase.PRE_PROCESSING, message);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        processingPostMessages();
        processingMessages();
        processingPreMessages();
    }

    private void processingPostMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.POST_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();

            MessageResponse messageResponse = message.onMessageComplete(this);

            iterator.remove();
            switch (messageResponse) {
                case INVALID:
                    message.resetMessage();
                    messageMap.put(Phase.PRE_PROCESSING, message);
                    continue messageIterator;
                case VALID:
                default:
            }

        }
    }

    private void processingMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();

            HashSet<INetworkNode> visited = new HashSet<>();
            Queue<INetworkNode> queue = new LinkedList<>();
            queue.offer(message.getOwner());

            while (!queue.isEmpty()) {
                INetworkNode node = queue.poll();
                visited.add(node);
                MessageResponse response = message.isValidNode(node);
                if (response == MessageResponse.INVALID)
                    continue;
                for (INetworkNode childNode : graph.adjacentTo(node))
                    if (!visited.contains(childNode))
                        queue.offer(childNode);
            }

            iterator.remove();
            messageMap.put(Phase.POST_PROCESSING, message);
        }
    }

    private void processingPreMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.PRE_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();

            if (!messageNodesOverride.isEmpty())
                for (INetworkNode node : messageNodesOverride) {
                    NetworkResponse.NetworkOverride networkOverride = node.onMessagePosted(message);
                    switch (networkOverride) {
                        case IGNORE:
                        default:
                            continue;
                        case DELETE:
                            iterator.remove();
                            continue messageIterator;
                        case INTERCEPT:
                            message.setOwner(node);
                    }
                }

            iterator.remove();
            messageMap.put(Phase.PROCESSING, message);
        }
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    private static enum Phase {
        /**
         * Messages being posted, this is the stage where intercepts would happen.
         */
        PRE_PROCESSING,
        /**
         * The main function of the message. Gets spread through the network in a Breadth-first search pattern.
         * Each node will be passed to the message via isValidNode();
         */
        PROCESSING,
        /**
         * The final stage of messages.
         * This will just call the owner and fired onMessageComplete, along with the message that was processed.
         */
        POST_PROCESSING;
    }
}
