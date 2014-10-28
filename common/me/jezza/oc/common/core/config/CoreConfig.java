package me.jezza.oc.common.core.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

import static me.jezza.oc.common.core.CoreProperties.logger;

public class CoreConfig {
    private static Configuration config;

    private static boolean debug_enableConsole = false;
    private static boolean debug_enableChat = false;

    public static void loadConfiguration(File file) {
        logger.info("Loading configuration from disk.");

        config = new Configuration(file);
        config.load();

        debug_enableConsole = config.get("DebugHelpers", "enableConsole", debug_enableConsole).getBoolean(debug_enableConsole);
        debug_enableChat = config.get("DebugHelpers", "enableChat", debug_enableChat).getBoolean(debug_enableChat);

        // check for debugging overrides in system environment
        checkSysOverrides();
        DebugHelper.setDebugModes(debug_enableConsole, debug_enableChat);
        config.save();
        logger.info("Configuration load completed.");
    }

    private static void checkSysOverrides() {
        if (System.getenv("DC_DBG_CONSOLE") != null) {
            debug_enableConsole = true;
            logger.info("DeusCore Console debugging override enabled via system properties.");
        }
        if (System.getenv("DC_DBG_CHAT") != null) {
            debug_enableChat = true;
            logger.info("DeusCore Chat debugging override enabled via system properties.");
        }
    }

}
