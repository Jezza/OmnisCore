package me.jezza.dc.common.core.debug;

public class DebugHelper {

    public static boolean debug_enableConsole = false;
    public static boolean debug_enableChat = false;

    private static boolean init = false;

    public static void setDebugModes(boolean debug_enableConsole, boolean debug_enableChat) {
        if (init)
            return;
        init = true;

        DebugHelper.debug_enableConsole = debug_enableConsole;
        DebugHelper.debug_enableChat = debug_enableChat;
    }

    public static boolean isDebug_enableChat() {
        return debug_enableChat;
    }

    public static boolean isDebug_enableConsole() {
        return debug_enableConsole;
    }
}
