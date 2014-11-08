package me.jezza.oc.api.interfaces;

import me.jezza.oc.api.NetworkResponse;

public interface INetworkNodeHandler {

    public NetworkResponse addNetworkNode(INetworkNode node);

    public NetworkResponse removeNetworkNode(INetworkNode node);

}
