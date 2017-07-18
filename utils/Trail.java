package utils;

import java.util.HashSet;

import structPatterns.PatternNode;

public class Trail {
	private PatternNode endPoint;
	private HashSet<PatternNode> joinOps = new HashSet<PatternNode>();

	/**
	 * @return the endPoint
	 */
	public PatternNode getEndPoint() {
		return endPoint;
	}

	/**
	 * @param endPoint
	 *            the endPoint to set
	 */
	public void setEndPoint(PatternNode endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * @return the joinOps
	 */
	public HashSet<PatternNode> getJoinOps() {
		return joinOps;
	}

	/**
	 * @param joinOps
	 *            the joinOps to set
	 */
	public void setJoinOps(HashSet<PatternNode> joinOps) {
		this.joinOps = joinOps;
	}

	public void gatherAllJoinsFromOtherTrail(Trail tr2) {
		for (PatternNode pn : tr2.getJoinOps()) {
			this.getJoinOps().add(pn);
		}
	}
}
