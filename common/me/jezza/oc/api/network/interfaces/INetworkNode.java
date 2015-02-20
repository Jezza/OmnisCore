package me.jezza.oc.api.network.interfaces;

import java.util.Collection;

public interface INetworkNode {

    /**
     * This can be done by many methods, do it of your own free will.
     * However that being said, it's probably best to cache it, and update it when a change occurs.
     *
     * @return all nearby network nodes; Nodes that are connected to this node.
     */
    public Collection<INetworkNode> getNearbyNodes();

    /**
     * This is used for searching a path around the network.
     * The map should follow a basic structure:
     * The direction:node pair should be the node that is in that direction from this node.
     *
     * @return Directional map represented what nodes are in what direction.
     */
//    public Map<ForgeDirection, INetworkNode> getDirectionalMap();

    /**
     * Gets a node in that direction.
     *
     * @param direction - Direction to look.
     * @return - the INetworkNode in that direction, OR null if no node was found.
     */
//    public INetworkNode getDirectionNode(ForgeDirection direction);

    /**
     * Gets set when you pass in the node to be added.
     * This allows you to post messages easily from the node without referring to the main NetworkInstance.
     * If you decide, for some stupid reason, to override this value, at least make sure it's an instance of INetworkNodeHandler, or you're going to get an exception thrown at your face, because of your stupidity.
     */
    public void setIMessageProcessor(IMessageProcessor messageProcessor);

//    /**
//     * @return the IMessageProcessor instance that is set upon adding a network node to a network.
//     */
//    public IMessageProcessor getIMessageProcessor();

    /**
     * Saves you an instanceof check.
     * Works with conjunction with processNode() as it won't be passed a different type.
     *
     * @return - the string identifying what type of networkNode it is.
     */
    public String getType();

    /**
     * This is used to notify the node of any details that might have happened.
     *
     * @param id - id of this call in particular
     * @param process - the processID.
     * @param data - Used to pass in extra data.
     */
    public void notifyNode(int id, int process, Object... data);

}
