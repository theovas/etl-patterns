package flowAnalysis;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.LabelledNode;

public class LabelBasedOnIONarity implements LabellingStrategy {

	@Override
	public LabelledNode labelNode(ETLFlowOperation o, ETLFlowGraph efg) {
		return new LabelledNode(o.getNodeID(), OperationClassification
				.inAndOutFlowNarityGrouping(o).getName());
	}

	@Override
	public String getLabel() {
		return "IOnarity";
	}

}
