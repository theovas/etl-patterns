package graphUtils;

public class LabelledEdge implements Comparable<LabelledEdge>{
	private int srcNodeId;
	private int trgNodeId;
	private String nodeLabel;
	public LabelledEdge(int srcNodeId, int trgNodeId, String nodeLabel) {
		super();
		this.srcNodeId = srcNodeId;
		this.trgNodeId = trgNodeId;
		this.nodeLabel = nodeLabel;
	}
	/**
	 * @return the srcNodeId
	 */
	public int getSrcNodeId() {
		return srcNodeId;
	}
	/**
	 * @param srcNodeId the srcNodeId to set
	 */
	public void setSrcNodeId(int srcNodeId) {
		this.srcNodeId = srcNodeId;
	}
	
	
	/**
	 * @return the trgNodeId
	 */
	public int getTrgNodeId() {
		return trgNodeId;
	}
	/**
	 * @param trgNodeId the trgNodeId to set
	 */
	public void setTrgNodeId(int trgNodeId) {
		this.trgNodeId = trgNodeId;
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
	public int compareTo(LabelledEdge ln2) {
		int srcCmp = Integer.compare(this.getSrcNodeId(),ln2.getSrcNodeId());
        return srcCmp == 0 ? Integer.compare(this.getTrgNodeId(),ln2.getTrgNodeId()) : srcCmp;
	}
	
	
}