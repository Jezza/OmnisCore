package me.jezza.dc.common.core;

import me.jezza.dc.DeusCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoreProperties {

    public static final String MOD_ID = "DeusCore";
    public static final String MOD_NAME = "Deus Core";
    public static final String VERSION = "0.0.1";

    private static final String BUILD = "1217";

    public static final String FML_REQ = "7.10.84." + BUILD;
    public static final String FML_REQ_MAX = "7.11";

    public static final String FORGE_REQ = "10.13.1." + BUILD;
    public static final String FORGE_REQ_MAX = "10.14";

    public static final String DEPENDENCIES = "required-after:FML@[" + FML_REQ + "," + FML_REQ_MAX + ");" + "required-after:Forge@[" + FORGE_REQ + "," + FORGE_REQ_MAX + ");";

    public static final String SERVER_PROXY = "me.jezza.dc.common.CommonProxy";
    public static final String CLIENT_PROXY = "me.jezza.dc.client.ClientProxy";

    public static final Logger logger = LogManager.getLogger(MOD_ID);
}
