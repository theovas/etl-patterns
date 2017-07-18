package flowAnalysis;

/**
 * A light version of etl edge, which contains only the source and target ids.
 * 
 * @author btheo
 *
 */
public class EdgeFly {

	private int source;
	private int target;

	public EdgeFly(int source, int target) {
		super();
		this.source = source;
		this.target = target;
	}

	/**
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EdgeFly [source=" + source + ", target=" + target + "]";
	}

	public boolean equals(EdgeFly e2) {
		if ((e2.getSource() == this.source) && (e2.getTarget() == this.target)) {
			return true;
		} else
			return false;
	}

	public EdgeFly copy() {
		// TODO Auto-generated method stub
		return new EdgeFly(this.source,this.target);
	}

}
