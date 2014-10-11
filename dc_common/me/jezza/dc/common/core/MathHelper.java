package me.jezza.dc.common.core;

public class MathHelper {

    public static int wrapInt(int value, int max) {
        return wrapInt(value, 0, max);
    }

    public static int wrapInt(int value, int min, int max) {
        if (value < min)
            value = max;
        if (value > max)
            value = min;
        return value;
    }

    public static float expandAwayFromZero(float value, float expansion) {
        return expandAwayFrom(value, 0, expansion);
    }

    public static float expandAwayFrom(float value, float awayFrom, float expansion) {
        if (value < awayFrom)
            return value - expansion;
        else if (value > awayFrom)
            return value + expansion;
        return value;
    }

    public static boolean withinValues(float value, float lowerBound, float upperBound) {
        return lowerBound < value && value < upperBound;
    }

    public static boolean withinRange(double value, double target, double tolerance) {
        return (target - tolerance) <= value && value <= (target + tolerance);
    }

    public static boolean withinRange(double value, double tolerance) {
        return withinRange(value, 0.0D, tolerance);
    }

    public static boolean withinRange(float value, float target, float tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static boolean withinRange(int value, int target, int tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static double clipDouble(double value, double min, double max) {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static int clipInt(int value, int max) {
        return clipInt(value, 0, max);
    }

    public static int clipInt(int value, int min, int max) {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static double interpolate(double a, double b, double d) {
        return a + (b - a) * d;
    }

    public static float interpolate(float a, float b, float d) {
        return a + (b - a) * d;
    }
}
