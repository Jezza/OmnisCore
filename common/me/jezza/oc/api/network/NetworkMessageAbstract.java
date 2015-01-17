package me.jezza.oc.api.network;

import me.jezza.oc.api.network.interfaces.INetworkMessage;
import me.jezza.oc.api.network.interfaces.INetworkNode;

/**
 * Just a simple abstract class to remove the monotony of setting and getting owners
 */
public abstract class NetworkMessageAbstract implements INetworkMessage {

    private INetworkNode owner;

    public NetworkMessageAbstract(INetworkNode owner) {
        this.owner = owner;
    }

    @Override
    public void setOwner(INetworkNode owner) {
        this.owner = owner;
    }

    @Override
    public INetworkNode getOwner() {
        return owner;
    }

}
