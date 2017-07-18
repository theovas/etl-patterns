package flowAnalysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;

/**
 * 
 * A light class that contains edges and nodes of an etl flow graph (complete
 * flow or only part of the flow). For the nodes, it only keeps the IDs of the
 * etl flow operations.
 * 
 * @author btheo
 *
 */
public class EFGFlyweight {

	/**
	 * A set that contains the IDs of all the nodes (etl flow operations) of the
	 * etl flow graph
	 */
	private Set<Integer> efgNodes;
	private Set<EdgeFly> efgEdges;

	/**
	 * the complete etl flow graph representation of the EFGFlyweight
	 */
	private ETLFlowGraph efg = null;
	private boolean syncToEfg = true;

	public EFGFlyweight(Set<Integer> efgNodes, Set<EdgeFly> efgEdges) {
		super();
		// Set<Integer> efgNodes2 = new HashSet<Integer>();
		// for (Integer n:efgNodes){
		// efgNodes2.add(n);
		// }
		// Set<EdgeFly> efgEdges2 = new HashSet<EdgeFly>();
		// for (EdgeFly ef:efgEdges){
		// efgEdges2.add(ef.copy());
		// }

		this.efgNodes = efgNodes;
		this.efgEdges = efgEdges;
	}

	public static EFGFlyweight getFlyweightFromEFG(ETLFlowGraph efg) {
		HashSet<Integer> efgNodes = new HashSet<Integer>();
		HashSet<EdgeFly> efgEdges = new HashSet<EdgeFly>();
		Iterator it = efg.iterator();
		while (it.hasNext()) {
			efgNodes.add((Integer) it.next());
		}

		for (Object e : efg.edgeSet()) {
			EdgeFly ef = new EdgeFly((int) ((ETLEdge) e).getSource(),
					(int) ((ETLEdge) e).getTarget());
			efgEdges.add(ef);
		}

		return new EFGFlyweight(efgNodes, efgEdges);

	}

	public ETLFlowGraph getEFG(ETLFlowGraph initialEfg) {
		if ((efg == null) || (syncToEfg == false)) {
			ETLFlowGraph efgNew = new ETLFlowGraph();
			for (Integer opId : this.efgNodes) {
				efgNew.addNode(initialEfg.getEtlFlowOperations().get(opId));
				System.out.println("adding node: " + opId);
			}
			for (EdgeFly ef : this.efgEdges) {
				try {
					System.out.println("adding edge: " + ef);
					efgNew.addDagEdge(ef.getSource(), ef.getTarget());
				} catch (CycleFoundException e) {
					e.printStackTrace();
				}
			}

			return efgNew;
		} else
			return efg;
	}

	/**
	 * @return the efgNodes
	 */
	public Set<Integer> getEfgNodes() {
		return efgNodes;
	}

	/**
	 * @param efgNodes
	 *            the efgNodes to set
	 */
	public void setEfgNodes(Set<Integer> efgNodes) {
		this.efgNodes = efgNodes;
	}

	/**
	 * @return the efgEdges
	 */
	public Set<EdgeFly> getEfgEdges() {
		return efgEdges;
	}

	/**
	 * @param efgEdges
	 *            the efgEdges to set
	 */
	public void setEfgEdges(Set<EdgeFly> efgEdges) {
		this.efgEdges = efgEdges;
	}

	public String toString(ETLFlowGraph efg) {
		return "EFGFlyweight [" + this.getEFG(efg).toString() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		s += "EFGFlyweight [efgNodes=[";
		for (Integer n : efgNodes) {
			s += n + " ";
		}
		s += "][efgEdges=[";
		for (EdgeFly e : efgEdges) {
			s += "(" + e.getSource() + "," + e.getTarget() + ")";
		}
		s += "]]";
		return s;
	}

	public boolean equals(EFGFlyweight efw2) {
		if (this.efgEdges.size() != efw2.efgEdges.size()) {
			return false;
		}
		if (this.efgNodes.size() != efw2.efgNodes.size()) {
			return false;
		}
		for (EdgeFly e : efw2.efgEdges) {
			if (!this.containsEdge(e)) {
				return false;
			}
		}

		if (!efw2.efgNodes.equals(this.efgNodes)) {
			return false;
		}
		return true;
	}

	public boolean containsEdge(EdgeFly e) {
		for (EdgeFly e2 : this.efgEdges) {
			if (e2.equals(e)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsNode(int nodeid) {
		if (this.efgNodes.contains(nodeid)) {
			return true;
		}
		return false;
	}

}
