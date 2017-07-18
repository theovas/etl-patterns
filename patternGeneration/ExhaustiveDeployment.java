package patternGeneration;

import java.util.ArrayList;
import java.util.Random;

import qPatterns.QPattern;
import qPatterns.QPatternApplication;
import structures.ApplPoint;
import structures.ApplPointId;
import structures.ApplPointKind;
import structures.EFGPatternApplicationFlyweight;
import structures.EdgeApplPoint;
import structures.NodeApplPoint;
import utilities.ObjectPair;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * This deployment policy concerns the case where patterns are deployed in all
 * possible application points, in all possible combinations.
 * 
 * @author btheo
 *
 */
public class ExhaustiveDeployment implements PatternDPolicy {

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * This method simply returns one deployment case which includes all the
	 * patterns deployed on the flow, each of them one
	 * X(maxNumberOfOccurencesInSameFlow) times
	 */
	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ArrayList<QPattern> patterns) {
		if ((patterns != null) && (!patterns.isEmpty())) {
			PatternLocator pLoc = new PatternLocator();
			Random randomGenerator = new Random();
			ETLFlowGraph nextEFG = null;
			QPattern pattern2 = null;
			for (QPattern pattern : patterns) {
				int j = 0;
				int occurences = pattern.getMaxNumberOfOccurencesInSameFlow();
				while (j < occurences) {
					pattern2 = pattern.getUpdatedInstance();
					pattern = pattern2;
					j++;
					ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> aplPoints = pLoc
							.locatePotentialApplPoints(efg, pattern);
					if ((aplPoints != null) && (!aplPoints.isEmpty())) {
						int index = randomGenerator.nextInt(aplPoints.size());
						ApplPoint apoint = aplPoints.get(index).left;
						apoint.setEfGraph(efg);
						nextEFG = pattern.deploy(apoint);
						efg = nextEFG;
					}
				}
			}
		}
		return efg;
	}

	@Override
	public ArrayList<ETLFlowGraph> getDeploymentWalkthrough(ETLFlowGraph efg,
			ArrayList<QPattern> patterns, int numberOfSteps) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns an array of the traces of all the possible combinations of
	 * pattern deployment on the etl flow, each pattern deployed at most
	 * X(maxNumberOfOccurencesInSameFlow) times.
	 * 
	 * IMPORTANT1: The order of the patterns plays a role in the applicability
	 * of the patterns! The first pattern will always be applied first, the second, if it exists, will always be applied second etc...
	 * TODO: Change algorithm to produce ALL of the combinations, regardless of the order of execution. This algorithm can be optimized and prune problem space, e.g., with dynamic programming.
	 * 
	 * IMPORTANT2: In the current implementation one pattern cannot be deployed
	 * "within" itself. TODO: handle cases of dependences
	 */
	@Override
	public ArrayList<EFGPatternApplicationFlyweight> getDeploymentWalkthroughLite(ETLFlowGraph efg, ArrayList<QPattern> patterns) {
	// for memory purposes, i keep the EFGFlyweights at each level and i
	// recreate the ETLFlowGraph every time a pattern is about to be added.
	// Obviously this requires more processing time, but saves on total memory
	// used at every time
		ArrayList<EFGPatternApplicationFlyweight> currentEFGFront = new ArrayList<EFGPatternApplicationFlyweight>();
		PatternLocator pLoc = new PatternLocator();
		ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> aplPoints = null;
	for (QPattern qp:patterns){	
		// this is for the first level, i.e., for the application of the first pattern in the list
		if (currentEFGFront.isEmpty()){
			aplPoints = pLoc.locatePotentialApplPoints(efg, qp);
//			String left;
//			String right;
//			ApplPointKind aPKind;
			for (ObjectPair<ApplPoint, ObjectPair<Integer, String>> apPoint:aplPoints){
				EFGPatternApplicationFlyweight eFly=new  EFGPatternApplicationFlyweight();
				eFly.addqPatternApplication(new QPatternApplication(qp.getName(),apPoint.left.getApplPointId(),qp.getConfiguration()));
				currentEFGFront.add(eFly);
			}
		}
		else{
			ArrayList<EFGPatternApplicationFlyweight> currentEFGFront2 = new ArrayList<EFGPatternApplicationFlyweight>();
			for (EFGPatternApplicationFlyweight efly2:currentEFGFront){
				currentEFGFront2.add(efly2);
				// here i get the actual elt from the etl flyweight
				ETLFlowGraph efgpana = efly2.getEFG(efg);
				aplPoints = pLoc.locatePotentialApplPoints(efgpana, qp);
				for (ObjectPair<ApplPoint, ObjectPair<Integer, String>> apPoint:aplPoints){
					EFGPatternApplicationFlyweight eFly3=new  EFGPatternApplicationFlyweight();
					eFly3.setqPatternApplications(efly2.getqPatternApplicationsCopy());
					eFly3.addqPatternApplication(new QPatternApplication(qp.getName(),apPoint.left.getApplPointId(),qp.getConfiguration()));
					currentEFGFront2.add(eFly3);
					
				}
			}
			currentEFGFront = currentEFGFront2;
		}
	}

		return currentEFGFront;
	}
}
