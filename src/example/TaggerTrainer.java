package example;

import gate.Annotation;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleResource;

/**
 * A GATE language analyzer that counts the frequency of part-of-speech
 * categories for token types.
 * 
 * Running this produces a {@link TaggerTrainer} for a document. These objects
 * can then be combined to produce a {@link Model}.
 * 
 * @author W.P. McNeill
 */
@CreoleResource(name = "Tagging Trainer", comment = "Counts the POS categories for each type")
public class TaggerTrainer extends TaggingMachineLearner {
	private static final long serialVersionUID = 1025580530531040398L;
	public static final String CATEGORY_COUNTS = "categoryCounts";

	/**
	 * Counts the part of speech categories for each type.
	 * 
	 * Tokens are read from string features of Token annotations. Types are read
	 * from category features.
	 * 
	 * @see gate.creole.AbstractProcessingResource#execute()
	 */
	@Override
	public void execute() throws ExecutionException {
		super.execute();
		CategoryCounts<String, String> categoryCounts = new CategoryCounts<String, String>();
		for (Annotation tokenAnnotation : getTokenAnnotations()) {
			FeatureMap annotationFeatures = tokenAnnotation.getFeatures();
			String type = getType(annotationFeatures);
			String category = getCategory(annotationFeatures);
			categoryCounts.addToken(type, category);
		}
		document.getFeatures().put(CATEGORY_COUNTS, categoryCounts);
	}
}
