package graphUtils;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLEdge;

public class NodeAndPathTraces {
private String nodeId;
private ArrayList<PathTrace> pathTraces = new ArrayList<PathTrace>();



public NodeAndPathTraces(String nodeId, ArrayList<PathTrace> pathTraces) {
	super();
	this.nodeId = nodeId;
	this.pathTraces = pathTraces;
}



/**
 * @return the enodeId
 */
public String getNodeId() {
	return nodeId;
}



/**
 * @param enodeId the enodeId to set
 */
public void setNodeId(String nodeId) {
	this.nodeId = nodeId;
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
	return "NodeAndPathTraces [node=" + nodeId + ", pathTraces=" + pathTracesToString()
			+ "]";
}

public void updatePathSignatures(){
	for (PathTrace pt:this.pathTraces){
		pt.updateSignature();
	}
}


}
