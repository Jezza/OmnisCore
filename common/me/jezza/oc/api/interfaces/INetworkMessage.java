package me.jezza.oc.api.interfaces;

public interface INetworkMessage {

    public void addRespondingNode(INetworkNode node);

    public void setOwner(INetworkNode node);

    public INetworkNode getOwner();

}
