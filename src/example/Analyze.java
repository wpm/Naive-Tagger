package example;

import gate.Corpus;
import gate.DataStore;
import gate.Factory;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.BasicConfigurator;
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
		BasicConfigurator.configure();
		Gate.init();

		String analyzerGappPath = args[0];
		String modelPath = args[1];
		String dataStorePath = args[2];
		String corpusName = args[3];

		// Load the tagger GATE application.
		SerialAnalyserController taggerController = (SerialAnalyserController) PersistenceManager
				.loadObjectFromFile(new File(analyzerGappPath));
		// Tell the tagger where the tagging model is.
		@SuppressWarnings("unchecked")
		ProcessingResource tagger = new Vector<ProcessingResource>(
				taggerController.getPRs()).lastElement();
		tagger.setParameterValue("modelPath", modelPath);
		// Open the data store containing the test data.
		DataStore dataStore = Factory.openDataStore(
				"gate.persist.SerialDataStore", new File(dataStorePath).toURI()
						.toString());
		try {
			Corpus corpus = Datastore.loadCorpusFromDatastore(dataStore,
					corpusName);
			try {
				// Tag the test corpus.
				taggerController.setCorpus(corpus);
				taggerController.execute();
			} finally {
				Factory.deleteResource(corpus);
			}
		} finally {
			dataStore.close();
		}
	}
}
