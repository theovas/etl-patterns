package structPatterns;

import java.util.HashSet;
import java.util.Set;

/**
 * A class to represent a node while searching for a pattern match on a graph
 * (reference graph). This node of the potential pattern instance corresponds to
 * one node in the pattern model and one other node in the reference graph. If a
 * pattern match is finally confirmed, then the StructPattern graph containing
 * nodes of this type will be the pattern instance match. The node id of each
 * object should be the same as the node id of the corresponding node on the
 * reference graph ETL flow.
 * 
 * @author vasileios
 *
 */
public class CandidatePatternMatchNode extends PatternNode {

	public CandidatePatternMatchNode() {
	}

	public CandidatePatternMatchNode(String label, int nodeId,
			int bindingNodeFromModel) {
		this.setLabel(label);
		this.setNodeID(nodeId);
		this.bindingNodeFromModel = bindingNodeFromModel;
	}

	// private boolean isBinded = false;
	/**
	 * the id of the pattern model node that possibly relates to this candidate
	 * pattern instance, if there is a match
	 */
	private int bindingNodeFromModel = -1;
	/**
	 * the reference graph to which this candidate pattern match node refers.
	 * For example if we are searching for pattern matches of a pattern P over a
	 * graph G, then G would be the reference graph.
	 */
	private StructPattern referenceGraph;

	/**
	 * @return the bindingNodeFromModel
	 */
	public int getBindingNodeFromModel() {
		return bindingNodeFromModel;
	}

	/**
	 * @param bindingNodeFromModel
	 *            the bindingNodeFromModel to set
	 */
	public void setBindingNodeFromModel(int bindingNodeFromModel) {
		this.bindingNodeFromModel = bindingNodeFromModel;
		// System.out.println("setBindingNodeFromModel for: " + this.getNodeID()
		// + ". this.strucPatt: " + this.getStructPattern());
		// System.out.println();
		Set<Integer> bns = ((CandidatePatternMatch) this.getStructPattern())
				.getBindedNodesFromModel();
		// System.out.println("binded nodes before: " + bns);
		// System.out.println("adding node: " + bindingNodeFromModel
		// + " to binded nodes from model");
		bns.add(bindingNodeFromModel);
		// System.out.println("binded nodes after: " + bns);
	}

	/**
	 * @return the referenceGraph
	 */
	public StructPattern getReferenceGraph() {
		return referenceGraph;
	}

	/**
	 * @param referenceGraph
	 *            the referenceGraph to set
	 */
	public void setReferenceGraph(StructPattern referenceGraph) {
		this.referenceGraph = referenceGraph;
	}

	/**
	 * @return the bindingNodes from all the neighbors of this node on the
	 *         reference graph
	 */
	public Set<Integer> getBindingNodesFromNeighborsInRefG() {
		Set<Integer> bNodes = new HashSet<Integer>();
		Set<Integer> refNeighbNds = this.getReferenceGraph().getPattNodes()
				.get(this.getNodeID()).getNeighborNodeIds();
		// System.out.println("ref neighbors" + refNeighbNds);
		CandidatePatternMatch sp = (CandidatePatternMatch) this
				.getStructPattern();
		// keeps only the neighbouring nodes that have already been included in
		// the candidate pattern match graph, and thus already binded to a node
		// from the pattern model
		refNeighbNds.retainAll(sp.getPattNodes().keySet());
		// System.out.println("reduced ref neighbors" + refNeighbNds);
		for (Integer cpmid : refNeighbNds) {
			// int bnode =
			// ((CandidatePatternMatchNode)sp.getPattNodes().get(cpm.getNodeID())).getBindingNodeFromModel();
			// // if the neighboring node in the reference graph has already
			// been included in the binded nodes
			// if (sp.getBindedNodesFromModel().contains(bnode)){
			bNodes.add(((CandidatePatternMatchNode) sp.getPattNodes()
					.get(cpmid)).getBindingNodeFromModel());
		}
		return bNodes;
	}

	/**
	 * in the reference graph, all the corresponding binded elements of the
	 * candidate pattern match graph of this node must have the same
	 * relationships with the candidate node, as they have with the
	 * corresponding nodes in the pattern model.
	 * 
	 * TODO: explain in an example
	 * 
	 * @param pn
	 * @return
	 */
	public boolean hasSameBindedTopologyWith(PatternNode patModelNd) {
		// System.out.println("checking if " + this.getNodeID()
		// + " has the same binded topology with "
		// + patModelNd.getNodeID());
		// the neighbors of the node in the pattern model
		Set<Integer> neighborIdsOfPMNd = patModelNd.getNeighborNodeIds();
		// keep only the intersection of already binded nodes and neighbor nodes
		// in the pattern model
		neighborIdsOfPMNd.retainAll(((CandidatePatternMatch) this
				.getStructPattern()).getBindedNodesFromModel());
		// the neighbors at the reference graph of the node in the candidate
		// pattern match graph. Since
		// this graph is being incrementally built, all these nodes will already
		// be binded to one node from the pattern model.
		Set<Integer> neighborBindings = this
				.getBindingNodesFromNeighborsInRefG();
		// System.out.println(neighborBindings);
		// System.out.println(neighborIdsOfPMNd);
		// System.out.println(neighborBindings.containsAll(neighborIdsOfPMNd));
		return neighborBindings.containsAll(neighborIdsOfPMNd);
	}

}
