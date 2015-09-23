package me.jezza.oc.api.network;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import me.jezza.oc.common.utils.collect.Graph;
import me.jezza.oc.api.network.NetworkResponse.MessageResponse;
import me.jezza.oc.api.network.interfaces.*;
import me.jezza.oc.api.network.search.SearchThread;
import me.jezza.oc.common.core.CoreProperties;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;

public class NetworkCore<T extends INetworkNode<T>> implements INetworkNodeHandler<T>, IMessageProcessor<T> {

    /**
     * Backing data structure for the network.
     * This maintains links/connections between nodes.
     * Something of my own design, will probably change through time.
     */
    protected Graph<T> graph = new Graph<>();

    /**
     * Messages that are posted to the network are placed in this map.
     * Each server tick they are promoted through {@link NetworkCore.Phase}
     * <p/>
     * O(1) across the board for a lot of actions.
     * Fastest, but not necessarily the best.
     */
    protected Map<Phase, Collection<INetworkMessage<T>>> messageMap;

    protected List<World> worlds;

    public NetworkCore() {
        messageMap = new EnumMap<>(Phase.class);
        messageMap.put(Phase.PRE_PROCESSING, new ArrayList<INetworkMessage<T>>());
        messageMap.put(Phase.PROCESSING, new ArrayList<INetworkMessage<T>>());
        messageMap.put(Phase.POST_PROCESSING, new ArrayList<INetworkMessage<T>>());
    }

    @Override
    public boolean addNetworkNode(T node) {
        if (!graph.addNode(node))
            return false;
        addWorld(node.getWorld());
        Collection<T> nearbyNodes = node.getNearbyNodes();
        if (nearbyNodes == null)
            throw new RuntimeException(node.getClass() + " returned a null set of nodes!");
        if (!nearbyNodes.isEmpty())
            for (T nearbyNode : nearbyNodes)
                graph.addEdge(node, nearbyNode);
        node.setIMessageProcessor(this);
        return true;
    }

    @Override
    public boolean removeNetworkNode(T node) {
        removeWorld(node.getWorld());
        return graph.removeNode(node);
    }

    @Override
    public boolean retainAll(Collection<? extends T> nodes) {
        return graph.retainAll(nodes);
    }

    @Override
    public void mergeNetwork(INetworkNodeHandler<T> nodeHandler) {
        graph.addAll(nodeHandler.getNodeMap());
        for (T node : graph.getNodes())
            node.setIMessageProcessor(this);
        for (World world : nodeHandler.getNetworkedWorlds())
            addWorld(world);

    }

    @Override
    public Map<? extends T, ? extends Collection<T>> getNodeMap() {
        return graph.asMap();
    }

    @Override
    public boolean requiresRegistration() {
        return true;
    }

    @Override
    public void addWorld(World world) {
        if (!(world == null || worlds.contains(world)))
            worlds.add(world);
    }

    @Override
    public void removeWorld(World world) {
        if (world != null && worlds.contains(world))
            worlds.remove(world);
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
    }

    @Override
    public boolean containsNode(T node) {
        return graph.containsNode(node);
    }

    @Override
    public boolean postMessage(INetworkMessage<T> message) {
        if (message.getOwner() == null) {
            CoreProperties.logger.info("{} has no owner! It returned null!", message.getClass());
            throw new NullPointerException();
        }

        Collection<INetworkMessage<T>> messages = messageMap.get(Phase.PRE_PROCESSING);
        messages.add(message);
        return messages.contains(message);
    }

    @Override
    public boolean postMessage(INetworkEntity<T> message) {
        T owner = message.getOwner();

        if (owner == null) {
            CoreProperties.logger.info("{} has no owner! It returned null!", message.getClass());
            throw new NullPointerException();
        }

        World startingWorld = owner.getWorld();
        Class<? extends Entity> entityClass = message.getEntityClass();

        Entity entity;

        try {
            entity = entityClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            CoreProperties.logger.info("Failed to instantiate entity from {}", entityClass);
            throw new RuntimeException(e);
        }


        startingWorld.spawnEntityInWorld(entity);

        throw new NotImplementedException("Has not yet been implemented!");
    }

    /**
     * Don't even ask...
     * This is going to be rewritten or destroyed...
     */
    @Override
    public ISearchResult<T> getPathFrom(T startNode, T endNode) {
        if (!(graph.containsNode(startNode) && graph.containsNode(endNode)))
            return SearchThread.emptySearch();
        return SearchThread.addSearchPattern(startNode, endNode, getNodeMap());
    }

    @Override
    public List<World> getNetworkedWorlds() {
        return ImmutableList.copyOf(worlds);
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
        Collection<INetworkMessage<T>> messages = messageMap.get(Phase.POST_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage<T>> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage<T> message = iterator.next();

            MessageResponse response = message.postProcessing(this);

            if (response == null)
                throw new RuntimeException(message.getClass() + " returned a null response from postProcessing()");

            switch (response) {
                case INVALID:
                    iterator.remove();
                    message.resetMessage();
                    messageMap.get(Phase.PRE_PROCESSING).add(message);
                    continue messageIterator;
                case WAIT:
                    break;
                default:
                case VALID:
                    iterator.remove();
            }
        }
    }

    private void processingMessages() {
        Collection<INetworkMessage<T>> messages = messageMap.get(Phase.PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage<T>> iterator = messages.iterator();

        messageIterator:
        while (iterator.hasNext()) {
            INetworkMessage<T> message = iterator.next();

            HashSet<T> visited = new HashSet<>(graph.size());
            Queue<T> queue = new ArrayDeque<>(graph.size());
            queue.offer(message.getOwner());

            while (!queue.isEmpty()) {
                T node = queue.poll();
                visited.add(node);

                MessageResponse response = message.processNode(this, node);

                if (response == null)
                    throw new RuntimeException(message.getClass() + " returned a null response from processNode()");

                switch (response) {
                    case VALID:
                        for (T childNode : graph.adjacentTo(node))
                            if (!visited.contains(childNode)) {
                                // TODO Look at type recursion.
//                                if (childNode.getType().equals(message.getOwner().getType()))
                                queue.offer(childNode);
                            }
                        break;
                    case WAIT:
                        continue messageIterator;
                    case INVALID:
                }
            }

            iterator.remove();
            messageMap.get(Phase.POST_PROCESSING).add(message);
        }
    }

    private void processingPreMessages() {
        Collection<INetworkMessage<T>> messages = messageMap.get(Phase.PRE_PROCESSING);
        if (messages.isEmpty())
            return;
        Iterator<INetworkMessage<T>> iterator = messages.iterator();

        while (iterator.hasNext()) {
            INetworkMessage<T> message = iterator.next();
            MessageResponse response = message.preProcessing(this);

            if (response == null)
                throw new RuntimeException(message.getClass() + " returned a null response from preProcessing()");

            switch (response) {
                case INVALID:
                    iterator.remove();
                    break;
                case WAIT:
                    continue;
                case VALID:
            }

            iterator.remove();
            messageMap.get(Phase.PROCESSING).add(message);
        }
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    private enum Phase {
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
        POST_PROCESSING
    }
}
