package graphUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;

/**
 * This object is used to store a number of alternative path flows that can be
 * combined with another path flow on a merging operator.
 * 
 * @author vasileios
 *
 */
public class AlternativePathFlows {

	/**
	 * key of external hash is node id, the node being a merging operator, and
	 * value is an hash map of edge ids and path traces, denoting which edge the
	 * path traces "pass" from
	 */
	private HashMap<String, HashMap<String, ArrayList<PathTrace>>> altPathTracesForNodes = new HashMap<String, HashMap<String, ArrayList<PathTrace>>>();

	private PathTrace examinedPathTrace;
	
	private ETLFlowGraph efg;
	
	
	
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

	/**
	 * @return the examinedPathTrace
	 */
	public PathTrace getExaminedPathTrace() {
		return examinedPathTrace;
	}

	/**
	 * @param examinedPathTrace the examinedPathTrace to set
	 */
	public void setExaminedPathTrace(PathTrace examinedPathTrace) {
		this.examinedPathTrace = examinedPathTrace;
	}

	/**
	 * @return the altPathTracesForNodes
	 */
	public HashMap<String, HashMap<String, ArrayList<PathTrace>>> getAltPathTracesForNodes() {
		return altPathTracesForNodes;
	}

	/**
	 * @param altPathTracesForNodes
	 *            the altPathTracesForNodes to set
	 */
	public void setAltPathTracesForNodes(
			HashMap<String, HashMap<String, ArrayList<PathTrace>>> altPathTracesForNodes) {
		this.altPathTracesForNodes = altPathTracesForNodes;
	}

	public void addPathToNode(PathTrace pt, ETLFlowOperation efo) {
		String edgeOrigin;
		String pathSignature = pt.getSignature();
		// find edge origin for this merging operator
		String mopStr = "M[" + Integer.toString(efo.getNodeID()) + "]";
		System.out.println("mop: " + mopStr);
		int strSize = mopStr.length();
		System.out.println("strsize: " + strSize);
		int beginIndex = pathSignature.indexOf(mopStr);
		System.out.println("begIndx: " + beginIndex);
		beginIndex += strSize;
		String str2 = pathSignature.substring(beginIndex);
		StringTokenizer stk = new StringTokenizer(str2, ".");
		String str3 = (String) stk.nextElement();
		edgeOrigin = str3.substring(2, str3.length() - 1);
		System.out.println("origin: " + edgeOrigin);
		HashMap<String, ArrayList<PathTrace>> pts = this.altPathTracesForNodes
				.get(Integer.toString(efo.getNodeID()));
		// initializing the object and adding first elements for a node
		if ((pts == null) || (pts.isEmpty())) {
			System.out.println("empty or null?: " + pts);
			pts = new HashMap<String, ArrayList<PathTrace>>();
			ArrayList<PathTrace> ptrs = new ArrayList<PathTrace>();
			ptrs.add(pt);
			pts.put(edgeOrigin, ptrs);
			this.altPathTracesForNodes.put(Integer.toString(efo.getNodeID()),
					pts);
		} else {
			// if there is already an entry for this node
			ArrayList<PathTrace> edgeAltPaths = pts.get(edgeOrigin);
			// if there is already an entry for this edge
			if ((edgeAltPaths == null) || (edgeAltPaths.isEmpty())) {
				System.out.println("empty or null?: " + edgeAltPaths);
				edgeAltPaths = new ArrayList<PathTrace>();
				edgeAltPaths.add(pt);
				pts.put(edgeOrigin, edgeAltPaths);
			}
			else{
				edgeAltPaths.add(pt);
			}
		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlternativePathFlows [altPathTracesForNodes="
				+ altPathTracesForNodes + "]";
	}

}
