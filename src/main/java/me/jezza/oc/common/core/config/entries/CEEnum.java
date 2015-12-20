package me.jezza.oc.common.core.config.entries;

import com.google.common.base.Joiner;
import me.jezza.oc.common.core.config.Config.ConfigEnum;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.helpers.StringHelper;
import net.minecraftforge.common.config.Property;

import java.lang.reflect.Field;

/**
 * @author Jezza
 */
public class CEEnum extends ConfigEntry<ConfigEnum, Enum<?>> {
	private static final Joiner COMMENT_JOINER = Joiner.on(", ");

	public CEEnum(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Enum.class.isAssignableFrom(field.getType());
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigEnum annotation, Enum<?> currentValue, Enum<?> defaultValue) {
		String comment = processComment(annotation.comment());
		String value = config.getString(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue.name(), comment);
		if (!StringHelper.useable(value))
			return defaultValue;
		for (Enum constant : (Enum[]) field.getType().getEnumConstants()) {
			if (value.equalsIgnoreCase(constant.name()))
				return constant;
		}
		return defaultValue;
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigEnum annotation, Enum<?> currentValue, Enum<?> defaultValue) {
		String comment = processComment(annotation.comment());
		Property prop = config.getStringProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue.name());
		prop.set(currentValue.name());
		StringBuilder enumComment = new StringBuilder(comment).append(" [values: ");
		COMMENT_JOINER.appendTo(enumComment, field.getType().getEnumConstants());
		prop.comment = enumComment.append("; default: ").append(defaultValue).append("]").toString();
	}
}
