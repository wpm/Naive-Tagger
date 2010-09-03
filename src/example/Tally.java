package example;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * The number of objects of a given type.
 * 
 * This keeps a tally of the number of objects of a given type. Missing objects
 * have a count of zero.
 * 
 * @author W.P. McNeill
 */
public class Tally<K extends Comparable<K>> implements Serializable {
	private static final long serialVersionUID = -5914227115281257970L;
	private Map<K, Long> map;

	public Tally() {
		map = new HashMap<K, Long>();
	}

	/**
	 * The count of an object.
	 * 
	 * Missing objects have a count of zero.
	 * 
	 * @param key
	 *            the object
	 * @return the count of key
	 */
	public long getCount(K key) {
		return map.containsKey(key) ? map.get(key) : 0;
	}

	/**
	 * Increment the count of an object
	 * 
	 * @param key
	 *            the object to increment
	 * @param n
	 *            the amount by which to increment
	 * @return the incremented count
	 */
	public long addToCount(K key, long n) {
		if (!map.containsKey(key))
			map.put(key, (long) 0);
		return map.put(key, map.get(key) + n);
	}

	/**
	 * Add the values of another tally to this one.
	 * 
	 * @param tally
	 * @return this object
	 */
	public Tally<K> addTally(Tally<K> tally) {
		for (K key : tally.map.keySet()) {
			addToCount(key, tally.getCount(key));
		}
		return this;
	}

	/**
	 * The object with the largest count.
	 * 
	 * If multiple objects have the same count, the first in the sort order is
	 * returned.  Return null if there are no objects.
	 * 
	 * @return object with the largest count
	 */
	public K getLargestKey() {
		if (map.isEmpty())
			return null;
		// Sort by type count and then by type.
		TreeSet<Map.Entry<K, Long>> s = new TreeSet<Map.Entry<K, Long>>(
				new Comparator<Map.Entry<K, Long>>() {
					@Override
					public int compare(Map.Entry<K, Long> e1,
							Map.Entry<K, Long> e2) {
						// Counts in descending order.
						int c = e2.getValue().compareTo(e1.getValue());
						// Types in ascending order.
						return c != 0 ? c : e1.getKey().compareTo(e2.getKey());
					}
				});
		s.addAll(map.entrySet());
		return s.first().getKey();
	}
}
