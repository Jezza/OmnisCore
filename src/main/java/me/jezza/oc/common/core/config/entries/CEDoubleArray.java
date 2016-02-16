package me.jezza.oc.common.core.config.entries;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

import java.lang.reflect.Field;

import me.jezza.oc.common.core.config.Config.ConfigDoubleArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

public class CEDoubleArray extends ConfigEntry<ConfigDoubleArray, double[]> {
	public CEDoubleArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.isType(field.getType(), double.class, 1);
	}

	@Override
	protected String fieldName(Field field, ConfigDoubleArray annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected double[] load(OmnisConfiguration config, AnnotatedField<ConfigDoubleArray, double[]> field) {
		ConfigDoubleArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getDoubleArray(annotation.category(), field.name(), field.defaultValue(), comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigDoubleArray, double[]> field) {
		ConfigDoubleArray annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getDoubleArrayProperty(annotation.category(), field.name(), field.defaultValue(), comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength()).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigDoubleArray, double[]> field) {
		double[] array = field.currentValue();
		buffer.writeInt(array.length);
		buffer.writeDoubles(array);
	}

	@Override
	protected double[] readField(InputBuffer buffer, AnnotatedField<ConfigDoubleArray, double[]> field) {
		return buffer.readDoubles(buffer.readInt());
	}
}
