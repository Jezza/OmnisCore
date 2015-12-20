package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigIntegerArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEIntegerArray extends ConfigEntry<ConfigIntegerArray, int[]> {
	public CEIntegerArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.isType(field.getType(), int.class, 1);
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getIntArray(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength());
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigIntegerArray annotation, int[] currentValue, int[] defaultValue) {
		String comment = processComment(annotation.comment());
		config.getIntArrayProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.minValue(), annotation.maxValue(), annotation.maxListLength()).set(currentValue);
	}
}
