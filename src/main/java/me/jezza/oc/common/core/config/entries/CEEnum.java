package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import com.google.common.base.Joiner;
import me.jezza.oc.common.core.config.Config.ConfigEnum;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.helpers.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Property;

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
	protected String fieldName(Field field, ConfigEnum annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	public Enum<?> load(OmnisConfiguration config, AnnotatedField<ConfigEnum, Enum<?>> field) {
		String comment = processComment(field.annotation().comment());
		String value = config.getString(field.annotation().category(), field.name(), field.defaultValue().name(), comment);
		if (StringHelper.useable(value)) {
			Enum constant = valueOf(field.raw(), value);
			if (constant != null)
				return constant;
		}
		return field.defaultValue();
	}

	@Override
	public void save(OmnisConfiguration config, AnnotatedField<ConfigEnum, Enum<?>> field) {
		String comment = processComment(field.annotation().comment());
		Property prop = config.getStringProperty(field.annotation().category(), field.name(), field.defaultValue().name());
		prop.set(field.currentValue().name());
		StringBuilder enumComment = new StringBuilder(comment).append(" [values: ");
		COMMENT_JOINER.appendTo(enumComment, field.type().getEnumConstants());
		prop.comment = enumComment.append("; default: ").append(field.defaultValue().name()).append(']').toString();
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigEnum, Enum<?>> field) {
		buffer.writeString(field.currentValue().name());
	}

	@Override
	protected Enum<?> readField(InputBuffer buffer, AnnotatedField<ConfigEnum, Enum<?>> field) {
		return valueOf(field.raw(), buffer.readString());
	}

	private Enum valueOf(Field field, String value) {
		for (Enum constant : (Enum[]) field.getType().getEnumConstants())
			if (value.equalsIgnoreCase(constant.name()))
				return constant;
		return null;
	}
}
