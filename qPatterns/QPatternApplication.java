package qPatterns;

import patternGeneration.ApplConfiguration;
import structures.ApplPointId;

public class QPatternApplication {

	private QPatternName patternName;
	private ApplPointId aPId;
	private ApplConfiguration apconf;

	// private Configurations;

	public QPatternApplication(QPatternName patternName, ApplPointId aPId, ApplConfiguration apconf) {
		super();
		this.patternName = patternName;
		this.aPId = aPId;
		this.apconf = apconf;
	}

	/**
	 * @return the patternName
	 */
	public QPatternName getPatternName() {
		return patternName;
	}

	/**
	 * @param patternName
	 *            the patternName to set
	 */
	public void setPatternName(QPatternName patternName) {
		this.patternName = patternName;
	}

	/**
	 * @return the aPId
	 */
	public ApplPointId getaPId() {
		return aPId;
	}

	/**
	 * @param aPId
	 *            the aPId to set
	 */
	public void setaPId(ApplPointId aPId) {
		this.aPId = aPId;
	}

	/**
	 * @return the apconf
	 */
	public ApplConfiguration getApconf() {
		return apconf;
	}

	/**
	 * @param apconf the apconf to set
	 */
	public void setApconf(ApplConfiguration apconf) {
		this.apconf = apconf;
	}

}
