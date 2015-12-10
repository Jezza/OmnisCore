package me.jezza.oc.common.utils;

import me.jezza.oc.common.core.config.Config.ConfigBoolean;

import static me.jezza.oc.OmnisCore.logger;

public class Debug {

	@ConfigBoolean(category = "Debug")
	private static boolean debugChat = false;

	@ConfigBoolean(category = "Debug")
	private static boolean debugConsole = false;

	private Debug() {
		throw new IllegalStateException();
	}

	public static boolean chat() {
		return debugChat;
	}

	public static boolean console() {
		return debugConsole;
	}

	public static void checkSysOverrides() {
		if (!debugConsole && System.getenv("OC_DBG_CONSOLE") != null) {
			debugConsole = true;
			logger.info("OmnisCore Console debugging override enabled via System properties.");
		}
		if (!debugChat && System.getenv("OC_DBG_CHAT") != null) {
			debugChat = true;
			logger.info("OmnisCore Chat debugging override enabled via System properties.");
		}
	}
}
