package flowAnalysis;

import graphUtils.LabelledEdge;
import graphUtils.LabelledNode;
import importXLM.ImportXLMToETLGraph;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import operationDictionary.ETLOperationType;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.HashBag;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.jgrapht.experimental.equivalence.EquivalenceComparator;
import org.jgrapht.experimental.isomorphism.AdaptiveIsomorphismInspectorFactory;
import org.jgrapht.experimental.isomorphism.GraphIsomorphismInspector;

import com.opencsv.CSVWriter;

import drivers.ImportPDIExportXML;
import structPatterns.CandidatePatternMatch;
import structPatterns.IsomorphismTester;
import structPatterns.LabelBasedNodeComparator;
import structPatterns.PatternNode;
import structPatterns.StructPattern;
import structPatterns.StructPatternAnalyzer;
import structPatterns.StructPatternParser;
import structPatterns.TestFlow;
import structPatterns.TestFlows;
import structPatterns.UniformEdgeComparator;
import utils.IOProcessor;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import patternRecognitionExperiments.TestExecutor;

/**
 * A class that contains methods for transforming the EFG to equivalent graph
 * models for example to be compliant for processing from various tools.
 * 
 * @author vasileios
 *
 */
public final class GraphTransformations {
	static String eol = System.getProperty("line.separator");
	static String newOld = "Om";

	// a private constructor to block instantiation and subclassing
	private GraphTransformations() {
	}

	public static void main(String[] args) {
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minAcc-test.fp");
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minBro-test.fp");
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minCom-test.fp");
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minCus-test.fp");
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minFac-test.fp");
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minFin-test.fp");
		// TestExecutor
		// .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/50/"
		// + "opType-minSec-test.fp");
		// ======================================================================================
		// EquivalenceComparator vertexChecker = new LabelBasedNodeComparator();
		// EquivalenceComparator edgeChecker = new UniformEdgeComparator();
		// //Graph g1 = graphs[0];
		// StructPattern g1 = new StructPattern();
		// g1.addNode(new PatternNode("label1"));
		// StructPattern g2 = new StructPattern();
		// g2.addNode(new PatternNode("label1"));
		// //Graph g2 = graphs[1];
		// System.out.println("g1:" + g1);
		// System.out.println("g2:" + g2);
		// long beforeTime = System.currentTimeMillis();
		// GraphIsomorphismInspector iso =
		// AdaptiveIsomorphismInspectorFactory.createIsomorphismInspector(
		// g1,
		// g2,
		// vertexChecker,
		// edgeChecker);
		// boolean isoResult = iso.isIsomorphic();
		// System.out.println("is isomorphic: "+isoResult);

		// ImportPDIExportXML
		// .importPDItoXML("/Users/btheo/Dropbox/anton/tpcdi/etls/xlmTranslatable/complete/IncrementalUpdates/UpdateFactHoldings");
		LabellingStrategy lbOpType = new LabelBasedOnOpType();
		GraphTransformations.createGraphTranFile(
				"complete/UpdateFactHoldings_agn", lbOpType);
		/*
		 * ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph(); for (TestFlow
		 * tf : TestFlows.values()) { if (tf.getFlowId() == 206) { try {
		 * List<String> lines = new ArrayList<String>(); Path file =
		 * Paths.get("tests/noPatternMatches/" +
		 * "expResults-fid206-Om_opType_50.0_all2.txt");
		 * 
		 * // PrintWriter writer = new PrintWriter( // "tests/noPatternMatches/"
		 * // + "expResults-fid0-O_opType_30.0.txt", // "UTF-8"); ETLFlowGraph
		 * efg = ix2g.getFlowGraph(tf.getFilepath()); // // ETLFlowGraph efg =
		 * ix2g // // //
		 * .getFlowGraph("tests/tpcdi/complete/DimAccount_agn.xml"); // for
		 * (ETLFlowOperation efo : efg.getEtlFlowOperations() // .values()) { //
		 * System.out.println(efo); // } // ArrayList<StructPattern> sps =
		 * GraphTransformations //
		 * .extractStructPatternsFromFile("tests/tpcdi/pafiResults/30/" // // +
		 * "IOnarity-schemaDiff-test-40.fp"); // + "opType-test-30-max.fp");
		 * ArrayList<LabellingStrategy> labels = new
		 * ArrayList<LabellingStrategy>(); labels.add(new
		 * LabelBasedOnIONarity()); labels.add(new LabelBasedOnSchemaDiff());
		 * ArrayList<ArrayList<CandidatePatternMatch>> matchesall = new
		 * ArrayList<ArrayList<CandidatePatternMatch>>(); String expType =
		 * "O_opType_40.0"; ArrayList<StructPattern> sps = StructPatternParser
		 * .readStructPatternsFromMongoDB(expType);
		 * System.out.println("size of id set: " +
		 * StructPatternAnalyzer.getNodeIdSet(sps).size()); for (StructPattern
		 * sp : sps) { ArrayList<CandidatePatternMatch> matches =
		 * IsomorphismTester .getAllPatternMatches(sp, efg, // new
		 * LabelBasedOnCombination(labels)); new LabelBasedOnOpType());
		 * matchesall.add(matches);
		 * 
		 * } System.out.println("size of id set for all matches: " +
		 * StructPatternAnalyzer .getNodeIdSetFromAllPatterns(matchesall)
		 * .size()); System.out.println("size of graph: " +
		 * efg.getEtlFlowOperations().entrySet().size()); for
		 * (ArrayList<CandidatePatternMatch> cpms : matchesall) { if
		 * (cpms.size() > 0) { System.out.println("size of pattern: " +
		 * ((CandidatePatternMatch) cpms.iterator()
		 * .next()).getPattNodes().size()); lines.add("pattern size: " +
		 * ((CandidatePatternMatch) cpms.iterator()
		 * .next()).getPattNodes().size()); System.out.println("pattern: ");
		 * System.out.println(((CandidatePatternMatch) cpms
		 * .iterator().next()).getPatternModel() .toLabelGraphDescr());
		 * System.out.print("pattern id: ");
		 * System.out.println(((CandidatePatternMatch) cpms
		 * .iterator().next()).getPatternModel() .getPattId());
		 * System.out.println("number of matches: " + cpms.size());
		 * lines.add("number of matches: " + cpms.size());
		 * System.out.println("------------------"); } } // String csv =
		 * "data.csv"; // CSVWriter writer = new CSVWriter(new FileWriter(csv));
		 * // // // Create record // String[] record = //
		 * "4,David,Miller,Australia,30".split(","); // // Write the record to
		 * file // writer.writeNext(record); // writer.writeNext(record); //
		 * String[] record2 = "4,David,Miller,Australia,30,5,4,3" //
		 * .split(","); // writer.writeNext(record2); // // close the writer //
		 * writer.close(); // // String csv2 = "data.csv"; // CSVWriter writer2
		 * = new CSVWriter( // new FileWriter(csv2, true)); // // String[]
		 * record3 = "3,David,Feezor,USA,40".split(","); // //
		 * writer2.writeNext(record3); // // writer2.close();
		 * 
		 * // TestExecutor //
		 * .serializeAndStoreStructPatternsFromFile("tests/tpcdi/pafiResults/100/"
		 * // + "IOnarityAndSchemaDiff-test-100-max.fp"); // String expType =
		 * "O_opType_100.0"; // System.out.println("size: " // +
		 * StructPatternParser // .readStructPatternsFromMongoDB(expType) //
		 * .size());
		 * 
		 * // + "opType-test-100-max.fp"); Files.write(file, lines,
		 * Charset.forName("UTF-8")); }
		 * 
		 * catch (CycleFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } // catch (IOException e) { // // TODO
		 * Auto-generated catch block // e.printStackTrace(); // } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (UnsupportedEncodingException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } }
		 */

		// .getFlowGraph("tests/tpcdi/dimAccount_agn.xml");
		// for (TestFlow tf : TestFlows.values()) {
		// try {
		// ETLFlowGraph efg = ix2g.getFlowGraph(tf.getFilepath());
		// System.out.println("- Test flow being tested: "
		// + tf.getFilepath());
		// // .getFlowGraph("tests/tpcdi/dimAccount_agn.xml");
		// // .getFlowGraph("tests/tpcdi/dimSecurity_agn.xml");
		// // Iterator it = efg.iterator();
		// // while (it.hasNext()) {
		// // System.out.println(efg.getEtlFlowOperations().get(it.next()));
		// // }
		//
		// // System.out.println("node id for regex:"
		// // + efg.getEtlFlowOperationByName(
		// // "DimSecurity.Regex Evaluation").getNodeID());
		// // System.out.println("neighbour nodes:"
		// // + efg.getEtlFlowOperations().get(265).getOperationType()
		// // + " "
		// // + efg.getEtlFlowOperations().get(290).getOperationType());
		// // Bag labels = new HashBag();
		// // labels.add("Filter");
		// // labels.add("pl");
		// // // labels.add("Filter");
		// // StructPattern efgSp =
		// // GraphTransformations.efgToStructPattern(efg,
		// // new LabelBasedOnOpType());
		// // System.out.println("this is it: " + efgSp);
		// // PatternNode pn = efgSp.getPattNodes().get(422);
		// // System.out.println("pn: " + pn);
		// // System.out.println("is connected: "
		// // + pn.checkIfConnectedToNodesWithLabels(labels));
		// // System.out.println("the dimsecurity efg:");
		// // System.out.println(efg);
		// ArrayList<StructPattern> sps = GraphTransformations
		// .extractStructPatternsFromFile("tests/tpcdi/pafiResults/"
		// + "opType-test.fp");
		// ArrayList<ArrayList<CandidatePatternMatch>> allMatches = new
		// ArrayList<ArrayList<CandidatePatternMatch>>();
		// for (StructPattern sp : sps) {
		// // StructPattern sp = sps.get(sps.size() - 1);
		// System.out
		// .println("pattern model that is being checked for its recognition on a efg: "
		// + sp);
		// ArrayList<CandidatePatternMatch> matches = IsomorphismTester
		// .getAllPatternMatches(sp, efg,
		// new LabelBasedOnOpType());
		// System.out.println("number of matches found: "
		// + matches.size());
		// for (CandidatePatternMatch cpm : matches) {
		// System.out.println(cpm);
		// }
		// allMatches.add(matches);
		// }
		// for (ArrayList<CandidatePatternMatch> mtch : allMatches) {
		// System.out.println("size: " + mtch.size());
		// }
		// Iterator it = efg.iterator();
		// while (it.hasNext()) {
		// int next = (int) it.next();
		// if (((ETLFlowOperation) efg.getEtlFlowOperations()
		// .get(next)).getOperationType() == ETLOperationType.etlOperationTypes
		// .get("Join")) {
		// System.out.println(next);
		// System.out.println(((ETLFlowOperation) efg
		// .getEtlFlowOperations().get(next))
		// .getOperationName());
		// }
		// }
		//
		// } catch (CycleFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }

		// ETLFlowGraph efg = new ETLFlowGraph();
		// ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
		// try {
		// efg = ix2g.getFlowGraph("tests/etl-initial_agn.xml");
		// // efg = ix2g.getFlowGraph("tests/flow1_simple.xml");
		// // efg = ix2g.getFlowGraph("tests/flow8StreamLookup_agn.xml");
		// // efg = ix2g.getFlowGraph("tests/flow2_complex.xml");
		// Iterator it = efg.iterator();
		// while (it.hasNext()) {
		// System.out.println(efg.getEtlFlowOperations().get(it.next()));
		// }
		// System.out.println(toGraphTranStringUnlabelled(efg));
		//
		// System.out.println(efg);
		//
		// } catch (CycleFoundException e) { // TODO Auto-generated catch block
		// System.out.println("what?3");
		// e.printStackTrace();
		// } finally {
		// System.out.println("what?4");
		// }
	}

	/**
	 * Transform the etl flow graph to a string that can be input for the FSG
	 * program of the PAFI Software Package (available here:
	 * "http://glaros.dtc.umn.edu/gkhome/pafi/download"). This method prints the
	 * same label for every node ("un") and the same label for every edge
	 * ("ue"), which is equivalent to no labelling.
	 * 
	 * @param efg
	 * @return
	 */
	public static String toGraphTranStringUnlabelled(ETLFlowGraph efg) {
		String gtf = "t"
		// +"\#"+"comments"
				+ eol;

		ArrayList<LabelledNode> vs = new ArrayList<LabelledNode>();

		Iterator it = efg.iterator();
		while (it.hasNext()) {
			ETLFlowOperation efo = (ETLFlowOperation) efg
					.getEtlFlowOperations().get(it.next());

			// do stuff here...

			vs.add(new LabelledNode(efo.getNodeID(), "un"));

		}
		Collections.sort(vs);
		for (LabelledNode ln : vs) {
			gtf += "v " + ln.getNodeId() + " " + ln.getNodeLabel() + eol;
		}

		ArrayList<LabelledEdge> es = new ArrayList<LabelledEdge>();

		Iterator it2 = efg.edgeSet().iterator();
		while (it2.hasNext()) {
			ETLEdge ee = (ETLEdge) it2.next();

			// do stuff here...

			es.add(new LabelledEdge((int) ee.getSource(), (int) ee.getTarget(),
					"ue"));

		}
		Collections.sort(es);
		for (LabelledEdge le : es) {
			gtf += "u " + le.getSrcNodeId() + " " + le.getTrgNodeId() + " "
					+ le.getNodeLabel() + eol;
		}

		// gtf += "v "+Integer.toString(efo.getNodeID()) + " " + "un" + eol;
		return gtf;
	}

	/**
	 * Transform the etl flow graph to a string that can be input for the FSG
	 * program of the PAFI Software Package (available here:
	 * "http://glaros.dtc.umn.edu/gkhome/pafi/download"). This method prints a
	 * label for every node according to the labeling strategy that is taken as
	 * an argument and the same label for every edge ("ue"), which is equivalent
	 * to no labelling for edges.
	 * 
	 * @param efg
	 * @return
	 */
	public static String toGraphTranString(ETLFlowGraph efg,
			LabellingStrategy lbst) {
		String gtf = "t"
		// +"\#"+"comments"
				+ eol;

		ArrayList<LabelledNode> vs = new ArrayList<LabelledNode>();

		Iterator it = efg.iterator();
		String label = "un";
		while (it.hasNext()) {
			ETLFlowOperation efo = (ETLFlowOperation) efg
					.getEtlFlowOperations().get(it.next());

			// label = lbst.labelNode(efo, efg).getNodeLabel();

			vs.add(lbst.labelNode(efo, efg));

		}
		Collections.sort(vs);
		for (LabelledNode ln : vs) {
			gtf += "v " + ln.getNodeId() + " " + ln.getNodeLabel() + eol;
		}

		ArrayList<LabelledEdge> es = new ArrayList<LabelledEdge>();

		Iterator it2 = efg.edgeSet().iterator();
		while (it2.hasNext()) {
			ETLEdge ee = (ETLEdge) it2.next();

			// do stuff here...

			es.add(new LabelledEdge((int) ee.getSource(), (int) ee.getTarget(),
					"ue"));

		}
		Collections.sort(es);
		for (LabelledEdge le : es) {
			gtf += "u " + le.getSrcNodeId() + " " + le.getTrgNodeId() + " "
					+ le.getNodeLabel() + eol;
		}

		// gtf += "v "+Integer.toString(efo.getNodeID()) + " " + "un" + eol;
		return gtf;
	}

	public static void createGraphTranFile(String efgName,
			LabellingStrategy lbst) {
		ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
		ETLFlowGraph efg;
		try {
			efg = ix2g.getFlowGraph("tests/tpcdi/" + efgName + ".xml");

			String gString = toGraphTranString(efg, lbst);
			IOProcessor.writeStringToFile(gString, "tests/tpcdi/graphTran/"
					+ efgName + "-" + lbst.getLabel() + ".txt");
			System.out.println(gString);
			System.out
					.println("File: " + efgName + "-" + lbst.getLabel()
							+ ".txt"
							+ " was created in tests/tpcdi/graphTran/complete");
		} catch (CycleFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Read an output fp file (e.g., "tests/tpcdi/pafiResults/opType-test.fp")
	 * from the the FSG program of the PAFI Software Package (available here:
	 * "http://glaros.dtc.umn.edu/gkhome/pafi/download") and translate it to a
	 * list of structural pattern objects. These objects are already in
	 * canonical form, lexicographically ordered.
	 * 
	 * 
	 * @param filenameInclPath
	 * @return
	 */
	public static ArrayList<StructPattern> extractStructPatternsFromFile(
			String filenameInclPath) {
		String flStr = IOProcessor.readFileToString(filenameInclPath);
		ArrayList<StructPattern> spatterns = new ArrayList<StructPattern>();
		String similThres = "0";
		String similThres2 = "";
		String labelStr = filenameInclPath.substring(
				filenameInclPath.lastIndexOf("/") + 1,
				filenameInclPath.indexOf("-"));

		String excluding = "none";
		excluding = filenameInclPath.substring(
				filenameInclPath.indexOf("-") + 1, filenameInclPath.indexOf(
						"-", filenameInclPath.indexOf("-") + 1));
		if (!excluding.startsWith("min")) {
			excluding = "none";
		}
		String max = "none";
		max = filenameInclPath.substring(filenameInclPath.lastIndexOf("-"));
		if (!max.startsWith("max")) {
			max = "false";
		} else {
			max = "true";
		}
		// escape initial comments
		boolean isComment = true;
		while (isComment) {
			if (flStr.startsWith("#")) {
				if (flStr.startsWith("#   Min Support Threshold:")) {
					flStr = flStr.substring(flStr.indexOf(":") + 1);
					similThres = flStr.substring(0, flStr.indexOf("%"));
					similThres2 = similThres.replaceAll("\\s", "");
					System.out.println("similThres: " + similThres2);
				}
				// System.out.println(flStr);
				System.out.println("escaping comments...");
				flStr = flStr.substring(flStr.indexOf('\n') + 1);
				// System.out.println(flStr);
			} else {
				isComment = false;
			}

		}
		// remove final comments
		if (!flStr.isEmpty()) {
			flStr = flStr.substring(0, flStr.indexOf("#   Size"));
			flStr = flStr.substring(flStr.indexOf("t #") + 1);

			// create one Structural Pattern for each graph
			String[] parts = flStr.split("t #");
			// System.out.println(parts[0]);
			for (String part : parts) {
				// if (!((part.startsWith("v")||part.startsWith("u")))){
				System.out.println("part: " + part);
				StructPattern sp = new StructPattern();
				// set the experiment type of the specific pattern, e.g.,
				// "O_opType_40" for 7 Old flows, based on opType with threshold
				// 40
				if (excluding.equals("none")) {
					sp.setExpType(newOld + "_" + labelStr + "_" + similThres2);
				} else {
					sp.setExpType(newOld + "_" + labelStr + "_" + similThres2
							+ "_" + excluding);
				}
				part = part.substring(part.indexOf("\n") + 1);
				// reading nodes
				int id = -1;
				String label = "";
				while (part.startsWith("v")) {
					// new Patternode
					id = Integer.parseInt(part.substring(2, 3));
					label = part.substring(4, part.indexOf("\n"));
					System.out.println("id node: " + id + " label: " + label);
					PatternNode pn = new PatternNode(label, id);
					sp.addNode(pn);
					// remove line
					part = part.substring(part.indexOf("\n") + 1);

				}
				// reading edges
				int id1 = -1;
				int id2 = -1;
				while (part.startsWith("u")) {
					// new Patteredge
					id1 = Integer.parseInt(part.substring(2, 3));
					id2 = Integer.parseInt(part.substring(4, 5));
					sp.addEdge(id1, id2);
					System.out.println("edge: " + id1 + " " + id2);
					// remove line
					part = part.substring(part.indexOf("\n") + 1);
				}
				System.out.println("sp: " + sp);
				sp.setSize(Integer.toString(sp.edgeSet().size()));
				sp.setLabellingType(labelStr);
				sp.setThreshold(similThres2);
				sp.setIsMax(max);
				spatterns.add(sp);
				// }

			}
		}
		System.out.println("here: " + flStr);
		return spatterns;

	}

	public static StructPattern efgToStructPattern(ETLFlowGraph efg,
			LabellingStrategy labelSt) {
		StructPattern sp = new StructPattern();
		// iterate and label nodes and add them to struct pattern
		Iterator it = efg.iterator();
		while (it.hasNext()) {
			ETLFlowOperation efo = (ETLFlowOperation) efg
					.getEtlFlowOperations().get(it.next());
			PatternNode pn = new PatternNode(labelSt.labelNode(efo, efg)
					.getNodeLabel(), efo.getNodeID());
			sp.addNode(pn);
			// System.out.println("just added node: " + pn.getNodeID()
			// + "corresponding to efowith id: " + efo.getNodeID());
		}
		// iterate and add edges to struct pattern
		Iterator it2 = efg.edgeSet().iterator();
		while (it2.hasNext()) {
			ETLEdge ee = (ETLEdge) it2.next();
			sp.addEdge((int) ee.getSource(), (int) ee.getTarget());

		}
		return sp;

	}

}
