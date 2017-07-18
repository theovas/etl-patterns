package structures;

/**
 * A POJO to represent the identifier of an application point. If the
 * application point is a node, the id will contain the operation name of this
 * node at the left part; if it is an edge, the id will contain the operation
 * name of the source at the left part and of the target at the right part. If
 * it is a graph, the left part of the id will contain the id of the flow.
 * 
 * @author btheo
 *
 */
public class ApplPointId {
	// the name of the operator
	private String leftPart;
	// if it is a node this will be -1
	private String rightPart;
	private ApplPointKind aPKind;

	public ApplPointId(String leftPart, String rightPart, ApplPointKind aPKind) {
		super();
		this.leftPart = leftPart;
		this.rightPart = rightPart;
		this.aPKind = aPKind;
	}

	/**
	 * @return the leftPart
	 */
	public String getLeftPart() {
		return leftPart;
	}

	/**
	 * @param leftPart
	 *            the leftPart to set
	 */
	public void setLeftPart(String leftPart) {
		this.leftPart = leftPart;
	}

	/**
	 * @return the rightPart
	 */
	public String getRightPart() {
		return rightPart;
	}

	/**
	 * @param rightPart
	 *            the rightPart to set
	 */
	public void setRightPart(String rightPart) {
		this.rightPart = rightPart;
	}

	/**
	 * @return the aPKind
	 */
	public ApplPointKind getaPKind() {
		return aPKind;
	}

	/**
	 * @param aPKind
	 *            the aPKind to set
	 */
	public void setaPKind(ApplPointKind aPKind) {
		this.aPKind = aPKind;
	}

}
