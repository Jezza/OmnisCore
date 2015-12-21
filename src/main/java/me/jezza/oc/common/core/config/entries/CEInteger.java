package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigInteger;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;

import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.useable;

public class CEInteger extends ConfigEntry<ConfigInteger, Integer> {
	public CEInteger(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == int.class;
	}

	@Override
	protected String fieldName(Field field, ConfigInteger annotation) {
		String name = annotation.name();
		return useable(name) ? name : super.fieldName(field, annotation);
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String name, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getInt(annotation.category(), name, defaultValue, annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String name, ConfigInteger annotation, Integer currentValue, Integer defaultValue) {
		String comment = processComment(annotation.comment());
		config.getIntProperty(annotation.category(), name, defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
	}
}
