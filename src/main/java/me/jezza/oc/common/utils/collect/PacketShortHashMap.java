package me.jezza.oc.common.utils.collect;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectShortHashMap;
import me.jezza.oc.api.exceptions.ChannelException;

import static me.jezza.oc.common.utils.helpers.StringHelper.format;

/**
 * @author Jezza
 */
public class PacketShortHashMap<T> {
	private final TIntObjectHashMap<T> from;
	private final TObjectShortHashMap<T> to;
	private final int max;
	private short nextIndex = 0;

	public PacketShortHashMap() {
		this(1 << 9, 16);
	}

	public PacketShortHashMap(int max) {
		this(max, 16);
	}

	public PacketShortHashMap(int max, int initialCapacity) {
		if (max > Short.MAX_VALUE)
			throw new IllegalStateException("DiscriminatorMap not permitted to be above " + Short.MAX_VALUE);
		if (initialCapacity > max)
			throw new IllegalStateException("Initial capacity is set to be above the max! " + initialCapacity + " > " + max);
		this.max = max;
		// Because you can't write a primitive short by itself.
		short value = -1;
		to = new TObjectShortHashMap<>(initialCapacity, 0.5F, value);
		from = new TIntObjectHashMap<>(initialCapacity, 0.5F, -1);
	}

	public short size() {
		return nextIndex;
	}

	public boolean isEmpty() {
		return nextIndex == 0;
	}

	@SuppressWarnings("unchecked")
	public T get(int key) {
		if (key < 0 || key >= max)
			throw new IndexOutOfBoundsException(format("A discriminator of {} is out of bounds; [{}, {})!", Integer.toString(key), 0, max));
		if (key >= nextIndex)
			throw new IndexOutOfBoundsException(format("No packets have been registered with a discriminator of {}!", Integer.toString(key)));
		T packet = from.get(key);
		if (packet == null)
			throw new ChannelException("No packet registered for discriminator: " + key);
		return packet;
	}

	public short get(T value) {
		short i = to.get(value);
		if (i < 0)
			throw new ChannelException(format("Packet Class not found! {}", value));
		return i;
	}

	public boolean add(T value) {
		from.put(nextIndex, value);
		to.put(value, nextIndex++);
		return false;
	}

	public void clear() {
		from.clear();
		to.clear();
		nextIndex = 0;
	}
}
