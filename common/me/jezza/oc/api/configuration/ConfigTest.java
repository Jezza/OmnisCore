package me.jezza.oc.api.configuration;

import static me.jezza.oc.api.configuration.Config.*;

public class ConfigTest {

    @ConfigInteger(category = "TestInteger", defaultValue = 5)
    public static int testInt = 0;

    @ConfigBoolean(category = "TestBoolean", defaultValue = true)
    public static boolean testBoolean = false;

}
