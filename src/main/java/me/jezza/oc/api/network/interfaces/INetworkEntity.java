package me.jezza.oc.api.network.interfaces;

import net.minecraft.entity.Entity;

public interface INetworkEntity<T extends INetworkNode<T>> extends INetworkMessage<T> {

	public Class<? extends Entity> getEntityClass();

}
