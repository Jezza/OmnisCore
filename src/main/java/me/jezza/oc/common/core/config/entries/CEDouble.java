package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigDouble;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.Classes;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEDouble extends ConfigEntry<ConfigDouble, Double> {
	public CEDouble(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return Classes.unwrap(field.getType()) == double.class;
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getDouble(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.minValue(), annotation.maxValue());
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigDouble annotation, Double currentValue, Double defaultValue) {
		String comment = processComment(annotation.comment());
		config.getDoubleProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.minValue(), annotation.maxValue()).set(currentValue);
	}
}
