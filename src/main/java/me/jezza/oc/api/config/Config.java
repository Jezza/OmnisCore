package me.jezza.oc.api.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Config {

    /**
     * If you want any custom config file location, override configFile() with the location.
     * NOTE: That any string that configFile returns will be pushed on the end of the default config directory.
     * EG, configFile() return "AwesomeMod/awesomeMod.cfg";
     * ConfigHandler will try locating the config file as such "config/AwesomeMod/awesomeMod.cfg"
     * If it fails, it will generate it.
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Controller {
        String configFile() default "";
    }

    /**
     * Implement for registering any custom annotations.
     */
    public interface IConfigRegistrar {
        void registerCustomAnnotations(IConfigRegistry registry);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigBoolean {
        String category();

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigBooleanArray {
        String category();

        int maxListLength() default -1;

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigInteger {
        String category();

        int minValue() default Integer.MIN_VALUE;

        int maxValue() default Integer.MAX_VALUE;

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigIntegerArray {
        String category();

        int minValue() default Integer.MIN_VALUE;

        int maxValue() default Integer.MAX_VALUE;

        int maxListLength() default -1;

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigFloat {
        String category();

        float minValue() default Float.MIN_VALUE;

        float maxValue() default Float.MAX_VALUE;

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigDouble {
        String category();

        double minValue() default Double.MIN_VALUE;

        double maxValue() default Double.MAX_VALUE;

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigDoubleArray {
        String category();

        double minValue() default Double.MIN_VALUE;

        double maxValue() default Double.MAX_VALUE;

        int maxListLength() default -1;

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigString {
        String category();

        String[] validValues() default {};

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigStringArray {
        String category();

        String[] comment() default {};
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ConfigEnum {
        String category();

        String[] comment() default {};
    }
}
