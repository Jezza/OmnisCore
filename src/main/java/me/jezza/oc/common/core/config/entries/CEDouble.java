package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigDouble;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Classes;
import net.minecraft.entity.player.EntityPlayer;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

public class CEDouble extends ConfigEntry<ConfigDouble, Double> {
	public CEDouble(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == double.class;
	}

	@Override
	protected String fieldName(Field field, ConfigDouble annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	protected Double load(OmnisConfiguration config, AnnotatedField<ConfigDouble, Double> field) {
		ConfigDouble annotation = field.annotation();
		String comment = processComment(annotation.comment());
		return config.getDouble(annotation.category(), field.name(), field.defaultValue(), comment, annotation.minValue(), annotation.maxValue());
	}

	@Override
	protected void save(OmnisConfiguration config, AnnotatedField<ConfigDouble, Double> field) {
		ConfigDouble annotation = field.annotation();
		String comment = processComment(annotation.comment());
		config.getDoubleProperty(annotation.category(), field.name(), field.defaultValue(), comment, annotation.minValue(), annotation.maxValue()).set(field.currentValue());
	}

	@Override
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<ConfigDouble, Double> field) {
		buffer.writeDouble(field.currentValue());
	}

	@Override
	protected Double readField(InputBuffer buffer, AnnotatedField<ConfigDouble, Double> field) {
		return buffer.readDouble();
	}
}
