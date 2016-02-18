package me.jezza.oc.common.utils;

import java.io.PrintStream;
import java.util.Arrays;

import me.jezza.oc.common.utils.reflect.ASM;
import org.apache.logging.log4j.Logger;

/**
 * Simply a more fleshed out version of {@link cpw.mods.fml.common.TracingPrintStream}.
 *
 * @author Jezza
 */
public class TracingPrintStream extends PrintStream {
	private static final int BASE_DEPTH = 4;

	private final Logger logger;

	public TracingPrintStream(final Logger logger, final PrintStream original) {
		super(original);
		this.logger = logger;
	}

	@Override
	public void print(final boolean b) {
		write(getPrefix().concat(Boolean.toString(b)));
	}

	@Override
	public void print(final char c) {
		write(getPrefix().concat(Character.toString(c)));
	}

	@Override
	public void print(final char[] s) {
		write(getPrefix().concat(Arrays.toString(s)));
	}

	@Override
	public void print(final double d) {
		write(getPrefix().concat(Double.toString(d)));
	}

	@Override
	public void print(final float f) {
		write(getPrefix().concat(Float.toString(f)));
	}

	@Override
	public void print(final int i) {
		write(getPrefix().concat(Integer.toString(i)));
	}

	@Override
	public void print(final long l) {
		write(getPrefix().concat(Long.toString(l)));
	}

	@Override
	public void print(final Object obj) {
		write(getPrefix().concat(String.valueOf(obj)));
	}

	@Override
	public void print(final String s) {
		write(getPrefix().concat(s));
	}

	@Override
	public void println(final boolean b) {
		write(getPrefix().concat(Boolean.toString(b)));
	}

	@Override
	public void println(final char c) {
		write(getPrefix().concat(Character.toString(c)));
	}

	@Override
	public void println(final char[] s) {
		write(getPrefix().concat(Arrays.toString(s)));
	}

	@Override
	public void println(final double d) {
		write(getPrefix().concat(Double.toString(d)));
	}

	@Override
	public void println(final float f) {
		write(getPrefix().concat(Float.toString(f)));
	}

	@Override
	public void println(final int i) {
		write(getPrefix().concat(Integer.toString(i)));
	}

	@Override
	public void println(final long l) {
		write(getPrefix().concat(Long.toString(l)));
	}

	@Override
	public void println(final Object obj) {
		write(getPrefix().concat(String.valueOf(obj)));
	}

	@Override
	public void println(final String s) {
		write(getPrefix().concat(s));
	}

	/**
	 * Yes, I am aware that this pushes all the print calls as a newline, but it shouldn't matter.
	 * If people are really concerned about it, they can disable this in OC's config.
	 *
	 * @param data - The string to write to the logger.
	 */
	private void write(final String data) {
		logger.info(data);
	}

	private static String getPrefix() {
		StackTraceElement[] elements = ASM.callingFrames();
		StackTraceElement elem = elements[BASE_DEPTH];
		if (elem.getClassName().startsWith("kotlin.io."))
			elem = elements[BASE_DEPTH + 2]; // Kotlin's io package masks origins 2 deeper in the stack.
		return "[" + elem.getClassName() + '.' + elem.getMethodName() + ':' + elem.getLineNumber() + "]: ";
	}
}


