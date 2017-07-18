package flowAnalysis;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.LabelledNode;

public class LabelBasedOnConsumeProduce implements LabellingStrategy{

	@Override
	public LabelledNode labelNode(ETLFlowOperation o, ETLFlowGraph efg) {
		// TODO Auto-generated method stub
		return new LabelledNode(o.getNodeID(),OperationClassification.consumeProduceGrouping(o));
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "consProd";
	}

}