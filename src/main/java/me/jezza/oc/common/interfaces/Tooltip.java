package me.jezza.oc.common.interfaces;

public interface Tooltip {
	int TRANSLATE = 0b100_0000_0000_0000_0000_0000_0000_0000;
	int WRAP = 0b10_0000_0000_0000_0000_0000_0000_0000;
	int COLOUR = 0b1_0000_0000_0000_0000_0000_0000_0000;

	int WRAP_MASK = 0b111111111;

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
