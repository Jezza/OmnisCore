package me.jezza.oc.api.network;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.jezza.oc.api.collect.Graph;
import me.jezza.oc.api.network.NetworkResponse.MessageResponse;
import me.jezza.oc.api.network.interfaces.*;

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
     * Each server tick they are promoted through {@link NetworkCore.Phase}
     * <p/>
     * O(1) across the board for a lot of actions.
     * Fastest, but not necessarily the best.
     */
    private LinkedHashMultimap<Phase, INetworkMessage> messageMap;

    /**
     * Used to keep track of what nodes would like to be notified of messages being posted to the system.
     * Useful if you wish to have a "brain" of the network.
     */
    private ArrayList<IMessageNodeOverride> messageNodesOverride;

    public NetworkCore() {
        graph = new Graph<>();
        messageMap = LinkedHashMultimap.create();
        messageNodesOverride = new ArrayList<>();
    }

    @Override
    public boolean addNetworkNode(INetworkNode node) {
        boolean flag = graph.addNode(node);
        Collection<INetworkNode> nearbyNodes = node.getNearbyNodes();
        if (!nearbyNodes.isEmpty())
            for (INetworkNode nearbyNode : nearbyNodes)
                graph.addEdge(node, nearbyNode);
        node.setIMessageProcessor(this);
        if (node instanceof IMessageNodeOverride)
            messageNodesOverride.add((IMessageNodeOverride) node);
        return flag;
    }

    @Override
    public boolean removeNetworkNode(INetworkNode node) {
        if (node instanceof IMessageNodeOverride)
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
    public void destroy() {
        graph.clear();
        messageMap.clear();
        messageNodesOverride.clear();
    }

    @Override
    public boolean containsNode(INetworkNode node) {
        return graph.containsNode(node);
    }

    @Override
    public boolean postMessage(INetworkMessage message) {
        return message.getOwner() != null && messageMap.put(Phase.PRE_PROCESSING, message);
    }

    @Override
    public List<INetworkNode> getPathFrom(INetworkNode from, INetworkNode to) {
        if (!(graph.containsNode(from) && graph.containsNode(to)))
            return Collections.<INetworkNode>emptyList();

        Deque<ArrayList<INetworkNode>> deque = new ArrayDeque<>();
        deque.push(Lists.newArrayList(from));
        HashSet<INetworkNode> visited = new HashSet<>();

        while (!deque.isEmpty()) {
            ArrayList<INetworkNode> path = deque.pop();
            INetworkNode networkNode = path.get(path.size() - 1);

            if (networkNode.equals(to))
                return path;

            Collection<INetworkNode> networkNodes = graph.adjacentTo(networkNode);
            for (INetworkNode childNode : networkNodes) {
                if (visited.contains(childNode))
                    continue;
                visited.add(childNode);

                ArrayList<INetworkNode> newPath = new ArrayList<>(path);
                newPath.add(childNode);
                deque.addLast(newPath);
            }
        }
        return Collections.<INetworkNode>emptyList();
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
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
            Queue<INetworkNode> queue = new ArrayDeque<>(graph.size());
            queue.offer(message.getOwner());

            while (!queue.isEmpty()) {
                INetworkNode node = queue.poll();
                visited.add(node);
//                Here's where the messages get processed.
                if (message.isValidNode(node) == MessageResponse.INVALID)
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
                for (IMessageNodeOverride node : messageNodesOverride) {
                    NetworkResponse.NetworkOverride networkOverride = node.onMessagePosted(message);
                    switch (networkOverride) {
                        case DELETE:
                            iterator.remove();
                            continue messageIterator;
                        case INTERCEPT:
                            message.setOwner(node);
                            continue messageIterator;
                        case INJECT:
                            message.dataChanged(node);
                            continue messageIterator;
                        case IGNORE:
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
         * The main phase of the message.
         * What happens during this phase of the message is up to the implementation.
         */
        PROCESSING,
        /**
         * The final stage of messages.
         * Finalising all the message properties to allow the message to act one last time before deletion.
         */
        POST_PROCESSING;
    }
}
