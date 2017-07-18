package MeasureUtils;

import java.util.ArrayList;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;

import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * A class that contains methods for performing estimations of basic metrics for
 * graph complexity. These measures refer to the graph representation of the
 * logical model of the ETL flows.
 * 
 * @author vasileios
 *
 */
public final class GraphComplexityMeasures {

	// a private constructor to block instantiation and subclassing
	private GraphComplexityMeasures() {
	}

	public static int activitiesNo(ETLFlowGraph efg) {
		return efg.getEtlFlowOperations().size();
	}
	
	
	public static int activityRelationshipsNo(ETLFlowGraph efg) {
		return efg.edgeSet().size();
	}

	/**
	 * Finds all paths from all source nodes to all target nodes and keeps the
	 * longest
	 * 
	 * @param efg
	 * @return
	 */
	public static int longestPathLength(ETLFlowGraph efg) {
		int longest = -1;
		int distance = -1;
		ArrayList<Integer> sources = efg.getAllSourceNodes();
		ArrayList<Integer> targets = efg.getAllTargetNodes();
		KShortestPaths ksp = null;
		GraphPath<Integer, ETLEdge> path = null;
		ArrayList<GraphPath<Integer, ETLEdge>> paths = null;
		for (int s : sources) {
			ksp = new KShortestPaths(efg, s, 1);
			for (int t : targets) {
				if (Integer.valueOf(t)!=s){
				paths = (ArrayList<GraphPath<Integer, ETLEdge>>) ksp
						.getPaths(Integer.valueOf(t));
				// if there is a path connecting the source to this application
				// point
				if ((paths != null) && (!paths.isEmpty())) {
					path = paths.iterator().next();
					// calculate the number of edges, hence the distance
					distance = path.getEdgeList().size();
					if (distance > longest) {
						longest = distance;
					}
				}
				}

			}

		}
		return longest;
	}

	public static int inputActivitiesNo(ETLFlowGraph efg) {
		// TODO:refine to keep only nodes that are actually input activities and not simply the ones that have no predecessors.
		return efg.getAllSourceNodes().size();
	}

	public static int outputActivitiesNo(ETLFlowGraph efg) {
		// TODO:refine to keep only nodes that are actually output activities and not simply the ones that have no successors.
		return efg.getAllTargetNodes().size();
	}

	/**
	 * Returns one estimation of cyclomatic complexity according to the formula:
	 * cc=(No of nodes−No of edges+No of cycles)
	 * 
	 * @param efg
	 * @return
	 */
	public static int cyclomaticComplexity(ETLFlowGraph efg) {
		// EFG is a DAG, so it should contain no cycles
		return (efg.getEtlFlowOperations().size()-efg.edgeSet().size());
	}

}
