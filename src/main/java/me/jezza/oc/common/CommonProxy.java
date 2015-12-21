package me.jezza.oc.common;

import me.jezza.oc.OmnisCore;

public class CommonProxy {

	public boolean isClient() {
		return false;
	}

	public boolean isServer() {
		return true;
	}


	public void preInit() {
	}

	public void init() {
	}

	public void postInit() {
		OmnisCore.testField = "Ok, it has appeared to have worked!";
	}
}
