package flowAnalysis;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLFlowGraph;

/**
 * 
 * This class represents a part of an ETL Flow Graph, i.e., a subset of its nodes and edges. 
 * 
 * @author btheo
 *
 */
public class FlowPart {
	
	
/**
 * A structure to maintain the various coherent parts of the flow that this flow part includes (i.e., parts for which all nodes are traversed by the same path or, if there are multiple paths, each of them is crossed with at least one other path within this part).
 */
private ArrayList<EFGFlyweight> subparts = new ArrayList<EFGFlyweight>();

/**
 * @return the subparts
 */
public ArrayList<EFGFlyweight> getSubparts() {
	return subparts;
}

/**
 * @param subparts the subparts to set
 */
public void setSubparts(ArrayList<EFGFlyweight> subparts) {
	this.subparts = subparts;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "FlowPart [subparts=" + subparts + "]";
}

public String toStringSubparts(){
	String s = "";
	for (EFGFlyweight e:this.subparts){
		s+=e.toString();
	}
	return s;
}

/**
 * returns true only if the two flow parts have the same number of subparts and each of the subparts exists in both of them. Order of subparts doesn't matter
 * 
 * @param fp2
 * @return
 */
public boolean equalsExactly(FlowPart fp2){
	if (this.subparts.size()!=fp2.subparts.size()){
		return false;
	}
	for (EFGFlyweight efw:fp2.subparts){
		if (!this.containsSubpart(efw)){
			return false;
		}
	}
	return true;
}

private boolean containsSubpart(EFGFlyweight efw) {
    for (EFGFlyweight ef2:this.subparts){
    	if (ef2.equals(efw)){
    		return true;
    	}
    }
	return false;
}

}
