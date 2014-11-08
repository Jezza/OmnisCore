package me.jezza.oc.api;

import com.google.common.collect.Lists;
import me.jezza.oc.api.interfaces.IMessageProcessor;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.api.interfaces.INetworkNodeHandler;

import java.util.List;

/**
 * This is the master instance.
 * Used to add and remove nodes to your network.
 */
public class NetworkInstance implements INetworkNodeHandler {

    private List<NetworkCore> networks;

    public NetworkInstance() {
        networks = Lists.newArrayList();
    }

    @Override
    public NetworkResponse addNetworkNode(INetworkNode node) {
        List<NetworkCore> networksFound = Lists.newArrayList();

        for (INetworkNode nearbyNode : node.getNearbyNodes()) {
            IMessageProcessor messageProcessor = nearbyNode.getNetworkCore();
            if (messageProcessor != null)
                networksFound.add((NetworkCore) messageProcessor);
        }

        NetworkCore networkCore = null;
        NetworkResponse response = null;
        switch (networksFound.size()) {
            case 0:
                networkCore = createNetworkCore();
                response = NetworkResponse.NETWORK_CREATION;
                break;
            case 1:
                networkCore = networksFound.get(0);
                response = NetworkResponse.NETWORK_JOIN;
                break;
            default:
                networkCore = networksFound.get(0);
                int hit = 0;
                for (NetworkCore networkFound : networksFound) {
                    if (hit++ == 0)
                        continue;
                    System.out.println("Merging network.");
                    networkCore.merge(networkFound);
                    removeNetworkCore(networkFound);
                }
                response = NetworkResponse.NETWORK_MERGE;
        }
        networkCore.addNetworkNode(node);
        return response;
    }

    @Override
    public NetworkResponse removeNetworkNode(INetworkNode node) {
        return NetworkResponse.NETWORK_CREATION;
    }

    private NetworkCore createNetworkCore() {
        NetworkCore networkCore = new NetworkCore();
//        FMLCommonHandler.instance().bus().register(networkCore);
        networks.add(networkCore);
        return networkCore;
    }

    private void removeNetworkCore(NetworkCore networkCore) {
//        FMLCommonHandler.instance().bus().unregister(networkCore);
        networks.remove(networkCore);
    }

}
