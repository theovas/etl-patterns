package patternGeneration;

import java.util.ArrayList;

import qPatterns.QPattern;
import structures.EFGPatternApplicationFlyweight;
import etlFlowGraph.graph.ETLFlowGraph;

public class WorkflowPatternsAdditionDeployment implements PatternDPolicy{

	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ArrayList<QPattern> patterns) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ETLFlowGraph> getDeploymentWalkthrough(ETLFlowGraph efg,
			ArrayList<QPattern> patterns, int numberOfSteps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<EFGPatternApplicationFlyweight> getDeploymentWalkthroughLite(
			ETLFlowGraph efg, ArrayList<QPattern> patterns) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
