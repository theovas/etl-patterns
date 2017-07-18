/**
 * 
 */
package patternGeneration;

import java.util.ArrayList;
import java.util.Set;

import qPatterns.QPattern;
import structures.EFGPatternApplicationFlyweight;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * @author vasileios
 *
 */
public class PatternDeployer {

	// the etl flow graph on which the pattern deployer will deploy patterns
	private ETLFlowGraph efgraph;
	// a set of the quality patterns that are of interest to be deployed to the etl flow graph
	private ArrayList<QPattern> activeQPatterns;
	
	public ETLFlowGraph deployPatterns(PatternDPolicy policy){
		
		return policy.deploy(this.getEfgraph(), this.getActiveQPatterns());
	}
	
	public ArrayList<EFGPatternApplicationFlyweight> getDeploymentWalkthroughLite(PatternDPolicy policy){
		return policy.getDeploymentWalkthroughLite(this.getEfgraph(), this.getActiveQPatterns());
	}
	
	
	public ETLFlowGraph getEfgraph() {
		return efgraph;
	}
	public void setEfgraph(ETLFlowGraph efgraph) {
		this.efgraph = efgraph;
	}
	public ArrayList<QPattern> getActiveQPatterns() {
		return activeQPatterns;
	}
	public void setActiveQPatterns(ArrayList<QPattern> activeQPatterns) {
		this.activeQPatterns = activeQPatterns;
	}
	
	
	
}
