package me.jezza.oc.api.interfaces;

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
     * Gets set when you pass in the node to be added.
     * This allows you to post messages easily from the node without referring to the main NetworkInstance.
     * If you decide, for some stupid reason, to override this value, at least make sure it's an instance of INetworkNodeHandler, or you're going to get an exception thrown at your face, because of your stupidity.
     */
    public void setIMessageProcessor(IMessageProcessor messageProcessor);

    /**
     * @return the IMessageProcessor instance that is set upon adding a network node to a network.
     */
    public IMessageProcessor getIMessageProcessor();

}
