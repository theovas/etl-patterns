package utils;

import java.util.ArrayList;
import java.util.HashMap;

import structPatterns.CandidatePatternMatch;
import structPatterns.IsomorphismTester;
import structPatterns.StructPattern;
import structPatterns.TestFlow;
import etlFlowGraph.graph.ETLFlowGraph;
import flowAnalysis.LabellingStrategy;

public class PatternMatchesVisitor implements MyVisitor {
	private ArrayList<CandidatePatternMatch> matches = new ArrayList<CandidatePatternMatch>();
	private StructPattern sp;
	private LabellingStrategy lbs;
	// hashmap of flow id and hashmap of all pattern matches for a specific
	// pattern. First Integer is the flow id and second is the pattern id.
	private HashMap<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>> allMatchesFromAllFlows = new HashMap<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>>();
	private HashMap<Integer, ETLFlowGraph> efgHash = new HashMap<Integer, ETLFlowGraph>();

	/**
	 * constructor so that every time it is created with a different structural
	 * pattern, the allMatchesFromAllFlows list is initialized
	 * 
	 * @param sp
	 */
	public PatternMatchesVisitor(StructPattern sp) {
		super();
		this.sp = sp;
	}

	public void visit(ETLFlowGraph efg, TestFlow tf) {
		efgHash.put(tf.getFlowId(), efg);
		matches = IsomorphismTester.getAllPatternMatches(sp, efg, lbs);
		// System.out.println("visiting flow: " + efg.toString());
		// System.out.println("number of matches: " + matches.size());
		if (matches.size() > 0) {
			HashMap<Integer, ArrayList<CandidatePatternMatch>> allMatchesOneFlowOnePattern = null;
			if (allMatchesFromAllFlows.get(tf.getFlowId()) != null) {
				allMatchesFromAllFlows.get(tf.getFlowId()).put(sp.getPattId(),
						matches);
				// System.out.println("adding to flow: " + tf.getFlowId()
				// + " pattern: " + sp.getPattId() + " matches");
			} else {
				allMatchesOneFlowOnePattern = new HashMap<Integer, ArrayList<CandidatePatternMatch>>();
				allMatchesOneFlowOnePattern.put(sp.getPattId(), matches);
				allMatchesFromAllFlows.put(tf.getFlowId(),
						allMatchesOneFlowOnePattern);
				// System.out.println("New - adding to flow: " + tf.getFlowId()
				// + " pattern: " + sp.getPattId() + " matches");
			}
		}

	}

	/**
	 * @return the matches
	 */
	public ArrayList<CandidatePatternMatch> getMatches() {
		return matches;
	}

	/**
	 * @return the sp
	 */
	public StructPattern getSp() {
		return sp;
	}

	/**
	 * @param sp
	 *            the sp to set
	 */
	public void setSp(StructPattern sp) {
		this.sp = sp;
	}

	/**
	 * @return the lbs
	 */
	public LabellingStrategy getLbs() {
		return lbs;
	}

	/**
	 * @param lbs
	 *            the lbs to set
	 */
	public void setLbs(LabellingStrategy lbs) {
		this.lbs = lbs;
	}

	/**
	 * @return the allMatchesFromAllFlows
	 */
	public HashMap<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>> getAllMatchesFromAllFlows() {
		return allMatchesFromAllFlows;
	}

	/**
	 * @return the efgHash
	 */
	public HashMap<Integer, ETLFlowGraph> getEfgHash() {
		return efgHash;
	}

}
