package graphUtils;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import etlFlowGraph.graph.ETLFlowGraph;

public class TracePart {
public enum TracePartType{
	route, mergingOp, splittingOp, sourceOp, sinkOp, edgeOrigin, other, delimiter;
}

private TracePartType tpType;
private String value;

public TracePart(TracePartType tpType, String value) {
	super();
	this.tpType = tpType;
	this.value = value;
}

public TracePart clone(){
	return new TracePart(this.tpType, this.value);
}

/**
 * @return the tpType
 */
public TracePartType getTpType() {
	return tpType;
}

/**
 * @param tpType the tpType to set
 */
public void setTpType(TracePartType tpType) {
	this.tpType = tpType;
}

/**
 * @return the value
 */
public String getValue() {
	return value;
}

/**
 * @param value the value to set
 */
public void setValue(String value) {
	this.value = value;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return value;
}

public String toStringOpNames(ETLFlowGraph efg) {
	// TODO Auto-generated method stub
	try{
	return efg.getEtlFlowOperations().get(Integer.valueOf(value)).getOperationName();
	}catch (NullPointerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return value;
}



}
