package flowAnalysis;

import importXLM.ImportXLMToETLGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import drivers.ImportPDIExportXML;
import utils.IOProcessor;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.EdgeAndPathTraces;
import graphUtils.FlowPathTraces;
import graphUtils.FlowPathsAnalyzer;
import graphUtils.NodeAndPathTraces;
import graphUtils.NodeAndTopoOrder;
import graphUtils.PathTrace;
import graphUtils.TracePart;

public class MaxNonMergingFlowPartsDecomposition implements DecompositionPolicy {

	public static final String quarryPath = "/Users/btheo/Desktop/Vasileios/experiments/Quarry/forge/xLM/tpch/";

	/**
	 * a hashmap of node ids and tagged path traces
	 */

	public static void main(String[] args) {
		ETLFlowGraph efg = new ETLFlowGraph();
		ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
		System.out.println("starting...");
		try {
			// efg = ix2g.getFlowGraph("tests/etl-initial_agn.xml");
			// efg = ix2g.getFlowGraph("tests/test3_again.xml");
			// Iterator it = efg.iterator();
			// while (it.hasNext()){
			// ETLFlowOperation efo = (ETLFlowOperation)
			// efg.getEtlFlowOperations().get(it.next());
			// System.out.println(efo.getOperationType() +
			// "grouping: "+OperationClassification.inFlowNarityGrouping(efo)
			// +" " + OperationClassification.outFlowNarityGrouping(efo)+" "
			// +OperationClassification.inAndOutFlowNarityGrouping(efo)+" input size: "+efo.getInputSchemata().size()+" output size: "+efo.getOutputSchemata().size());
			// }

			LabellingStrategy ls = new LabelBasedOnIONarity();
			// LabellingStrategy ls2 = new LabelBasedOnConsumeProduce();
			// LabellingStrategy ls3 = new LabelBasedOnOpType();
			LabellingStrategy ls4 = new LabelBasedOnSchemaDiff();
			ArrayList<LabellingStrategy> lss = new ArrayList<LabellingStrategy>();
			lss.add(ls);
			lss.add(ls4);
			LabellingStrategy ls5 = new LabelBasedOnCombination(lss);
			String etlName = "financial_agn";
			// ETLFlowOperation efo = ix2g
			// .getFlowGraph("tests/tpcdi/dimBroker_agn.xml")
			// .getEtlFlowOperations().get(141);
			// System.out.println(efo.getOperationType());
			// GraphTransformations.createGraphTranFile(etlName,ls);
			// GraphTransformations.createGraphTranFile(etlName,ls2);
			// GraphTransformations.createGraphTranFile(etlName,ls3);
			GraphTransformations.createGraphTranFile(etlName, ls5);

			// IOProcessor.readFileToString("tests/tpcdi/" +
			// "dimSecurity_agn.xml");

			// GraphTransformations
			// .extractStructPatternsFromFile("tests/tpcdi/pafiResults/"
			// + "opType-test.fp");

			// System.out.println(GraphTransformations.toGraphTranFile(efg,
			// ls2));
			// efg = ix2g.getFlowGraph("tests/etl-initial_agn.xml");
			// System.out.println(GraphTransformations.toGraphTranFile(efg,
			// ls2));
			// =======
			// String s =
			// ImportPDIExportXML.importPDItoXML("/Users/btheo/Dropbox/anton/tpcdi/etls/xlmTranslatable/DimSecurity");
			// System.out.println("the efg created:"+"\n"+s);
			// System.out.println(efg);
			// System.out.println((new
			// MaxNonMergingFlowPartsDecomposition()).decomposeFlow(efg));
			// System.out.println((new MaxNonMergingFlowPartsDecomposition())
			// .getContainingFlowpart(efg, 237));
			// =======
			// IOProcessor.writeStringToFile(s, "tests/tpcdi/" +
			// "dimSecurity_agn.xml");
			// efg = ix2g.getFlowGraph("tests/tpcdi/dimSecurity_agn.xml");
			// (new MaxNonMergingFlowPartsDecomposition()).decomposeFlow(efg);
			// =======IOProcessor.writeStringToFile(efg.toStringXLM(),
			// quarryPath
			// ======= + "dimSecurity_agn.xml");

		} finally {
			// } catch (CycleFoundException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		}

	}

	@Override
	public FlowDecomposition decomposeFlow(ETLFlowGraph efg) {
		HashMap<String, NodeAndPathTraces> taggedNodes = new HashMap<String, NodeAndPathTraces>();
		FlowDecomposition fDec = new FlowDecomposition();
		fDec.setEfg(efg);
		int curNode = -1;
		int decsNo = 0;
		Iterator it = efg.iterator();
		while (it.hasNext()) {
			curNode = (int) it.next();
			// if it is input source
			if (FlowPathsAnalyzer.inputSourceTypes.contains(efg
					.getEtlFlowOperations().get(curNode).getOperationType())) {
				ArrayList<PathTrace> pathTraces = new ArrayList<PathTrace>();
				PathTrace pt = new PathTrace();
				pt.addPartToTrace(new TracePart(
						TracePart.TracePartType.sourceOp, Integer
								.toString(curNode)));
				pathTraces.add(pt);
				NodeAndPathTraces ndpt = new NodeAndPathTraces(
						Integer.toString(curNode), pathTraces);
				taggedNodes.put(Integer.toString(curNode), ndpt);
			} else
			// if it is
			if ((FlowPathsAnalyzer.mergingTypes.contains(efg
					.getEtlFlowOperations().get(curNode).getOperationType()))
					|| (FlowPathsAnalyzer.splittingTypes.contains(efg
							.getEtlFlowOperations().get(curNode)
							.getOperationType()))
					|| (FlowPathsAnalyzer.outputSourceTypes.contains(efg
							.getEtlFlowOperations().get(curNode)
							.getOperationType()))) {
				Set<ETLEdge> inEdges = efg.incomingEdgesOf(curNode);
				for (ETLEdge e : inEdges) {
					// System.out.println("node: "
					// + efg.getEtlFlowOperations().get(curNode)
					// .getOperationName()
					// + " incoming node: "
					// + efg.getEtlFlowOperations().get(e.getSource())
					// .getOperationName());
					NodeAndPathTraces ndpt = taggedNodes.get(Integer
							.toString((int) e.getSource()));
					if (ndpt != null) {
						ArrayList<PathTrace> pts = ndpt.getPathTraces();
						for (PathTrace pt : pts) {
							PathTrace pt2 = pt.clone();
							pt2.addPartToTrace(new TracePart(
									TracePart.TracePartType.delimiter, Integer
											.toString(curNode)));
							// here create a flyw and add it to the fDec
							System.out.println("path trace: "
									+ pt2.toStringOpNames(efg));
							HashSet<Integer> efgNodes = new HashSet<Integer>();
							HashSet<EdgeFly> efgEdges = new HashSet<EdgeFly>();
							int curnd = -1;
							int prvnd = -1;
							for (TracePart tprt : pt2.getTrace()) {
								curnd = Integer.valueOf(tprt.getValue());
								efgNodes.add(curnd);
								if (prvnd != -1) {
									efgEdges.add(new EdgeFly(prvnd, curnd));
								}
								prvnd = curnd;
							}
							EFGFlyweight efw = new EFGFlyweight(efgNodes,
									efgEdges);
							FlowPart fp = new FlowPart();
							fp.getSubparts().add(efw);
							fDec.getDecomposition().add(fp);
							decsNo++;
						}
					}
				}
				ArrayList<PathTrace> pathTraces = new ArrayList<PathTrace>();
				PathTrace pt = new PathTrace();
				pt.addPartToTrace(new TracePart(
						TracePart.TracePartType.delimiter, Integer
								.toString(curNode)));
				pathTraces.add(pt);
				NodeAndPathTraces ndpt = new NodeAndPathTraces(
						Integer.toString(curNode), pathTraces);
				taggedNodes.put(Integer.toString(curNode), ndpt);

			} else {
				Set<ETLEdge> inEdges = efg.incomingEdgesOf(curNode);
				for (ETLEdge e : inEdges) {
					NodeAndPathTraces ndpt = taggedNodes.get(Integer
							.toString((int) e.getSource()));
					// PathTrace pt = new PathTrace();
					if (ndpt != null) {
						ArrayList<PathTrace> pts = ndpt.getPathTraces();
						ArrayList<PathTrace> pts2 = new ArrayList<PathTrace>();
						for (PathTrace pt : pts) {
							PathTrace pt2 = pt.clone();
							pt2.addPartToTrace(new TracePart(
									TracePart.TracePartType.other, Integer
											.toString(curNode)));
							pts2.add(pt2);
						}
						NodeAndPathTraces ndpt2 = new NodeAndPathTraces(
								Integer.toString(curNode), pts2);
						taggedNodes.put(Integer.toString(curNode), ndpt2);
					}
				}
			}
		}

		System.out.println("number of parts: " + decsNo);
		return fDec;

	}

	public FlowDecomposition decomposeFlowNonEfficiently(ETLFlowGraph efg) {
		FlowPathTraces fpt = FlowPathsAnalyzer.generatePathTraces(efg);
		ArrayList<PathTrace> allPaths = fpt.getFinalPTs();
		HashMap<Integer, Integer> topos = fpt.getTopoIndx();
		FlowDecomposition fDec = new FlowDecomposition();
		// HashMap<String, EdgeAndPathTraces> taggedEdges =
		// fpt.getTaggedEdges();
		Set<ETLEdge> eset = efg.edgeSet();
		ArrayList<ArrayList<NodeAndTopoOrder>> allPathsTopos = new ArrayList<ArrayList<NodeAndTopoOrder>>();
		System.out.println(allPaths.size());
		for (PathTrace ptfin : allPaths) {
			// EFGFlyweight efgf= new EFGFlyweight();
			// HashSet<Integer> efgNodes = new HashSet<Integer>();
			// HashSet<ETLEdge> efgEdges = new HashSet<ETLEdge>();
			ArrayList<NodeAndTopoOrder> natos = new ArrayList<NodeAndTopoOrder>();
			for (ETLEdge e : eset) {
				ArrayList<PathTrace> pts = fpt.getPathTracesOfEdge(e);
				for (PathTrace pt : pts) {
					if (pt.isIncludedLeftIn(ptfin)) {
						// efgEdges.add(e);
						if (FlowPathsAnalyzer.inputSourceTypes.contains(efg
								.getEtlFlowOperations().get(e.getSource())
								.getOperationType())) {
							natos.add(new NodeAndTopoOrder((Integer) e
									.getSource(), topos.get((Integer) e
									.getSource())));
							// efgNodes.add((Integer) e.getSource());
							// System.out.println("adding node: "+e.getSource());
						}
						natos.add(new NodeAndTopoOrder((Integer) e.getTarget(),
								topos.get((Integer) e.getTarget())));
						// System.out.println("adding node: "+e.getTarget());
						// efgNodes.add((Integer) e.getTarget());
						// if
						// (FlowPathsAnalyzer.mergingTypes.contains(efg.getEtlFlowOperations().get(e.getTarget()))){
						//
						// }
						break;
					}
				}

			}
			Collections.sort(natos);
			allPathsTopos.add(natos);
			// System.out.println("path: "+ptfin.toString());
		}
		for (ArrayList<NodeAndTopoOrder> natos42 : allPathsTopos) {
			HashSet<Integer> efgNodes = new HashSet<Integer>();
			HashSet<EdgeFly> efgEdges = new HashSet<EdgeFly>();
			int previousNode = -1;
			int curNode = -1;
			EFGFlyweight efw = null;
			for (NodeAndTopoOrder nato : natos42) {
				// System.out.println("nato node id: "+nato.getNodeId());
				curNode = nato.getNodeId();
				efgNodes.add(curNode);
				if (previousNode != -1) {
					efgEdges.add(new EdgeFly(previousNode, curNode));
				}
				previousNode = curNode;
				if ((FlowPathsAnalyzer.mergingTypes
						.contains(efg.getEtlFlowOperations().get(curNode)
								.getOperationType()))
						|| (FlowPathsAnalyzer.splittingTypes.contains(efg
								.getEtlFlowOperations().get(curNode)
								.getOperationType()))) {
					efw = new EFGFlyweight(efgNodes, efgEdges);
					// System.out.println("right after creating the efg: "+efw);
					FlowPart fp = new FlowPart();
					fp.getSubparts().add(efw);
					// fDec.getDecomposition().add(fp);
					fDec.addIfNotExists(fp);
					efgNodes = new HashSet<Integer>();
					efgEdges = new HashSet<EdgeFly>();
					previousNode = -1;
				}

			}
			efw = new EFGFlyweight(efgNodes, efgEdges);
			FlowPart fp = new FlowPart();
			fp.getSubparts().add(efw);
			System.out.println("right before addifnotexists: " + efw);
			// fDec.getDecomposition().add(fp);
			fDec.addIfNotExists(fp);
			System.out.println("number of subparts: "
					+ fDec.getDecomposition().size());
		}

		return fDec;
	}

	/**
	 * Returns a flow part object that represents a <i>linear</i> part of the
	 * etl flow in which the node with the specified nodeid is contained, or
	 * null if not found. IMPORTANT: If the node is of a merging/joining type,
	 * whih means that it can potentially have more than one incoming/outgoing
	 * edge, i.e., the node can participate in more than one etl flow parts,
	 * then the first one to be randomly found will be returned.
	 * 
	 * @param efg
	 * @param nodeid
	 * @return
	 */
	public FlowPart getContainingFlowpart(ETLFlowGraph efg, int nodeid) {
		FlowDecomposition fdec = this.decomposeFlow(efg);
		for (FlowPart fp : fdec.getDecomposition()) {
			for (EFGFlyweight efw : fp.getSubparts()) {
				if (efw.containsNode(nodeid)) {
					return fp;
				}
			}
		}
		return null;

	}

	/**
	 * Returns a flow part object within the provided flow decomposition that
	 * represents a <i>linear</i> part of the etl flow in which the node with
	 * the specified nodeid is contained, or null if not found. IMPORTANT: If
	 * the node is of a merging/joining type, whih means that it can potentially
	 * have more than one incoming/outgoing edge, i.e., the node can participate
	 * in more than one etl flow parts, then the first one to be randomly found
	 * will be returned.
	 * 
	 * @param fdec
	 * @param nodeid
	 * @return
	 */
	public FlowPart getContainingFlowpart(FlowDecomposition fdec, int nodeid) {
		for (FlowPart fp : fdec.getDecomposition()) {
			for (EFGFlyweight efw : fp.getSubparts()) {
				if (efw.containsNode(nodeid)) {
					return fp;
				}
			}
		}
		return null;

	}

}
