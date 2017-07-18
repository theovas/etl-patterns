package flowAnalysis;

import java.util.ArrayList;

import qPatterns.QPattern;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.LabelledNode;

public interface LabellingStrategy {

	public LabelledNode labelNode(ETLFlowOperation o, ETLFlowGraph efg);
	public String getLabel();
	
}
