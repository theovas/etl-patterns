package qPatterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import patternGeneration.ApplConfiguration;
import structures.ApplPoint;
import structures.StepComponent;
import structures.StepComposite;
import utilities.ObjectPair;
import etlFlowGraph.graph.ETLFlowGraph;

/**
 * 
 * @author vasileios
 * 
 */
public abstract class QPattern implements PatternDeployment {

	private ApplPoint ap;
	private ETLFlowGraph efg;
	// the application point types supported by this quality pattern
	private Set<applPointType> aPTypes = new HashSet<applPointType>();

	private static final String separator = "@@";
	
	private int maxNumberOfOccurencesInSameFlow = 1;

	/**
	 * 
	 * Enumeration to show the type of the application points on which this
	 * pattern can be applied.<br>
	 * <br>
	 * node: application point is a node on the etl flow graph<br>
	 * edge: application point is an edge on the etl flow graph<br>
	 * graph: application point is the whole etl flow graph<br>
	 * 
	 * @author vasileios
	 * 
	 */
	public enum applPointType {
		node, edge, graph;
	}

	/**
	 * A list, each element of which is a possible implementation of the
	 * pattern, in form of a step component. The first element of the list is
	 * the default implementation.
	 * 
	 */
	private ArrayList<StepComponent> implementations = new ArrayList<StepComponent>();
	/**
	 * The prerequisites that have to be fulfilled for the pattern to be
	 * applicable. <br>
	 */
	private ArrayList<TopologyCondition> applicPrereqs = new ArrayList<TopologyCondition>();

	/**
	 * 
	 * This method returns an object pair. The left part is an integer within
	 * (inclusive) range of [0,100], denoting the percentage of appropriateness
	 * of application of this pattern at a specific application point (min is 0
	 * and max is 100). The right part is a String for potential
	 * information/comments about the fitness of the pattern for the specific
	 * point.
	 * 
	 * @param ap
	 * @return
	 */
	public abstract ObjectPair<Integer, String> getFitness(ApplPoint ap);

	/**
	 * This method returns an object pair. The left part is a boolean, denoting
	 * whether or not this pattern is applicable to a specific application
	 * point, which is equivalent to
	 * "all of the conditions specified for this pattern return true for the specified application point"
	 * . The right part is a String for potential information/comments about the
	 * applicability of the pattern for the specific point.
	 * 
	 * @param ap
	 * @return
	 */
	public ObjectPair<Boolean, String> isApplicable(ApplPoint ap) {
		ObjectPair<Boolean, String> op = new ObjectPair<Boolean, String>();
		ObjectPair<Boolean, String> opFin = new ObjectPair<Boolean, String>();
		String comments = "";
		for (TopologyCondition tc : applicPrereqs) {
			op = tc.check(ap);
			if (op.left == null) {
				comments += separator + "One of the conditions returns null";
				opFin.left = false;
				opFin.right = comments;
				return opFin;
			}
			if (op.left == false) {
				comments += op.right;
				comments += separator + "One of the conditions does not apply";
				opFin.left = false;
				opFin.right = comments;
				return opFin;
			} else {
				comments += op.right;
			}

		}
		// if none of the conditions returned null or false
		opFin.left = true;
		opFin.right = comments;
		return opFin;
	}

	public ArrayList<StepComponent> getImplementations() {
		return implementations;
	}

	public void setImplementations(ArrayList<StepComponent> implementation) {
		this.implementations = implementation;
	}

	public void addImplementation(StepComponent implementation) {
		this.implementations.add(implementation);
	}

	public ArrayList<TopologyCondition> getApplicPrereqs() {
		return applicPrereqs;
	}

	public void setApplicPrereqs(ArrayList<TopologyCondition> applicPrereqs) {
		this.applicPrereqs = applicPrereqs;
	}

	public ApplPoint getAp() {
		return ap;
	}

	public void setAp(ApplPoint ap) {
		this.ap = ap;
	}

	public ETLFlowGraph getEfg() {
		return efg;
	}

	public void setEfg(ETLFlowGraph efg) {
		this.efg = efg;
	}

	public Set<applPointType> getAPTypes() {
		return aPTypes;
	}

	public void setAPTypes(Set<applPointType> aPTypes) {
		this.aPTypes = aPTypes;
	}

	public void addAPTypes(applPointType aPType) {
		this.aPTypes.add(aPType);
	}
	// Returns the name of this pattern, which is an element of the QPatternName enum.
	public abstract QPatternName getName();

	/**
	 * @return the maxNumberOfOccurencesInSameFlow
	 */
	public int getMaxNumberOfOccurencesInSameFlow() {
		return maxNumberOfOccurencesInSameFlow;
	}

	/**
	 * @param maxNumberOfOccurencesInSameFlow the maxNumberOfOccurencesInSameFlow to set
	 */
	public void setMaxNumberOfOccurencesInSameFlow(
			int maxNumberOfOccurencesInSameFlow) {
		this.maxNumberOfOccurencesInSameFlow = maxNumberOfOccurencesInSameFlow;
	}
	
	public abstract int getPatternId();
	
	/**
	 * refresh the pattern for subsequent use for the case of multiple uses of the same pattern, e.g., increase indexes
	 * 
	 * @return
	 */
	public abstract QPattern getUpdatedInstance();
	
	public ApplConfiguration getConfiguration(){
		return null;
	}
	
////TODO: create a construct to store configurations, e.g., a list of object pairs of string and string
//	public Configurations getConfigurations();
}
