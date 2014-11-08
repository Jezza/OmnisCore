package me.jezza.oc.api.interfaces;

import me.jezza.oc.api.NetworkResponse;

import java.util.Collection;

public interface INetworkNode {

    /**
     * If you wish to override a message being posted, if returned with
     * - IGNORE, should be a default response, means you don't wish for anything to change.
     * - DELETE, the process will stop there, and the system will no longer do anything with that message.
     * - INTERCEPT, It will give you full control over the message, and label this node as the handler. It will not be removed from the queue.
     *
     * @param message The message in question that was posted.
     */
    public NetworkResponse.Override onMessagePosted(INetworkMessage message);

    /**
     * Received when a message is delivered directly to the node.
     * Similar to onMessagePosted().
     * - IGNORE, The message will continue on it's own path.
     * - DELETE, The system will remove the instance from it.
     * - INTERCEPT, The system will modify the owner of the message to be the current node.
     *
     * @param message
     * @return
     */
    public NetworkResponse.Override onMessageReceived(INetworkMessage message);

    /**
     * Used to determine what the system should do with the message after giving passing it off to this method.
     * This is after the message has returned and has been completed.
     * This is only called on the node that posted the message.
     * - VALID, the system will drop it, as the message is no longer needed.
     * - INVALID, the system will repost the message again to get re-processed.
     *
     * @param message The message in question that has finished being processed.
     * @return
     */
    public NetworkResponse.MessageResponse onMessageComplete(INetworkMessage message);

    /**
     * This can be done by many methods, do it of your own free will.
     * However that being said, it's probably best to cache it, and update it when a change occurs.
     *
     * @return all nearby network nodes; Nodes that are also connected to this node.
     */
    public Collection<INetworkNode> getNearbyNodes();

    /**
     * Gets set when you pass in the node to be added.
     * This allows you to post messages easily from the node without referring to the main NetworkInstance.
     */
    public void setNetworkCore(IMessageProcessor networkCore);

    public IMessageProcessor getNetworkCore();

}
