package me.jezza.oc.common.core.config;

import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.channel.OmnisBuffer;
import me.jezza.oc.common.core.config.Config.ConfigSync;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Debug;
import org.apache.logging.log4j.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static me.jezza.oc.common.utils.helpers.StringHelper.*;

public abstract class ConfigEntry<T extends Annotation, V> {
	protected final Map<String, AnnotatedField<T, V>> configMap = new LinkedHashMap<>(8);
	protected final OmnisConfiguration config;

	public ConfigEntry(OmnisConfiguration config) {
		this.config = config;
	}

	public void add(Field field, T annotation) {
		String name = field.getDeclaringClass().getCanonicalName() + '.' + field.getName();
		if (!checkField(field))
			throw error(format("@{} threw an error on {}! Invalid field type.", annotation.annotationType().getSimpleName(), name));
		if (!configMap.containsKey(name))
			configMap.put(name, new AnnotatedField<T, V>(field, annotation));
	}

	public void processFields(boolean saveFlag) {
		for (AnnotatedField<T, V> wrapper : configMap.values()) {
			if (Debug.console())
				OmnisCore.logger.info("Operating on: " + wrapper);
			config.load();
			try {
				if (!saveFlag) {
					Object object = load(config, wrapper.field(), wrapper.fieldName(), wrapper.annotation(), wrapper.currentValue(), wrapper.defaultValue());
					wrapper.field().set(null, object);
				} else {
					save(config, wrapper.field(), wrapper.fieldName(), wrapper.annotation(), wrapper.currentValue(), wrapper.defaultValue());
				}
			} catch (Exception e) {
				OmnisCore.logger.log(Level.FATAL, format("Failed to configure field: {}", wrapper), e);
			} finally {
				if (config.hasChanged())
					config.save();
			}
		}
	}

	public void syncFields(OmnisBuffer buffer, boolean write) {
		for (AnnotatedField<T, V> wrapper : configMap.values()) {
			if (canSync(wrapper)) {
				if (write) {
					writeField(buffer, wrapper.field(), wrapper.fieldName(), wrapper.annotation(), wrapper.currentValue());
				} else {
					readField(buffer, wrapper.field(), wrapper.fieldName(), wrapper.annotation(), wrapper.currentValue());
				}
			}
		}
	}

	protected boolean canSync(AnnotatedField<T, V> wrapper) {
		return wrapper.canSync();
	}

	/**
	 * For your use.
	 * It processes all strings, attempts to localise each one, and puts in all in one string.
	 *
	 * @param comments - The comments in question.
	 * @return - The coherent "fully localised"* string. *Unless it fails... :/
	 */
	protected String processComment(String... comments) {
		if (comments.length == 0)
			return "";
		StringBuilder builder = new StringBuilder();
		for (String comment : comments) {
			if (useable(comment))
				builder.append(translateWithFallback(comment)).append(System.lineSeparator());
		}
		return builder.toString();
	}

	protected final ConfigurationException error(String message) {
		return new ConfigurationException(message);
	}

	protected final ConfigurationException error(String message, Object... params) {
		return new ConfigurationException(format(message, params));
	}

	protected void writeField(OutputBuffer buffer, Field field, String fieldName, T annotation, V currentValue) {
		throw error("@{} write has not implemented on @{}.", ConfigSync.class.getSimpleName(), annotation.annotationType().getSimpleName());
	}

	protected void readField(InputBuffer buffer, Field field, String fieldName, T annotation, V currentValue) {
		throw error("@{} read has not implemented on @{}.", ConfigSync.class.getSimpleName(), annotation.annotationType().getSimpleName());
	}

	/**
	 * Used to determine the correct use of the annotation on the field.
	 * Generally used to check the field type.
	 *
	 * @param field - The field that the annotation is attached to.
	 * @return - true, if the field is valid, false will throw an exception.
	 */
	protected abstract boolean checkField(Field field);

	/**
	 * @param config       -   The config instance of the file that the system determined was the correct hierarchy
	 * @param field        -   The field that has the annotation.
	 * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the config file.
	 * @param annotation   -   The annotation type that was applied to the field.
	 * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
	 * @param defaultValue -   The default value, if any, assigned to the field on startup.
	 * @return -   The object to set the field to. Can be null.
	 */
	protected abstract Object load(OmnisConfiguration config, Field field, String fieldName, T annotation, V currentValue, V defaultValue);

	/**
	 * @param config       -   The config instance of the file that the system determined was the correct hierarchy
	 * @param field        -   The field that has the annotation.
	 * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the config file.
	 * @param annotation   -   The annotation type that was applied to the field.
	 * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
	 * @param defaultValue -   The default value, if any, assigned to the field on startup.
	 */
	protected abstract void save(OmnisConfiguration config, Field field, String fieldName, T annotation, V currentValue, V defaultValue);
}
