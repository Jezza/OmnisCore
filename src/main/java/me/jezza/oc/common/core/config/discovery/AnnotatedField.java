package me.jezza.oc.common.core.config.discovery;

import com.google.common.base.Throwables;
import me.jezza.oc.OmnisCore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static me.jezza.oc.common.utils.helpers.StringHelper.convertToLowerSnakeCase;

/**
 * @author Jezza
 */
public class AnnotatedField<T extends Annotation, V> {
	protected final Field field;
	protected final T annotation;
	protected final V defaultValue;
	protected final String fieldName;

	/**
	 * @param field      A static field
	 * @param annotation The annotation on the field
	 */
	public AnnotatedField(Field field, T annotation) {
		this.field = field;
		this.fieldName = convertToLowerSnakeCase(field.getName());
		this.annotation = annotation;
		this.defaultValue = currentValue();
	}

	public final Field field() {
		return field;
	}

	public final T annotation() {
		return annotation;
	}

	public final V defaultValue() {
		return defaultValue;
	}

	public final String fieldName() {
		return fieldName;
	}

	/**
	 * @return The value currently stored in the field, can be null.
	 */
	@SuppressWarnings("unchecked")
	public V currentValue() {
		try {
			return (V) field.get(null);
		} catch (IllegalAccessException e) {
			OmnisCore.logger.error("Failed to get current value.");
			throw Throwables.propagate(e);
		} catch (ClassCastException e) {
			OmnisCore.logger.error("Field with: " + annotation.annotationType() + " not of expected type:" + field.toGenericString());
			throw Throwables.propagate(e);
		}
	}
}
