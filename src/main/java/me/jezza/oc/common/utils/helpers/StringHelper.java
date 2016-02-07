package me.jezza.oc.common.utils.helpers;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

import java.util.*;

/**
 * @author Jezza
 */
public enum StringHelper {
	;

	public static final char FORMATTING_CHAR = '$';
	public static final Map<Character, EnumChatFormatting> COLOUR_MAP;
	public static final String OBJECT_REP = "{}";
	public static final Splitter WORD_SPLITTER = Splitter.on(" ").trimResults().omitEmptyStrings();
	public static final Splitter NEW_LINE_SPLITTER = Splitter.on('\n').omitEmptyStrings();

	static {
		Map<Character, EnumChatFormatting> formattingMap = ReflectionHelper.getPrivateValue(EnumChatFormatting.class, null, "formattingCodeMapping");
		COLOUR_MAP = Collections.unmodifiableMap(formattingMap);
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
		if (target == null)
			return null;
		if (!useable(target) || params == null || params.length == 0)
			return target;
		int index = target.indexOf(OBJECT_REP);
		if (index < 0)
			return target;
		StringBuilder builder = new StringBuilder(target);
		for (Object param : params) {
			builder.replace(index, index + OBJECT_REP.length(), String.valueOf(param));
			index = builder.indexOf(OBJECT_REP);
			if (index < 0)
				return builder.toString();
		}
		return builder.toString();
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
				EnumChatFormatting format = COLOUR_MAP.get(builder.charAt(i + 1));
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
		final List<String> lines = new LinkedList<>();
		if (ignoreNewlines) {
			text = text.replace(System.lineSeparator(), " ");
			text = text.replace('\n', ' ');
			split(lines, text, size);
			return lines;
		}
		for (String part : NEW_LINE_SPLITTER.split(text)) {
			if (part.length() <= size) {
				lines.add(part);
			} else {
				split(lines, part, size);
			}
		}
		return lines;
	}

	private static void split(List<String> lines, String text, int size) {
		Iterator<String> it = WORD_SPLITTER.split(text).iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			if (sb.length() == 0) {
				sb.append(it.next());
				continue;
			}
			String word = it.next();
			if (sb.length() + word.length() + 1 > size) {
				lines.add(sb.toString());
				sb.setLength(0);
			} else {
				sb.append(' ');
			}
			sb.append(word);
		}
		lines.add(sb.toString());
	}

	public static String[] split(String target) {
		List<String> strings = WORD_SPLITTER.splitToList(target);
		return strings.toArray(new String[0]);
	}

	/**
	 * Oxford/Serial comma.
	 * joinNiceString("Science", "Art", "Religion") returns "Science, Art, and Religion"
	 */
	public static String joinNiceString(Object[] objects) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < objects.length; ++i) {
			if (i > 0) {
				builder.append(", ");
				if (i == objects.length - 1)
					builder.append("and ");
			}
			builder.append(String.valueOf(objects[i]));
		}
		return builder.toString();
	}

	/**
	 * Oxford/Serial comma.
	 * joinNiceString("Science", "Art", "Religion") returns "Science, Art, and Religion"
	 */
	public static IChatComponent joinNiceString(IChatComponent[] components) {
		ChatComponentText component = new ChatComponentText("");
		for (int i = 0; i < components.length; ++i) {
			if (i > 0) {
				component.appendText(", ");
				if (i == components.length - 1)
					component.appendText("and ");
			}
			component.appendSibling(components[i]);
		}
		return component;
	}

	public static boolean startsWith(String[] target, String[] with) {
		if (with.length > target.length)
			return false;
		for (int i = 0; i < with.length; i++)
			if (!with[i].equals(target[i]))
				return false;
		return true;
	}
}
