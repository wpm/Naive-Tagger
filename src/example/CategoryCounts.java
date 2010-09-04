package example;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Type frequencies for a set of tokens.
 * 
 * This is a table of the form:
 * 
 * {type => {category => count}}
 * 
 * for example:
 * 
 * {"bank" => {"NN" => 3, "VB" => 2}, {"run" => {"NN" => 2, "VB" => 1}}}
 * 
 * T is the type class. C is the category class.
 * 
 * @author W.P. McNeill
 * 
 */
@SuppressWarnings("serial")
public class CategoryCounts<T extends Comparable<T>, C extends Comparable<C>>
		extends HashMap<T, Tally<C>> {
	/**
	 * The natural order of category count entries.
	 * 
	 * Sort by types in descending order by number of total categories and then
	 * in ascending type natural order.
	 */
	private final class CategoryCountsComparator implements
			Comparator<Map.Entry<T, Tally<C>>> {
		public int compare(Map.Entry<T, Tally<C>> e1, Entry<T, Tally<C>> e2) {
			Long c1 = e1.getValue().getTotalCount();
			Long c2 = e2.getValue().getTotalCount();
			int c = c2.compareTo(c1);
			return c != 0 ? c : e1.getKey().compareTo(e2.getKey());
		}
	}

	@Override
	public String toString() {
		List<Map.Entry<T, Tally<C>>> entries = new LinkedList<Map.Entry<T, Tally<C>>>(
				entrySet());
		Collections.sort(entries, new CategoryCountsComparator());
		StringBuilder s = new StringBuilder();
		Iterator<Map.Entry<T, Tally<C>>> i = entries.iterator();
		while (i.hasNext()) {
			Entry<T, Tally<C>> entry = i.next();
			s.append(entry.getKey() + ":\n" + entry.getValue());
			if (i.hasNext())
				s.append("\n");
		}
		return s.toString();
	}

	/**
	 * Increment the type count for a token.
	 * 
	 * @param type
	 *            a type to add, e.g. "bank"
	 * @param category
	 *            the type's category, e.g. "NN"
	 */
	public void addToken(T type, C category) {
		if (!containsKey(type))
			put(type, new Tally<C>());
		get(type).addToCount(category, 1);
	}

	/**
	 * Add the counts from another type counts object into this one.
	 * 
	 * @param categoryCounts
	 *            another type counts object
	 * @return this object
	 */
	public CategoryCounts<T, C> addCategoryCounts(
			CategoryCounts<T, C> categoryCounts) {
		for (Entry<T, Tally<C>> entry : categoryCounts.entrySet()) {
			T type = entry.getKey();
			Tally<C> tally = entry.getValue();
			if (!containsKey(type))
				put(type, new Tally<C>());
			get(type).addTally(tally);
		}
		return this;
	}

	/**
	 * The category that has the largest count across all the types.
	 * 
	 * @return the most common category, e.g. "NN"
	 */
	public C getMostCommonCategory() {
		Tally<C> categoryTotal = new Tally<C>();
		for (Entry<T, Tally<C>> entry : entrySet())
			categoryTotal.addTally(entry.getValue());
		return categoryTotal.getLargestKey();
	}
}
