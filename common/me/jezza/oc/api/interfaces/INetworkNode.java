package me.jezza.oc.api.interfaces;

import me.jezza.oc.api.NetworkResponse;

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
     * Called on a separate thread to check if the node in question desires it.
     * Don't modify anything in your class unless you know enough of concurrent modifications.
     * - VALID, This node is a valid option.
     * - INVALID, This node is an invalid option.
     * <p/>
     * DON'T CHANGE ANYTHING IN THE MESSAGE, IT'S FOR CHECKING ONLY.
     *
     * @param message The message in question.
     * @return If this node is interested in it.
     */
    public NetworkResponse.MessageResponse isValidMessage(INetworkMessage message);

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
     * - VALIDATE, the system will drop it, as the message is no longer needed.
     * - INVALIDATE, the system will repost the message again into the messageQueue.
     *
     * @param message The message in question that has finished being processed.
     * @return
     */
    public NetworkResponse onMessageComplete(INetworkMessage message);

    /**
     * This can be done by many methods, of your own free will.
     * Probably best to cache this, and update it when a change occurs.
     *
     * @return all nearby network nodes.
     */
    public Iterable<INetworkNode> getNearbyNodes();

}
