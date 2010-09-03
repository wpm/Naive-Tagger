package example;

import gate.Corpus;
import gate.DataStore;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Run a GATE application to generate a part-of-speech tagging model.
 * 
 * @author W.P. McNeill
 * 
 */
public class Train {
	static Logger logger = Logger.getLogger(TaggerTrainer.class.getName());

	/**
	 * Train a model from a list of document URLs.
	 * 
	 * @param trainerGappPath
	 *            saved Tagger Trainer GAPP file
	 * @param modelPath
	 *            location to write model file
	 * @param urls
	 *            URLs of documents to train from
	 * @throws GateException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static CategoryCounts<String, String> trainFromURLs(
			String trainerGappPath, String modelPath, ArrayList<String> urls)
			throws GateException, IOException {
		// Create an accumulator for type counts across all the documents.
		CategoryCounts<String, String> categoryCounts = new CategoryCounts<String, String>();
		// Load the category counter plugin.
		SerialAnalyserController categoryCounter = (SerialAnalyserController) PersistenceManager
				.loadObjectFromFile(new File(trainerGappPath));
		// Create a dummy corpus to hold one document at a time.
		Corpus corpus = Factory.newCorpus("Training Data");
		try {
			categoryCounter.setCorpus(corpus);
			// Iterate over documents, counting types in each. Put the documents
			// into the corpus one at a time to keep the memory profile down.
			for (String url : urls) {
				logger.info("Processing " + url);
				Document document = Factory.newDocument(new URL(url));
				try {
					corpus.add(document);
					categoryCounter.execute();
					CategoryCounts<String, String> documentCounts = (CategoryCounts<String, String>) document
							.getFeatures().get(TaggerTrainer.CATEGORY_COUNTS);
					categoryCounts.addCategoryCounts(documentCounts);
				} finally {
					corpus.clear();
					Factory.deleteResource(document);
				}
			}
		} finally {
			Factory.deleteResource(corpus);
		}
		return categoryCounts;
	}

	/**
	 * Train a model from a corpus in a data store.
	 * 
	 * @param trainerGappPath
	 *            saved Tagger Trainer GAPP file
	 * @param modelPath
	 *            location to write model file
	 * @param dataStorePath
	 *            location of the data store
	 * @param corpusName
	 *            the name of a corpus inside the data store
	 * @throws GateException
	 * @throws IOException
	 */
	public static CategoryCounts<String, String> trainFromDataStore(
			String trainerGappPath, String modelPath, String dataStorePath,
			String corpusName) throws GateException, IOException {
		// Load the category counter plugin.
		SerialAnalyserController categoryCounter = (SerialAnalyserController) PersistenceManager
				.loadObjectFromFile(new File(trainerGappPath));
		// Create an accumulator for type counts across all the documents.
		CategoryCounts<String, String> categoryCounts = new CategoryCounts<String, String>();
		// Open the data store.
		DataStore dataStore = Factory.openDataStore(
				"gate.persist.SerialDataStore", new File(dataStorePath).toURI()
						.toString());
		try {
			// Extract lists of corpus names and LRIDs from the data store.
			// These lists have corresponding elements.
			Corpus corpus = Datastore.loadCorpusFromDatastore(dataStore,
					corpusName);
			try {
				// Make category counts over all the documents in the corpus.
				categoryCounter.setCorpus(corpus);
				categoryCounter.execute();
				// Iterate over documents in the corpus collecting counts.
				for (@SuppressWarnings("rawtypes")
				Iterator iterator = corpus.iterator(); iterator.hasNext();) {
					Document document = (Document) iterator.next();
					@SuppressWarnings("unchecked")
					CategoryCounts<String, String> documentCounts = (CategoryCounts<String, String>) document
							.getFeatures().get(TaggerTrainer.CATEGORY_COUNTS);
					categoryCounts.addCategoryCounts(documentCounts);
				}
			} finally {
				Factory.deleteResource(corpus);
			}
		} finally {
			dataStore.close();
		}
		return categoryCounts;
	}

	/**
	 * Generate a serialized model file from a set of training URLs.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws GateException
	 * @throws IOException
	 */
	public static void main(String[] args) throws GateException, IOException {
		BasicConfigurator.configure();
		Gate.init();

		String trainerGappPath = args[0];
		String modelPath = args[1];

		// TODO Get data store vs. URLs difference from a command line switch.
		boolean fromDatastore = true;
		CategoryCounts<String, String> categoryCounts;
		if (fromDatastore) {
			String dataStorePath = args[2];
			String corpusName = args[3];
			categoryCounts = trainFromDataStore(trainerGappPath, modelPath,
					dataStorePath, corpusName);
		} else {
			// Parse the command line.
			if (args.length < 3) {
				System.err.println("Incorrect number of arguments.");
				System.exit(-1);
			}
			ArrayList<String> documentPaths = new ArrayList<String>();
			for (int i = 2; i < args.length; i++)
				documentPaths.add(args[i]);
			// Collect counts from the training documents.
			categoryCounts = trainFromURLs(trainerGappPath, modelPath,
					documentPaths);
		}

		// Create a model from the counts and save it.
		logger.info("Total type counts " + categoryCounts.toString());
		Model<String, String> model = new Model<String, String>(categoryCounts);
		FileOutputStream fos = new FileOutputStream(modelPath);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(model);
		out.close();
	}
}
