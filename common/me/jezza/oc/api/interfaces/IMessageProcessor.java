package me.jezza.oc.api.interfaces;

/**
 * Just an interface the nodes can use to harmlessly post messages through to their current {@link me.jezza.oc.api.interfaces.INetworkNodeHandler}.
 */
public interface IMessageProcessor {

    /**
     * Method used to post said messages.
     * This removes the need for the node to have a direct reference to the {@link me.jezza.oc.api.interfaces.INetworkNodeHandler}
     * This can remove a lot of stupid errors when someone has direct access to the object.
     * @param message The message being posted.
     */
    public boolean postMessage(INetworkMessage message);

}
