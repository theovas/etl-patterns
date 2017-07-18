package utils;

import java.util.HashMap;
import java.util.Set;

public class FlowSubpartInfo {

	/**
	 * the **relative** file path of the ETL flow that has been produced
	 */
	private String filePath;
	/**
	 * key is the input file name as an integer and value is a string with the
	 * name of the operator. For example, if one file is "1.txt" then the key
	 * would be 1 and the value would be the name of the ETL operator from the
	 * initial ETL flow of which this file is the log.
	 */
	private HashMap<Integer, String> inputFilesCorrespondence = new HashMap<Integer, String>();
	/**
	 * key is the output file name as an integer and value is a string with the
	 * name of the operator. For example, if one file is "1001.txt" then the key
	 * would be 1001 and the value would be the name of the ETL operator from
	 * the initial ETL flow of which this file is the log. By default, the
	 * numbering starts from 1000.
	 */
	private HashMap<Integer, String> outputFilesCorrespondence = new HashMap<Integer, String>();

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the inputFilesCorrespondence
	 */
	public HashMap<Integer, String> getInputFilesCorrespondence() {
		return inputFilesCorrespondence;
	}

	/**
	 * @param inputFilesCorrespondence
	 *            the inputFilesCorrespondence to set
	 */
	public void setInputFilesCorrespondence(
			HashMap<Integer, String> inputFilesCorrespondence) {
		this.inputFilesCorrespondence = inputFilesCorrespondence;
	}

	/**
	 * @return the outputFilesCorrespondence
	 */
	public HashMap<Integer, String> getOutputFilesCorrespondence() {
		return outputFilesCorrespondence;
	}

	/**
	 * @param outputFilesCorrespondence
	 *            the outputFilesCorrespondence to set
	 */
	public void setOutputFilesCorrespondence(
			HashMap<Integer, String> outputFilesCorrespondence) {
		this.outputFilesCorrespondence = outputFilesCorrespondence;
	}

	public Set<Integer> getInputFilesNames() {
		return this.inputFilesCorrespondence.keySet();
	}

	public Set<Integer> getOutputFilesNames() {
		return this.outputFilesCorrespondence.keySet();
	}

}
