package graphUtils;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;

public class FlowPathTraces {

	/**
	 * an array list of all the final path traces - all the path traces starting
	 * with a source and ending with a sink.
	 */
	private ArrayList<PathTrace> finalPTs = new ArrayList<PathTrace>();
	
	/**
	 * a hash map of all the final path traces - all the path traces starting
	 * with a source and ending with a sink - indexed by their path id
	 */
	private HashMap<Integer,PathTrace> finalPTsHash = new HashMap<Integer,PathTrace>();

	/**
	 * a hashmap of edge ids and tagged path traces (also including the edge
	 * itself)
	 */
	private HashMap<String, EdgeAndPathTraces> taggedEdges = new HashMap<String, EdgeAndPathTraces>();

	/**
	 * a hashmap of node ids and their indexes, according to their topological
	 * order in the graph, as they are traversed during the extraction phase
	 */
	private HashMap<Integer, Integer> topoIndx = new HashMap<Integer, Integer>();

	/**
	 * the etl flow graph to which these flow path traces are related
	 */
	private ETLFlowGraph efg;

	/**
	 * @return the finalPTs
	 */
	public ArrayList<PathTrace> getFinalPTs() {
		return finalPTs;
	}

	/**
	 * @param finalPTs
	 *            the finalPTs to set
	 */
	public void setFinalPTs(ArrayList<PathTrace> finalPTs) {
		this.finalPTs = finalPTs;
	}

	/**
	 * @return the taggedEdges
	 */
	public HashMap<String, EdgeAndPathTraces> getTaggedEdges() {
		return taggedEdges;
	}

	/**
	 * @param taggedEdges
	 *            the taggedEdges to set
	 */
	public void setTaggedEdges(HashMap<String, EdgeAndPathTraces> taggedEdges) {
		this.taggedEdges = taggedEdges;
	}

	public void addEdgeAndPathTraces(String eedge, ArrayList<PathTrace> pts) {
		EdgeAndPathTraces ept = new EdgeAndPathTraces(eedge, pts);
		taggedEdges.put(eedge, ept);
	}

	public ArrayList<PathTrace> getPathTracesOfEdge(ETLEdge eedge) {
		EdgeAndPathTraces eapt = this.taggedEdges.get(eedge.toString());
		if (eapt != null) {
			return eapt.getPathTraces();

		} else {
			return null;
		}
	}

	public String finalPTsToString() {
		String str = "[";
		for (PathTrace pt : finalPTs) {
			str += pt.toString() + ", ";
		}
		str += "]";
		return str;
	}

	public String taggedEdgesToString() {
		String str = "[";
		Iterator it = taggedEdges.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			str += ((EdgeAndPathTraces) pair.getValue()).toString() + ", ";
		}
		str += "]";
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FlowPathTraces [finalPTs=" + finalPTsToString()
				+ ", taggedEdges=" + taggedEdgesToString() + "]";
	}

	public void updatePathSignatures() {
		for (PathTrace pt : this.finalPTs) {
			pt.updateSignature();
		}
		Iterator it = taggedEdges.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			((EdgeAndPathTraces) pair.getValue()).updatePathSignatures();
		}
	}

	/**
	 * @return the efg
	 */
	public ETLFlowGraph getEfg() {
		return efg;
	}

	/**
	 * @param efg
	 *            the efg to set
	 */
	public void setEfg(ETLFlowGraph efg) {
		this.efg = efg;
	}

	/**
	 * @return the topoIndx
	 */
	public HashMap<Integer, Integer> getTopoIndx() {
		return topoIndx;
	}

	/**
	 * @param topoIndx
	 *            the topoIndx to set
	 */
	public void setTopoIndx(HashMap<Integer, Integer> topoIndx) {
		this.topoIndx = topoIndx;
	}

	public void addToFinalPTs(PathTrace pt2) {
		if (pt2!=null){
		this.finalPTs.add(pt2);
		this.finalPTsHash.put(pt2.getId(), pt2);
		}
		
	}

	/**
	 * @return the finalPTsHash
	 */
	public HashMap<Integer, PathTrace> getFinalPTsHash() {
		return finalPTsHash;
	}

	/**
	 * @param finalPTsHash the finalPTsHash to set
	 */
	public void setFinalPTsHash(HashMap<Integer, PathTrace> finalPTsHash) {
		this.finalPTsHash = finalPTsHash;
	}
	
	

}
