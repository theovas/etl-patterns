package graphUtils;

import importXLM.ImportXLMToETLGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import defaultQPatterns.CrosscheckSources;
import defaultQPatterns.FilterLTRandom;
import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;
import patternGeneration.PatternDeployer;
import patternGeneration.RandomDeployment;
import qPatterns.QPattern;
import structures.DataSource;
import structures.IdAttrPair;
import structures.JoinKeys;
import structures.NodeApplPoint;
import utilities.ObjectPair;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.graph.ETLEdge;

/**
 * @author vasileios
 *
 */
public class FlowPathsAnalyzer {
	// IMPORTANT!!! An etl flow can only start with an input source, in other
	// words a source of the DAG will always be an ETL input source!
	public static final HashSet<ETLOperationType> inputSourceTypes = new HashSet<ETLOperationType>();

	// here i fill in the set with allowed operation types as input sources.
	static {
		inputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("FileInput"));
		inputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("TableInput"));
		inputSourceTypes
				.add(ETLOperationType.etlOperationTypes.get("XMLInput"));
		inputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("ExcelInput"));
		inputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("getXMLData"));
	}

	public static final HashSet<ETLOperationType> outputSourceTypes = new HashSet<ETLOperationType>();

	// here i fill in the set with allowed operation types as output sources.
	static {
		outputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("FileOutput"));
		outputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("TableOutput"));
		outputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("XMLOutput"));
		outputSourceTypes.add(ETLOperationType.etlOperationTypes
				.get("ExcelOutput"));
	}

	public static final HashSet<ETLOperationType> mergingTypes = new HashSet<ETLOperationType>();

	// here i fill in the set with operation types that are merging flow.
	static {
		mergingTypes.add(ETLOperationType.etlOperationTypes.get("Join"));
		mergingTypes.add(ETLOperationType.etlOperationTypes.get("Union"));
		mergingTypes.add(ETLOperationType.etlOperationTypes
				.get("LeftOuterJoin"));
		mergingTypes.add(ETLOperationType.etlOperationTypes
				.get("FullOuterJoin"));
	}

	/**
	 * this hashset includes all the merging operation types, of operators that
	 * require input from multiple input flows in order to "pass" a result to
	 * the output flows
	 */
	public static final HashSet<ETLOperationType> mergingReqMultiInputTypes = new HashSet<ETLOperationType>();

	// here i fill in the set with operation types that are merging flow.
	static {
		mergingReqMultiInputTypes.add(ETLOperationType.etlOperationTypes
				.get("Join"));
		// mergingTypes.add(ETLOperationType.etlOperationTypes.get("Union"));
		mergingReqMultiInputTypes.add(ETLOperationType.etlOperationTypes
				.get("LeftOuterJoin"));
	}

	public static final HashSet<ETLOperationType> splittingTypes = new HashSet<ETLOperationType>();

	// here i fill in the set with allowed operation types as input sources.
	static {
		splittingTypes.add(ETLOperationType.etlOperationTypes.get("Filter"));
		splittingTypes.add(ETLOperationType.etlOperationTypes.get("Splitter"));
		splittingTypes.add(ETLOperationType.etlOperationTypes.get("Router"));
	}
	
	public static final HashSet<ETLOperationType> routingTypes = new HashSet<ETLOperationType>();

	// here i fill in the set with allowed operation types as input sources.
	static {
		// !!!
		//
		// !!! ATTENTION !!! 
		// commented out the filter as an operation just for the experiments!!!
		//
		//
		//
		//routingTypes.add(ETLOperationType.etlOperationTypes.get("Filter"));
		routingTypes.add(ETLOperationType.etlOperationTypes.get("Router"));
	}

	public static void main(String[] args) {
		ETLFlowGraph efg = new ETLFlowGraph();
		ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
		// ImportKTRToETLGraph ik2g = new ImportKTRToETLGraph();
		System.out.println("starting...");
		try {
			efg = ix2g.getFlowGraph("tests/etl-initial_agn-2.xml");
			
			
			NodeApplPoint nap = new NodeApplPoint();
			nap.setEfGraph(efg);
			//System.out.println(efg.getEtlFlowOperations().values().iterator().next().getOperationName());
			nap.setEfOperator(efg.getEtlFlowOperations().values().iterator().next());
			System.out.println(nap.getEfOperator());
			PatternDeployer pd = new PatternDeployer();
			pd.setEfgraph(efg);
			ArrayList<QPattern> activePs = new ArrayList<QPattern>();
//			FilterNullValues filterNV = new FilterNullValues();
			FilterLTRandom filterLTR = new FilterLTRandom();
			JoinKeys joinKeys = new JoinKeys();
//			// here i arbitrarily chose l_suppkey  
			Attribute attr = efg.getEtlFlowOperations().values().iterator().next().getInputSchemata().iterator().next().getAttributes().get(0);
			System.out.println("attr to be used as key: "+attr);
			IdAttrPair iap = new IdAttrPair(-1, attr);
			joinKeys.addKey(iap );
			ArrayList<DataSource> alternativeSources = new ArrayList<DataSource>();
			DataSource e = new DataSource("d/d/d/d/d/d", OperationTypeName.FileInput, nap.getRefProperties().getOutSchemata().iterator().next());
			System.out.println("filepath: "+e.getFilepath());
			alternativeSources.add(e);
			CrosscheckSources cross = new CrosscheckSources(alternativeSources, joinKeys);
//			//cross.setMaxNumberOfOccurencesInSameFlow(50);
//			//filterNV.setMaxNumberOfOccurencesInSameFlow(50);

//			// filterTstP.setRangeMin(5);
//			// filterTstP.setRangeMax(10);
			activePs.clear();
			activePs.add(cross);
//			activePs.add(filterLTR);
			pd.setActiveQPatterns(activePs);
//			ExhaustiveDeployment randomP = new ExhaustiveDeployment();
			RandomDeployment randomP = new RandomDeployment();
			ETLFlowGraph efgAddedFltr = pd.deployPatterns(randomP);
			System.out.println("one join: "+efgAddedFltr.toStringJSONNames());
			activePs.clear();
			cross = new CrosscheckSources(alternativeSources, joinKeys);
			activePs.add(cross);
//			activePs.add(filterLTR);
			pd.setActiveQPatterns(activePs);
//			ExhaustiveDeployment randomP = new ExhaustiveDeployment();
			//RandomDeployment randomP = new RandomDeployment();
			pd.setEfgraph(efgAddedFltr);
			ETLFlowGraph efg4 = pd.deployPatterns(randomP);
			System.out.println("two joins: "+efg4.toStringJSONNames());
			
			activePs.clear();
			cross = new CrosscheckSources(alternativeSources, joinKeys);
			activePs.add(cross);
//			activePs.add(filterLTR);
			pd.setActiveQPatterns(activePs);
//			ExhaustiveDeployment randomP = new ExhaustiveDeployment();
			//RandomDeployment randomP = new RandomDeployment();
			pd.setEfgraph(efg4);
			ETLFlowGraph efg5 = pd.deployPatterns(randomP);
			System.out.println("three joins: "+efg5.toStringJSONNames());
			
			activePs.clear();
			cross = new CrosscheckSources(alternativeSources, joinKeys);
			activePs.add(cross);
//			activePs.add(filterLTR);
			pd.setActiveQPatterns(activePs);
//			ExhaustiveDeployment randomP = new ExhaustiveDeployment();
			//RandomDeployment randomP = new RandomDeployment();
			pd.setEfgraph(efg5);
			ETLFlowGraph efg6 = pd.deployPatterns(randomP);
			System.out.println("four joins: "+efg6.toStringJSONNames());
			
			pd.setEfgraph(efg6);
			ETLFlowGraph efgAdded2Fltr = efg6;
			ETLFlowGraph efg3;
			for (int i42=0;i42<8;i42++){
				activePs.clear();
			FilterLTRandom filterLTR2 = new FilterLTRandom();
			activePs.add(filterLTR2);
			pd.setEfgraph(efgAdded2Fltr);
			efg3 = pd.deployPatterns(randomP);
			efgAdded2Fltr = efg3;
			}
			System.out.println("2: "+efgAdded2Fltr.toStringJSONNames());
//			System.out.println("3: "+efgAdded2Fltr.toStringJSONNames());
			
			long start = System.currentTimeMillis();
			FlowPathTraces fpc = FlowPathsAnalyzer.generatePathTraces(efgAdded2Fltr);
//			DataGenPattern dgp = constructDataGenPattern(fpc.getFinalPTs().get(0), fpc);
			long patTime = System.currentTimeMillis();
			long elapsedTimeMillis1 = patTime - start;
//			dgp.generateRanges();
			long rangeTime = System.currentTimeMillis();
			long elapsedTimeMillis2 = rangeTime - patTime;
			// System.out.println(fpc.toString());
			System.out.println("pattern in: " + elapsedTimeMillis1 + "ms and ranges in: "+elapsedTimeMillis2 + "ms");
			System.out.println("total: "+(elapsedTimeMillis1+elapsedTimeMillis2));
			System.out.println("an graph: "+efgAdded2Fltr.toStringJSONNames());
			// FlowPathsAnalyzer.gatherAlternativePaths(fpc.getFinalPTs().get(0),fpc);

			// efg = ix2g.getFlowGraph("tests/flow1_simple.xml");
			// efg = ix2g.getFlowGraph("tests/flow8StreamLookup_agn.xml");
			// efg = ix2g.getFlowGraph("tests/flow2_complex.xml");
			// System.out.println(efg.toStringJSON());
			// Iterator it = efg.iterator();
			// while (it.hasNext()) {
			// System.out.println(efg.getEtlFlowOperations().get(it.next()));
			// }

			// System.out.println(efg.iterator().next());
			// System.out.println(efg.iterator().next());

		} catch (CycleFoundException e) { // TODO Auto-generated catch block
			System.out.println("cycle found exception");
			e.printStackTrace();
		} finally {
			System.out.println("finally");
		}
	}

	/**
	 * The actual Bijoux algorithm for path extraction. This method returns a
	 * FlowPathTraces object that includes all the final path traces and all the
	 * edges with their tagged path traces.
	 * 
	 * @param efg
	 * @return
	 */
	public static FlowPathTraces generatePathTraces(ETLFlowGraph efg) {
		// iteration of efg in topological order
		FlowPathTraces fpts = new FlowPathTraces();
		fpts.setEfg(efg);
		Iterator it = efg.iterator();
		int order = 0;
		while (it.hasNext()) {
			order++;
			ETLFlowOperation op = (ETLFlowOperation) efg.getEtlFlowOperations()
					.get(it.next());
			fpts.getTopoIndx().put(op.getNodeID(), order);
			System.out.println("o id: " + op.getNodeID());
			if (inputSourceTypes.contains(op.getOperationType())) {
				Set<ETLEdge> outEdges = efg.outgoingEdgesOf(op.getNodeID());
				// this is an indexing variable to reference the i-th route in
				// case of a splitter
				// int i = 0;
				for (ETLEdge e : outEdges) {
					// i++;
					PathTrace pt = new PathTrace();
					System.out.println("id: " + pt.getId());
					ArrayList<TracePart> tps = generateTraceParts(op,
							e.toString(), "");
					pt.addPartsToTrace(tps);
					ArrayList<PathTrace> pts = new ArrayList<PathTrace>();
					pts.add(pt);
					fpts.addEdgeAndPathTraces(e.toString(), pts);
				}
			} else {
				Set<ETLEdge> inEdges = efg.incomingEdgesOf(op.getNodeID());
				ArrayList<ObjectPair<PathTrace, String>> unionPts = new ArrayList<ObjectPair<PathTrace, String>>();
				for (ETLEdge e : inEdges) {
					ArrayList<PathTrace> pts = fpts.getPathTracesOfEdge(e);
					for (PathTrace pt : pts) {
						String EdgeId = e.toString();
						ObjectPair<PathTrace, String> objP = new ObjectPair(pt,
								EdgeId);
						unionPts.add(objP);
					}
				}
				if (outputSourceTypes.contains(op.getOperationType())) {
					for (ObjectPair<PathTrace, String> ptS : unionPts) {
						PathTrace pt2 = ptS.left.clone();
						pt2.addPartsToTrace(generateTraceParts(op, "",
								ptS.right));
						pt2.setFinalPath(true);
						fpts.addToFinalPTs(pt2);
					}

				} else {
					Set<ETLEdge> outEdges = efg.outgoingEdgesOf(op.getNodeID());

					// this is an indexing variable to reference the i-th route
					// in
					// case of a splitter
					// int i = 0;
					for (ETLEdge e : outEdges) {
						// i++;
						ArrayList<PathTrace> pts = new ArrayList<PathTrace>();
						for (ObjectPair<PathTrace, String> ptS : unionPts) {
							PathTrace pt2 = ptS.left.clone();
							System.out.println("id clone: " + pt2.getId());
							pt2.addPartsToTrace(generateTraceParts(op,
									e.toString(), ptS.right));
							pts.add(pt2);
						}
						fpts.addEdgeAndPathTraces(e.toString(), pts);

					}
				}

			}
		}
		fpts.updatePathSignatures();
		return fpts;

	}

	private static ArrayList<TracePart> generateTraceParts(ETLFlowOperation op,
			String ithRoute, String edgeOrigin) {
		ArrayList<TracePart> tps = new ArrayList<TracePart>();
		if (inputSourceTypes.contains(op.getOperationType())) {
			TracePart tp1 = new TracePart(TracePart.TracePartType.sourceOp,
					"I[" + Integer.toString(op.getNodeID()) + "]");
			tps.add(tp1);
		}
		if (mergingTypes.contains(op.getOperationType())) {
			TracePart tp = new TracePart(TracePart.TracePartType.mergingOp,
					"M[" + Integer.toString(op.getNodeID()) + "]");
			TracePart tp2 = new TracePart(TracePart.TracePartType.edgeOrigin,
					"e[" + edgeOrigin + "]");
			// "M["+op.getOperationName()+"]");
			tps.add(tp);
			tps.add(tp2);
		} else if (splittingTypes.contains(op.getOperationType())) {
			TracePart tp1 = new TracePart(TracePart.TracePartType.splittingOp,
					"S[" + Integer.toString(op.getNodeID()) + "]");
			// "S["+op.getOperationName()+"]");
			TracePart tp2 = new TracePart(TracePart.TracePartType.route, "e["
					+ ithRoute + "]");
			tps.add(tp1);
			tps.add(tp2);
		}
		if (outputSourceTypes.contains(op.getOperationType())) {
			TracePart tp1 = new TracePart(TracePart.TracePartType.sinkOp, "O["
					+ Integer.toString(op.getNodeID()) + "]");
			tps.add(tp1);
		}
		return tps;
	}

	/**
	 * This method gathers all the alternative paths in cases of merging
	 * operators
	 * 
	 * @param pathTrace
	 * @param fpts
	 * @return
	 */
	public static AlternativePathFlows gatherAlternativePaths(
			PathTrace pathTrace, FlowPathTraces fpts) {
		// DataGenPattern dgp = new DataGenPattern();
		AlternativePathFlows apf = new AlternativePathFlows();
		apf.setExaminedPathTrace(pathTrace);
		ETLFlowGraph efg = fpts.getEfg();
		apf.setEfg(efg);
		ETLFlowOperation sinkNode = null;
		Set<ETLEdge> eset = efg.edgeSet();
		for (ETLEdge e : eset) {
			ArrayList<PathTrace> pts = fpts.getPathTracesOfEdge(e);
			for (PathTrace pt : pts) {
				if (pt.isIncludedLeftIn(pathTrace)) {
					ETLFlowOperation srcNode = efg.getEtlFlowOperations().get(
							e.getSource());
					ETLFlowOperation trgNode = efg.getEtlFlowOperations().get(
							e.getTarget());
					// here i must take care of the merging operators
					if (mergingReqMultiInputTypes.contains(trgNode
							.getOperationType())) {
						for (PathTrace fpt : fpts.getFinalPTs()) {
							if ((fpt.contains("M["
									+ Integer.toString(trgNode.getNodeID())
									+ "]"))
									&& (!fpt.contains("M["
											+ Integer.toString(trgNode
													.getNodeID()) + "]" + ".e["
											+ e.toString() + "]"))) {
								System.out.println("alt path: " + fpt);//
								apf.addPathToNode(fpt, trgNode);
							}
						}
					}
					// if
					// (outputSourceTypes.contains(trgNode.getOperationType())){
					if (efg.outDegreeOf(trgNode.getNodeID()) == 0) {
						sinkNode = trgNode;
					}
				}
			}
		}
		System.out.println("all alt paths: " + apf.toString());
		return apf;

	}



	

}
