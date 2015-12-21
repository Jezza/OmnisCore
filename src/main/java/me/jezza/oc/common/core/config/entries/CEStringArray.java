package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigStringArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

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
	public Object load(OmnisConfiguration config, Field field, String name, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getStringArray(annotation.category(), name, defaultValue, comment);
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String name, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		config.getStringArrayProperty(annotation.category(), name, defaultValue, comment).set(currentValue);
	}
}
