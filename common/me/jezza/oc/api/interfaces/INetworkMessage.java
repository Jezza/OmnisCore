package me.jezza.oc.api.interfaces;

public interface INetworkMessage {

    public void setOwner(INetworkNode node);

    public INetworkNode getOwner();

    /**
     * Called when the message gets reposted upon an invalid network traversal.
     * This is determined by the owner node.
     *
     * Use this to reset any values before the repost.
     */
    public void resetMessage();

    /**
     * Fired for the use of the message.
     * If the message wants to add it to a list, or alter something, you have the ability to.
     *
     * @param node The node being processed.
     */
    public void isValidNode(INetworkNode node);

}
