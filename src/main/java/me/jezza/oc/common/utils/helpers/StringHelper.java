package me.jezza.oc.common.utils.helpers;

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
        for (int i = 0; i < charSequence.length(); i++) {
            if (charSequence.charAt(i) > ' ')
                return true;
        }
        return false;
    }
}
