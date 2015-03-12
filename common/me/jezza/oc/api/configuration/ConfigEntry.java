package me.jezza.oc.api.configuration;

import com.google.common.base.Throwables;
import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.utils.Localise;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ConfigEntry<T extends Annotation, D> {

    private Map<Field, FieldWrapper> configMap;

    public ConfigEntry() {
        configMap = new LinkedHashMap<>();
    }

    public void add(Field field, T annotation) {
        if (!configMap.containsKey(field))
            configMap.put(field, new FieldWrapper(field, annotation));
    }

    @SuppressWarnings("unchecked")
    public void processFields(Configuration config, boolean saveFlag) {
        for (Map.Entry<Field, FieldWrapper> entry : configMap.entrySet()) {
            Field field = entry.getKey();
            FieldWrapper wrapper = entry.getValue();

            String fieldName = field.getName().toLowerCase();
            try {
                if (!saveFlag) {
                    Object object = loadAnnotation(config, fieldName, wrapper.annotation, wrapper.currentValue(), wrapper.defaultValue);
                    field.set(null, object);
                } else
                    saveAnnotation(config, fieldName, wrapper.annotation, wrapper.currentValue(), wrapper.defaultValue);
            } catch (Exception e) {
                CoreProperties.logger.log(Level.FATAL, String.format("Failed to configure field: %s, with annotated type: %s", fieldName, wrapper.annotation.annotationType().getSimpleName()), e);
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
    public String processComment(String... comments) {
        if (comments.length == 0)
            return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String comment : comments)
            stringBuilder.append(Localise.safeTranslate(comment));
        return stringBuilder.toString().trim() + System.lineSeparator();
    }

    /**
     * @param config       -   The config instance of the file that the system determined was the correct hierarchy
     * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the configuration file.
     * @param annotation   -   The annotation type that was applied to the field.
     * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
     * @param defaultValue -   The default value, if any, already assigned to the field.
     * @return -   The object to set the field to. Can be null if no defaultValue is given.
     */
    public abstract Object loadAnnotation(Configuration config, String fieldName, T annotation, D currentValue, D defaultValue);

    /**
     * @param config       -   The config instance of the file that the system determined was the correct hierarchy
     * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the configuration file.
     * @param annotation   -   The annotation type that was applied to the field.
     * @param currentValue -   The value, if any, already assigned to the field. Can be the same as defaultValue
     * @param defaultValue -   The default value, if any, already assigned to the field.
     */
    public abstract void saveAnnotation(Configuration config, String fieldName, T annotation, D currentValue, D defaultValue);

    private class FieldWrapper {
        public D defaultValue;
        public T annotation;
        private Field field;

        @SuppressWarnings("unchecked")
        public FieldWrapper(Field field, T annotation) {
            this.field = field;
            this.annotation = annotation;
            try {
                this.defaultValue = (D) field.get(null);
            } catch (IllegalAccessException ignored) {
                this.defaultValue = null;
            }
        }

        @SuppressWarnings("unchecked")
        private D currentValue() {
            try {
                return (D) field.get(null);
            } catch (IllegalAccessException e) {
                CoreProperties.logger.info("Failed to get current value.");
                Throwables.propagate(e);
            }
            return null;
        }
    }

}
