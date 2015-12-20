package me.jezza.oc.common.utils;

import me.jezza.oc.common.core.config.Config.ConfigBoolean;

import static me.jezza.oc.OmnisCore.logger;

public enum Debug {
	;

	private static final String DEBUG_CONSOLE_KEY = "OC_DBG_CONSOLE";
	private static final String DEBUG_CHAT_KEY = "OC_DBG_CHAT";

	@ConfigBoolean(category = "Debug", name = "debug_console")
	private static boolean debugConsole = false;

	@ConfigBoolean(category = "Debug", name = "debug_chat")
	private static boolean debugChat = false;

	public static boolean chat() {
		return debugChat;
	}

	public static boolean console() {
		return debugConsole;
	}

	public static void checkOverrides() {
		if (!debugConsole && System.getenv(DEBUG_CONSOLE_KEY) != null) {
			debugConsole = true;
			logger.info("OmnisCore Console debugging override enabled via System properties.");
		}
		if (!debugChat && System.getenv(DEBUG_CHAT_KEY) != null) {
			debugChat = true;
			logger.info("OmnisCore Chat debugging override enabled via System properties.");
		}
	}
}
