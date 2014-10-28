package me.jezza.oc.client.gui.lib;

import org.lwjgl.opengl.GL11;

/**
 * A class representing an RGBA colour and various helper functions. Mainly to improve readability elsewhere.
 *
 * @author MachineMuse
 */
public class Colour {
    public static final Colour WHITE = new Colour(1.0, 1.0, 1.0, 1.0);

    public static final Colour LIGHT_BLUE = new Colour(0.5, 0.5, 1.0, 1.0);
    public static final Colour BLUE = new Colour(0.0, 0.0, 1.0, 1.0);
    public static final Colour DARK_BLUE = new Colour(0.0, 0.0, 0.5, 1.0);

    public static final Colour LIGHT_RED = new Colour(1.0, 0.5, 0.5, 1.0);
    public static final Colour RED = new Colour(1.0, 0.2, 0.2, 1.0);
    public static final Colour DARK_RED = new Colour(0.5, 0.0, 0.0, 1.0);

    public static final Colour LIGHT_GREEN = new Colour(0.5, 1.0, 0.5, 1.0);
    public static final Colour GREEN = new Colour(0.0, 1.0, 0.0, 1.0);

    public static final Colour YELLOW = new Colour(1.0, 1.0, 0.0, 1.0);
    public static final Colour CYAN = new Colour(0.0, 1.0, 1.0, 1.0);
    public static final Colour PINK = new Colour(1.0, 0.0, 1.0, 1.0);

    public static final Colour NEON_BLUE = new Colour(0.4, 0.8, 1.0, 1.0);
    public static final Colour ORANGE = new Colour(0.9, 0.6, 0.2, 1.0);

    public static final Colour PURPLE = new Colour(0.5, 0.0, 1.0, 1.0);

    public static final Colour LIGHT_GREY = new Colour(0.8, 0.8, 0.8, 1.0);
    public static final Colour GREY = new Colour(0.5, 0.5, 0.5, 1.0);
    public static final Colour DARK_GREY = new Colour(0.2, 0.2, 0.2, 1.0);

    public static final Colour BLACK = new Colour(0.0, 0.0, 0.0, 1.0);

    public double r, g, b, a;

    public Colour(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Colour(int colour) {
        this.a = (colour >> 24 & 255) / 255.0F;
        this.r = (colour >> 16 & 255) / 255.0F;
        this.g = (colour >> 8 & 255) / 255.0F;
        this.b = (colour & 255) / 255.0F;
    }

    public int getInt() {
        return getInt(r, g, b, a);
    }

    public void doGLColor4() {
        GL11.glColor4d(r, g, b, a);
    }

    public boolean equals(Colour o) {
        return r == o.r && g == o.g && b == o.b && a == o.a;
    }

    public static int getInt(double r, double g, double b, double a) {
        int temp = 0;
        temp = temp | ((int) (a * 255) << 24);
        temp = temp | ((int) (r * 255) << 16);
        temp = temp | ((int) (g * 255) << 8);
        temp = temp | ((int) (b * 255));
        return temp;
    }

    public static void resetToWhite() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}