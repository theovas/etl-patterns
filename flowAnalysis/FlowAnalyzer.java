package flowAnalysis;

import etlFlowGraph.graph.ETLFlowGraph;

public class FlowAnalyzer {
	
	public static FlowDecomposition decomposeFlow(ETLFlowGraph efg, DecompositionPolicy dp){
		return dp.decomposeFlow(efg);
	}

}
