package me.jezza.oc.client;

import me.jezza.oc.common.CommonProxy;

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
