package me.jezza.oc.common.core.network.interfaces;

import net.minecraft.entity.Entity;

public interface INetworkEntity<T extends INetworkNode<T>> extends INetworkMessage<T> {
	Class<? extends Entity> getEntityClass();
}
