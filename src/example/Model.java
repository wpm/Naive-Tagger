package example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A mapping of types to categories.
 * 
 * T is the type class. C is the category class.
 * 
 * @author W.P. McNeill
 */
public class Model<T extends Comparable<T>, C extends Comparable<C>> implements Serializable {
	private static final long serialVersionUID = 6153442941071347565L;

	/**
	 * A map of types to category predictions.
	 */
	private Map<T, C> category;
	/**
	 * The category predicted by types not in the category map.
	 */
	private C defaultCategory;

	/**
	 * Construct a model from a table of category counts.
	 * 
	 * For a given type, this model predicts the most common category in the
	 * counts. For unknown types, this model predicts the most common category
	 * across all types.
	 * 
	 * @param categoryCounts
	 */
	public Model(CategoryCounts<T, C> categoryCounts) {
		defaultCategory = categoryCounts.getMostCommonCategory();
		category = new HashMap<T, C>();
		for (Entry<T, Tally<C>> entry : categoryCounts.entrySet())
			category.put(entry.getKey(), entry.getValue().getLargestKey());
	}

	/**
	 * The model's type assignment to the token.
	 * 
	 * This returns the default type if the model has no entry for this token.
	 * 
	 * @param type
	 *            a type to tag, e.g. "bank"
	 * @return the category of the type, e.g. "NN"
	 */
	public C getCategory(T type) {
		return category.containsKey(type) ? category.get(type)
				: defaultCategory;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		for (Entry<T, C> entry : category.entrySet()) {
			s = s.concat(entry.getKey() + "\t\t" + entry.getValue() + "\n");
		}
		return s.concat("Default category: " + defaultCategory);
	}

	/**
	 * Print a serialized model file.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		File modelFile = new File(args[0]);
		ObjectInputStream in;
		in = new ObjectInputStream(new FileInputStream(modelFile));
		@SuppressWarnings("unchecked")
		Model<String, String> model = (Model<String, String>) in.readObject();
		System.out.println(model.toString());
	}
}
