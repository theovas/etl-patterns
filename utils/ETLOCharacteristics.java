package utils;

import java.util.HashMap;

import operationDictionary.ETLOperationType;

/**
 * @author vasileios
 * 
 *         An object to hold characteristics per etl operation type, e.g., what
 *         are the frequencies of other operation types succeeding this, number
 *         of outgoing edges (fanout) etc.
 *
 */
public class ETLOCharacteristics {

	private int numberOfOccurrences = 0;
	private int avgFanout = 0;
	private int numberOfNextHops = 0;
	/**
	 * for every etl operation type how many times it appears
	 */
	private HashMap<ETLOperationType, Integer> occurrencesOneAfter = new HashMap<ETLOperationType, Integer>();
	private HashMap<ETLOperationType, Integer> occurrencesTwoAfter = new HashMap<ETLOperationType, Integer>();

	// ...

	/**
	 * @return the occurrencesOneAfter
	 */
	public HashMap<ETLOperationType, Integer> getOccurrencesOneAfter() {
		return occurrencesOneAfter;
	}

	/**
	 * @return the numberOfOccurrences
	 */
	public int getNumberOfOccurrences() {
		return numberOfOccurrences;
	}

	/**
	 * @param numberOfOccurrences
	 *            the numberOfOccurrences to set
	 */
	public void setNumberOfOccurrences(int numberOfOccurrences) {
		this.numberOfOccurrences = numberOfOccurrences;
	}

	/**
	 * @return the avgFanout
	 */
	public int getAvgFanout() {
		return avgFanout;
	}

	/**
	 * @param avgFanout
	 *            the avgFanout to set
	 */
	public void setAvgFanout(int avgFanout) {
		this.avgFanout = avgFanout;
	}

	/**
	 * @param occurrencesOneAfter
	 *            the occurrencesOneAfter to set
	 */
	public void setOccurrencesOneAfter(
			HashMap<ETLOperationType, Integer> occurrencesOneAfter) {
		this.occurrencesOneAfter = occurrencesOneAfter;
	}

	/**
	 * @return the occurrencesTwoAfter
	 */
	public HashMap<ETLOperationType, Integer> getOccurrencesTwoAfter() {
		return occurrencesTwoAfter;
	}

	/**
	 * @param occurrencesTwoAfter
	 *            the occurrencesTwoAfter to set
	 */
	public void setOccurrencesTwoAfter(
			HashMap<ETLOperationType, Integer> occurrencesTwoAfter) {
		this.occurrencesTwoAfter = occurrencesTwoAfter;
	}

	/**
	 * @return the numberOfNextHops
	 */
	public int getNumberOfNextHops() {
		return numberOfNextHops;
	}

	/**
	 * @param numberOfNextHops
	 *            the numberOfNextHops to set
	 */
	public void setNumberOfNextHops(int numberOfNextHops) {
		this.numberOfNextHops = numberOfNextHops;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ETLOCharacteristics [numberOfOccurrences="
				+ numberOfOccurrences + ", numberOfNextHops="
				+ numberOfNextHops + ", occurrencesOneAfter="
				+ occurrencesOneAfterToString() + ", occurrencesTwoAfter="
				+ occurrencesTwoAfterToString() + "]";
	}

	private String occurrencesOneAfterToString() {
		String str = "";
		for (ETLOperationType name : occurrencesOneAfter.keySet()) {
			String value = occurrencesOneAfter.get(name).toString();
			str += "[" + name + ": " + value + "}";

		}
		return str;
	}

	private String occurrencesTwoAfterToString() {
		String str = "";
		for (ETLOperationType name : occurrencesTwoAfter.keySet()) {
			String value = occurrencesTwoAfter.get(name).toString();
			str += "[" + name + ": " + value + "}";

		}
		return str;
	}

}
