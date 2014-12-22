package me.jezza.oc.common.utils;

import net.minecraft.client.resources.I18n;

public class Localise {

    public static String format(String local, Object... objects) {
        return I18n.format(local, objects);
    }

    public static String formatSafe(String local, Object... objects) {
        String localised = I18n.format(local);
        return localised.equals(local) ? local : localised;
    }

    public static String attemptToLocalise(String local, String defaultLocal) {
        String localised = I18n.format(local);
        return localised.equals(local) ? defaultLocal : localised;
    }

}
