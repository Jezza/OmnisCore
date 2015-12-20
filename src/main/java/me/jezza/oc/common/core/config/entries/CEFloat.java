package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigFloat;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEFloat extends ConfigEntry<ConfigFloat, Float> {
	public CEFloat(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == float.class;
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getFloat(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, annotation.minValue(), annotation.maxValue(), comment);
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigFloat annotation, Float currentValue, Float defaultValue) {
		String comment = processComment(annotation.comment());
		config.getFloatProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, annotation.minValue(), annotation.maxValue(), comment).set(currentValue);
	}
}
