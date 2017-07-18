/**
 * 
 */
package patternGeneration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import qPatterns.QPattern;
import structures.ApplPoint;
import structures.EFGPatternApplicationFlyweight;
import utilities.ObjectPair;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * @author vasileios
 * 
 */
public class RandomDeployment implements PatternDPolicy {

	private int stepsCount = 1;

	/*
	 * 
	 * Repetitively deploys any of the specified patterns randomly on the etl
	 * flow graph, at a random potential position. This is just a showcase
	 * method and it is not claiming to provide any optimal solutions
	 */
	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ArrayList<QPattern> patterns) {

		if ((patterns != null) && (!patterns.isEmpty())) {
			PatternLocator pLoc = new PatternLocator();
			ETLFlowGraph previousEFG = efg;
			ETLFlowGraph nextEFG = efg;
			int iteration = 0;
			Random randomGenerator = new Random();

			while (iteration < stepsCount) {
				iteration++;
				int index = randomGenerator.nextInt(patterns.size());
				QPattern qp = patterns.get(index);
				// for (QPattern qp : patterns) {
				ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> aplPoints = pLoc
						.locatePotentialApplPoints(previousEFG, qp);
				if ((aplPoints != null) && (!aplPoints.isEmpty())) {
					int index2 = randomGenerator.nextInt(aplPoints.size());

					ApplPoint apoint = aplPoints.get(index2).left;
					apoint.setEfGraph(previousEFG);
					//nextEFG = qp.deploy(previousEFG, apoint);
					nextEFG = qp.deploy(apoint);
					previousEFG = nextEFG;
				}
			}
			return nextEFG;
		} else {
			return efg;
		}
	}

	public int getNumberOfSTeps() {
		return stepsCount;
	}

	public void setNumberOfSTeps(int numberOfSTeps) {
		this.stepsCount = numberOfSTeps;
	}

	/*
	 * 
	 * Repetitively deploys any of the specified patterns randomly on the etl
	 * flow graph, at a random potential position and stores the graph of every
	 * step at an array list which is finally returned. If no pattern id
	 * selected, it returns an array list with only the initial graph. This is
	 * just a showcase method and it is not claiming to provide any optimal
	 * solutions
	 */
	@Override
	public ArrayList<ETLFlowGraph> getDeploymentWalkthrough(ETLFlowGraph efg,
			ArrayList<QPattern> patterns, int numberOfSteps) {
		ArrayList<ETLFlowGraph> graphs = new ArrayList<ETLFlowGraph>();
		// add the initial graph to the returned <<walkthrough>>
		graphs.add(efg);
		if ((!patterns.isEmpty()) && (patterns != null)) {
			PatternLocator pLoc = new PatternLocator();
			ETLFlowGraph previousEFG = efg;
			ETLFlowGraph nextEFG = efg;
			int iteration = 0;
			Random randomGenerator = new Random();

			while (iteration < numberOfSteps) {
				iteration++;
				int index = randomGenerator.nextInt(patterns.size());
				QPattern qp = patterns.get(index);
				// for (QPattern qp : patterns) {
				ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> aplPoints = pLoc
						.locatePotentialApplPoints(previousEFG, qp);
				if ((!aplPoints.isEmpty()) && (aplPoints != null)) {
					int index2 = randomGenerator.nextInt(aplPoints.size());

					ApplPoint apoint = aplPoints.get(index2).left;
					apoint.setEfGraph(previousEFG);
					//nextEFG = qp.deploy(previousEFG, apoint);
					nextEFG = qp.deploy(apoint);
					previousEFG = nextEFG;
				}
				graphs.add(nextEFG);
			}
			return graphs;
		} else {
			// if no patterns are selected, return an array list containing only
			// the initial graph
			return graphs;
		}
	}

	@Override
	public ArrayList<EFGPatternApplicationFlyweight> getDeploymentWalkthroughLite(
			ETLFlowGraph efg, ArrayList<QPattern> patterns) {
		// TODO Auto-generated method stub
		return null;
	}

}
