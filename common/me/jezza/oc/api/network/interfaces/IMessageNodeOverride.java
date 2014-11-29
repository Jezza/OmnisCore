package me.jezza.oc.api.network.interfaces;

import me.jezza.oc.api.network.NetworkResponse;

public interface IMessageNodeOverride extends INetworkNode {

    /**
     * If you wish to override a message being posted, if returned with
     * - IGNORE, should be a default response, means you don't wish for anything to change.
     * - DELETE, the process will stop there, and the system will no longer do anything with that message.
     * - INTERCEPT, It will give you full control over the message, and label this node as the handler. It will not be removed from the queue.
     *
     * @param message The message in question that was posted.
     */
    public NetworkResponse.NetworkOverride onMessagePosted(INetworkMessage message);

}
