package graphUtils;

public class NodeAndTopoOrder implements Comparable<NodeAndTopoOrder>{

	private int nodeId;
	private int topoOrder;
	
	public NodeAndTopoOrder(int nodeId, int topoOrder) {
		super();
		this.nodeId = nodeId;
		this.topoOrder = topoOrder;
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
	 * @return the topoOrder
	 */
	public int getTopoOrder() {
		return topoOrder;
	}

	/**
	 * @param topoOrder the topoOrder to set
	 */
	public void setTopoOrder(int topoOrder) {
		this.topoOrder = topoOrder;
	}

	@Override
	public int compareTo(NodeAndTopoOrder nato) {
		// TODO Auto-generated method stub
		return Integer.compare(this.getTopoOrder(), nato.getTopoOrder());
	}
	
	
	
}
