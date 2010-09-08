/**
 * 
 */
package example;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import gate.Document;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.AbstractVisualResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.GuiType;
import gate.event.FeatureMapListener;

/**
 * @author W.P. McNeill
 * 
 */
@CreoleResource(name = "Statistics Viewer", comment = "Shows document statistics", resourceDisplayed = "gate.Document", guiType = GuiType.LARGE, mainViewer = true)
public class CountsViewer extends AbstractVisualResource implements
		FeatureMapListener {

	private static final long serialVersionUID = -7505075313672997840L;
	private JTextPane textPane;
	private FeatureMap targetFeatures;

	@Override
	public Resource init() throws ResourceInstantiationException {
		textPane = new JTextPane();
		add(new JScrollPane(textPane));
		return super.init();
	}

	@Override
	public void setTarget(Object target) {
		if (null != targetFeatures)
			targetFeatures.removeFeatureMapListener(this);
		targetFeatures = ((Document) target).getFeatures();
		targetFeatures.addFeatureMapListener(this);
		featureMapUpdated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gate.event.FeatureMapListener#featureMapUpdated()
	 */
	public void featureMapUpdated() {
		textPane.setText(targetFeatures.get(TaggerTrainer.CATEGORY_COUNTS)
				.toString());
	}

}
