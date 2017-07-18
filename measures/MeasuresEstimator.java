package measures;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLFlowGraph;
import measureDictionary.MeasureName;
import patternGeneration.FlowFamily;
import structures.EFGPatternApplicationFlyweight;
import utilities.ObjectPair;

public class MeasuresEstimator {
	
	public static int flowId = 0; 

	public static ArrayList<FlowMeasures> produceMeasures(FlowFamily flows, ArrayList<MeasureName> activeMeasures){
		ETLFlowGraph initialEFG = flows.getInitialEfg();
		ArrayList<FlowMeasures> flowMeasures = new ArrayList<FlowMeasures>();
		ETLFlowGraph efg;
		for (EFGPatternApplicationFlyweight ew:flows.getFlows()){
			efg = ew.getEFG(initialEFG);
			FlowMeasures fm = new FlowMeasures();
			flowId++;
			fm.setFlowId(flowId);
			for (MeasureName mn:activeMeasures){
				Measure m = Measures.valueOf(mn.name());
				MeasureAndValue msrVl = new MeasureAndValue(mn,m.estimateValue(efg));
				fm.addMeasureValue(msrVl);

			}
			flowMeasures.add(fm);
		}
		return flowMeasures;
		
	}
	//get the etl lites and active measures and produce all the measures for them.
}
