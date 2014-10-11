package me.jezza.dc.common.core.config;

public class DebugHelper {

    private static boolean debug_enableConsole = false;
    private static boolean debug_enableChat = false;

    private static boolean init = false;

    public static void setDebugModes(boolean debug_enableConsole, boolean debug_enableChat) {
        if (init)
            return;
        init = true;

        DebugHelper.debug_enableConsole = debug_enableConsole;
        DebugHelper.debug_enableChat = debug_enableChat;
    }

    /**
     * Usable for all mods.
     */
    public static boolean isDebug_enableChat() {
        return debug_enableChat;
    }

    /**
     *  Usable for all mods.
     */
    public static boolean isDebug_enableConsole() {
        return debug_enableConsole;
    }
}
