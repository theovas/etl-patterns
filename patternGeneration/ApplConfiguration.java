package patternGeneration;

import java.util.ArrayList;

import structures.DataSource;
import structures.JoinKeys;

public class ApplConfiguration {
	
	private ArrayList<DataSource> alternativeSources; 
	private JoinKeys joinKeys;
	/**
	 * @return the alternativeSources
	 */
	public ArrayList<DataSource> getAlternativeSources() {
		return alternativeSources;
	}
	/**
	 * @param alternativeSources the alternativeSources to set
	 */
	public void setAlternativeSources(ArrayList<DataSource> alternativeSources) {
		this.alternativeSources = alternativeSources;
	}
	/**
	 * @return the joinKeys
	 */
	public JoinKeys getJoinKeys() {
		return joinKeys;
	}
	/**
	 * @param joinKeys the joinKeys to set
	 */
	public void setJoinKeys(JoinKeys joinKeys) {
		this.joinKeys = joinKeys;
	}
	
	

}
