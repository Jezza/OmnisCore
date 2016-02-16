package me.jezza.oc.common.core.config.discovery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.google.common.base.Throwables;
import cpw.mods.fml.relauncher.SideOnly;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.config.Config.ConfigSync;

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
	public final Field raw() {
		return field;
	}

	/**
	 * @return - The declared type of the field.
	 */
	public final Class<?> type() {
		return field.getType();
	}

	/**
	 * @return - The name that was passed in from the constructor.
	 */
	public final String name() {
		return name;
	}

	/**
	 * @return - The annotation that is attached to the field.
	 */
	public final T annotation() {
		return annotation;
	}

	/**
	 * @return - The default value, if any, assigned to the field on startup.
	 */
	public final V defaultValue() {
		return defaultValue;
	}

	/**
	 * @return - The value, if any, currently assigned to the field, can be null.
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

	/**
	 * @param value - Assigns the value to the field.
	 */
	public final void set(V value) {
		try {
			field.set(null, value);
		} catch (IllegalAccessException e) {
			OmnisCore.logger.error("Failed to set value of {}.{}.", field.getDeclaringClass(), field.getName());
			throw Throwables.propagate(e);
		}
	}

	/**
	 * Lazily checks if the field can be synchronised.
	 *
	 * @return - true, if the field should be sent to the client.
	 */
	public final boolean canSync() {
		if (sync == null)
			sync = calculateSync();
		return sync;
	}

	/**
	 * Override this, if you wish to specify another means the field uses to identify its ability to be sent to the client.
	 *
	 * @return - True, if the field can be sent to the client.
	 */
	protected boolean calculateSync() {
		return !field.isAnnotationPresent(SideOnly.class) && field.isAnnotationPresent(ConfigSync.class);
	}

	@Override
	public String toString() {
		return field.getDeclaringClass().getCanonicalName() + '.' + field.getName() + ", @" + annotation.annotationType().getSimpleName() + ", sync: " + canSync();
	}
}
