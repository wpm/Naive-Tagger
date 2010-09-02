package mcneill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import gate.Annotation;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;

/**
 * A GATE language analyzer that assigns part-of-speech categories to tokens
 * given a tagging model.
 * 
 * @author W.P. McNeill
 */
@CreoleResource(name = "Tagging Analyzer", comment = "Assigns POS tags to categories")
public class TaggerAnalyzer extends TaggingMachineLearner {
	private static final long serialVersionUID = -8136185449506503026L;

	// Creole parameter
	private String modelPath;

	/**
	 * Load the tagging model from a path in a runtime parameter.
	 * 
	 * @return the tagging model
	 * @throws ExecutionException
	 */
	@SuppressWarnings("unchecked")
	private Model<String, String> loadModel() throws ExecutionException {
		Model<String, String> model;

		if (null == modelPath || "".equals(modelPath))
			throw new ExecutionException("Model path not set.");
		File modelFile = new File(modelPath);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new FileInputStream(modelFile));
		} catch (FileNotFoundException e) {
			throw new ExecutionException(modelPath + " does not exist.");
		} catch (IOException e) {
			throw new ExecutionException("Cannot open " + modelPath + ".");
		}
		try {
			model = (Model<String, String>) in.readObject();
		} catch (IOException e) {
			throw new ExecutionException("Cannot read from " + modelPath + ".");
		} catch (ClassNotFoundException e) {
			throw new ExecutionException("Cannot deserialize " + modelPath
					+ ".");
		}
		return model;
	}

	/**
	 * Iterate over tokens in a document adding a category feature.
	 * 
	 * @see gate.creole.AbstractProcessingResource#execute()
	 */
	@Override
	public void execute() throws ExecutionException {
		super.execute();
		Model<String, String> model = loadModel();
		for (Annotation tokenAnnotation : getTokenAnnotations()) {
			FeatureMap annotationFeatures = tokenAnnotation.getFeatures();
			String type = getType(annotationFeatures);
			String category = model.getCategory(type);
			annotationFeatures.put(categoryFeatureName, category);
		}
	}

	/**
	 * @return the modelPath
	 */
	public String getModelPath() {
		return modelPath;
	}

	/**
	 * @param modelPath
	 *            the modelPath to set
	 */
	@CreoleParameter(comment = "Path to the tagger model file.")
	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
}
