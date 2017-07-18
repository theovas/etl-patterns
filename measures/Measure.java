package measures;

import java.math.BigDecimal;

import etlFlowGraph.graph.ETLFlowGraph;
import measureDictionary.InfluenceKind;
import measureDictionary.QualityCharacteristicName;

public interface Measure {

	/**
	 * Returns the name of the quality characteristic that this measure is
	 * intended to estimate. Note that each measure is intended to measure only
	 * one characteristic! If one measure can be used to measure multiple
	 * characteristics, then one different measure should be defined for each of
	 * those characteristics, even though some of their methods might be similar
	 * or identical.
	 * 
	 * @return
	 */
	public QualityCharacteristicName getQCharacteristicName();
	
	/**
	 * Returns the name of the measure.
	 * 
	 * @return
	 */
	
	public String getName();
	/**
	 * Returns a concise textual description of the measure.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Returns the kind of influence that this measure has to the examined
	 * quality characteristic, i.e., positive, negative e.t.c.
	 * 
	 * @return
	 */
	public InfluenceKind getInfluenceToCharacteristic();

	/**
	 * Returns true if the measure requires simulation for its estimation.
	 * 
	 * @return
	 */
	public boolean requiresSimulation();

	/**
	 * Returns a numerical estimation of this measure for the ETL Flow graph
	 * that is passed as an argument.
	 * 
	 * @param efg
	 * @return
	 */
	public double estimateValue(ETLFlowGraph efg);

}
