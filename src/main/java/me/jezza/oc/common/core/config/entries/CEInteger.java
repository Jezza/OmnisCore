package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigInteger;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEInteger extends ConfigEntry<ConfigInteger, Integer> {
	public CEInteger(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == int.class;
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getInt(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
		String comment = processComment(annotation.comment());
		config.getIntProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
	}
}
