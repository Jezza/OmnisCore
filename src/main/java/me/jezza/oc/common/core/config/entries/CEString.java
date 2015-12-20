package me.jezza.oc.common.core.config.entries;

import me.jezza.oc.common.core.config.Config.ConfigString;
import me.jezza.oc.common.core.config.ConfigEntry;
import me.jezza.oc.common.core.config.OmnisConfiguration;
import me.jezza.oc.common.utils.helpers.StringHelper;

import java.lang.reflect.Field;

public class CEString extends ConfigEntry<ConfigString, String> {
	public CEString(OmnisConfiguration config) {
		super(config);
	}

	@Override
	protected boolean checkField(Field field) {
		return field.getType() == String.class;
	}

	@Override
	public Object load(OmnisConfiguration config, Field field, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
		String comment = processComment(annotation.comment());
		return config.getString(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.validValues());
	}

	@Override
	public void save(OmnisConfiguration config, Field field, String fieldName, ConfigString annotation, String currentValue, String defaultValue) {
		String comment = processComment(annotation.comment());
		config.getStringProperty(annotation.category(), StringHelper.firstUseable(annotation.name(), fieldName), defaultValue, comment, annotation.validValues()).set(currentValue);
	}
}
