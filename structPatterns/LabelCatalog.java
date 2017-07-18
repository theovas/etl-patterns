package structPatterns;

import java.util.HashSet;
import java.util.Set;

public class LabelCatalog {
	private String label;
	private int nodesCount = 0;

	public LabelCatalog(String label) {
		super();
		this.label = label;
	}

	/**
	 * a set to contain the nodes (node ids) of the graph with specific label
	 */
	private Set<Integer> nodes = new HashSet<Integer>();

	public void addNode(PatternNode pn) {
		nodes.add(pn.getNodeID());
		nodesCount++;

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
	 * @return the nodesCount
	 */
	public int getNodesCount() {
		return nodesCount;
	}

	/**
	 * @param nodesCount
	 *            the nodesCount to set
	 */
	public void setNodesCount(int nodesCount) {
		this.nodesCount = nodesCount;
	}

	/**
	 * @return the nodes
	 */
	public Set<Integer> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Set<Integer> nodes) {
		this.nodes = nodes;
	}

}
