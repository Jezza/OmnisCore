package me.jezza.oc.common.utils.helpers;

import com.google.common.base.CaseFormat;
import net.minecraft.util.StatCollector;

/**
 * @author Jezza
 */
public class StringHelper {

    public static boolean nullOrEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean notNullOrEmpty(CharSequence charSequence) {
        return charSequence != null && charSequence.length() != 0;
    }

    public static boolean useable(CharSequence charSequence) {
        if (charSequence == null || charSequence.length() == 0)
            return false;
        for (int i = 0; i < charSequence.length(); i++)
            if (charSequence.charAt(i) > ' ')
                return true;
        return false;
    }

    public static String translate(String key) {
        return StatCollector.translateToLocal(key);
    }

    public static String translate(String key, Object... params) {
        return format(translate(key), params);
    }

    public static String format(String target, Object... params) {
        if (params != null && params.length != 0)
            for (Object param : params)
                target = target.replaceFirst("\\{\\}", String.valueOf(param));
        return target;
    }

    public static String translateWithFallback(String key) {
        return translateWithFallback(key, key);
    }

    public static String translateWithFallback(String key, String defaultString) {
        String translated = translate(key);
        return translated.equals(key) ? defaultString : translated;
    }

    public static String convertToSnakeCase(String value) {
        if (!useable(value))
            return "";
        CaseFormat origin = value.contains("_") ? CaseFormat.UPPER_UNDERSCORE : CaseFormat.LOWER_CAMEL;
        return origin.to(CaseFormat.LOWER_UNDERSCORE, value);
    }
}
