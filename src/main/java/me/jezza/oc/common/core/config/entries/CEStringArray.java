package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import me.jezza.oc.common.core.config.Config.ConfigStringArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import net.minecraft.entity.player.EntityPlayer;

public class CEStringArray extends ConfigEntry<ConfigStringArray, String[]> {
	public CEStringArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return field.getType() == String[].class;
	}

	@Override
	protected String fieldName(Field field, ConfigStringArray annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected String[] load(OmnisConfiguration config, AnnotatedField<ConfigStringArray, String[]> field) {
		ConfigStringArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getStringArray(annotation.category(), field.name(), field.defaultValue(), comment);
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigStringArray, String[]> field) {
		ConfigStringArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getStringArrayProperty(annotation.category(), field.name(), field.defaultValue(), comment).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigStringArray, String[]> field) {
		String[] array = field.currentValue();
		buffer.writeInt(array.length);
		for (String value : array)
			buffer.writeString(value);
	}

	@Override
	protected String[] readField(InputBuffer buffer, AnnotatedField<ConfigStringArray, String[]> field) {
		int length = buffer.readInt();
		String[] array = new String[length];
		for (int i = 0; i < length; i++)
			array[i] = buffer.readString();
		return array;
	}
}
