package me.jezza.oc.api.interfaces;

import java.util.Collection;
import java.util.Map;

public interface INetworkNodeHandler {

    /**
     * @param node Node being added to the network
     * @return true if the node was added.
     */
    public boolean addNetworkNode(INetworkNode node);

    /**
     * @param node Node being removed from the network
     * @return true if the node was removed.
     */
    public boolean removeNetworkNode(INetworkNode node);

    /**
     * Called by the main NetworkInstance to merge two or more networks into a master network, in the event that a node is placed between two networks.
     * A map of the nodes and they're connections.
     * Think of this as an addAll for a data structure.
     *
     * @param networkNodeMap
     */
    public void mergeNetwork(Map<? extends INetworkNode, ? extends Collection<INetworkNode>> networkNodeMap);

    /**
     * @return A map of all nodes and their connections.
     */
    public Map<? extends INetworkNode, ? extends Collection<INetworkNode>> getNodeMap();

    /**
     * If you wish for this class to be registered with:
     * FMLCommonHandler.instance().bus().register(networkCore);
     * Upon creation.
     * <p/>
     * It will be unregistered if the instance is no longer required.
     */
    public boolean requiresRegistration();

    /**
     * @return number of nodes in the network.
     */
    public int size();

    /**
     * @return true if there is no nodes currently in the network.
     */
    public boolean isEmpty();

}
