package me.jezza.oc.api;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.jezza.oc.api.collect.Graph;
import me.jezza.oc.api.interfaces.IMessageProcessor;
import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;

import java.util.Collection;
import java.util.Iterator;

public class NetworkCore implements IMessageProcessor {

    private HashMultimap<Phase, INetworkMessage> messageMap;
    private Graph<INetworkNode> graph;

    public NetworkCore() {
        graph = new Graph<>();
        messageMap = HashMultimap.create();
    }

    public void addNetworkNode(INetworkNode node) {
        graph.addNode(node);
        for (INetworkNode nearbyNodes : node.getNearbyNodes())
            graph.addEdge(node, nearbyNodes);
        node.setNetworkCore(this);
    }

    /**
     * @param node
     * @return if graph is empty
     */
    public boolean removeNetworkNode(INetworkNode node) {
        graph.removeNode(node);
        return graph.isEmpty();
    }

    public boolean hasNetworkNode(INetworkNode node) {
        return graph.hasNode(node);
    }

    public void merge(NetworkCore otherCore) {
        graph.addAll(otherCore.graph);
    }

    public int size() {
        return graph.size();
    }

    @Override
    public void postMessage(INetworkMessage message) {
        messageMap.put(Phase.PRE_PROCESSING, message);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        postProcessingMessages();
        processingMessages();
        preProcessingMessages();
    }

    private void postProcessingMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.POST_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();
            NetworkResponse.MessageResponse messageResponse = message.getOwner().onMessageComplete(message);
            iterator.remove();
            switch (messageResponse) {
                case VALID:
                    break;
                case INVALID:
                    message.resetMessage();
                    messageMap.put(Phase.PRE_PROCESSING, message);
                    break;
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
            for (INetworkNode node : graph.getNodes())
                message.isValidNode(node);
            iterator.remove();
            messageMap.put(Phase.POST_PROCESSING, message);
        }
    }

    private void preProcessingMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.PRE_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();
            for (INetworkNode node : graph.getNodes()) {
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
