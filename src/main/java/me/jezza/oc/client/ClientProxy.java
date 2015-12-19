package me.jezza.oc.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.CommonProxy;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public boolean isClient() {
		return true;
	}

	@Override
	public boolean isServer() {
		return false;
	}


	@Override
	public void preInit() {
	}

	@Override
	public void init() {
		OmnisCore.logger.info("-- Initialising ClientTickHandler --");
		Client.init();
	}

	@Override
	public void postInit() {
	}
}
