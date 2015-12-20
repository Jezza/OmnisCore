package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigBooleanArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEBooleanArray extends ConfigEntry<ConfigBooleanArray, boolean[]> {
	public CEBooleanArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.isType(field.getType(), boolean.class, 1);
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigBooleanArray annotation, boolean[] currentValue, boolean[] defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getBooleanArray(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.maxListLength());
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigBooleanArray annotation, boolean[] currentValue, boolean[] defaultValue) {
		String comment = processComment(annotation.comment());
		config.getBooleanArrayProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.maxListLength()).set(currentValue);
	}
}
