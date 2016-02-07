package me.jezza.oc.common.utils.collect;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Jezza
 */
public class Collections3 {

	private Collections3() {
		throw new IllegalStateException();
	}

	public static <T> List<T> filter(List<T> unfiltered, Predicate<T> predicate) {
		return filter((Collection<T>) unfiltered, new ArrayList<T>(unfiltered.size()), predicate);
	}

	public static <T> List<T> filter(List<T> unfiltered, List<T> populator, Predicate<T> predicate) {
		return filter((Collection<T>) unfiltered, populator, predicate);
	}

	public static <T> Set<T> filter(Set<T> unfiltered, Predicate<T> predicate) {
		return filter((Collection<T>) unfiltered, new HashSet<T>(unfiltered.size()), predicate);
	}

	public static <T> Set<T> filter(Set<T> unfiltered, Set<T> populator, Predicate<T> predicate) {
		return filter((Collection<T>) unfiltered, populator, predicate);
	}

	public static <C extends Collection<T>, T> C filter(Collection<T> unfiltered, C populator, Predicate<T> predicate) {
		for (T t : unfiltered)
			if (predicate.apply(t))
				populator.add(t);
		return populator;
	}

	public static <K, V> Map<K, V> filterByKey(Map<K, V> unfiltered, Predicate<K> predicate) {
		return filterByKey(unfiltered, new HashMap<K, V>(unfiltered.size()), predicate);
	}

	public static <K, V> Map<K, V> filterByKey(Map<K, V> unfiltered, Map<K, V> populator, Predicate<K> predicate) {
		for (Entry<K, V> entry : unfiltered.entrySet()) {
			K key = entry.getKey();
			if (predicate.apply(key))
				populator.put(key, entry.getValue());
		}
		return populator;
	}

	public static <K, V> Map<K, V> filterByValue(Map<K, V> unfiltered, Predicate<V> predicate) {
		return filterByValue(unfiltered, new HashMap<K, V>(unfiltered.size()), predicate);
	}

	public static <K, V> Map<K, V> filterByValue(Map<K, V> unfiltered, Map<K, V> populator, Predicate<V> predicate) {
		for (Entry<K, V> entry : unfiltered.entrySet()) {
			V value = entry.getValue();
			if (predicate.apply(value))
				populator.put(entry.getKey(), value);
		}
		return populator;
	}

	public static <C extends Collection<T>, T> C addAll(C collection, Collection<T> items, Predicate<T> predicate) {
		for (T item : items)
			if (predicate.apply(item))
				collection.add(item);
		return collection;
	}

	public static <T> Collection<T> unmodifiable(Collection<T> collection) {
		if (collection == null || collection.isEmpty())
			return Collections.emptySet();
		if (collection.size() == 1) {
			T first = Iterables.getFirst(collection, null);
			return first != null ? Collections.singleton(first) : Collections.<T>emptySet();
		}
		return Collections.unmodifiableCollection(collection);
	}

	public static <T> Set<T> unmodifiable(Set<T> set) {
		if (set == null || set.isEmpty())
			return Collections.emptySet();
		if (set.size() == 1) {
			T first = Iterables.getFirst(set, null);
			return first != null ? Collections.singleton(first) : Collections.<T>emptySet();
		}
		return Collections.unmodifiableSet(set);
	}

	public static <T> List<T> unmodifiable(List<T> list) {
		if (list == null || list.isEmpty())
			return Collections.emptyList();
		if (list.size() == 1)
			return Collections.singletonList(list.get(0));
		return Collections.unmodifiableList(list);
	}

	public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
		if (map == null || map.isEmpty())
			return Collections.emptyMap();
		if (map.size() == 1) {
			Entry<K, V> first = Iterables.getFirst(map.entrySet(), null);
			return first != null ? Collections.singletonMap(first.getKey(), first.getValue()) : Collections.<K, V>emptyMap();
		}
		return Collections.unmodifiableMap(map);
	}

}
