package me.jezza.oc.common.utils.maths;

public class Maths {
	public static int wrap(int value, int min, int max) {
		return value < min ? max : value > max ? min : value;
	}

	public static float wrap(float value, float min, float max) {
		return value < min ? max : value > max ? min : value;
	}

	public static double wrap(double value, double min, double max) {
		return value < min ? max : value > max ? min : value;
	}

	public static int expandAway(int value, int origin, int expansion) {
		return value < origin ? value - expansion : value > origin ? value + expansion : value;
	}

	public static float expandAway(float value, float origin, float expansion) {
		return value < origin ? value - expansion : value > origin ? value + expansion : value;
	}

	public static double expandAway(double value, double origin, double expansion) {
		return value < origin ? value - expansion : value > origin ? value + expansion : value;
	}

	public static boolean withinValues(int value, int lowerBound, int upperBound) {
		return lowerBound < value && value < upperBound;
	}

	public static boolean withinValues(float value, float lowerBound, float upperBound) {
		return lowerBound < value && value < upperBound;
	}

	public static boolean withinValues(double value, double lowerBound, double upperBound) {
		return lowerBound < value && value < upperBound;
	}

	public static boolean withinRange(int value, int target, int tolerance) {
		return target - tolerance <= value && value <= target + tolerance;
	}

	public static boolean withinRange(float value, float target, float tolerance) {
		return target - tolerance <= value && value <= target + tolerance;
	}

	public static boolean withinRange(double value, double target, double tolerance) {
		return target - tolerance <= value && value <= target + tolerance;
	}

	public static int clip(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}

	public static float clip(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}

	public static double clip(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}

	public static int interpolate(int a, int b, int d) {
		return a + (b - a) * d;
	}

	public static double interpolate(double a, double b, double d) {
		return a + (b - a) * d;
	}

	public static float interpolate(float a, float b, float d) {
		return a + (b - a) * d;
	}
}
