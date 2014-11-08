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
    public void isValidNode(INetworkNode node) {
        if (respondingNodes.contains(node)) {
            System.out.println("HOLY SHIT BRA, YOU DUN FAQ'D UP!");
        }
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

    @Override
    public void resetMessage() {
    }
}
