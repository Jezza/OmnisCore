package me.jezza.dc.client;

import me.jezza.dc.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isServer() {
        return false;
    }

}
