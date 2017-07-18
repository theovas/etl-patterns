package utils;

import java.util.HashMap;

import operationDictionary.ETLOperationType;

public class GenParams {
	private int startInputNo;
	private HashMap<ETLOperationType, ETLOCharacteristics> charsPerOp;
	private int flowSize;
	/**
	 * a number within [0,100] to show the % probability of generating a new
	 * joining operation when the node to be added is of joining type, instead
	 * of merging with an existing joining operation
	 */
	private int newJoinProbability;

	/**
	 * @return the startInputNo
	 */
	public int getStartInputNo() {
		return startInputNo;
	}

	/**
	 * @param startInputNo
	 *            the startInputNo to set
	 */
	public void setStartInputNo(int startInputNo) {
		this.startInputNo = startInputNo;
	}

	/**
	 * @return the charsPerOp
	 */
	public HashMap<ETLOperationType, ETLOCharacteristics> getCharsPerOp() {
		return charsPerOp;
	}

	/**
	 * @param charsPerOp
	 *            the charsPerOp to set
	 */
	public void setCharsPerOp(
			HashMap<ETLOperationType, ETLOCharacteristics> charsPerOp) {
		this.charsPerOp = charsPerOp;
	}

	/**
	 * @return the flowSize
	 */
	public int getFlowSize() {
		return flowSize;
	}

	/**
	 * @param flowSize
	 *            the flowSize to set
	 */
	public void setFlowSize(int flowSize) {
		this.flowSize = flowSize;
	}

	/**
	 * @return the newJoinProbability
	 */
	public int getNewJoinProbability() {
		return newJoinProbability;
	}

	/**
	 * @param newJoinProbability
	 *            the newJoinProbability to set
	 */
	public void setNewJoinProbability(int newJoinProbability) {
		this.newJoinProbability = newJoinProbability;
	}

}
