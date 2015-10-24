package me.jezza.oc.common.interfaces;

public interface Tooltip {
	int TRANSLATE = 0x1000;
	int WRAP = 0x800;
	int COLOUR = 0x400;
	int WRAP_MASK = 0x3ff;

	Tooltip defaultInfo();

	Tooltip add(CharSequence sequence);
	Tooltip add(Iterable<? extends CharSequence> sequences);

	Tooltip add(CharSequence sequence, int flags);
	Tooltip add(Iterable<? extends CharSequence> sequences, int flags);

	boolean shift();
	boolean shift(int key);

	boolean ctrl();
	boolean ctrl(int key);

	boolean alt();
	boolean alt(int key);

	boolean pressed(int key);
}
