package me.jezza.oc.tests;

import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;

import java.util.ArrayList;

public class TestMessage implements INetworkMessage {

    private ArrayList<INetworkNode> respondingNodes;
    private INetworkNode owner;

    public TestMessage(INetworkNode owner) {
        this.owner = owner;
    }

    @Override
    public void addRespondingNode(INetworkNode node) {
        if (!respondingNodes.contains(node))
            respondingNodes.add(node);
    }

    @Override
    public void setOwner(INetworkNode node) {
        this.owner = owner;
    }

    @Override
    public INetworkNode getOwner() {
        return owner;
    }
}
