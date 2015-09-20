package me.jezza.oc.common.utils;

import net.minecraft.util.StatCollector;

public class Localise {

    public static String translate(String key) {
        return StatCollector.translateToLocal(key);
    }

    public static String translate(String key, Object... params) {
        return format(translate(key), params);
    }

    public static String format(String target, Object... params) {
        if (params == null || params.length == 0)
            return target;
        for (int i = 0; i < params.length; i++) {
            target = target.replace("{" + i + "}", String.valueOf(params[i]));
        }
        return target;
    }

    public static String translateWithFallback(String key) {
        return translateWithFallback(key, key);
    }

    public static String translateWithFallback(String key, String defaultString) {
        String translated = translate(key);
        return translated.equals(key) ? defaultString : translated;
    }
}
