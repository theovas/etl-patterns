package structPatterns;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.HashBag;

public class PatternNode {

	/**
	 * The struct pattern object that this pattern node belongs to
	 */
	private StructPattern structPattern;
	private String label;
	private static int nodeIDCnt = 0;

	public static int getNodeIDCnt() {
		return ++nodeIDCnt;
	}

	private int nodeID = -1;
	private int parentPatternID = -1;

	private Bag neighborLabels;

	public PatternNode(String label) {
		this.label = label;
		this.nodeID = getNodeIDCnt();
	}

	public PatternNode(String label, int nodeId) {
		this.label = label;
		this.nodeID = nodeId;
	}

	public PatternNode() {
		this.nodeID = getNodeIDCnt();
	}

	/**
	 * @param parentFlowID
	 *            the parentFlowID to set
	 */
	public void setParentPatternID(int parentPatternID) {
		this.parentPatternID = parentPatternID;
	}

	/**
	 * @return the parentPatternID
	 */
	public int getParentPatternID() {
		return parentPatternID;
	}

	/**
	 * @return the nodeID
	 */
	public int getNodeID() {
		return nodeID;
	}

	/**
	 * @param nodeID
	 *            the nodeID to set
	 */
	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the structPattern
	 */
	public StructPattern getStructPattern() {
		return structPattern;
	}

	/**
	 * @param structPattern
	 *            the structPattern to set
	 */
	public void setStructPattern(StructPattern structPattern) {
		this.structPattern = structPattern;
	}

	public Bag getNeighborLabels() {
		// System.out.println("getting neighbor labels...");
		if (neighborLabels == null) {
			neighborLabels = new HashBag();
			// get all edges touching this node
			Set<PatternEdge> touchingEdges = this.structPattern
					.edgesOf(this.nodeID);

			for (PatternEdge e : touchingEdges) {
				// System.out.println(e);
				if ((Integer) e.getSource() == this.nodeID) {
					// System.out.println("getting label for target neighbor: "
					// + e.getTarget()
					// + " "
					// + this.structPattern.getPattNodes()
					// .get(e.getTarget()).getLabel());
					neighborLabels.add(this.structPattern.getPattNodes()
							.get(e.getTarget()).getLabel());
				} else {
					// System.out.println("getting label for source neighbor: "
					// + e.getSource()
					// + " "
					// + this.structPattern.getPattNodes()
					// .get(e.getSource()).getLabel());
					neighborLabels.add(this.structPattern.getPattNodes()
							.get(e.getSource()).getLabel());
				}
			}
		}
		return neighborLabels;

	}

	public Set<PatternNode> getNeighborNodes() {
		Set<PatternNode> neighborNodes = new HashSet<PatternNode>();
		// get all edges touching this node
		Set<PatternEdge> touchingEdges = this.structPattern
				.edgesOf(this.nodeID);

		for (PatternEdge e : touchingEdges) {
			// System.out.println(e);
			if ((Integer) e.getSource() == this.nodeID) {
				neighborNodes.add(this.structPattern.getPattNodes().get(
						e.getTarget()));
			} else {
				neighborNodes.add(this.structPattern.getPattNodes().get(
						e.getSource()));
			}
		}
		return neighborNodes;

	}

	public Set<Integer> getNeighborNodeIds() {
		Set<Integer> neighborNodes = new HashSet<Integer>();
		// get all edges touching this node
		Set<PatternEdge> touchingEdges = this.structPattern
				.edgesOf(this.nodeID);

		for (PatternEdge e : touchingEdges) {
			// System.out.println(e);
			if ((Integer) e.getSource() == this.nodeID) {
				neighborNodes.add(this.structPattern.getPattNodes().get(
						e.getTarget()).nodeID);
			} else {
				neighborNodes.add(this.structPattern.getPattNodes().get(
						e.getSource()).nodeID);
			}
		}
		return neighborNodes;

	}

	/**
	 * checks if for every label of the bag of strings labels, this node is
	 * connected with at least one node with that label. For example, if there
	 * are two labels L1 and three labels L2 inside labels, there must be at
	 * least two neighboring nodes of this node with label L1 and at least three
	 * with label L2.
	 * 
	 * @param labels
	 * @return
	 */
	public boolean checkIfConnectedToNodesWithLabels(Bag labels) {
		// return true if labels is a subset of els
		// System.out.println("checking if connected with labels" + labels
		// + ". NeighborLabels of node " + this.nodeID + ": "
		// + this.getNeighborLabels());
		return this.getNeighborLabels().containsAll(labels);

	}

}
