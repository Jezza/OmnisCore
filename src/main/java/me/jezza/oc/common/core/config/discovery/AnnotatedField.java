package me.jezza.oc.common.core.config.discovery;

import com.google.common.base.Throwables;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.Config.ConfigSync;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * A representation of a field that was annotated by a specific annotation.
 *
 * @author Jezza
 */
public class AnnotatedField<T extends Annotation, V> {
	protected final Field field;
	protected final T annotation;
	protected final V defaultValue;
	protected final String name;

	protected transient Boolean sync = null;

	/**
	 * @param field      - A static field
	 * @param annotation - The annotation on the field
	 * @param name       - A name of the field.
	 */
	public AnnotatedField(Field field, T annotation, String name) {
		this.field = field;
		this.annotation = annotation;
		this.name = name;
		this.defaultValue = currentValue();
	}

	/**
	 * @return - The field that has been annotated.
	 */
	public final Field field() {
		return field;
	}

	/**
	 * @return - The annotation that is attached to the field.
	 */
	public final T annotation() {
		return annotation;
	}

	/**
	 * @return - The value of the field at time of startup.
	 */
	public final V defaultValue() {
		return defaultValue;
	}

	/**
	 * @return - The name that was passed in from the constructor.
	 */
	public final String name() {
		return name;
	}

	/**
	 * @return The value currently stored in the field, can be null.
	 */
	@SuppressWarnings("unchecked")
	public final V currentValue() {
		try {
			return (V) field.get(null);
		} catch (IllegalAccessException e) {
			OmnisCore.logger.error("Failed to retrieve current value from {}.{}.", field.getDeclaringClass(), field.getName());
			throw Throwables.propagate(e);
		} catch (ClassCastException e) {
			OmnisCore.logger.error("Field with: {} not of expected type: {}", annotation.annotationType(), field.toGenericString());
			throw Throwables.propagate(e);
		}
	}

	public final void set(V value) {
		try {
			field.set(null, value);
		} catch (IllegalAccessException e) {
			OmnisCore.logger.error("Failed to set value of {}.{}.", field.getDeclaringClass(), field.getName());
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Lazily checks if the field has a {@link ConfigSync} has been attached.
	 *
	 * @return - true, if {@link ConfigSync} is present on the field.
	 */
	public boolean canSync() {
		if (sync == null)
			sync = field.isAnnotationPresent(ConfigSync.class);
		return sync;
	}

	@Override
	public final String toString() {
		return field.getDeclaringClass().getCanonicalName() + '.' + field.getName() + ", @" + annotation.annotationType().getSimpleName() + ", sync: " + canSync();
	}
}
