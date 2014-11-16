package me.jezza.oc.api.interfaces;

import me.jezza.oc.api.NetworkResponse.MessageResponse;

public interface INetworkMessage {

    /**
     * Should be set upon creation.
     *
     * @param node the message being passed ownership.
     */
    public void setOwner(INetworkNode node);

    /**
     * @return current INetworkNode that is viewed as the node that sent out this message.
     */
    public INetworkNode getOwner();

    /**
     * Called when the message gets reposted upon an invalid network traversal.
     * This is determined by the owner node.
     * <p/>
     * Use this to reset any values before the repost.
     */
    public void resetMessage();

    /**
     * Fired for the use of the message.
     * If the message wants to add it to a list, or alter something, you have the ability to.
     *
     * @param node The node being processed.
     */
    public MessageResponse isValidNode(INetworkNode node);

}
