package graphUtils;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLEdge;

public class EdgeAndPathTraces {
private String eedgeId;
private ArrayList<PathTrace> pathTraces = new ArrayList<PathTrace>();



public EdgeAndPathTraces(String eedgeId, ArrayList<PathTrace> pathTraces) {
	super();
	this.eedgeId = eedgeId;
	this.pathTraces = pathTraces;
}



/**
 * @return the eedgeId
 */
public String getEedgeId() {
	return eedgeId;
}



/**
 * @param eedgeId the eedgeId to set
 */
public void setEedgeId(String eedgeId) {
	this.eedgeId = eedgeId;
}



/**
 * @return the pathTraces
 */
public ArrayList<PathTrace> getPathTraces() {
	return pathTraces;
}



/**
 * @param pathTraces the pathTraces to set
 */
public void setPathTraces(ArrayList<PathTrace> pathTraces) {
	this.pathTraces = pathTraces;
}



public String pathTracesToString(){
	String str = "[";
	for (PathTrace pt:this.pathTraces){
		str+=pt.toString()+", ";
	}
	str+="]";
	return str;
}


/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "EdgeAndPathTraces [eedge=" + eedgeId + ", pathTraces=" + pathTracesToString()
			+ "]";
}

public void updatePathSignatures(){
	for (PathTrace pt:this.pathTraces){
		pt.updateSignature();
	}
}


}
