package graphUtils;

public class LabelledNode implements Comparable<LabelledNode>{
	private int nodeId;
	private String nodeLabel;
	public LabelledNode(int nodeId, String nodeLabel) {
		super();
		this.nodeId = nodeId;
		this.nodeLabel = nodeLabel;
	}
	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the nodeLabel
	 */
	public String getNodeLabel() {
		return nodeLabel;
	}
	/**
	 * @param nodeLabel the nodeLabel to set
	 */
	public void setNodeLabel(String nodeLabel) {
		this.nodeLabel = nodeLabel;
	}
	
	@Override
	public int compareTo(LabelledNode ln2) {
		return Integer.compare(this.getNodeId(), ln2.getNodeId());
	}
	
	
}