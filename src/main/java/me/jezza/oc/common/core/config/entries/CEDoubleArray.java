package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigDoubleArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

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
	public Object load(OmnisConfiguration config, Field field, String name, ConfigDoubleArray annotation, double[] currentValue, double[] defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getDoubleArray(annotation.category(), name, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String name, ConfigDoubleArray annotation, double[] currentValue, double[] defaultValue) {
		String comment = processComment(annotation.comment());
		config.getDoubleArrayProperty(annotation.category(), name, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength()).set(currentValue);
	}
}
