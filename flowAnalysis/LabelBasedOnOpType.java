package flowAnalysis;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.LabelledNode;

public class LabelBasedOnOpType implements LabellingStrategy{

	@Override
	public LabelledNode labelNode(ETLFlowOperation o, ETLFlowGraph efg) {
		return new LabelledNode(o.getNodeID(),o.getOperationType().getOpTypeName().name());
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "opType";
	}

}
