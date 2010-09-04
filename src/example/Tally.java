package example;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The number of objects of a given type.
 * 
 * This keeps a tally of the number of objects of a given type. Missing objects
 * have a count of zero.
 * 
 * @author W.P. McNeill
 */
/**
 * @author bill
 * 
 * @param <K>
 */
public class Tally<K extends Comparable<K>> implements Serializable {

	private static final long serialVersionUID = -5914227115281257970L;
	private Map<K, Long> map;

	/**
	 * The natural order of tally map entries.
	 * 
	 * Sort by values in descending order then keys in ascending order.
	 */
	private final class TallyComparator implements
			Comparator<Map.Entry<K, Long>> {
		@Override
		public int compare(Map.Entry<K, Long> e1, Map.Entry<K, Long> e2) {
			int c = e2.getValue().compareTo(e1.getValue());
			return c != 0 ? c : e1.getKey().compareTo(e2.getKey());
		}
	}

	public Tally() {
		map = new HashMap<K, Long>();
	}

	/**
	 * Print the map entries in order.
	 * 
	 * @see java.lang.Object#toString()
	 * @see TallyComparator
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		List<Map.Entry<K, Long>> entries = new LinkedList<Map.Entry<K, Long>>(
				map.entrySet());
		Collections.sort(entries, new TallyComparator());
		Iterator<Entry<K, Long>> i = entries.iterator();
		while (i.hasNext()) {
			Entry<K, Long> entry = i.next();
			s.append(entry.getKey() + "\t" + entry.getValue());
			if (i.hasNext())
				s.append("\n");
		}
		return s.toString();
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
	 * The sum of the counts for all the keys.
	 * 
	 * @return the total number of counts in this tally
	 */
	public Long getTotalCount() {
		long total = 0;
		for (K key : map.keySet())
			total += map.get(key);
		return total;
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
	 * This is the first object in the natural order of the tally's map entries.
	 * 
	 * @return object with the largest count
	 * @see TallyComparator
	 */
	public K getLargestKey() {
		if (map.isEmpty())
			return null;
		List<Map.Entry<K, Long>> entries = new LinkedList<Map.Entry<K, Long>>(
				map.entrySet());
		Collections.sort(entries, new TallyComparator());
		return entries.get(0).getKey();
	}
}
