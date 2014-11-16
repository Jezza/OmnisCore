package me.jezza.oc.api;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.jezza.oc.api.collect.Graph;
import me.jezza.oc.api.interfaces.IMessageProcessor;
import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.api.interfaces.INetworkNodeHandler;

import java.util.*;

public class NetworkCore implements INetworkNodeHandler, IMessageProcessor {

    private HashMultimap<Phase, INetworkMessage> messageMap;
    private Graph<INetworkNode> graph;
    private LinkedHashSet<INetworkNode> messageNodesOverride;

    public NetworkCore() {
        graph = new Graph<>();
        messageMap = HashMultimap.create();
        messageNodesOverride = new LinkedHashSet<>();
    }

    @Override
    public boolean addNetworkNode(INetworkNode node) {
        boolean flag = graph.addNode(node);
        for (INetworkNode nearbyNode : node.getNearbyNodes())
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
    public void postMessage(INetworkMessage message) {
        messageMap.put(Phase.PRE_PROCESSING, message);
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

        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();

            NetworkResponse.MessageResponse messageResponse = message.getOwner().onMessageComplete(message);

            switch (messageResponse) {
                case VALID:
                    break;
                case INVALID:
                    message.resetMessage();
                    messageMap.put(Phase.PRE_PROCESSING, message);
                    break;
            }

            iterator.remove();
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
            Queue<INetworkNode> queue = new PriorityQueue<>();
            queue.offer(message.getOwner());

            while (!queue.isEmpty()) {
                INetworkNode node = queue.poll();
                visited.add(node);
                NetworkResponse.MessageResponse response = message.isValidNode(node);
                if (response == NetworkResponse.MessageResponse.INVALID)
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
        if (messages.isEmpty() || messageNodesOverride.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();
            Iterator<INetworkNode> networkNodeIterator = messageNodesOverride.iterator();

            while (networkNodeIterator.hasNext()) {
                INetworkNode node = networkNodeIterator.next();
                NetworkResponse.Override override = node.onMessagePosted(message);
                switch (override) {
                    case IGNORE:
                        continue;
                    case DELETE:
                        iterator.remove();
                        continue messageIterator;
                    case INTERCEPT:
                        message.setOwner(node);
                        continue;
                }
            }

            iterator.remove();
            messageMap.put(Phase.PROCESSING, message);
        }
    }

    private static enum Phase {
        PRE_PROCESSING,
        PROCESSING,
        POST_PROCESSING;
    }

}
