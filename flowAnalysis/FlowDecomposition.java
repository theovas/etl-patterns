package flowAnalysis;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLFlowGraph;

public class FlowDecomposition {
	
	private ArrayList<FlowPart> decomposition = new ArrayList<FlowPart>();
	/**
	 * the etl flow graph that is being decomposed through this decomposition
	 */
	private ETLFlowGraph efg;

	/**
	 * @return the decomposition
	 */
	public ArrayList<FlowPart> getDecomposition() {
		return decomposition;
	}

	/**
	 * @param decomposition the decomposition to set
	 */
	public void setDecomposition(ArrayList<FlowPart> decomposition) {
		this.decomposition = decomposition;
	}
	

	/**
	 * @return the efg
	 */
	public ETLFlowGraph getEfg() {
		return efg;
	}

	/**
	 * @param efg the efg to set
	 */
	public void setEfg(ETLFlowGraph efg) {
		this.efg = efg;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FlowDecomposition [decomposition=" + decomposition + "]";
	}
	
	public String toStringDecomposition(){
		String s="";
		for (FlowPart fp:this.decomposition){
			s+="["+fp.toString()+"]";
		}
		return s;
	}

	public boolean addIfNotExists(FlowPart fp){
		for (FlowPart fp2:this.decomposition){
			//System.out.println("fp: "+fp+"fp2: "+fp2);
			if (fp.equalsExactly(fp2)){
				//System.out.println("fps are equal!!!");
				return false;
			}
		}
		this.decomposition.add(fp);
		return true;
	}
}
