package flowAnalysis;

import etlFlowGraph.graph.ETLFlowGraph;

public interface DecompositionPolicy {
	
	public FlowDecomposition decomposeFlow(ETLFlowGraph efg);

}
