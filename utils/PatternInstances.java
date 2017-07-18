package utils;

import java.util.ArrayList;
import java.util.HashMap;

import structPatterns.CandidatePatternMatch;
import structPatterns.StructPattern;

/**
 * Object to hold, for a specific pattern the pattern instances on the different
 * flows, just as struct patterns with the corresponding ETL flow node ids. For
 * now the struct patterns are not complete and do not contain any edges. They
 * are just used as structures to test if the set of nodes is a subset of the
 * set of nodes in another
 * 
 * @author vasileios
 *
 */
public class PatternInstances {

	public PatternInstances(int pattId) {
		super();
		this.pattId = pattId;
	}

	// the id of the pattern
	private int pattId;
	// for each flow id (key of the hash) there is a corresponding arraylist of
	// struct patterns, each one being a pattern instance on the flow
	private HashMap<Integer, ArrayList<StructPattern>> pattInstancesOnFlows = new HashMap<Integer, ArrayList<StructPattern>>();

	/**
	 * @return the pattId
	 */
	public int getPattId() {
		return pattId;
	}

	/**
	 * @param pattId
	 *            the pattId to set
	 */
	public void setPattId(int pattId) {
		this.pattId = pattId;
	}

	/**
	 * @return the pattInstancesOnFlows
	 */
	public HashMap<Integer, ArrayList<StructPattern>> getPattInstancesOnFlows() {
		return pattInstancesOnFlows;
	}

	/**
	 * @param pattInstancesOnFlows
	 *            the pattInstancesOnFlows to set
	 */
	public void setPattInstancesOnFlows(
			HashMap<Integer, ArrayList<StructPattern>> pattInstancesOnFlows) {
		this.pattInstancesOnFlows = pattInstancesOnFlows;
	}

}
