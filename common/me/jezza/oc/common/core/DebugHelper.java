package me.jezza.oc.common.core;

import me.jezza.oc.api.configuration.Config.ConfigBoolean;

import static me.jezza.oc.common.core.CoreProperties.logger;

public class DebugHelper {

    @ConfigBoolean(category = "DebugHelpers", comment = "Testing comments")
    private static boolean debug_enableConsole = false;

    @ConfigBoolean(category = "DebugHelpers", comment = {"Making sure", "Multiple lines work."})
    private static boolean debug_enableChat = false;

    /**
     * Usable for all mods.
     */
    public static boolean isDebug_enableChat() {
        return debug_enableChat;
    }

    /**
     * Usable for all mods.
     */
    public static boolean isDebug_enableConsole() {
        return debug_enableConsole;
    }

    public static void checkSysOverrides() {
        if (!debug_enableConsole && System.getenv("OC_DBG_CONSOLE") != null) {
            debug_enableConsole = true;
            logger.info("OmnisCore Console debugging override enabled via system properties.");
        }
        if (!debug_enableChat && System.getenv("OC_DBG_CHAT") != null) {
            debug_enableChat = true;
            logger.info("OmnisCore Chat debugging override enabled via system properties.");
        }
    }
}
