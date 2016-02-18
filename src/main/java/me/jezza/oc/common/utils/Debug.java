package me.jezza.oc.common.utils;

import static me.jezza.oc.OmnisCore.logger;
import static me.jezza.oc.common.core.CoreProperties.MOD_ID;

import me.jezza.oc.common.core.config.Config.ConfigBoolean;

public class Debug {

	private static final String DEBUG_CONSOLE_KEY = "OC_DBG_CONSOLE";
	private static final String DEBUG_CHAT_KEY = "OC_DBG_CHAT";
	private static final String DEBUG_TPS = "OC_TPS";

	private Debug() {
		throw new IllegalStateException();
	}

	@ConfigBoolean(category = "Debug", name = "debug_console", comment = "Enables debug output in console.")
	private static boolean debugConsole = false;

	@ConfigBoolean(category = "Debug", name = "debug_chat", comment = "Enables debug output in chat.")
	private static boolean debugChat = false;

	@ConfigBoolean(category = "Debug", name = "tracing_print_stream", comment = "Overrides FML's TracingPrintStream.")
	private static boolean tracingPrintStream = false;

	public static boolean chat() {
		return debugChat;
	}

	public static boolean console() {
		return debugConsole;
	}

	public static boolean tracingPrintStream() {
		return tracingPrintStream;
	}

	public static void checkOverrides() {
		if (!debugConsole && System.getenv(DEBUG_CONSOLE_KEY) != null) {
			debugConsole = true;
			logger.info("{} Console debugging override enabled via System properties.", MOD_ID);
		}
		if (!debugChat && System.getenv(DEBUG_CHAT_KEY) != null) {
			debugChat = true;
			logger.info("{} Chat debugging override enabled via System properties.", MOD_ID);
		}
		if (!tracingPrintStream && System.getenv(DEBUG_TPS) != null) {
			tracingPrintStream = true;
			logger.info("{} TracingPrintStream override enabled via System properties.", MOD_ID);
		}
	}
}
