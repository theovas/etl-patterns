/**
 * 
 */
package patternGeneration;

import java.util.ArrayList;

import qPatterns.QPattern;
import structures.EFGPatternApplicationFlyweight;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * 
 * not yet implemented. this method will tale as input measures and provide the optimal deployment of patterns.
 * 
 * @author vasileios
 *
 */
public class MeasureBasedDeployment implements PatternDPolicy {

	/* (non-Javadoc)
	 * @see patternGeneration.PatternDPolicy#deploy(etlFlowGraph.graph.ETLFlowGraph, java.util.ArrayList)
	 */
	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ArrayList<QPattern> patterns) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see patternGeneration.PatternDPolicy#getDeploymentWalkthrough(etlFlowGraph.graph.ETLFlowGraph, java.util.ArrayList, int)
	 */
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
