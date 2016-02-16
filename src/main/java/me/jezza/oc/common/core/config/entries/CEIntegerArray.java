package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import me.jezza.oc.common.core.config.Config.ConfigIntegerArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

public class CEIntegerArray extends ConfigEntry<ConfigIntegerArray, int[]> {
	public CEIntegerArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.isType(field.getType(), int.class, 1);
	}

	@Override
	protected String fieldName(Field field, ConfigIntegerArray annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected int[] load(OmnisConfiguration config, AnnotatedField<ConfigIntegerArray, int[]> field) {
		ConfigIntegerArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getIntArray(annotation.category(), field.name(), field.defaultValue(), comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigIntegerArray, int[]> field) {
		ConfigIntegerArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getIntArrayProperty(annotation.category(), field.name(), field.defaultValue(), comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength()).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigIntegerArray, int[]> field) {
		int[] array = field.currentValue();
		buffer.writeInt(array.length);
		buffer.writeInts(array);
	}

	@Override
	protected int[] readField(InputBuffer buffer, AnnotatedField<ConfigIntegerArray, int[]> field) {
		return buffer.readInts(buffer.readInt());
	}
}
