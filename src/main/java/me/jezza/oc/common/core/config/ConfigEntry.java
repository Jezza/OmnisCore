package me.jezza.oc.common.core.config;

import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.Config.ConfigKey;
import me.jezza.oc.common.core.config.Config.ConfigSync;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.interfaces.InputBuffer;
import me.jezza.oc.common.interfaces.OutputBuffer;
import me.jezza.oc.common.utils.Debug;
import me.jezza.oc.common.utils.helpers.StringHelper;
import net.minecraft.entity.player.EntityPlayer;
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
		if (!configMap.containsKey(name)) {
			String fieldName = fieldName(field, annotation);
			configMap.put(name, new AnnotatedField<T, V>(field, annotation, fieldName));
		}
	}

	public void processFields(boolean saveFlag) {
		for (AnnotatedField<T, V> wrapper : configMap.values()) {
			if (Debug.console())
				OmnisCore.logger.info("Operating on: " + wrapper);
			config.load();
			try {
				if (!saveFlag) {
					Object object = load(config, wrapper.field(), wrapper.name(), wrapper.annotation(), wrapper.currentValue(), wrapper.defaultValue());
					wrapper.field().set(null, object);
				} else {
					save(config, wrapper.field(), wrapper.name(), wrapper.annotation(), wrapper.currentValue(), wrapper.defaultValue());
				}
			} catch (Exception e) {
				OmnisCore.logger.log(Level.FATAL, format("Failed to configure field: {}", wrapper), e);
			} finally {
				if (config.hasChanged())
					config.save();
			}
		}
	}

	public void writeSync(EntityPlayer player, OutputBuffer buffer) {
		for (AnnotatedField<T, V> wrapper : configMap.values()) {
			if (canSync(wrapper))
				writeField(player, buffer, wrapper);
		}
	}

	public void readSync(InputBuffer buffer) {
		for (AnnotatedField<T, V> wrapper : configMap.values()) {
			if (canSync(wrapper))
				readField(buffer, wrapper);
		}
	}

	/**
	 * Used to determine the name that is used throughout the running instance.
	 *
	 * @param field      - The field in question.
	 * @param annotation - The annotation that is attached to the field.
	 * @return - The name that the field should be known by.
	 */
	protected String fieldName(Field field, T annotation) {
		ConfigKey key = field.getAnnotation(ConfigKey.class);
		return key != null ? key.value() : StringHelper.convertToLowerSnakeCase(field.getName());
	}

	/**
	 * Note: If this method returns true it's assumed that {@link #writeField(EntityPlayer, OutputBuffer, AnnotatedField)}
	 * and {@link #readField(InputBuffer, AnnotatedField)} are overridden.
	 * By default, it passes the entire method off to {@link AnnotatedField#canSync()}.
	 *
	 * @param wrapper - The wrapper object that is used for handling the field and the attached annotation.
	 * @return - true, if the field can be sent to the client.
	 */
	protected boolean canSync(AnnotatedField<T, V> wrapper) {
		return wrapper.canSync();
	}

	/**
	 * A simple helper method to create a {@link ConfigurationException}, valid for throwing.
	 *
	 * @param message - The exception's message.
	 * @return - A valid configuration exception, ready to be thrown.
	 */
	protected final ConfigurationException error(String message) {
		return new ConfigurationException(message);
	}

	/**
	 * A simple helper method to create a {@link ConfigurationException}, valid for throwing.
	 *
	 * @param message - The exception's message.
	 * @param params  - The parameters that will be passed in with the message for formatting via {@link me.jezza.oc.common.utils.helpers.StringHelper#format(String, Object...)}
	 * @return - A valid configuration exception, ready to be thrown.
	 */
	protected final ConfigurationException error(String message, Object... params) {
		return new ConfigurationException(format(message, params));
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
		for (int i = 0; i < comments.length; i++) {
			String comment = comments[i];
			if (useable(comment)) {
				if (i != 0)
					builder.append(System.lineSeparator());
				builder.append(translateWithFallback(comment));
			}
		}
		return builder.toString();
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
	 * @param name         -   The name of the field that has the annotation. Typically used for the key in the config file.
	 * @param annotation   -   The annotation type that was applied to the field.
	 * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
	 * @param defaultValue -   The default value, if any, assigned to the field on startup.
	 * @return -   The field is set to this value. It can be null.
	 */
	protected abstract Object load(OmnisConfiguration config, Field field, String name, T annotation, V currentValue, V defaultValue);

	/**
	 * @param config       -   The config instance of the file that the system determined was the correct hierarchy
	 * @param field        -   The field that has the annotation.
	 * @param name         -   The name of the field that has the annotation. Typically used for the key in the config file.
	 * @param annotation   -   The annotation type that was applied to the field.
	 * @param currentValue -   The value, if any, already assigned to the field.
	 * @param defaultValue -   The default value, if any, assigned to the field on startup.
	 */
	protected abstract void save(OmnisConfiguration config, Field field, String name, T annotation, V currentValue, V defaultValue);

	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<T, V> wrapper) {
		throw error("@{} writeField has not implemented on @{}.", ConfigSync.class.getSimpleName(), wrapper.annotation().annotationType().getSimpleName());
	}

	protected void readField(InputBuffer buffer, AnnotatedField<T, V> wrapper) {
		throw error("@{} readField has not implemented on @{}.", ConfigSync.class.getSimpleName(), wrapper.annotation().annotationType().getSimpleName());
	}
}
