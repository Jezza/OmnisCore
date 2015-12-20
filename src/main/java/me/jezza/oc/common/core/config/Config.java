package me.jezza.oc.common.core.config;

import me.jezza.oc.common.core.config.entries.*;

import java.lang.annotation.*;

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
	 * Place this on the annotation that you want to be a ConfigAnnotation.
	 * You need to provide a ConfigEntry that pairs with each ConfigAnnotation you provide.
	 * Simply look below for an example.
	 * A ConfigEntry will be created once per mod that uses the annotation that it's associated with.
	 */
	@Target(ElementType.ANNOTATION_TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ConfigAnnotation {
		Class<? extends ConfigEntry<? extends Annotation, ?>> value();
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ConfigSync {
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEBoolean.class)
	public @interface ConfigBoolean {
		String category();

		String name() default "";

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEBooleanArray.class)
	public @interface ConfigBooleanArray {
		String category();

		String name() default "";

		int maxListLength() default -1;

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEInteger.class)
	public @interface ConfigInteger {
		String category();

		String name() default "";

		int minValue() default Integer.MIN_VALUE;

		int maxValue() default Integer.MAX_VALUE;

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEIntegerArray.class)
	public @interface ConfigIntegerArray {
		String category();

		String name() default "";

		int minValue() default Integer.MIN_VALUE;

		int maxValue() default Integer.MAX_VALUE;

		int maxListLength() default -1;

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEFloat.class)
	public @interface ConfigFloat {
		String category();

		String name() default "";

		float minValue() default Float.MIN_VALUE;

		float maxValue() default Float.MAX_VALUE;

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEDouble.class)
	public @interface ConfigDouble {
		String category();

		String name() default "";

		double minValue() default Double.MIN_VALUE;

		double maxValue() default Double.MAX_VALUE;

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEDoubleArray.class)
	public @interface ConfigDoubleArray {
		String category();

		String name() default "";

		double minValue() default Double.MIN_VALUE;

		double maxValue() default Double.MAX_VALUE;

		int maxListLength() default -1;

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEString.class)
	public @interface ConfigString {
		String category();

		String name() default "";

		String[] validValues() default {};

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEStringArray.class)
	public @interface ConfigStringArray {
		String category();

		String name() default "";

		String[] comment() default {};
	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@ConfigAnnotation(CEEnum.class)
	public @interface ConfigEnum {
		String category();

		String name() default "";

		String[] comment() default {};
	}
}
