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

public abstract class ConfigEntry<A extends Annotation, V> {
	protected final Map<String, AnnotatedField<A, V>> configMap = new LinkedHashMap<>(8);
	protected final OmnisConfiguration config;

	public ConfigEntry(OmnisConfiguration config) {
		this.config = config;
	}

	public void add(Field field, A annotation) {
		String canonicalName = field.getDeclaringClass().getCanonicalName() + '.' + field.getName();
		if (!checkField(field))
			throw error(format("@{} threw an error on {}! Invalid field type.", annotation.annotationType().getSimpleName(), canonicalName));
		if (!configMap.containsKey(canonicalName))
			configMap.put(canonicalName, newAnnotatedField(field, annotation, fieldName(field, annotation)));
	}

	protected AnnotatedField<A, V> newAnnotatedField(Field field, A annotation, String name) {
		return new AnnotatedField<>(field, annotation, name);
	}

	public void processFields(boolean saveFlag) {
		for (AnnotatedField<A, V> field : configMap.values()) {
			if (Debug.console())
				OmnisCore.logger.info("Operating on: " + field);
			config.load();
			try {
				if (saveFlag) {
					save(config, field);
				} else {
					field.set(load(config, field));
				}
			} catch (Exception e) {
				OmnisCore.logger.log(Level.FATAL, format("Failed to configure field: {}", field), e);
			} finally {
				if (config.hasChanged())
					config.save();
			}
		}
	}

	public void writeSync(EntityPlayer player, OutputBuffer buffer) {
		for (AnnotatedField<A, V> field : configMap.values())
			if (canSync(field))
				writeField(player, buffer, field);
	}

	public void readSync(InputBuffer buffer) {
		for (AnnotatedField<A, V> field : configMap.values())
			if (canSync(field))
				field.set(readField(buffer, field));
	}

	/**
	 * Used to determine the name that is used throughout the running instance.
	 *
	 * @param field      - The field in question.
	 * @param annotation - The annotation that is attached to the field.
	 * @return - The name that the field should be known by.
	 */
	protected String fieldName(Field field, A annotation) {
		ConfigKey key = field.getAnnotation(ConfigKey.class);
		return key != null ? key.value() : StringHelper.convertToLowerSnakeCase(field.getName());
	}

	/**
	 * Note: If this method returns true it's assumed that {@link #writeField(EntityPlayer, OutputBuffer, AnnotatedField)}
	 * and {@link #readField(InputBuffer, AnnotatedField)} are overridden.
	 * By default, it passes the entire method off to {@link AnnotatedField#canSync()}.
	 *
	 * @param field - The object that is used for handling the field and the attached annotation.
	 * @return - true, if the field can be sent to the client.
	 */
	protected boolean canSync(AnnotatedField<A, V> field) {
		return field.canSync();
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
	 * @param config - The config instance of the file that the system determined was the correct hierarchy
	 * @param field  - The field that was annotated.
	 * @return - The field is set to this value. It can be null.
	 */
	protected abstract V load(OmnisConfiguration config, AnnotatedField<A, V> field);

	/**
	 * @param config - The config instance of the file that the system determined was the correct hierarchy
	 * @param field  - The field that was annotated.
	 */
	protected abstract void save(OmnisConfiguration config, AnnotatedField<A, V> field);

	/**
	 * Used to write the fields value to an {@link OutputBuffer}, so it can be read and set on the client.
	 *
	 * @param player - The player that was logging on.
	 * @param buffer - The buffer to write to.
	 * @param field  - The field that is being sent.
	 */
	protected void writeField(EntityPlayer player, OutputBuffer buffer, AnnotatedField<A, V> field) {
		throw error("@{} writeField has not implemented on @{}.", ConfigSync.class.getSimpleName(), field.annotation().annotationType().getSimpleName());
	}

	/**
	 * Used to set the field from the values read out of an {@link InputBuffer}.
	 *
	 * @param buffer - The buffer to read from.
	 * @param field  - The field that is being read.
	 * @return - the value to set the field.
	 */
	protected V readField(InputBuffer buffer, AnnotatedField<A, V> field) {
		throw error("@{} readField has not implemented on @{}.", ConfigSync.class.getSimpleName(), field.annotation().annotationType().getSimpleName());
	}
}
