package me.jezza.oc.api.abstracts;

import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;

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
