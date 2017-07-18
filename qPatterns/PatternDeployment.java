package qPatterns;
import java.util.Iterator;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import structures.ApplPoint;


import importXLM.ImportXLMToETLGraph;
import etlFlowGraph.graph.ETLFlowGraph;


public interface PatternDeployment {

public ETLFlowGraph deploy(ApplPoint applPoint);

}
