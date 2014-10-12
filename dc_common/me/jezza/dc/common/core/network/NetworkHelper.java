package me.jezza.dc.common.core.network;

import cpw.mods.fml.relauncher.Side;

import static me.jezza.dc.DeusCore.networkDispatcher;

public class NetworkHelper {

    private static boolean init = false;

    public static void init() {
        if (init)
            return;
        init = true;

        networkDispatcher.registerMessage(PacketGuiNotify.class, Side.SERVER);
    }

}
