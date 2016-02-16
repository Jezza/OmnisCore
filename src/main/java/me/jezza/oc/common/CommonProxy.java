package me.jezza.oc.common;

import net.minecraft.command.CommandHandler;
import net.minecraft.server.MinecraftServer;

public class CommonProxy {
	private static CommandHandler SERVER_INSTANCE;

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
	}

	public CommandHandler commandHandler() {
		if (SERVER_INSTANCE == null)
			SERVER_INSTANCE = (CommandHandler) MinecraftServer.getServer().getCommandManager();
		return SERVER_INSTANCE;
	}
}
