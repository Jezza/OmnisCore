package me.jezza.oc.common.core.config;

import me.jezza.oc.common.utils.maths.Maths;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;
import java.util.regex.Pattern;

import static net.minecraftforge.common.config.Property.Type.STRING;

/**
 * @author Jezza
 */
public class OmnisConfiguration extends Configuration {

	public OmnisConfiguration() {
	}

	/**
	 * Create a configuration file for the file given in parameter.
	 */
	public OmnisConfiguration(File file) {
		this(file, null);
	}

	/**
	 * Create a configuration file for the file given in parameter with the provided config version number.
	 */
	public OmnisConfiguration(File file, String configVersion) {
		super(file, configVersion);
	}

	public OmnisConfiguration(File file, String configVersion, boolean caseSensitiveCustomCategories) {
		super(file, configVersion, caseSensitiveCustomCategories);
	}

	public OmnisConfiguration(File file, boolean caseSensitiveCustomCategories) {
		super(file, caseSensitiveCustomCategories);
	}

	public boolean getBoolean(String category, String key, boolean defaultValue) {
		return getBooleanProperty(category, key, defaultValue, null, key).getBoolean();
	}

	@Override
	public boolean getBoolean(String category, String key, boolean defaultValue, String comment) {
		return getBooleanProperty(category, key, defaultValue, comment, key).getBoolean();
	}

	@Override
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
		Property prop = get(category, key, defaultValue, comment);
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
		return get(category, key, defaultValues, comment, maxListLength >= 0, maxListLength);
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
		return get(category, key, defaultValue, comment, minValue, maxValue);
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
		return get(category, key, defaultValues, comment, minValue, maxValue, maxListLength >= 0, maxListLength);
	}

	@Override
	public float getFloat(String category, String key, float defaultValue, float minValue, float maxValue, String comment) {
		return getFloat(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	@Override
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
		Property prop = get(category, key, Float.toString(defaultValue), key);
		prop.setLanguageKey(langKey);
		prop.comment = comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]";
		prop.setMinValue(minValue);
		prop.setMaxValue(maxValue);
		return prop;
	}

	@Override
	public int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
		return getInt(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	@Override
	public int getInt(String category, String key, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
		int value = getIntProperty(category, key, defaultValue, minValue, maxValue, comment, langKey).getInt(defaultValue);
		return Maths.clip(value, minValue, maxValue);
	}

	public Property getIntProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment) {
		return getIntProperty(category, key, defaultValue, minValue, maxValue, comment, key);
	}

	public Property getIntProperty(String category, String key, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
		Property prop = get(category, key, defaultValue);
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
		return get(category, key, defaultValues, comment, minValue, maxValue, maxListLength >= 0, maxListLength);
	}

	public String getString(String category, String key, String defaultValue) {
		return getStringProperty(category, key, defaultValue, null, (String[]) null).getString();
	}

	@Override
	public String getString(String category, String key, String defaultValue, String comment) {
		return getStringProperty(category, key, defaultValue, comment, (String[]) null).getString();
	}

	@Override
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
		Property prop = get(category, key, defaultValue, comment, STRING);
		prop.setLanguageKey(langKey);
		if (validValues != null)
			prop.setValidValues(validValues);
		return prop;
	}

	@Override
	public String getString(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
		return getStringProperty(category, key, defaultValue, comment, key, validationPattern).getString();
	}

	@Override
	public String getString(String category, String key, String defaultValue, String comment, String langKey, Pattern validationPattern) {
		return getStringProperty(category, key, defaultValue, comment, langKey, validationPattern).getString();
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment, Pattern validationPattern) {
		return getStringProperty(category, key, defaultValue, comment, key, validationPattern);
	}

	public Property getStringProperty(String category, String key, String defaultValue, String comment, String langKey, Pattern validationPattern) {
		Property prop = get(category, key, defaultValue, comment, STRING);
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
		return get(category, key, defaultValues, comment, isListLengthFixed, maxListLength, validationPattern);
	}
}
