package me.jezza.oc.api;

import com.google.common.collect.HashMultimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.common.core.Graph;

import java.util.Collection;
import java.util.Iterator;

public class NetworkCore {

    private HashMultimap<Phase, INetworkMessage> messageMap;
    private Graph<INetworkNode> network;

    public NetworkCore() {
        network = new Graph<>();
        messageMap = HashMultimap.create();
//        FMLCommonHandler.instance().bus().register(this);
    }

    public void addNetworkNode(INetworkNode node) {
        network.addNode(node);
        for (INetworkNode nearbyNode : node.getNearbyNodes())
            network.addEdge(node, nearbyNode);
    }

    public void removeNetworkNode(INetworkNode node) {
        network.removeNode(node);
    }

    public void postMessage(INetworkMessage message) {
        messageMap.put(Phase.PRE_PROCESSING, message);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        postProcessingMessages();
        processingMessages();
        preProcessingMessages();
//        CoreProperties.logger.info("Server Tick");
    }

    private void postProcessingMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.POST_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();
            message.getOwner().onMessageComplete(message);
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
            for (INetworkNode node : network.getNodes()) {
                NetworkResponse.MessageResponse response = node.isValidMessage(message);
                switch (response) {
                    case VALID:
                        message.addRespondingNode(node);
                        continue;
                    case INVALID:
                        continue;
                }
            }
            iterator.remove();
            messageMap.put(Phase.POST_PROCESSING, message);
        }
    }

    private void preProcessingMessages() {
        Collection<INetworkMessage> messages = messageMap.get(Phase.PRE_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage> iterator = messages.iterator();
        while (iterator.hasNext()) {
            INetworkMessage message = iterator.next();
            for (INetworkNode node : network.getNodes()) {
                NetworkResponse.Override override = node.onMessagePosted(message);
                switch (override) {
                    case IGNORE:
                        continue;
                    case DELETE:
                        return;
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
