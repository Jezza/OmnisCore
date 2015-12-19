package me.jezza.oc.common.utils.collect;

import com.google.common.base.Predicate;

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
		return (List<T>) filter((Collection<T>) unfiltered, new ArrayList<T>(unfiltered.size()), predicate);
	}

	public static <T> List<T> filter(List<T> unfiltered, List<T> populator, Predicate<T> predicate) {
		return (List<T>) filter((Collection<T>) unfiltered, populator, predicate);
	}

	public static <T> Set<T> filter(Set<T> unfiltered, Predicate<T> predicate) {
		return (Set<T>) filter((Collection<T>) unfiltered, new HashSet<T>(unfiltered.size()), predicate);
	}

	public static <T> Set<T> filter(Set<T> unfiltered, Set<T> populator, Predicate<T> predicate) {
		return (Set<T>) filter((Collection<T>) unfiltered, populator, predicate);
	}

	public static <T> Collection<T> filter(Collection<T> unfiltered, Collection<T> populator, Predicate<T> predicate) {
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

	public static <T> Collection<T> add(Collection<T> collection, T item, Predicate<T> predicate) {
		if (predicate.apply(item))
			collection.add(item);
		return collection;
	}

	public static <T> Collection<T> addAll(Collection<T> collection, Collection<T> items, Predicate<T> predicate) {
		for (T item : items)
			if (predicate.apply(item))
				collection.add(item);
		return collection;
	}
}
