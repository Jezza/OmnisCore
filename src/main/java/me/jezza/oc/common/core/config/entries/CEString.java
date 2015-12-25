package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigString;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

public class CEString extends ConfigEntry<ConfigString, String> {
	public CEString(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return field.getType() == String.class;
	}

	@Override
	protected String fieldName(Field field, ConfigString annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected String load(OmnisConfiguration config, AnnotatedField<ConfigString, String> field) {
		ConfigString annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getString(annotation.category(), field.name(), field.defaultValue(), comment, annotation.validValues());
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigString, String> field) {
		ConfigString annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getStringProperty(annotation.category(), field.name(), field.defaultValue(), comment, annotation.validValues()).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigString, String> field) {
		buffer.writeString(field.currentValue());
	}

	@Override
	protected String readField(InputBuffer buffer, AnnotatedField<ConfigString, String> field) {
		return buffer.readString();
	}
}
