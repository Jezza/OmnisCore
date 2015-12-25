package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigBooleanArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

public class CEBooleanArray extends ConfigEntry<ConfigBooleanArray, boolean[]> {
	public CEBooleanArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.isType(field.getType(), boolean.class, 1);
	}

	@Override
	protected String fieldName(Field field, ConfigBooleanArray annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	public boolean[] load(OmnisConfiguration config, AnnotatedField<ConfigBooleanArray, boolean[]> field) {
		ConfigBooleanArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getBooleanArray(annotation.category(), field.name(), field.defaultValue(), comment, annotation.maxListLength());
	}

	@Override
	public void save(OmnisConfiguration config, AnnotatedField<ConfigBooleanArray, boolean[]> field) {
		ConfigBooleanArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getBooleanArrayProperty(annotation.category(), field.name(), field.defaultValue(), comment, annotation.maxListLength()).set(field.defaultValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigBooleanArray, boolean[]> field) {
		boolean[] array = field.currentValue();
		buffer.writeInt(array.length);
		buffer.writeBooleans(array);
	}

	@Override
	protected boolean[] readField(InputBuffer buffer, AnnotatedField<ConfigBooleanArray, boolean[]> field) {
		return buffer.readBooleans(buffer.readInt());
	}
}
