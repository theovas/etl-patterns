package structPatterns;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;

public class CandidatePatternMatch extends StructPattern {

	private StructPattern patternModel;

	/**
	 * a utility collection to store current frontier integers, corresponding to
	 * node ids, in case of optimization or other problem searches.
	 */
	private Hashtable<String, HashSet<Integer>> workingSetIndexedByLabel = new Hashtable<String, HashSet<Integer>>();

	/**
	 * a set to keep track of the nodes from the pattern model that have been
	 * already binded to some node from this candidate pattern match
	 */
	private Set<Integer> bindedNodesFromModel = new HashSet<Integer>();

	/**
	 * @return the workingSetIndexedByLabel
	 */
	public Hashtable<String, HashSet<Integer>> getWorkingSetIndexedByLabel() {
		return workingSetIndexedByLabel;
	}

	/**
	 * @param workingSetIndexedByLabel
	 *            the workingSetIndexedByLabel to set
	 */
	public void setWorkingSetIndexedByLabel(
			Hashtable<String, HashSet<Integer>> workingSetIndexedByLabel) {
		this.workingSetIndexedByLabel = workingSetIndexedByLabel;
	}

	/**
	 * @return the bindedNodesFromModel
	 */
	public Set<Integer> getBindedNodesFromModel() {
		return bindedNodesFromModel;
	}

	/**
	 * @param bindedNodesFromModel
	 *            the bindedNodesFromModel to set
	 */
	public void setBindedNodesFromModel(Set<Integer> bindedNodesFromModel) {
		this.bindedNodesFromModel = bindedNodesFromModel;
	}

	public void addAllToWorkingSetIndexedByLabel(Set<PatternNode> neighborNodes) {
		for (PatternNode pn : neighborNodes) {
			String label = pn.getLabel();
			if (this.workingSetIndexedByLabel.get(label) == null) {
				HashSet<Integer> set = new HashSet<Integer>();
				set.add(pn.getNodeID());
				this.workingSetIndexedByLabel.put(label, set);
			} else {
				this.workingSetIndexedByLabel.get(label).add(pn.getNodeID());
			}
		}

	}

	public CandidatePatternMatch deepClone() {
		// System.out.println("cloning this: " + this);
		CandidatePatternMatch cpm = new CandidatePatternMatch();
		// copy workingSetIndexedByLabel
		Hashtable<String, HashSet<Integer>> wsibl = new Hashtable<String, HashSet<Integer>>();
		Set<String> keys = this.workingSetIndexedByLabel.keySet();
		for (String key : keys) {
			HashSet<Integer> hs = new HashSet<Integer>();
			for (Integer i : this.workingSetIndexedByLabel.get(key)) {
				hs.add(new Integer(i));
			}
			wsibl.put(key, hs);
		}
		cpm.setWorkingSetIndexedByLabel(wsibl);
		// copy set with binded nodes from model
		Set<Integer> bn = new HashSet<Integer>();
		for (Integer i : this.bindedNodesFromModel) {
			bn.add(new Integer(i));
		}
		cpm.setBindedNodesFromModel(bn);
		// copy nodes
		// --------------- this can be more efficient if the nodes are not
		// copied deeply but just used in multiple graphs
		Hashtable<Integer, PatternNode> pns = new Hashtable<Integer, PatternNode>();
		Collection<PatternNode> pns2 = this.getPattNodes().values();
		// System.out.println("keys: " + pns2);
		for (PatternNode pn : pns2) {
			// System.out.println("node to be copied: " + pn.getNodeID());
			CandidatePatternMatchNode originalNd = (CandidatePatternMatchNode) pn;
			CandidatePatternMatchNode cpmn = new CandidatePatternMatchNode();
			cpmn.setNodeID(originalNd.getNodeID());
			cpmn.setReferenceGraph(originalNd.getReferenceGraph());
			cpm.addNode(cpmn);
			cpmn.setBindingNodeFromModel((originalNd.getBindingNodeFromModel()));
		}
		// -----------------------------
		// TODO: don't forget to link the nodes!!! or not.. probably not
		// necessary
		// -----------------------------
		// System.out.println("new clone: " + cpm);
		cpm.setPatternModel(this.patternModel);
		return cpm;
	}

	/**
	 * @return the patternModel
	 */
	public StructPattern getPatternModel() {
		return patternModel;
	}

	/**
	 * @param patternModel
	 *            the patternModel to set
	 */
	public void setPatternModel(StructPattern patternModel) {
		this.patternModel = patternModel;
	}

	public StructPattern getDirectionalPModel(ETLFlowGraph efg) {
		StructPattern directSP = new StructPattern();
		CandidatePatternMatchNode cpmn;
		for (PatternNode pn : this.getPattNodes().values()) {
			cpmn = (CandidatePatternMatchNode) pn;
			directSP.addNode(new PatternNode(Integer.toString(cpmn
					.getBindingNodeFromModel()), cpmn.getBindingNodeFromModel()));
			System.out.println("pn: " + cpmn.getBindingNodeFromModel());
		}
		ETLEdge ee;
		Integer targetId;
		Integer sourceId;
		for (PatternNode pn : this.getPattNodes().values()) {
			Set<ETLEdge> touchingEdges = efg.edgesOf(pn.getNodeID());
			for (ETLEdge e : touchingEdges) {
				// System.out.println(e);
				if ((Integer) e.getSource() == pn.getNodeID()) {
					targetId = (Integer) e.getTarget();
					if (this.getPattNodes().get(targetId) != null) {
						System.out.println("adding edge 1: " + pn.getNodeID()
								+ "->" + targetId);
						directSP.addEdge(getModelNdIdFromNdId(pn.getNodeID()),
								getModelNdIdFromNdId(targetId));
					}
				} else {
					sourceId = (Integer) e.getSource();
					if (this.getPattNodes().get(sourceId) != null) {
						System.out.println("adding edge 2: " + sourceId + "->"
								+ pn.getNodeID());
						directSP.addEdge(getModelNdIdFromNdId(sourceId),
								getModelNdIdFromNdId(pn.getNodeID()));
					}
				}
			}
		}
		for (Object e : directSP.edgeSet()) {
			System.out.println("edge:" + ((PatternEdge) e).getSource() + "->"
					+ ((PatternEdge) e).getTarget());
		}
		return directSP;
	}

	public int getModelNdIdFromNdId(int nodeId) {
		return ((CandidatePatternMatchNode) this.getPattNodes().get(nodeId))
				.getBindingNodeFromModel();
	}
}
