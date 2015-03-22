package me.jezza.oc.api.network.interfaces;

import me.jezza.oc.api.network.NetworkResponse.MessageResponse;

/**
 * Used to define a non-physical message.
 * Gives a proper interface for the system to propagate easily.
 * <p/>
 * The basic idea of a message is the container of information.
 * Once posted the message will be run through the processing system delivering it to all nearby nodes connected.
 * This implementation can vary.
 * For instance, in {@link me.jezza.oc.api.network.NetworkCore}, I run it through a breadth-first search, resulting in a propagation.
 * <p/>
 */
public interface INetworkMessage<T extends INetworkNode<T>> {

    /**
     * @param node the node being passed ownership to.
     */
    public void setOwner(T node);

    /**
     * @return the current INetworkNode that is viewed as the node that sent out this message.
     */
    public T getOwner();

    /**
     * Called when the message gets reposted upon completion and requires reposting.
     * <p/>
     * Use this to reset any values (if you want to reset anything) before the repost.
     */
    public void resetMessage();

    /**
     * The contents of this message were changed in some way.
     *
     * @param node The node that returned the INJECT state.
     */
    public void onDataChanged(T node);

    /**
     * Fired during the preProcessing Phase.
     * Can be used to setup/(wait for) data.
     * Such as a path to a node, or waiting for another messages execution.
     *
     * @param messageProcessor - The IMessageProcessor that handles this message.
     * @return Take a look at {@link me.jezza.oc.api.network.NetworkResponse.MessageResponse}
     */
    public MessageResponse preProcessing(IMessageProcessor<T> messageProcessor);

    /**
     * Fired during the processing Phase.
     * <p/>
     * If the message wants to add it to a list, or alter something, you have the ability to.
     * A node will not be fired with this method more than once.
     *
     * @param messageProcessor - The IMessageProcessor that handles this message.
     * @param node             - A node that exists as a part of the network.
     * @return Take a look at {@link me.jezza.oc.api.network.NetworkResponse.MessageResponse}
     */
    public MessageResponse processNode(IMessageProcessor<T> messageProcessor, T node);

    /**
     * Fired during the postProcessing Phase.
     * <p/>
     * Used to determine what the system should do with the message after giving passing it off to this method.
     * This is after the message has been completed.
     *
     * @param messageProcessor - The IMessageProcessor that handles this message.
     * @return Take a look at {@link me.jezza.oc.api.network.NetworkResponse.MessageResponse}
     */
    public MessageResponse postProcessing(IMessageProcessor<T> messageProcessor);

}
