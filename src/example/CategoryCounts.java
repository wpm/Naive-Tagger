package example;

import java.util.HashMap;
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
 * {"bank" => {"NN" => 4, "VB" => 1}, {"run" => {"NN" => 3}}}
 * 
 * T is the type class. C is the category class.
 * 
 * @author W.P. McNeill
 * 
 */
@SuppressWarnings("serial")
public class CategoryCounts<T, C extends Comparable<C>> extends
		HashMap<T, Tally<C>> {
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

	@Override
	public String toString() {
		return "CategoryCounts [" + keySet().size() + " types]";
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
