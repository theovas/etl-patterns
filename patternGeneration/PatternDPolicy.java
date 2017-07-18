/**
 * 
 */
package patternGeneration;

import java.util.ArrayList;

import qPatterns.QPattern;
import structures.EFGPatternApplicationFlyweight;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * @author vasileios
 *
 */
public interface PatternDPolicy {

	/**
	 * Deploys to the etl flow graph the set of patterns provided as an argument, according to a specific deployment policy. 
	 * 
	 * @param efg
	 * @param patterns
	 * @return
	 */
	public ETLFlowGraph deploy(ETLFlowGraph efg, ArrayList<QPattern> patterns);
	
	// TODO: also create a deployment walkthrough with EFGFlyweight objects, because storing complete EFG objects might be inefficient!
	public ArrayList<ETLFlowGraph> getDeploymentWalkthrough(ETLFlowGraph efg, ArrayList<QPattern> patterns, int numberOfSteps);
	
	
	public ArrayList<EFGPatternApplicationFlyweight> getDeploymentWalkthroughLite(ETLFlowGraph efg, ArrayList<QPattern> patterns);


}
