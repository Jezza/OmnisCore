package me.jezza.oc.api.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Config {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigBoolean {
        String category();

        boolean defaultValue();

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigBooleanArray {
        String category();

        boolean[] defaultArray();

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigInteger {
        String category();

        int defaultValue();

        int minValue() default Integer.MIN_VALUE;

        int maxValue() default Integer.MAX_VALUE;

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigIntegerArray {
        String category();

        int[] defaultArray();

        int minValue() default Integer.MIN_VALUE;

        int maxValue() default Integer.MAX_VALUE;

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigFloat {
        String category();

        float defaultValue();

        float minValue() default Float.MIN_VALUE;

        float maxValue() default Float.MAX_VALUE;

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigDouble {
        String category();

        double defaultValue();

        double minValue() default Double.MIN_VALUE;

        double maxValue() default Double.MAX_VALUE;

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigDoubleArray {
        String category();

        double[] defaultArray();

        double minValue() default Double.MIN_VALUE;

        double maxValue() default Double.MAX_VALUE;

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigString {
        String category();

        String defaultValue();

        String[] validValues() default {};

        String comment() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ConfigStringArray {
        String category();

        String[] defaultArray();

        String[] validValues() default {};

        boolean isListLengthFixed() default false;

        int maxListLength() default -1;

        String comment() default "";
    }

}
