package example;

import gate.AnnotationSet;
import gate.FeatureMap;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;

/**
 * Base class for the part-of-speech tagging process resources.
 * 
 * This contains functionality that is common to the training and application
 * steps.
 * 
 * @author W.P. McNeill
 */
public abstract class TaggingMachineLearner extends AbstractLanguageAnalyser {
	private static final long serialVersionUID = -1458737905865525273L;
	public static final String TOKEN = "Token";

	// Creole parameters
	protected String annotationSetName;
	protected String typeFeatureName;
	protected String categoryFeatureName;

	/**
	 * Get a set of token annotations from the document.
	 * 
	 * @return token annotations for the document
	 * @throws ExecutionException
	 */
	protected AnnotationSet getTokenAnnotations() throws ExecutionException {
		AnnotationSet tokenAnnotSet = document
				.getAnnotations(annotationSetName);
		if (null == tokenAnnotSet)
			throw new ExecutionException("Annotation set '" + annotationSetName
					+ "' does not exist.");
		return tokenAnnotSet.get(TOKEN);
	}

	/**
	 * Get the token type from its annotation feature map.
	 * 
	 * @param annotationFeatures
	 *            a token's annotation feature map
	 * @return the token's type, e.g. "bank"
	 * @throws ExecutionException
	 */
	protected String getType(FeatureMap annotationFeatures)
			throws ExecutionException {
		String type = (String) annotationFeatures.get(typeFeatureName);
		if (null == type)
			throw new ExecutionException(TOKEN + " has no '" + typeFeatureName
					+ "' value.");
		return type;
	}

	/**
	 * Get the token category from its annotation feature map.
	 * 
	 * @param annotationFeatures
	 *            a token's annotation feature map
	 * @return the token's category, e.g. "NN"
	 * @throws ExecutionException
	 */
	protected String getCategory(FeatureMap annotationFeatures)
			throws ExecutionException {
		String category = (String) annotationFeatures.get(categoryFeatureName);
		if (null == category)
			throw new ExecutionException("Token has no '" + categoryFeatureName
					+ "' value.  Did you run a tagger?");
		return category;
	}

	/**
	 * Verify that a document is specified.
	 * 
	 * @see gate.creole.AbstractProcessingResource#execute()
	 */
	@Override
	public void execute() throws ExecutionException {
		if (null == document)
			throw new ExecutionException("Document not set.");
	}

	/**
	 * @return the annotationSetName
	 */
	public String getAnnotationSetName() {
		return annotationSetName;
	}

	/**
	 * @param annotationSetName
	 *            the annotationSetName to set
	 */
	@Optional
	@RunTime
	@CreoleParameter(comment = "The annotation set containing the tokens to tag.")
	public void setAnnotationSetName(String annotationSetName) {
		this.annotationSetName = annotationSetName;
	}

	/**
	 * @return the typeFeatureName
	 */
	public String getTypeFeatureName() {
		return typeFeatureName;
	}

	/**
	 * @param typeFeatureName
	 *            the typeFeatureName to set
	 */
	@Optional
	@RunTime
	@CreoleParameter(comment = "Token feature from which to read types.", defaultValue = "string")
	public void setTypeFeatureName(String typeFeatureName) {
		this.typeFeatureName = typeFeatureName;
	}

	/**
	 * @return the categoryFeatureName
	 */
	public String getCategoryFeatureName() {
		return categoryFeatureName;
	}

	/**
	 * @param categoryFeatureName
	 *            the categoryFeatureName to set
	 */
	@Optional
	@RunTime
	@CreoleParameter(comment = "Token feature from which to read categories.", defaultValue = "category")
	public void setCategoryFeatureName(String categoryFeatureName) {
		this.categoryFeatureName = categoryFeatureName;
	}
}