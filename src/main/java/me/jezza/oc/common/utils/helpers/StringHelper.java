package me.jezza.oc.common.utils.helpers;

import com.google.common.base.CaseFormat;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.*;

/**
 * @author Jezza
 */
public class StringHelper {
	public static final char FORMATTING_CHAR = '$';
	public static final Map<Character, EnumChatFormatting> formattingCodeMapping;

	static {
		Map<Character, EnumChatFormatting> formattingMap = ReflectionHelper.getPrivateValue(EnumChatFormatting.class, null, "formattingCodeMapping");
		formattingCodeMapping = Collections.unmodifiableMap(formattingMap);
	}

	private StringHelper() {
		throw new IllegalStateException();
	}

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
        return StatCollector.canTranslate(key) ? translate(key) : defaultString;
    }

    public static String formatColour(String value) {
		StringBuilder builder = new StringBuilder(value);
		for (int i = 0; i < builder.length(); i++) {
			char c = builder.charAt(i);
			if (c == FORMATTING_CHAR) {
				if (i != 0 && builder.charAt(i - 1) == '\\')
					continue;
				EnumChatFormatting format = formattingCodeMapping.get(builder.charAt(i + 1));
				if (format != null)
					builder.replace(i, i + 2, format.toString());
			}
		}
		return builder.toString();
	}

    public static String convertToLowerSnakeCase(String value) {
        if (!useable(value))
            return "";
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '_')
				break;
			if (Character.isLowerCase(c))
				return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
		}
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, value);
	}

    public static List<String> wrap(String text, int size) {
        return wrap(text, size, false);
    }

    public static List<String> wrap(String text, int size, boolean ignoreNewlines) {
		final List<String> lines;
        StringBuilder sb;
        String[] words;
        String space;

        if (ignoreNewlines) {
            text = text.replaceAll(System.lineSeparator(), " ");
            text = text.replaceAll("\\n", " ");

            words = text.split(" ");
        	lines = new ArrayList<>(words.length);
            sb = new StringBuilder();
            space = "";
			for (int i = 0; i < words.length; i++) {
				String word = words[i];
				if (i != 0 && sb.length() + space.length() + word.length() > size) {
					lines.add(sb.toString());
					sb.setLength(0);
					space = "";
				}
				sb.append(space).append(word);
				space = " ";
			}
            lines.add(sb.toString());
			return lines;
        }

		lines = new ArrayList<>(6);
		for (String part : text.split("\\n")) {
			if (part.length() <= size) {
				lines.add(part);
			} else {
				words = part.split(" ");
				sb = new StringBuilder();
				space = "";
				for (int i = 0; i < words.length; i++) {
					String word = words[i];
					if (i != 0 && sb.length() + space.length() + word.length() > size) {
						lines.add(sb.toString());
						sb.setLength(0);
						space = "";
					}
					sb.append(space).append(word);
					space = " ";
				}
				lines.add(sb.toString());
			}
		}
        return lines;
    }
}
