package utils;

import structPatterns.TestFlow;
import etlFlowGraph.graph.ETLFlowGraph;

public interface MyVisitor {
	public void visit(ETLFlowGraph efg, TestFlow tf);
}
