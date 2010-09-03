package mcneill;

import gate.Corpus;
import gate.DataStore;
import gate.Factory;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * @author W.P. McNeill
 * 
 */
public class Analyze {
	static Logger logger = Logger.getLogger(TaggerTrainer.class.getName());

	/**
	 * @param args
	 * @throws GateException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws GateException, IOException,
			ClassNotFoundException {
		String analyzerGappPath = args[0];
		String modelPath = args[1];
		String dataStorePath = args[2];
		String corpusName = args[3];

		// Load the tagger GATE application.
		SerialAnalyserController categoryAnalyzer = (SerialAnalyserController) PersistenceManager
				.loadObjectFromFile(new File(analyzerGappPath));
		categoryAnalyzer.setParameterValue("modelPath", modelPath);
		// Open the data store containing the test data.
		DataStore dataStore = Factory.openDataStore(
				"gate.persist.SerialDataStore", new File(dataStorePath).toURI()
						.toString());
		try {
			logger.info("Open corpus " + corpusName + " in " + dataStorePath);
			Corpus corpus = Datastore.loadCorpusFromDatastore(dataStore,
					corpusName);
			try {
				// Tag the corpus.
				categoryAnalyzer.setCorpus(corpus);
				categoryAnalyzer.execute();
			} finally {
				Factory.deleteResource(corpus);
			}
		} finally {
			dataStore.close();
		}

	}
}
