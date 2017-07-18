package flowAnalysis;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.LabelledNode;

public class LabelBasedOnSchemaDiff implements LabellingStrategy {

	@Override
	public LabelledNode labelNode(ETLFlowOperation o, ETLFlowGraph efg) {
		return new LabelledNode(o.getNodeID(),
				OperationClassification.schemaDiffGrouping(o));
	}

	@Override
	public String getLabel() {
		return "schemaDiff";
	}

}