package structPatterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Bag;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import etlFlowGraph.graph.ETLFlowGraph;
import flowAnalysis.GraphTransformations;
import flowAnalysis.LabellingStrategy;

public class IsomorphismTester {

	public static ArrayList<CandidatePatternMatch> getAllPatternMatches(
			StructPattern pattern, ETLFlowGraph efg, LabellingStrategy lbs) {
		List<CandidatePatternMatch> candidateInstances = new ArrayList<CandidatePatternMatch>();
		StructPattern efgSP = GraphTransformations.efgToStructPattern(efg, lbs);

		Hashtable<String, LabelCatalog> patternInfo = StructPatternAnalyzer
				.getStructPatternInfo(pattern);
		Hashtable<String, LabelCatalog> efgInfo = StructPatternAnalyzer
				.getStructPatternInfo(efgSP);
		// System.out.println("no of filters: "
		// + efgInfo.get("Filter").getNodesCount());

		// start from the node in the pattern that has the label with the least
		// frequent count in the efg
		Set<String> labels = patternInfo.keySet();
		// System.out.println("labels: " + labels);
		int min = Integer.MAX_VALUE;
		int curCount = Integer.MAX_VALUE;
		String selLabel = "";
		LabelCatalog lc;
		for (String label : labels) {
			lc = efgInfo.get(label);
			if (lc != null) {
				curCount = lc.getNodesCount();
				if (curCount < min) {
					min = curCount;
					selLabel = label;
				}
				// System.out.println("current label: " + label
				// + " selected Label: " + selLabel);
			}
		}
		// System.out.println("final selected label: " + selLabel);
		// if no label is found on the flow, return an empty list
		if (selLabel == "") {
			return (ArrayList<CandidatePatternMatch>) candidateInstances;
		}

		// initialize the list of candidate matches by adding as candidate nodes
		// all the nodes in the graph with the specific label - a label with
		// the least frequent count in efg as found above
		// System.out.println(patternInfo.get(selLabel));
		int patRootId = patternInfo.get(selLabel).getNodes().iterator().next();
		PatternNode patRootNode = pattern.getPattNodes().get(patRootId);
		Bag patRootNeighbors = patRootNode.getNeighborLabels();
		for (Integer i : efgInfo.get(selLabel).getNodes()) {
			PatternNode curNd = efgSP.getPattNodes().get(i);
			// if the candidate node has the same properties as the
			// corresponding node in the pattern model
			if (curNd.checkIfConnectedToNodesWithLabels(patRootNeighbors)) {
				CandidatePatternMatch sp = new CandidatePatternMatch();
				sp.setPatternModel(pattern);
				CandidatePatternMatchNode cpmn = new CandidatePatternMatchNode();
				cpmn.setNodeID(i);
				cpmn.setReferenceGraph(efgSP);
				sp.addNode(cpmn);
				// System.out.println("i am here!");
				cpmn.setBindingNodeFromModel(patRootId);
				sp.addAllToWorkingSetIndexedByLabel(curNd.getNeighborNodes());
				candidateInstances.add(sp);
			}
		}

		// checks if a candidate pattern match node has the same properties of
		// interest as the corresponding node in the pattern model, so that they
		// can be matched and "binded" if true.

		// reminder: starting point is one (random) element in the pattern with
		// selected
		// label found above
		GraphIterator iterator = new BreadthFirstIterator(pattern, patRootId);
		// skip the root element
		if (iterator.hasNext()) {
			iterator.next();
		}
		String label;
		// the new generation of candidate instances that will be renewed in
		// every round, for each visit of a new node on the pattern model
		List<CandidatePatternMatch> candidateInstancesNew = candidateInstances;
		// iterate the nodes f the pattern (BFS)
		while (iterator.hasNext()) {
			Integer nodeId = (Integer) iterator.next();
			// get the next node on the pattern model
			PatternNode pn = (PatternNode) pattern.getPattNodes().get(nodeId);
			label = pn.getLabel();
			Bag pnNeighbors = pn.getNeighborLabels();
			// System.out.println("next node in model while BFS: " + nodeId
			// + " label: " + label);
			candidateInstances = candidateInstancesNew;
			candidateInstancesNew = new ArrayList<CandidatePatternMatch>();
			// for each candidate pattern match

			// for (CandidatePatternMatch cpm : candidateInstances) {
			// System.out.println("candidate instances: " + cpm);
			// }

			for (CandidatePatternMatch candSP : candidateInstances) {
				Set<Integer> workingSet = candSP.getWorkingSetIndexedByLabel()
						.get(label);
				if (workingSet != null) {
					for (Integer candNdId : workingSet) {
						// System.out.println("candNdId: " + candNdId);
						// BAD IMPLEMENTATION!
						// // check if the node is already part of the candidate
						// pattern match graph and skip it if it is
						// if
						// (!candSP.getPattNodes().keySet().contains(candNdId)){
						CandidatePatternMatchNode cpmn = new CandidatePatternMatchNode();

						cpmn.setNodeID(candNdId);
						cpmn.setReferenceGraph(efgSP);
						// adding node to candidate struct pattern match just
						// for the check phase. It will get removed at the end
						// of this loop.
						candSP.addNode(cpmn);
						// System.out.println("new node id: " +
						// cpmn.getNodeID());
						cpmn.setBindingNodeFromModel(nodeId);
						// all the binded elements of the candidate sp must have
						// the
						// same relationships with the candidate node in the
						// referenced graph, as they have with the corresponding
						// nodes in the pattern model
						// System.out.println("id: " + candNdId + " patnode: "
						// + candSP.getPattNodes().get(candNdId));
						// if (((CandidatePatternMatchNode)
						// candSP.getPattNodes()
						// .get(candNdId)).hasSameBindedTopologyWith(pn)) {
						if (cpmn.hasSameBindedTopologyWith(pn)) {
							// pruning based on whether the candidate node has
							// the
							// same properties as the corresponding node in the
							// pattern model, which for now means if the
							// neighboring
							// labels in the model node
							// are also neighboring labels in the candidate node
							// on
							// the reference graph
							PatternNode refNode = efgSP.getPattNodes().get(
									candNdId);
							if (refNode
									.checkIfConnectedToNodesWithLabels(pnNeighbors)) {
								CandidatePatternMatch sp = candSP.deepClone();
								// System.out.println("sp: " + sp);
								// CandidatePatternMatchNode cpmn = new
								// CandidatePatternMatchNode();
								// cpmn.setBindingNodeFromModel(nodeId);
								// cpmn.setNodeID(candNdId);
								// cpmn.setReferenceGraph(efgSP);
								// this step is not needed because the cpmn node
								// has already been added to candSP
								// sp.addNode(cpmn);
								// it will not be null and the specific int will
								// be there because it is a deep copy and the
								// related check has already been performed.
								sp.getWorkingSetIndexedByLabel().get(label)
										.remove(candNdId);
								Set<Integer> neigh = refNode
										.getNeighborNodeIds();
								// this is to ensure that one node does not
								// enter the candidate p m graph twice for
								// example because of cycles
								neigh.removeAll(candSP.getPattNodes().keySet());
								Set<PatternNode> neighbNodesClean = new HashSet<PatternNode>();
								Hashtable<Integer, PatternNode> allNodes = efgSP
										.getPattNodes();
								for (Integer id : neigh) {
									neighbNodesClean.add(allNodes.get(id));
								}
								sp.addAllToWorkingSetIndexedByLabel(neighbNodesClean);
								candidateInstancesNew.add(sp);
								// System.out
								// .println("new candidate p m instances: ");
								// for (CandidatePatternMatch cm :
								// candidateInstancesNew) {
								// System.out
								// .println("candidate instances new: "
								// + cm);
								// }
							}
						}
						candSP.removeNode(cpmn);
						// }
					}
				}
			}

		}
		return (ArrayList<CandidatePatternMatch>) candidateInstancesNew;

	}

	public static ArrayList<CandidatePatternMatch> getAllPatternMatches(
			StructPattern pattern, StructPattern efgSP) {
		List<CandidatePatternMatch> candidateInstances = new ArrayList<CandidatePatternMatch>();
		// this is the only line of code that is different from the above method
		// StructPattern efgSP = GraphTransformations.efgToStructPattern(efg,
		// lbs);

		Hashtable<String, LabelCatalog> patternInfo = StructPatternAnalyzer
				.getStructPatternInfo(pattern);
		Hashtable<String, LabelCatalog> efgInfo = StructPatternAnalyzer
				.getStructPatternInfo(efgSP);
		// System.out.println("no of filters: "
		// + efgInfo.get("Filter").getNodesCount());

		// start from the node in the pattern that has the label with the least
		// frequent count in the efg
		Set<String> labels = patternInfo.keySet();
		// System.out.println("labels: " + labels);
		int min = Integer.MAX_VALUE;
		int curCount = Integer.MAX_VALUE;
		String selLabel = "";
		LabelCatalog lc;
		for (String label : labels) {
			lc = efgInfo.get(label);
			if (lc != null) {
				curCount = lc.getNodesCount();
				if (curCount < min) {
					min = curCount;
					selLabel = label;
				}
				// System.out.println("current label: " + label
				// + " selected Label: " + selLabel);
			}
		}
		// System.out.println("final selected label: " + selLabel);
		// if no label is found on the flow, return an empty list
		if (selLabel == "") {
			return (ArrayList<CandidatePatternMatch>) candidateInstances;
		}

		// initialize the list of candidate matches by adding as candidate nodes
		// all the nodes in the graph with the specific label - a label with
		// the least frequent count in efg as found above
		// System.out.println(patternInfo.get(selLabel));
		int patRootId = patternInfo.get(selLabel).getNodes().iterator().next();
		PatternNode patRootNode = pattern.getPattNodes().get(patRootId);
		Bag patRootNeighbors = patRootNode.getNeighborLabels();
		for (Integer i : efgInfo.get(selLabel).getNodes()) {
			PatternNode curNd = efgSP.getPattNodes().get(i);
			// if the candidate node has the same properties as the
			// corresponding node in the pattern model
			if (curNd.checkIfConnectedToNodesWithLabels(patRootNeighbors)) {
				CandidatePatternMatch sp = new CandidatePatternMatch();
				sp.setPatternModel(pattern);
				CandidatePatternMatchNode cpmn = new CandidatePatternMatchNode();
				cpmn.setNodeID(i);
				cpmn.setReferenceGraph(efgSP);
				sp.addNode(cpmn);
				// System.out.println("i am here!");
				cpmn.setBindingNodeFromModel(patRootId);
				sp.addAllToWorkingSetIndexedByLabel(curNd.getNeighborNodes());
				candidateInstances.add(sp);
			}
		}

		// checks if a candidate pattern match node has the same properties of
		// interest as the corresponding node in the pattern model, so that they
		// can be matched and "binded" if true.

		// reminder: starting point is one (random) element in the pattern with
		// selected
		// label found above
		GraphIterator iterator = new BreadthFirstIterator(pattern, patRootId);
		// skip the root element
		if (iterator.hasNext()) {
			iterator.next();
		}
		String label;
		// the new generation of candidate instances that will be renewed in
		// every round, for each visit of a new node on the pattern model
		List<CandidatePatternMatch> candidateInstancesNew = candidateInstances;
		// iterate the nodes f the pattern (BFS)
		while (iterator.hasNext()) {
			Integer nodeId = (Integer) iterator.next();
			// get the next node on the pattern model
			PatternNode pn = (PatternNode) pattern.getPattNodes().get(nodeId);
			label = pn.getLabel();
			Bag pnNeighbors = pn.getNeighborLabels();
			// System.out.println("next node in model while BFS: " + nodeId
			// + " label: " + label);
			candidateInstances = candidateInstancesNew;
			candidateInstancesNew = new ArrayList<CandidatePatternMatch>();
			// for each candidate pattern match

			// for (CandidatePatternMatch cpm : candidateInstances) {
			// System.out.println("candidate instances: " + cpm);
			// }

			for (CandidatePatternMatch candSP : candidateInstances) {
				Set<Integer> workingSet = candSP.getWorkingSetIndexedByLabel()
						.get(label);
				if (workingSet != null) {
					for (Integer candNdId : workingSet) {
						// System.out.println("candNdId: " + candNdId);
						// BAD IMPLEMENTATION!
						// // check if the node is already part of the candidate
						// pattern match graph and skip it if it is
						// if
						// (!candSP.getPattNodes().keySet().contains(candNdId)){
						CandidatePatternMatchNode cpmn = new CandidatePatternMatchNode();

						cpmn.setNodeID(candNdId);
						cpmn.setReferenceGraph(efgSP);
						// adding node to candidate struct pattern match just
						// for the check phase. It will get removed at the end
						// of this loop.
						candSP.addNode(cpmn);
						// System.out.println("new node id: " +
						// cpmn.getNodeID());
						cpmn.setBindingNodeFromModel(nodeId);
						// all the binded elements of the candidate sp must have
						// the
						// same relationships with the candidate node in the
						// referenced graph, as they have with the corresponding
						// nodes in the pattern model
						// System.out.println("id: " + candNdId + " patnode: "
						// + candSP.getPattNodes().get(candNdId));
						// if (((CandidatePatternMatchNode)
						// candSP.getPattNodes()
						// .get(candNdId)).hasSameBindedTopologyWith(pn)) {
						if (cpmn.hasSameBindedTopologyWith(pn)) {
							// pruning based on whether the candidate node has
							// the
							// same properties as the corresponding node in the
							// pattern model, which for now means if the
							// neighboring
							// labels in the model node
							// are also neighboring labels in the candidate node
							// on
							// the reference graph
							PatternNode refNode = efgSP.getPattNodes().get(
									candNdId);
							if (refNode
									.checkIfConnectedToNodesWithLabels(pnNeighbors)) {
								CandidatePatternMatch sp = candSP.deepClone();
								// System.out.println("sp: " + sp);
								// CandidatePatternMatchNode cpmn = new
								// CandidatePatternMatchNode();
								// cpmn.setBindingNodeFromModel(nodeId);
								// cpmn.setNodeID(candNdId);
								// cpmn.setReferenceGraph(efgSP);
								// this step is not needed because the cpmn node
								// has already been added to candSP
								// sp.addNode(cpmn);
								// it will not be null and the specific int will
								// be there because it is a deep copy and the
								// related check has already been performed.
								sp.getWorkingSetIndexedByLabel().get(label)
										.remove(candNdId);
								Set<Integer> neigh = refNode
										.getNeighborNodeIds();
								// this is to ensure that one node does not
								// enter the candidate p m graph twice for
								// example because of cycles
								neigh.removeAll(candSP.getPattNodes().keySet());
								Set<PatternNode> neighbNodesClean = new HashSet<PatternNode>();
								Hashtable<Integer, PatternNode> allNodes = efgSP
										.getPattNodes();
								for (Integer id : neigh) {
									neighbNodesClean.add(allNodes.get(id));
								}
								sp.addAllToWorkingSetIndexedByLabel(neighbNodesClean);
								candidateInstancesNew.add(sp);
								// System.out
								// .println("new candidate p m instances: ");
								// for (CandidatePatternMatch cm :
								// candidateInstancesNew) {
								// System.out
								// .println("candidate instances new: "
								// + cm);
								// }
							}
						}
						candSP.removeNode(cpmn);
						// }
					}
				}
			}

		}
		return (ArrayList<CandidatePatternMatch>) candidateInstancesNew;

	}
}
