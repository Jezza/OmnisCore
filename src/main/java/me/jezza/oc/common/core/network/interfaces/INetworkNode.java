package me.jezza.oc.common.core.network.interfaces;

import java.util.Collection;

import net.minecraft.world.World;

public interface INetworkNode<T extends INetworkNode<T>> {

	/**
	 * This can be done by many methods, do it of your own free will.
	 * However that being said, it's probably best to cache it, and update it when a change occurs.
	 *
	 * @return all nearby network nodes; Nodes that are connected to this node.
	 */
	Collection<T> getNearbyNodes();

	/**
	 * This is used to notify the node of any details that might have happened.
	 *
	 * @param id      - id of this call in particular
	 * @param process - the processID.
	 * @param data    - Used to pass in extra data.
	 * @return Any object that the message requires. Can be null.
	 */
	Object notifyNode(int id, int process, Object... data);

	/**
	 * Gets set when you pass in the node to be added.
	 * This allows you to post messages easily from the node without referring to the main NetworkInstance.
	 * If you decide, for some stupid reason, to override this value, at least make sure it's an instance of INetworkNodeHandler, or you're going to get an exception thrown at your face, because of your stupidity.
	 */
	void setIMessageProcessor(IMessageProcessor<T> messageProcessor);

	/**
	 * @return The current world of the node.
	 */
	World getWorld();
}
