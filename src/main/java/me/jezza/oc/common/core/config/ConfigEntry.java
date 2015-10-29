package me.jezza.oc.common.core.config;

import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.core.config.discovery.AnnotatedField;
import me.jezza.oc.common.utils.helpers.StringHelper;
import me.jezza.oc.common.utils.maths.Maths;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static me.jezza.oc.common.utils.helpers.StringHelper.format;
import static me.jezza.oc.common.utils.helpers.StringHelper.translateWithFallback;
import static me.jezza.oc.common.utils.helpers.StringHelper.useable;
import static net.minecraftforge.common.config.Property.Type.STRING;

public abstract class ConfigEntry<T extends Annotation, V> {

	protected final Map<String, AnnotatedField<T, V>> configMap = new LinkedHashMap<>(4);
	protected final Configuration config;

	public ConfigEntry(Configuration config) {
		this.config = config;
	}

	public void add(Field field, T annotation) {
		String fieldName = field.getDeclaringClass().getCanonicalName() + '.' + field.getName();
		if (!checkField(field, annotation))
			throw error(format("@{} threw an error on {}! Please confirm the field type.", annotation.annotationType().getSimpleName(), fieldName));
		if (!configMap.containsKey(fieldName))
			configMap.put(fieldName, new AnnotatedField<T, V>(field, annotation));
	}

	protected boolean checkField(Field field, T annotation) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public void processFields(boolean saveFlag) {
		for (Map.Entry<String, AnnotatedField<T, V>> entry : configMap.entrySet()) {
			AnnotatedField<T, V> wrapper = entry.getValue();
			config.load();
			try {
				if (!saveFlag) {
					Object object = loadAnnotation(config, wrapper.field(), wrapper.fieldName(), wrapper.annotation(), wrapper.currentValue(), wrapper.defaultValue());
					wrapper.field().set(null, object);
				} else {
					saveAnnotation(config, wrapper.field(), wrapper.fieldName(), wrapper.annotation(), wrapper.currentValue(), wrapper.defaultValue());
				}
			} catch (Exception e) {
				CoreProperties.logger.log(Level.FATAL, format("Failed to configure field: %s, with annotated type: %s", wrapper.fieldName(), wrapper.annotation().annotationType().getSimpleName()), e);
			} finally {
				if (config.hasChanged())
					config.save();
			}
		}
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
			if (useable(comment)) {
				String translation = translateWithFallback(comment);
				if (useable(translation))
					builder.append(translation).append(System.lineSeparator());
			}
		}
		return builder.toString();
	}

	/**
	 * Finds the first useable string.
	 * @param first - The first String to check.
	 * @param second - The second String to check.
	 * @param others - The rest to check.
	 * @return - the first String that passes {@link StringHelper#useable(CharSequence)}, null otherwise.
	 */
	protected String useableOr(String first, String second, String... others) {
		if (useable(first))
			return first;
		if (useable(second))
			return second;
		for (String other : others)
			if (useable(other))
				return other;
		return null;
	}

	/**
	 * @param config       -   The config instance of the file that the system determined was the correct hierarchy
	 * @param field        -   The field that has the annotation.
	 * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the config file.
	 * @param annotation   -   The annotation type that was applied to the field.
	 * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
	 * @param defaultValue -   The default value, if any, assigned to the field on startup.
	 * @return -   The object to set the field to. Can be null.
	 */
	public abstract Object loadAnnotation(Configuration config, Field field, String fieldName, T annotation, V currentValue, V defaultValue);

	/**
	 * @param config       -   The config instance of the file that the system determined was the correct hierarchy
	 * @param field        -   The field that has the annotation.
	 * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the config file.
	 * @param annotation   -   The annotation type that was applied to the field.
	 * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
	 * @param defaultValue -   The default value, if any, assigned to the field on startup.
	 */
	public abstract void saveAnnotation(Configuration config, Field field, String fieldName, T annotation, V currentValue, V defaultValue);

	protected ConfigurationException error(String message) {
		return new ConfigurationException(message);
	}

	protected ConfigurationException error(String message, Object... params) {
		return new ConfigurationException(StringHelper.format(message, params));
	}

	/**
	 * Getters for properties and values.
	 * This way you don't have to deal with the inconsistencies of Forge's config methods.
	 */

	public boolean getBoolean(String category, String key, boolean defaultValue) {
		return getBooleanProperty(category, key, defaultValue, null, key).getBoolean();
	}

	public boolean getBoolean(String category, String key, boolean defaultValue, String comment) {
		return getBooleanProperty(category, key, defaultValue, comment, key).getBoolean();
	}

	public boolean getBoolean(String category, String key, boolean defaultValue, String comment, String langKey) {
		return getBooleanProperty(category, key, defaultValue, comment, langKey).getBoolean();
	}

	public Property getBooleanProperty(String category, String key, boolean defaultValue) {
		return getBooleanProperty(category, key, defaultValue, null, key);
	}

	public Property getBooleanProperty(String category, String key, boolean defaultValue, String comment) {
		return getBooleanProperty(category, key, defaultValue, comment, key);
	}

	public Property getBooleanProperty(String category, String key, boolean defaultValue, String comment, String langKey) {
		Property prop = config.get(category, key, defaultValue, comment);
		prop.setLanguageKey(langKey);
		prop.comment += " [default: " + defaultValue + "]";
		return prop;
	}

	public boolean[] getBooleanArray(String category, String key, boolean[] defaultValues) {
		return getBooleanArrayProperty(category, key, defaultValues, null, -1).getBooleanList();
	}

	public boolean[] getBooleanArray(String category, String key, boolean[] defaultValues, String comment) {
		return getBooleanArrayProperty(category, key, defaultValues, comment, -1).getBooleanList();
	}

	public boolean[] getBooleanArray(String category, String key, boolean[] defaultValues, String comment, int maxListLength) {
		return getBooleanArrayProperty(category, key, defaultValues, comment, maxListLength).getBooleanList();
	}

	public Property getBooleanArrayProperty(String category, String key, boolean[] defaultValues) {
		return getBooleanArrayProperty(category, key, defaultValues, null, -1);
	}

	public Property getBooleanArrayProperty(String category, String key, boolean[] defaultValues, String comment) {
		return getBooleanArrayProperty(category, key, defaultValues, comment, -1);
	}

	public Property getBooleanArrayProperty(String category, String key, boolean[] defaultValues, String comment, int maxListLength) {
		return config.get(category, key, defaultValues, comment, maxListLength >= 0, maxListLength);
	}

	public double getDouble(String category, String key, double defaultValue) {
		return getDoubleProperty(category, key, defaultValue, (String) null, Double.MIN_VALUE, Double.MAX_VALUE).getDouble();
	}

	public double getDouble(String category, String key, double defaultValue, String comment) {
		return getDoubleProperty(category, key, defaultValue, comment, Double.MIN_VALUE, Double.MAX_VALUE).getDouble();
	}

	public double getDouble(String category, String key, double defaultValue, String comment, double minValue, double maxValue) {
		return getDoubleProperty(category, key, defaultValue, comment, minValue, maxValue).getDouble();
	}

	public Property getDoubleProperty(String category, String key, double defaultValue) {
		return getDoubleProperty(category, key, defaultValue, (String) null, Double.MIN_VALUE, Double.MAX_VALUE);
	}

	public Property getDoubleProperty(String category, String key, double defaultValue, String comment) {
		return getDoubleProperty(category, key, defaultValue, comment, Double.MIN_VALUE, Double.MAX_VALUE);
	}

	public Property getDoubleProperty(String category, String key, double defaultValue, String comment, double minValue, double maxValue) {
		return config.get(category, key, defaultValue, comment, minValue, maxValue);
	}

	public double[] getDoubleArray(String category, String key, double[] defaultValues) {
		return getDoubleArrayProperty(category, key, defaultValues, null, Double.MIN_VALUE, Double.MAX_VALUE, -1).getDoubleList();
	}

	public double[] getDoubleArray(String category, String key, double[] defaultValues, String comment) {
		return getDoubleArrayProperty(category, key, defaultValues, comment, Double.MIN_VALUE, Double.MAX_VALUE, -1).getDoubleList();
	}

	public double[] getDoubleArray(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue) {
		return getDoubleArrayProperty(category, key, defaultValues, comment, minValue, maxValue, -1).getDoubleList();
	}

	public double[] getDoubleArray(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue, int maxListLength) {
		return getDoubleArrayProperty(category, key, defaultValues, comment, minValue, maxValue, maxListLength).getDoubleList();
	}

	public Property getDoubleArrayProperty(String category, String key, double[] defaultValues) {
		return getDoubleArrayProperty(category, key, defaultValues, null, Double.MIN_VALUE, Double.MAX_VALUE, -1);
	}

	public Property getDoubleArrayProperty(String category, String key, double[] defaultValues, String comment) {
		return getDoubleArrayProperty(category, key, defaultValues, comment, Double.MIN_VALUE, Double.MAX_VALUE, -1);
	}

	public Property getDoubleArrayProperty(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue) {
		return getDoubleArrayProperty(category, key, defaultValues, comment, minValue, maxValue, -1);
	}

	public Property getDoubleArrayProperty(String category, String key, double[] defaultValues, String comment, double minValue, double maxValue, int maxListLength) {
		return config.get(category, key, defaultValues, comment, minValue, maxValue, maxListLength >= 0, maxListLength);
	}

	public float getFloat(String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
		return getFloat(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	public float getFloat(String category, String key, float defaultValue, float minValue, float maxValue, String comment, String langKey) {
		Property prop = getFloatProperty(category, key, defaultValue, minValue, maxValue, comment, langKey);
		try {
			return Maths.clip(Float.parseFloat(prop.getString()), minValue, maxValue);
		} catch (Exception ignored) {
		}
		return defaultValue;
	}

	public Property getFloatProperty(String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
		return getFloatProperty(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	public Property getFloatProperty(String category, String key, float defaultValue, float minValue, float maxValue, String comment, String langKey) {
		Property prop = config.get(category, key, Float.toString(defaultValue), key);
		prop.setLanguageKey(langKey);
		prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
		prop.setMinValue(minValue);
		prop.setMaxValue(maxValue);
		return prop;
	}

	public int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
		return getInt(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	public int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
		int value = getIntProperty(category, key, defaultValue, minValue, maxValue, comment, langKey).getInt(defaultValue);
		return Maths.clip(value, minValue, maxValue);
	}

	public Property getIntProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
		return getIntProperty(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	public Property getIntProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
		Property prop = config.get(category, key, defaultValue);
		prop.setLanguageKey(langKey);
		prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
		prop.setMinValue(minValue);
		prop.setMaxValue(maxValue);
		return prop;
	}

	public int[] getIntArray(String category, String key, int[] defaultValues) {
		return getIntArrayProperty(category, key, defaultValues, null, Integer.MIN_VALUE, Integer.MAX_VALUE, -1).getIntList();
	}

	public int[] getIntArray(String category, String key, int[] defaultValues, String comment) {
		return getIntArrayProperty(category, key, defaultValues, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, -1).getIntList();
	}

	public int[] getIntArray(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue) {
		return getIntArrayProperty(category, key, defaultValues, comment, minValue, maxValue, -1).getIntList();
	}

	public int[] getIntArray(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue, int maxListLength) {
		return getIntArrayProperty(category, key, defaultValues, comment, minValue, maxValue, maxListLength).getIntList();
	}

	public Property getIntArrayProperty(String category, String key, int[] defaultValues) {
		return getIntArrayProperty(category, key, defaultValues, (String) null, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
	}

	public Property getIntArrayProperty(String category, String key, int[] defaultValues, String comment) {
		return getIntArrayProperty(category, key, defaultValues, comment, Integer.MIN_VALUE, Integer.MAX_VALUE, -1);
	}

	public Property getIntArrayProperty(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue) {
		return getIntArrayProperty(category, key, defaultValues, comment, minValue, maxValue, -1);
	}

	public Property getIntArrayProperty(String category, String key, int[] defaultValues, String comment, int minValue, int maxValue, int maxListLength) {
		return config.get(category, key, defaultValues, comment, minValue, maxValue, maxListLength >= 0, maxListLength);
	}

	public String getString(String category, String key, String defaultValue) {
		return getStringProperty(category, key, defaultValue, null, (String[]) null).getString();
	}

	public String getString(String category, String key, String defaultValue, String comment) {
		return getStringProperty(category, key, defaultValue, comment, (String[]) null).getString();
	}

	public String getString(String category, String key, String defaultValue, String comment, String[] validValues) {
		return getStringProperty(category, key, defaultValue, comment, validValues).getString();
	}

	public String getString(String category, String key, String defaultValue, String comment, String langKey, String[] validValues) {
		return getStringProperty(category, key, defaultValue, comment, langKey, validValues).getString();
	}

	public Property getStringProperty(String category, String key, String defaultValue) {
		return getStringProperty(category, key, defaultValue, null, key, (String[]) null);
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment) {
		return getStringProperty(category, key, defaultValue, comment, key, (String[]) null);
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment, String[] validValues) {
		return getStringProperty(category, key, defaultValue, comment, key, validValues);
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment, String langKey, String[] validValues) {
		Property prop = config.get(category, key, defaultValue, comment, STRING);
		prop.setLanguageKey(langKey);
		if (validValues != null)
			prop.setValidValues(validValues);
		return prop;
	}

	public String getString(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
		return getStringProperty(category, key, defaultValue, comment, key, validationPattern).getString();
	}

	public String getString(String category, String key, String defaultValue, String comment, String langKey, Pattern validationPattern) {
		return getStringProperty(category, key, defaultValue, comment, langKey, validationPattern).getString();
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
		return getStringProperty(category, key, defaultValue, comment, key, validationPattern);
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment, String langKey, Pattern validationPattern) {
		Property prop = config.get(category, key, defaultValue, comment, STRING);
		prop.setLanguageKey(langKey);
		if (validationPattern != null)
			prop.setValidationPattern(validationPattern);
		return prop;
	}

	public String[] getStringArray(String category, String key, String[] defaultValues) {
		return getStringArrayProperty(category, key, defaultValues, null, false, -1, null).getStringList();
	}

	public String[] getStringArray(String category, String key, String[] defaultValues, String comment) {
		return getStringArrayProperty(category, key, defaultValues, comment, false, -1, null).getStringList();
	}

	public String[] getStringArray(String category, String key, String[] defaultValues, String comment, Pattern validationPattern) {
		return getStringArrayProperty(category, key, defaultValues, comment, false, -1, validationPattern).getStringList();
	}

	public String[] getStringArray(String category, String key, String[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength, Pattern validationPattern) {
		return getStringArrayProperty(category, key, defaultValues, comment, isListLengthFixed, maxListLength, validationPattern).getStringList();
	}

	public Property getStringArrayProperty(String category, String key, String[] defaultValues) {
		return getStringArrayProperty(category, key, defaultValues, null, false, -1, null);
	}

	public Property getStringArrayProperty(String category, String key, String[] defaultValues, String comment) {
		return getStringArrayProperty(category, key, defaultValues, comment, false, -1, null);
	}

	public Property getStringArrayProperty(String category, String key, String[] defaultValues, String comment, Pattern validationPattern) {
		return getStringArrayProperty(category, key, defaultValues, comment, false, -1, validationPattern);
	}

	public Property getStringArrayProperty(String category, String key, String[] defaultValues, String comment, boolean isListLengthFixed, int maxListLength, Pattern validationPattern) {
		return config.get(category, key, defaultValues, comment, isListLengthFixed, maxListLength, validationPattern);
	}
}
