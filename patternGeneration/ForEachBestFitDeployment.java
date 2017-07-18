/**
 * 
 */
package patternGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import qPatterns.QPattern;
import structures.ApplPoint;
import structures.EFGPatternApplicationFlyweight;
import utilities.ObjectPair;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * @author vasileios
 * 
 */
public class ForEachBestFitDeployment implements PatternDPolicy {

	/*
	 * 
	 * Deploys each of the specified patterns once on the etl flow graph, at the
	 * position where they fit the best. This is just a showcase method and it
	 * is not claiming to provide any optimal solutions, for example different
	 * order of the patterns may produce different result
	 */
	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ArrayList<QPattern> patterns) {

		PatternLocator pLoc = new PatternLocator();
		ETLFlowGraph previousEFG = efg;
		ETLFlowGraph nextEFG = efg;
		for (QPattern qp : patterns) {
			ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> aplPoints = pLoc
					.locatePotentialApplPoints(previousEFG, qp);
			if ((!aplPoints.isEmpty()) && (aplPoints != null)) {
				// sort application points according to fitness values
				Collections
						.sort(aplPoints,
								new Comparator<ObjectPair<ApplPoint, ObjectPair<Integer, String>>>() {
									public int compare(
											ObjectPair<ApplPoint, ObjectPair<Integer, String>> o1,
											ObjectPair<ApplPoint, ObjectPair<Integer, String>> o2) {
										return o1.right.left
												.compareTo(o2.right.left);
									}
								});
				ApplPoint apoint = aplPoints.get(0).left;
				apoint.setEfGraph(previousEFG);
				//nextEFG = qp.deploy(previousEFG, apoint);
				nextEFG = qp.deploy(apoint);
				previousEFG = nextEFG;
			}
		}
		return nextEFG;
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
