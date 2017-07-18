package utils;

import java.util.ArrayList;
import java.util.HashMap;

import operationDictionary.ETLOperationType;

public class OPTPercentageBounds {
	private ETLOperationType eot;
	// integer is the upper bound of the interval and eot is the corresponding
	// operation. The lower bound is just the upper bound of the previous
	// element or 0 if it is the first element in the arraylist.
	private ArrayList<MyPair<Integer, ETLOperationType>> boundsAndEOTs = new ArrayList<MyPair<Integer, ETLOperationType>>();

	/**
	 * @return the eot
	 */
	public ETLOperationType getEot() {
		return eot;
	}

	/**
	 * @param eot
	 *            the eot to set
	 */
	public void setEot(ETLOperationType eot) {
		this.eot = eot;
	}

	/**
	 * @return the boundsAndEOTs
	 */
	public ArrayList<MyPair<Integer, ETLOperationType>> getBoundsAndEOTs() {
		return boundsAndEOTs;
	}

	/**
	 * @param boundsAndEOTs
	 *            the boundsAndEOTs to set
	 */
	public void setBoundsAndEOTs(
			ArrayList<MyPair<Integer, ETLOperationType>> boundsAndEOTs) {
		this.boundsAndEOTs = boundsAndEOTs;
	}

}
