package me.jezza.oc.api.configuration;

import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.core.CoreProperties;
import me.jezza.oc.common.utils.Localise;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ConfigEntry<T extends Annotation, D> {

    private LinkedHashMap<Field, T> configMap;
    private boolean isClientSide;

    public ConfigEntry() {
        configMap = new LinkedHashMap<>();
        isClientSide = OmnisCore.proxy.isClient();
    }

    public boolean containsField(Field field) {
        return configMap.containsKey(field);
    }

    public void add(Field field, T annotation) {
        if (!containsField(field))
            configMap.put(field, annotation);
    }

    @SuppressWarnings("unchecked")
    public void processCurrentEntries(Configuration config) {
        for (Map.Entry<Field, T> entry : configMap.entrySet()) {
            Field field = entry.getKey();
            T value = entry.getValue();
            String fieldName = field.getName().toLowerCase();
            try {
                field.setAccessible(true);
                Object object = processAnnotation(config, fieldName, value, (D) field.get(null));
                field.set(null, object);
            } catch (Exception e) {
                CoreProperties.logger.log(Level.FATAL, String.format("Failed to configure field: %s, with annotated type: %s", fieldName, value.annotationType().getSimpleName()), e);
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
        for (int i = 0; i < comments.length; i++) {
            String comment = comments[i];
            stringBuilder.append(isClientSide ? Localise.formatSafe(comment) : comment);
            if (i != comments.length - 1)
                stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString().trim() + System.lineSeparator();
    }

    /**
     * The only method you will ever need to worry about in this class.
     *
     * @param config       -   The config instance of the file that the system determined was the correct hierarchy
     * @param fieldName    -   The name of the field that has the annotation. Typically used for the key in the configuration file.
     * @param annotation   -   The annotation type that was applied to the field.
     * @param defaultValue -   The default value, if any, already assigned to the field.
     * @return -   The object to set the field to. Can be null.
     */
    public abstract Object processAnnotation(Configuration config, String fieldName, T annotation, @Nullable D defaultValue);

}
