package me.jezza.oc.common.core.network;

import java.util.HashMap;

import me.jezza.oc.common.core.network.interfaces.INetworkMessage;
import me.jezza.oc.common.core.network.interfaces.INetworkNode;

/**
 * Just a simple abstract class to remove the monotony of setting and getting owners
 */
public abstract class NetworkMessageAbstract<T extends INetworkNode<T>> implements INetworkMessage<T> {

	/**
	 * Any useful data that you want the message to carry.
	 * Such as a world, or coordinate position.
	 */
	public HashMap<String, Object> dataMap;

	protected T owner;

	public NetworkMessageAbstract(T owner) {
		this.owner = owner;
	}

	protected void initDataMap() {
		if (dataMap != null && !dataMap.isEmpty())
			dataMap.clear();
		dataMap = new HashMap<>();
	}

	@Override
	public void resetMessage() {
		if (dataMap != null)
			dataMap.clear();
	}

	@Override
	public void setOwner(T owner) {
		this.owner = owner;
	}

	@Override
	public T getOwner() {
		return owner;
	}

}
