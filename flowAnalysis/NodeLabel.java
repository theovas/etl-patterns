package flowAnalysis;

import measureDictionary.InfluenceKind;
import measureDictionary.QualityCharacteristicName;
import etlFlowGraph.graph.ETLFlowGraph;

public interface NodeLabel {	
	
	/**
	 * 
	 * @return
	 */
	public ClassificationType getClassificationType();
	
	/**
	 * Returns the name of the label as a string.
	 * 
	 * @return
	 */
	
	public String getName();
	/**
	 * Returns a concise textual description of the label.
	 * 
	 * @return
	 */
	public String getDescription();

}
