package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigStringArray;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEStringArray extends ConfigEntry<ConfigStringArray, String[]> {
	public CEStringArray(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return field.getType() == String[].class;
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getStringArray(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment);
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigStringArray annotation, String[] currentValue, String[] defaultValue) {
		String comment = processComment(annotation.comment());
		config.getStringArrayProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment).set(currentValue);
	}
}
