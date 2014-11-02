package me.jezza.oc.tests;

import com.google.common.collect.Lists;
import me.jezza.oc.api.NetworkResponse;
import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.common.core.CoreProperties;

public class TestNode implements INetworkNode {

    public TestNode() {
    }

    @Override
    public NetworkResponse.Override onMessagePosted(INetworkMessage message) {
        CoreProperties.logger.info("Ignoring...");
        return NetworkResponse.Override.IGNORE;
    }

    @Override
    public NetworkResponse.MessageResponse isValidMessage(INetworkMessage message) {
        CoreProperties.logger.info("Is this a valid message...");
        return NetworkResponse.MessageResponse.INVALID;
    }

    @Override
    public NetworkResponse.Override onMessageReceived(INetworkMessage message) {
        CoreProperties.logger.info("On message received...");
        return NetworkResponse.Override.IGNORE;
    }

    @Override
    public NetworkResponse onMessageComplete(INetworkMessage message) {
        CoreProperties.logger.info("On message complete...");
        return NetworkResponse.VALIDATE;
    }

    @Override
    public Iterable<INetworkNode> getNearbyNodes() {
        CoreProperties.logger.info("Getting all nearby nodes.");
        return Lists.newArrayList();
    }
}
