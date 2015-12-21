package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigIntegerArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

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
	public Object load(OmnisConfiguration config, Field field, String name, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getIntArray(annotation.category(), name, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String name, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
		String comment = processComment(annotation.comment());
		config.getIntArrayProperty(annotation.category(), name, defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength()).set(currentValue);
	}
}
