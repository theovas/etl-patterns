package utils;

import importXLM.ImportXLMToETLGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import patternRecognitionExperiments.TestExecutor;
import drivers.ImportPDIExportXML;
import drivers.ImportXLMExportPDI;
import structPatterns.CandidatePatternMatch;
import structPatterns.CandidatePatternMatchNode;
import structPatterns.IsomorphismTester;
import structPatterns.PatternEdge;
import structPatterns.PatternNode;
import structPatterns.StructPattern;
import structPatterns.StructPatternParser;
import structPatterns.TestFlow;
import structPatterns.TestFlows;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;
import flowAnalysis.GraphTransformations;
import flowAnalysis.LabelBasedOnOpType;
import flowAnalysis.LabellingStrategy;

public class IOFlowFilesTester {
	static int inc = 0;
	static String phase = "HistoricalLoad";
	static String dir = "/Users/btheo/Dropbox/anton/tpcdi/etls/xlmTranslatable/complete/";
	static String etl = "DimSecurity";
	static HashSet<String> inputOpTypes = new HashSet<String>();
	static HashSet<String> consistentFlows = new HashSet<String>();
	static ArrayList<Pair> allETLs;
	static ArrayList<StructPattern> allPatterns = new ArrayList<StructPattern>();
	static {
		inputOpTypes.add("CsvInput");
		inputOpTypes.add("TableInput");
		inputOpTypes.add("XMLInputStream");
		inputOpTypes.add("TextFileInput");
		// get all the etl flows as efg in memory
		// allETLs = IOFlowFilesTester.getAllFlows();
		consistentFlows.add("DimCustomer");
		consistentFlows.add("Financial");
		consistentFlows.add("FactCashBalances");
		consistentFlows.add("DimBroker");
		consistentFlows.add("FactHoldings");
		consistentFlows.add("Prospect");
		consistentFlows.add("DimCompany");
		// ----
		ArrayList<StructPattern> sps1 = StructPatternParser.readStructPatternsFromMongoDB("Om_opType_20.0");
		allPatterns.addAll(sps1);
	}

	public static void main(String[] args) {
		System.out.println("wtf is going on?????????!?!?!");
		LabellingStrategy lbs = new LabelBasedOnOpType();
		ArrayList<CandidatePatternMatch> cpms = null;
		ArrayList<Pair> mnsAndPatts = new ArrayList<Pair>();
		CandidatePatternMatchNode originalNd = null;
		HashSet<Integer> allMatchedNodes = new HashSet<Integer>();
		ArrayList<Pair> allFlows = getAllFlows();
		int size = 0;
		for (Pair p : allFlows) {
			size += ((ETLFlowGraph) p.o1).getEtlFlowOperations().size();
		}
		System.out.println("All etls size: " + size);

		/*
		 * for (Pair p : allFlows) { StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_24.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_28.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_32.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_36.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_40.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_44.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_48.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_52.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_56.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_60.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * allPatterns =
		 * StructPatternParser.readStructPatternsFromMongoDB("Om_opType_64.0");
		 * allMatchedNodes = new HashSet<Integer>(); for (Pair p : allFlows) {
		 * StructPattern efgSP =
		 * GraphTransformations.efgToStructPattern((ETLFlowGraph) p.o1, lbs);
		 * for (StructPattern sp : allPatterns) { cpms =
		 * IsomorphismTester.getAllPatternMatches(sp, efgSP); for
		 * (CandidatePatternMatch cpm : cpms) { Collection<PatternNode> pns2 =
		 * cpm.getPattNodes().values(); for (PatternNode pn : pns2) { originalNd
		 * = (CandidatePatternMatchNode) pn;
		 * allMatchedNodes.add(originalNd.getNodeID()); } } } }
		 * 
		 * mnsAndPatts.add(new Pair(allMatchedNodes.size(),
		 * allPatterns.size()));
		 * System.out.println("Number of all matched nodes: " +
		 * allMatchedNodes.size()); System.out.println("number of all models: "
		 * + allPatterns.size());
		 * 
		 * for (Pair p : mnsAndPatts) { System.out.println("nodes: " + p.o1 +
		 * "patterns: " + p.o2); }
		 */
		// analyzePattPerformance();
		// IOFlowFilesTester.iterateOverAllFlows();
		// storeAllPatternsFromAllFPsToMongodb();
		//
		// get all patterns of specific type from mongodb
		//
		// check

		// ArrayList<StructPattern> allPatterns = new
		// ArrayList<StructPattern>();
		// ArrayList<StructPattern> sps1 = StructPatternParser
		// .readStructPatternsFromMongoDB("Om_opType_20.0");
		/*
		 * // check ArrayList<StructPattern> sps2 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_24.0"); // check
		 * ArrayList<StructPattern> sps3 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_28.0"); // check
		 * ArrayList<StructPattern> sps4 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_32.0"); // check
		 * ArrayList<StructPattern> sps5 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_36.0"); // check
		 * ArrayList<StructPattern> sps6 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_40.0"); // check
		 * ArrayList<StructPattern> sps7 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_44.0"); // check
		 * ArrayList<StructPattern> sps8 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_48.0"); // check
		 * ArrayList<StructPattern> sps9 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_52.0"); // check
		 * ArrayList<StructPattern> sps10 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_56.0"); // check
		 * ArrayList<StructPattern> sps11 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_60.0"); // check
		 * ArrayList<StructPattern> sps12 = StructPatternParser
		 * .readStructPatternsFromMongoDB("Om_opType_64.0");
		 */
		// allPatterns.addAll(sps1);
		/*
		 * allPatterns.addAll(sps2); allPatterns.addAll(sps3);
		 * allPatterns.addAll(sps4); allPatterns.addAll(sps5);
		 * allPatterns.addAll(sps6); allPatterns.addAll(sps7);
		 * allPatterns.addAll(sps8); allPatterns.addAll(sps9);
		 * allPatterns.addAll(sps10); allPatterns.addAll(sps11);
		 * allPatterns.addAll(sps12); System.out.println("all patterns size: " +
		 * allPatterns.size()); // writeAllMatchesPerPattToFiles(allPatterns);
		 * 
		 * // keep only distinct patterns allPatterns =
		 * getDistinctPatts(allPatterns);
		 * 
		 * // getAllPatternsDirectionalVariations(allPatterns);
		 * 
		 * // create a csv with all the pattern occurrences for each flow. //
		 * createPattOccurrencesCsv(allPatterns, "results");
		 * 
		 * StructPattern sp1 = null; StructPattern sp2 = null;
		 * 
		 * for (StructPattern sp : allPatterns) { if (sp.getPattId() == 61) {
		 * sp1 = sp; } if (sp.getPattId() == 139) { sp2 = sp; } } if ((sp1 !=
		 * null) && (sp2 != null)) { System.out.println("61: " +
		 * sp1.toLabelGraphDescr()); System.out.println("139: " +
		 * sp2.toLabelGraphDescr()); }
		 * 
		 * 
		 * // writeAllMatchesPerPattToFilesInclOpNames(allPatterns);
		 * //------------------------------------------------------------------
		 * ArrayList<SubflowToTest> subflows =
		 * getSubflowsFromPatternOccurenceFile(
		 * "/Users/btheo/Desktop/Vasileios/Papers/etlQualityPatterns/experiments/patterns4testing/1.csv"
		 * ); for (SubflowToTest sf : subflows) { System.out.println("fid:" +
		 * sf.getFlowId()); System.out.println("poid:" +
		 * sf.getPatternOccurenceId()); System.out.println("pid:" +
		 * sf.getPatternId()); System.out.println("subflow: " + sf.getSp()); for
		 * (Integer key : sf.getSp().getPattNodes().keySet()) {
		 * System.out.println(sf.getSp().getPattNodes().get(key) .getLabel()); }
		 * }
		 * 
		 * //-------------------------------------------------------------------
		 * - ---------
		 * 
		 * StructPattern subflowExample = new StructPattern(); PatternNode pn1 =
		 * new PatternNode("Filter rows", 1); PatternNode pn2 = new
		 * PatternNode("Regex Evaluation", 2); PatternNode pn = new
		 * PatternNode("input", 1); subflowExample.addNode(pn1);
		 * subflowExample.addNode(pn2); // subflowExample.addNode(pn);
		 * subflowExample.addEdge(1, 2); //
		 * getFlowSubpartAsKTR("/Users/btheo/Desktop/DimSecurity.ktr", //
		 * subflowExample, "tests/subflows/", "tests/templates/"); //
		 * ------------------------------------------------- ImportXLMToETLGraph
		 * ix2g = new ImportXLMToETLGraph(); // get the input text file template
		 * as etl // flow graph // read the text input etl template, //
		 * transform it to xlm String tieXlm =
		 * ImportPDIExportXML.importPDItoXML("tests/templates/" +
		 * "text-input-text-output-etl");
		 * System.out.println("transformed to xlm..."); //
		 * System.out.println(tieXlm); System.out.println(".......");
		 * ETLFlowGraph efgIn = ETLTransformer
		 * .xlmToEfg("tests/templates/text-input-text-output-etl_agn.xml");
		 * System.out.println("efg: " + efgIn); for (ETLFlowOperation eo :
		 * efgIn.getEtlFlowOperations().values()) {
		 * System.out.println("printing ops: "); System.out.println(eo); } //
		 * ix2g.getFlowGraph(templatesDir // + "text-input-etl_agn.xml");
		 * IOProcessor.writeStringToFile(ETLTransformer.efgToXlm(efgIn),
		 * "tests/templates/efgInAsXlm.xml"); // export as xlm String efgInAsKtr
		 * = ImportXLMExportPDI
		 * .importXLMtoPDI("tests/templates/efgInAsXlm.xml");
		 * IOProcessor.writeStringToFile(efgInAsKtr,
		 * "tests/templates/efgInAsKtr.ktr");
		 */

		// -----------------------------------------------------------

		// System.out.println("131: ");
		// System.out.println(getStructPattFromId(allPatterns, 131));
		// System.out.println("132: ");
		// System.out.println(getStructPattFromId(allPatterns, 132));

		// ImportPDIExportXML.importPDItoXML(dir + phase + "/" + etl);
		// -------------------------------

		// ---

		// File dir = new File("tests/tpcdi/complete/");
		// File[] directoryListing = dir.listFiles();
		// if (directoryListing != null) {
		// for (File child : directoryListing) {
		//
		// try {
		// ETLFlowGraph efg = ix2g.getFlowGraph(child.getPath());
		// // for (ETLFlowOperation efo :
		// // efg.getEtlFlowOperations().values()) {
		// // System.out.println(efo);
		// // }
		//
		// System.out.println(child.getName() + ": "
		// + efg.getEtlFlowOperations().size());
		// } catch (CycleFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// } else {
		// // Handle the case where dir is not really a directory.
		// // Checking dir.isDirectory() above would not be sufficient
		// // to avoid race conditions with another process that deletes
		// // directories.
		// }

	}

	public static HashMap<Integer, ArrayList<Pair>> getAllPatternsDirectionalVariations(
			ArrayList<StructPattern> allPatterns) {
		HashMap<Integer, ArrayList<Pair>> patternsDirectionalVariations = new HashMap<Integer, ArrayList<Pair>>();
		// ArrayList<Pair> allETLs = IOFlowFilesTester.getAllFlows();
		HashMap<Integer, ETLFlowGraph> etls = new HashMap<Integer, ETLFlowGraph>();
		for (Pair p : allETLs) {
			etls.put(((TestFlow) p.o2).getFlowId(), (ETLFlowGraph) p.o1);
		}

		ArrayList<Pair> pVars;
		ArrayList<CandidatePatternMatch> allMatches = null;
		boolean found;
		for (StructPattern sp : allPatterns) {
			allMatches = null;
			PatternInstancesDetailed pistDtl = readPatternInstancesDetailed(sp.getPattId());
			pVars = new ArrayList<Pair>();
			for (int flowId : pistDtl.getPattInstancesOnFlows().keySet()) {
				for (CandidatePatternMatch cpm : pistDtl.getPattInstancesOnFlows().get(flowId)) {
					if (pVars.size() == 0) {
						pVars.add(new Pair(cpm.getDirectionalPModel(etls.get(flowId)), 1));
					} else {
						found = false;
						for (Pair sp2 : pVars) {
							if (cpm.getDirectionalPModel(etls.get(flowId)).equalsDirected((StructPattern) sp2.o1)) {
								sp2.o2 = new Integer(((Integer) sp2.o2).intValue() + 1);
								found = true;
								break;
							}
						}
						if (found == false) {
							pVars.add(new Pair(cpm.getDirectionalPModel(etls.get(flowId)), 1));
						}
					}
				}
			}
			patternsDirectionalVariations.put(sp.getPattId(), pVars);
		}

		HashMap<Integer, StructPattern> allPattsHashed = new HashMap<Integer, StructPattern>();
		for (StructPattern sp09 : allPatterns) {
			allPattsHashed.put(sp09.getPattId(), sp09);
		}

		Iterator it56 = patternsDirectionalVariations.entrySet().iterator();
		while (it56.hasNext()) {
			Map.Entry pair = (Map.Entry) it56.next();
			System.out.println("PattId: " + pair.getKey());
			System.out.println(allPattsHashed.get(pair.getKey()).toLabelGraphDescr());
			ArrayList<Pair> pattVariat = (ArrayList<Pair>) pair.getValue();
			System.out.println("number of vars: " + pattVariat.size());
			for (Pair sp87 : pattVariat) {
				System.out.println(((StructPattern) sp87.o1).toLabelGraphDescr());
				System.out.println("number of times: " + sp87.o2);
				System.out.println();
			}
		}
		return patternsDirectionalVariations;
	}

	public static void createPattOccurrencesCsv(ArrayList<StructPattern> allPatterns, String filename) {
		// get the <superpattern> classification for all the patterns in
		// allPattenrs
		HashMap<Integer, ArrayList<StructPattern>> pattClassification = getPattClassification(allPatterns);

		Iterator it2 = pattClassification.entrySet().iterator();
		// System.out.println("printing superpattersn 2:....." + it2.hasNext());
		String csvEntry = "";
		csvEntry += "pattId" + "," + "flowId" + "," + "totalOcurrences" + "," + "distinctOccurencesNo";
		for (int i = 1; i <= 156; i++) {
			csvEntry += "," + "superId" + i + "," + "nestedOccurencesNo";
		}
		csvEntry += "\n";
		int pattId;
		int flowId;
		int distOccs;
		int superId = -1;
		int nestedOccs = 0;
		boolean foundInside = false;
		while (it2.hasNext()) {
			// System.out.println("printing superpatterns:.....");
			Map.Entry pair = (Map.Entry) it2.next();
			pattId = (Integer) pair.getKey();
			// printing the number of superpatterns for each pattern
			// System.out.println(pair.getKey() + " = "
			// + ((ArrayList<StructPattern>) pair.getValue()).size());
			PatternInstances pis = readPatternInstances((Integer) pair.getKey());
			// iterate over entries for pis
			Iterator it4 = pis.getPattInstancesOnFlows().entrySet().iterator();
			while (it4.hasNext()) {
				distOccs = 0;
				Map.Entry pair2 = (Map.Entry) it4.next();
				flowId = (int) pair2.getKey();
				HashMap<Integer, Integer> inclPerSuperId = new HashMap<Integer, Integer>();
				csvEntry += pattId + "," + flowId + ",";
				ArrayList<StructPattern> instOnFlow = pis.getPattInstancesOnFlows().get(flowId);
				// total occurences on this flow
				csvEntry += instOnFlow.size() + ",";
				PatternInstances pisSuper;
				for (StructPattern sp : instOnFlow) {
					// System.out.println("pattern instance on flow: "
					// + sp.toLabelGraphDescr());
					foundInside = false;
					for (StructPattern superP : (ArrayList<StructPattern>) pair.getValue()) {
						// System.out.println("superP pattId: "
						// + superP.getPattId());
						nestedOccs = 0;
						pisSuper = readPatternInstances(superP.getPattId());
						superId = superP.getPattId();
						// csvEntry += superId + ",";
						// make the check for specific superpattern
						ArrayList<StructPattern> superInstOnFlow = pisSuper.getPattInstancesOnFlows().get(flowId);
						// foundInside = false;
						if (superInstOnFlow != null) {

							for (StructPattern spSuper : superInstOnFlow) {
								if (spSuper.getPattNodes().keySet().containsAll(sp.getPattNodes().keySet())) {
									foundInside = true;
									nestedOccs++;
									// System.out
									// .println("increasing
									// nestedOccs----------");
								}
							}
						}
						// if (foundInside == false) {
						// distOccs++;
						// }
						if (inclPerSuperId.get(superId) == null) {
							inclPerSuperId.put(superId, nestedOccs);
						} else {
							Integer value = inclPerSuperId.get(superId);
							inclPerSuperId.put(superId, new Integer(value.intValue() + nestedOccs));
							System.out.println("new value: " + new Integer(value.intValue() + nestedOccs) + " value: "
									+ value + " nestedOccs: " + nestedOccs);
						}
					}
					if (foundInside == false) {
						distOccs++;
					}
				}
				csvEntry += distOccs;
				// read the hashmap
				Iterator it42 = inclPerSuperId.entrySet().iterator();
				while (it42.hasNext()) {
					Map.Entry pair42 = (Map.Entry) it42.next();
					superId = (int) pair42.getKey();
					csvEntry += "," + superId + ",";
					nestedOccs = (int) pair42.getValue();
					csvEntry += nestedOccs;
				}
				csvEntry += "\n";
			}

			it2.remove(); // avoids a ConcurrentModificationException
		}
		IOProcessor.writeStringToFile(csvEntry, "tests/superPatternsAnalysis/" + "results.csv");

	}

	/**
	 * return a hashmap where the key is the pattern id and the value is an
	 * arraylist of all the patterns that are superpatterns of this pattern
	 * 
	 * @param allPatterns
	 * @return
	 */
	public static HashMap<Integer, ArrayList<StructPattern>> getPattClassification(
			ArrayList<StructPattern> allPatterns) {
		// hash patterns by their size
		HashMap<Integer, ArrayList<StructPattern>> pattBySize = getHashPattsBySize(allPatterns);
		// index is the pattId and value is all the patterns that are
		// superpatterns of that pattern
		HashMap<Integer, ArrayList<StructPattern>> pattClassification = new HashMap<Integer, ArrayList<StructPattern>>();
		for (int i = 1; i < pattBySize.size() + 1; i++) {
			ArrayList<StructPattern> pattsI = pattBySize.get(i);
			System.out.println("pattsI: " + pattsI);
			if (pattsI != null) {
				ArrayList<StructPattern> pattsIplusOne = pattBySize.get(i + 1);
				for (StructPattern s : pattsI) {
					ArrayList<StructPattern> superpatts = new ArrayList<StructPattern>();
					if (pattsIplusOne != null) {
						for (StructPattern s2 : pattsIplusOne) {
							// System.out.println("is subpattern: "
							// + s.isSubpatternOf(s2));
							if (s.isSubpatternOf(s2)) {
								superpatts.add(s2);
							}
						}
					}
					pattClassification.put(s.getPattId(), superpatts);
				}
			}
		}
		return pattClassification;
	}

	/**
	 * Return a hashmap where the key is the pattern size and the value is an
	 * array list containing all the patterns of that size
	 * 
	 * @param allPatterns
	 * @return
	 */
	public static HashMap<Integer, ArrayList<StructPattern>> getHashPattsBySize(ArrayList<StructPattern> allPatterns) {
		HashMap<Integer, ArrayList<StructPattern>> pattBySize = new HashMap<Integer, ArrayList<StructPattern>>();
		String sizeStr;
		int size;
		for (StructPattern sp : allPatterns) {
			size = sp.edgeSet().size();
			if (pattBySize.get(size) == null) {
				ArrayList<StructPattern> sps = new ArrayList<StructPattern>();
				sps.add(sp);
				pattBySize.put(size, sps);
			} else {
				pattBySize.get(size).add(sp);
			}

		}
		/*
		 * Iterator it = pattBySize.entrySet().iterator(); while (it.hasNext())
		 * { Map.Entry pair = (Map.Entry) it.next(); //
		 * System.out.println(pair.getKey() + " = " // +
		 * ((ArrayList<StructPattern>) pair.getValue()).size()); // System.out
		 * // .println("equals: " // + ((ArrayList<StructPattern>)
		 * pair.getValue()).get( // 0) // .equals(((ArrayList<StructPattern>)
		 * pair // .getValue()).get(0))); if (((ArrayList<StructPattern>)
		 * pair.getValue()).size() > 1) { System.out.println("id: " +
		 * ((ArrayList<StructPattern>) pair.getValue()).get(0) .getPattId() +
		 * " equals id: " + ((ArrayList<StructPattern>) pair.getValue()).get(1)
		 * .getPattId() + " : " + ((ArrayList<StructPattern>)
		 * pair.getValue()).get(0) .equals(((ArrayList<StructPattern>) pair
		 * .getValue()).get(1))); } // it.remove(); // avoids a
		 * ConcurrentModificationException }
		 */
		return pattBySize;
	}

	/**
	 * keep only distinct patterns from an array list of patterns and return a
	 * new array list of patterns with replicas removed
	 * 
	 * @param allPatterns
	 * @return
	 */
	public static ArrayList<StructPattern> getDistinctPatts(ArrayList<StructPattern> allPatterns) {
		HashSet<Integer> allPatternReplicas = new HashSet<Integer>();
		ArrayList<StructPattern> allDistinctPatterns = new ArrayList<StructPattern>();
		boolean noEqs = false;
		for (StructPattern s1 : allPatterns) {
			if (!allPatternReplicas.contains(s1.getPattId())) {
				allDistinctPatterns.add(s1);
				noEqs = true;
				for (StructPattern s2 : allPatterns) {
					if (!allPatternReplicas.contains(s2.getPattId())) {
						if (s1.equals(s2)) {
							if (s1.getPattId() != s2.getPattId()) {
								noEqs = false;
								allPatternReplicas.add(s2.getPattId());
							}

						}
					}
				}
				if (noEqs) {
					;
				}
			}
		}
		// System.out.println("replicas size: " + allPatternReplicas.size());
		// System.out.println("distinct patterns size: "
		// + allDistinctPatterns.size());
		return allDistinctPatterns;

	}

	/**
	 * read pattern matches for specific pattern from corresponding file and
	 * return them as a PatternInstances object
	 * 
	 * @param pattId
	 * @return
	 */
	public static PatternInstances readPatternInstances(int patternId) {
		PatternInstances pi = new PatternInstances(patternId);
		String csvFile = "tests/matchesPerPattern/" + patternId + ".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			// escape first line with headers
			br.readLine();
			String flowIdPrev = null;
			String matchIdPrev = null;
			// String nodeIdPrev = null;
			String flowId = null;
			String matchId;
			String nodeId;
			ArrayList<StructPattern> flowPattInstances;
			StructPattern sp = new StructPattern();
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] pattNode = line.split(cvsSplitBy);
				flowId = pattNode[0];
				matchId = pattNode[1];
				if ((!matchId.equals(matchIdPrev)) && (matchIdPrev != null)) {
					flowPattInstances = pi.getPattInstancesOnFlows().get(Integer.parseInt(flowIdPrev));
					if (flowPattInstances == null) {
						flowPattInstances = new ArrayList<StructPattern>();
						flowPattInstances.add(sp);
						pi.getPattInstancesOnFlows().put(Integer.parseInt(flowIdPrev), flowPattInstances);
						// System.out.println("new sp, new flow id:" + flowId
						// + "sp: " + sp);
					} else {
						flowPattInstances.add(sp);
						// System.out.println("new sp, same flow id:" + flowId
						// + "sp: " + sp);
					}

					sp = new StructPattern();

				}
				matchIdPrev = matchId;
				flowIdPrev = flowId;
				sp.addNode(new PatternNode(pattNode[2], Integer.parseInt(pattNode[2])));
				// System.out.println("Patt node [flow = " + pattNode[0]
				// + " ,matchId = " + pattNode[1] + " ,node = "
				// + pattNode[2] + " , binded to = " + pattNode[3] + "]");

			}
			// do the same for the last line
			flowPattInstances = pi.getPattInstancesOnFlows().get(Integer.parseInt(flowId));
			if (flowPattInstances == null) {
				flowPattInstances = new ArrayList<StructPattern>();
				flowPattInstances.add(sp);
				pi.getPattInstancesOnFlows().put(Integer.parseInt(flowId), flowPattInstances);
				// System.out.println("last line: new sp, new flow id");
			} else {
				flowPattInstances.add(sp);
				// System.out.println("last line: new sp, same flow id");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
		return pi;
	}

	/**
	 * read pattern matches for specific pattern from corresponding file and
	 * return them as a **detailed** pattern instances object
	 * 
	 * @param pattId
	 * @return
	 */
	public static PatternInstancesDetailed readPatternInstancesDetailed(int patternId) {
		PatternInstancesDetailed pi = new PatternInstancesDetailed(patternId);
		String csvFile = "tests/matchesPerPattern/" + patternId + ".csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			// escape first line with headers
			br.readLine();
			String flowIdPrev = null;
			String matchIdPrev = null;
			// String nodeIdPrev = null;
			String flowId = null;
			String matchId;
			String nodeId;
			ArrayList<CandidatePatternMatch> flowPattInstances;
			CandidatePatternMatch sp = new CandidatePatternMatch();
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] pattNode = line.split(cvsSplitBy);
				flowId = pattNode[0];
				matchId = pattNode[1];
				if ((!matchId.equals(matchIdPrev)) && (matchIdPrev != null)) {
					flowPattInstances = pi.getPattInstancesOnFlows().get(Integer.parseInt(flowIdPrev));
					if (flowPattInstances == null) {
						flowPattInstances = new ArrayList<CandidatePatternMatch>();
						flowPattInstances.add(sp);
						pi.getPattInstancesOnFlows().put(Integer.parseInt(flowIdPrev), flowPattInstances);
						// System.out.println("new sp, new flow id:" + flowId
						// + "sp: " + sp);
					} else {
						flowPattInstances.add(sp);
						// System.out.println("new sp, same flow id:" + flowId
						// + "sp: " + sp);
					}

					sp = new CandidatePatternMatch();

				}
				matchIdPrev = matchId;
				flowIdPrev = flowId;
				sp.addNode(new CandidatePatternMatchNode(pattNode[2], Integer.parseInt(pattNode[2]),
						Integer.parseInt(pattNode[3])));
				// System.out.println("Patt node [flow = " + pattNode[0]
				// + " ,matchId = " + pattNode[1] + " ,node = "
				// + pattNode[2] + " , binded to = " + pattNode[3] + "]");

			}
			// do the same for the last line
			flowPattInstances = pi.getPattInstancesOnFlows().get(Integer.parseInt(flowId));
			if (flowPattInstances == null) {
				flowPattInstances = new ArrayList<CandidatePatternMatch>();
				flowPattInstances.add(sp);
				pi.getPattInstancesOnFlows().put(Integer.parseInt(flowId), flowPattInstances);
				// System.out.println("last line: new sp, new flow id");
			} else {
				flowPattInstances.add(sp);
				// System.out.println("last line: new sp, same flow id");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
		return pi;
	}

	/**
	 * find a struct pattern with specific id from an array list of struct
	 * patterns. If does not exist, return null
	 * 
	 * @param sps
	 * @param id
	 * @return
	 */
	public static StructPattern getStructPattFromId(ArrayList<StructPattern> sps, int id) {
		for (StructPattern sp : sps) {
			if (sp.getPattId() == id) {
				return sp;
			}
		}
		return null;
	}

	/**
	 * Receives as an argument an array list of patterns and for each of them
	 * creates a csv file (with the pattern id as name) containing all the
	 * patterns instances in the 25 flows from TPC-DI
	 * 
	 * @param patterns
	 */
	public static void writeAllMatchesPerPattToFiles(ArrayList<StructPattern> patterns) {
		// get all matches of all patterns of a list in all of the flows
		// HashMap<Integer, ArrayList<StructPattern>>
		// allMatchesOfeachPatternForAllFlows = new HashMap<Integer,
		// ArrayList<StructPattern>>();

		LabellingStrategy lbs = new LabelBasedOnOpType();
		String resultsOnePattern = "";
		String patternId = "";
		String flowId = "";
		String CPMid = "";
		String CPMnode = "";
		String bindedPattNode = "";
		String csvEntry = "";
		ArrayList<Pair> efgs = allETLs;
		for (StructPattern sp : patterns) {
			csvEntry = "flowId" + "," + "CPMid" + "," + "CPMnode" + "," + "bindedPatternNode" + "\n";
			patternId = Integer.toString(sp.getPattId());
			// resultsOnePattern = "Pattern id: "
			// + Integer.toString(sp.getPattId()) + "\n";
			// resultsOnePattern += "Pattern description: " + "\n";
			// resultsOnePattern += sp.toLabelGraphDescr() + "\n";
			PatternMatchesVisitor matcher = new PatternMatchesVisitor(sp);
			matcher.setLbs(lbs);
			// matcher.setSp(sp);
			visitAllFlows(matcher, efgs);
			HashMap<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>> allMatches = matcher
					.getAllMatchesFromAllFlows();
			HashMap<Integer, ArrayList<CandidatePatternMatch>> innerHash;
			ETLFlowGraph efg;
			for (HashMap.Entry<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>> entry : allMatches
					.entrySet()) {
				// System.out.println("Flow ID : " + entry.getKey());
				flowId = entry.getKey().toString();
				// resultsOnePattern += "Flow ID : " + entry.getKey() + "\n";
				// resultsOnePattern += "Flow: " + "\n";

				// efg = matcher.getEfgHash().get(entry.getKey());
				// Iterator it = efg.iterator();
				// while (it.hasNext()) {
				// resultsOnePattern += efg.getEtlFlowOperations().get(
				// it.next())
				// + "\n";
				// }

				innerHash = entry.getValue();
				for (HashMap.Entry<Integer, ArrayList<CandidatePatternMatch>> entryIn : innerHash.entrySet()) {
					for (CandidatePatternMatch cp : entryIn.getValue()) {
						CPMid = Integer.toString(cp.getPattId());
						// resultsOnePattern += "Match with id: " +
						// cp.getPattId()
						// + "\n";
						// resultsOnePattern += cp.getPattNodes().toString()
						// + "\n";
						for (Entry<Integer, PatternNode> pn : cp.getPattNodes().entrySet()) {
							CandidatePatternMatchNode cpmn = (CandidatePatternMatchNode) pn.getValue();
							CPMnode = Integer.toString(cpmn.getNodeID());
							bindedPattNode = Integer.toString(cpmn.getBindingNodeFromModel());
							csvEntry += flowId + "," + CPMid + "," + CPMnode + "," + bindedPattNode + "\n";
							// resultsOnePattern += "int id: " + pn.getKey()
							// + " pn id: " + cpmn.getNodeID()
							// + " binded to: "
							// + cpmn.getBindingNodeFromModel() + "\n";
						}
						// toLabelGraphDescr() + "\n";
					}
					// System.out
					// .println("Pattern ID : " + entryIn.getKey()
					// + " no of occurences: "
					// + entryIn.getValue().size());

				}
				// System.out.println(resultsOnePattern);
			}
			IOProcessor.writeStringToFile(csvEntry, "tests/matchesPerPattern/" + patternId + ".csv");
			// end of one pattern. must write to the file <pattid.csv>
		}
	}

	/**
	 * Receives as an argument an array list of patterns and for each of them
	 * creates a csv file (with the pattern id as name) containing all the
	 * patterns instances in the 25 flows from TPC-DI, where for each node, the
	 * etl operation name of the corresponding node in the etl flow is denoted.
	 * 
	 * @param patterns
	 */
	public static void writeAllMatchesPerPattToFilesInclOpNames(ArrayList<StructPattern> patterns) {
		// get all matches of all patterns of a list in all of the flows
		// HashMap<Integer, ArrayList<StructPattern>>
		// allMatchesOfeachPatternForAllFlows = new HashMap<Integer,
		// ArrayList<StructPattern>>();

		LabellingStrategy lbs = new LabelBasedOnOpType();
		String resultsOnePattern = "";
		String patternId = "";
		String flowId = "";
		String CPMid = "";
		String opName = "";
		String bindedPattNode = "";
		String csvEntry = "";
		ArrayList<Pair> efgs = allETLs;
		for (StructPattern sp : patterns) {
			csvEntry = "flowId" + "," + "CPMid" + "," + "OpName" + "," + "bindedPatternNode" + "\n";
			patternId = Integer.toString(sp.getPattId());
			// resultsOnePattern = "Pattern id: "
			// + Integer.toString(sp.getPattId()) + "\n";
			// resultsOnePattern += "Pattern description: " + "\n";
			// resultsOnePattern += sp.toLabelGraphDescr() + "\n";
			PatternMatchesVisitor matcher = new PatternMatchesVisitor(sp);
			matcher.setLbs(lbs);
			// matcher.setSp(sp);
			visitAllFlows(matcher, efgs);
			HashMap<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>> allMatches = matcher
					.getAllMatchesFromAllFlows();
			HashMap<Integer, ArrayList<CandidatePatternMatch>> innerHash;
			ETLFlowGraph efg;
			for (HashMap.Entry<Integer, HashMap<Integer, ArrayList<CandidatePatternMatch>>> entry : allMatches
					.entrySet()) {
				// System.out.println("Flow ID : " + entry.getKey());
				flowId = entry.getKey().toString();
				// resultsOnePattern += "Flow ID : " + entry.getKey() + "\n";
				// resultsOnePattern += "Flow: " + "\n";

				efg = matcher.getEfgHash().get(entry.getKey());
				// Iterator it = efg.iterator();
				// while (it.hasNext()) {
				// resultsOnePattern += efg.getEtlFlowOperations().get(
				// it.next())
				// + "\n";
				// }

				innerHash = entry.getValue();
				for (HashMap.Entry<Integer, ArrayList<CandidatePatternMatch>> entryIn : innerHash.entrySet()) {
					for (CandidatePatternMatch cp : entryIn.getValue()) {
						CPMid = Integer.toString(cp.getPattId());
						// resultsOnePattern += "Match with id: " +
						// cp.getPattId()
						// + "\n";
						// resultsOnePattern += cp.getPattNodes().toString()
						// + "\n";
						for (Entry<Integer, PatternNode> pn : cp.getPattNodes().entrySet()) {
							CandidatePatternMatchNode cpmn = (CandidatePatternMatchNode) pn.getValue();
							// CPMnode = Integer.toString(cpmn.getNodeID());
							opName = efg.getEtlFlowOperations().get(cpmn.getNodeID()).getOperationName();
							bindedPattNode = Integer.toString(cpmn.getBindingNodeFromModel());
							csvEntry += flowId + "," + CPMid + "," + opName + "," + bindedPattNode + "\n";
							// resultsOnePattern += "int id: " + pn.getKey()
							// + " pn id: " + cpmn.getNodeID()
							// + " binded to: "
							// + cpmn.getBindingNodeFromModel() + "\n";
						}
						// toLabelGraphDescr() + "\n";
					}
					// System.out
					// .println("Pattern ID : " + entryIn.getKey()
					// + " no of occurences: "
					// + entryIn.getValue().size());

				}
				// System.out.println(resultsOnePattern);
			}
			IOProcessor.writeStringToFile(csvEntry, "tests/matchesPerPattern/opNames/" + patternId + ".csv");
			// end of one pattern. must write to the file <pattid.csv>
		}
	}

	public static ArrayList<Pair> getAllFlows() {
		ArrayList<Pair> efgs = new ArrayList<Pair>();
		ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
		Pair pair;
		for (TestFlow tf : TestFlows.values()) {
			try {
				if (tf.getXLMFilepath() != null) {
					ETLFlowGraph efg = ix2g.getFlowGraph(tf.getXLMFilepath());
					System.out.println("- Test flow being tested: " + tf.getXLMFilepath());
					pair = new Pair(efg, tf);
					efgs.add(pair);
				}
			} catch (CycleFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return efgs;
	}

	public static void visitAllFlows(MyVisitor v, ArrayList<Pair> efgs) {
		// ArrayList<Pair> efgs = getAllFlows();
		for (Pair pair : efgs) {
			v.visit((ETLFlowGraph) pair.getFirst(), (TestFlow) pair.getSecond());
		}
	}

	public static void getAndVisitAllFlows(MyVisitor v) {
		ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();

		for (TestFlow tf : TestFlows.values()) {
			try {
				if (tf.getXLMFilepath() != null) {
					ETLFlowGraph efg = ix2g.getFlowGraph(tf.getXLMFilepath());
					System.out.println("- Test flow being tested: " + tf.getXLMFilepath());
					// System.out.println("- Test flow size: "
					// + efg.getEtlFlowOperations().size());
					// System.out.println("- Test flow category: "
					// + tf.getFlowCategory());
					v.visit(efg, tf);
				}
			} catch (CycleFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void storeAllPatternsFromAllFPsToMongodb() {
		File dir = new File("tests/tpcdi/pafiResults/");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("1: " + child.getAbsolutePath());
				File[] directoryListing2 = child.listFiles();
				if (directoryListing2 != null) {
					for (File child2 : directoryListing2) {
						System.out.println("2: " + child2.getAbsolutePath());
						TestExecutor.serializeAndStoreStructPatternsFromFile(child2.getAbsolutePath());
					}
				}
			}

		}
	}

	/**
	 * Gets as argument a flow id and a struct pattern and returns the
	 * flowsubpart info of the ETL flow that it generates, which is a .ktr file.
	 * The generated file is an ETL flow containing only the nodes from the
	 * initial flow that also exist inside the struct pattern. The struct
	 * pattern has pattern nodes, with the ids of the nodes (integers) and for
	 * each patternNode there is a label containing the etl operation name for
	 * this pattern node, as it appears on the initial etl flow.
	 * 
	 * @param flowId
	 * @param flowSubpart
	 * @return
	 */
	public static FlowSubpartInfo getFlowSubpartAsKTR(String flowFileLocation, StructPattern flowSubpart,
			String outputDir, String templatesDir) {
		FlowSubpartInfo fspInfo = new FlowSubpartInfo();
		// create the set of etl operation names
		HashSet<String> operationNames = new HashSet<String>();
		ArrayList<Element> stepElements = new ArrayList<Element>();
		HashSet<String> inputSteps = new HashSet<String>();
		HashSet<String> outputSteps = new HashSet<String>();

		for (Integer pnId : flowSubpart.getPattNodes().keySet()) {
			String pnName = flowSubpart.getPattNodes().get(pnId).getLabel();
			operationNames.add(pnName);
			System.out.println("adding: " + pnName + " to operationNames");
		}
		HashSet<String> removedNodes = new HashSet<String>();
		HashSet<Element> removedNdElements = new HashSet<Element>();
		HashSet<Element> removedEdgeElements = new HashSet<Element>();
		ArrayList<Pair> remainingHops = new ArrayList<Pair>();
		ArrayList<Pair> allHopsAsPairs = new ArrayList<Pair>();

		String etlName = flowFileLocation.substring(flowFileLocation.lastIndexOf("/") + 1);
		ETLFlowGraph efg = null;
		TestFlow tf = null;
		for (Pair pair : allETLs) {
			if (((TestFlow) pair.getSecond()).getFileName().equals(etlName)) {
				efg = (ETLFlowGraph) pair.getFirst();
				tf = (TestFlow) pair.getSecond();
			}
		}
		if (efg == null) {
			System.out.println("corresponding etl flow graph not found");
			return null;
		}
		// parse the xml (.ktr) file
		try {
			String filepath = flowFileLocation;
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Get the root element
			Node transformation = doc.getFirstChild();

			// get the etl nodes
			NodeList steps = doc.getElementsByTagName("step");
			System.out.println("steps length: " + steps.getLength());
			int stepslength = steps.getLength();
			for (int i = 0; i < stepslength; i++) {
				System.out.println("i: " + i);
				Element step = (Element) steps.item(i);
				stepElements.add(step);
				System.out.println("step: " + step);
				Element name = (Element) step.getElementsByTagName("name").item(0);
				String sName = name.getTextContent();
				System.out.println("step name: " + sName);
				if (!operationNames.contains(sName)) {
					removedNodes.add(sName);
					removedNdElements.add(step);
					System.out.println("parentNode: " + step.getParentNode());
					if (step.getParentNode().isSameNode(transformation)) {
						System.out.println("yes it is same");
						// transformation.removeChild(step);
					}
					// System.out.println("removed step");
				}
			}

			// Get the edges
			NodeList hops = doc.getElementsByTagName("hop");
			int hopslength = hops.getLength();
			Pair pe;
			for (int j = 0; j < hopslength; j++) {
				Element hop = (Element) hops.item(j);
				System.out.println(hop);
				Element from = (Element) hop.getElementsByTagName("from").item(0);
				String hFrom = from.getTextContent();
				pe = new Pair((((Element) hop).getElementsByTagName("from").item(0).getTextContent()),
						(((Element) hop).getElementsByTagName("to").item(0).getTextContent()));
				allHopsAsPairs.add(pe);
				System.out.println("hFrom: " + hFrom);
				if (removedNodes.contains(hFrom)) {
					removedEdgeElements.add(hop);
					// hop.getParentNode().removeChild(hop);
					System.out.println("hfrom in removed nodes");
				} else {
					Element to = (Element) hop.getElementsByTagName("to").item(0);
					String hTo = to.getTextContent();
					System.out.println("hTo: " + hTo);
					if (removedNodes.contains(hTo)) {
						removedEdgeElements.add(hop);
						// hop.getParentNode().removeChild(hop);
						System.out.println("hto in removed nodes");
					} else {
						remainingHops
								.add(new Pair((((Element) hop).getElementsByTagName("from").item(0).getTextContent()),
										(((Element) hop).getElementsByTagName("to").item(0).getTextContent())));
					}
				}
			}
			boolean hasNxt;
			boolean hasPrv;
			for (String o : operationNames) {
				hasNxt = false;
				hasPrv = false;
				for (Pair e : remainingHops) {
					if (((String) e.o1).equals(o)) {
						hasNxt = true;
					} else if (((String) e.o2).equals(o)) {
						hasPrv = true;
					}
				}
				if (!hasPrv) {
					inputSteps.add(o);
				}
				if (!hasNxt) {
					outputSteps.add(o);
				}

			}
			// reading the input text file template
			String filepath2 = templatesDir + "text-input.ktr";
			DocumentBuilderFactory docFactory2 = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder2 = docFactory2.newDocumentBuilder();
			Document doc2 = docBuilder2.parse(filepath2);

			// Get the root element
			Node transformation2 = doc2.getFirstChild();

			// get the input text file node
			Node inputFileNd = doc2.getElementsByTagName("step").item(0);
			int inC = 0;
			for (String ins : inputSteps) {
				inC++;
				// find the corresponding element
				for (Element ndEl : stepElements) {
					if (((Element) ndEl.getElementsByTagName("name").item(0)).getTextContent().equals(ins)) {
						// if the operation is an input operation already...
						if (inputOpTypes
								.contains(((Element) ndEl.getElementsByTagName("type").item(0)).getTextContent())) {
							// do something...
						} else {
							// find all predecessors
							for (Pair p : allHopsAsPairs) {
								if (((String) p.o2).equals(ins)) {
									String predecessor = (String) p.o1;
									// find pre as element
									// Element preElement;
									// for (Element ndElpre : stepElements) {
									// if (((Element) ndElpre
									// .getElementsByTagName("name")
									// .item(0)).getTextContent()
									// .equals(predecessor)) {
									// preElement = ndElpre;
									// break;
									// }
									// }

									// find pre inside corresponding etl flow
									// graph
									System.out.println("predecessor: " + predecessor);
									for (ETLFlowOperation op : efg.getEtlFlowOperations().values()) {
										System.out.println("opname: " + op.getOperationName());
									}
									String fullEfoName = etlName.substring(0, etlName.lastIndexOf('.')) + "."
											+ predecessor;
									System.out.println("full etl name: " + fullEfoName);
									ETLFlowOperation efo = efg.getEtlFlowOperationByName(fullEfoName);
									// get the output schema of that operation
									ArrayList<Schema> schemaOut = efo.getOutputSchemata();
									// this is for the case that efo is an
									// inputtype operation, so it will only have
									// inputschema
									if (schemaOut == null) {
										schemaOut = efo.getInputSchemata();
									}
									System.out.println("schemaOut: " + schemaOut);
									// create a new input text operation as efg
									ETLFlowOperation efoIn = new ETLFlowOperation(ETLNodeKind.Datastore,
											ETLOperationType.etlOperationTypes.get(OperationTypeName.FileOutput.name()),
											"FCP" + "_InTextFile");
									ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic("file_name",
											"", "file_name", "=", "", "c/c//c/c/c");
									efoIn.addoProperty("file_name", efc);
									ETLNonFunctionalCharacteristic efc2 = new ETLNonFunctionalCharacteristic(
											"storage_type", "", "storage_type", "=", "", "LocalFileSystem");
									efoIn.addoProperty("storage_type", efc2);
									efoIn.setInputSchemata(schemaOut);
									// create a new efg that will contain only
									// this input text file operation
									ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
									// get the input text file template as etl
									// flow graph
									ETLFlowGraph efgIn;
									// read the text input etl template,
									// transform it to xlm
									String tieXlm = ImportPDIExportXML
											.importPDItoXML(templatesDir + "text-input-text-output-etl");
									System.out.println("transformed to xlm...");
									System.out.println(tieXlm);
									System.out.println(".......");
									// and load
									// it as an etl flow graph
									// IOProcessor.writeStringToFile(tieXlm,
									// templatesDir + "inputEtlAsXlm.xml");
									// efgIn = ix2g.getFlowGraph(tieXlm);
									efgIn = ETLTransformer
											.xlmToEfg(templatesDir + "text-input-text-output-etl_agn.xml");
									System.out.println("efg: " + efgIn);
									for (ETLFlowOperation eo : efgIn.getEtlFlowOperations().values()) {
										System.out.println("printing ops: ");
										System.out.println(eo);
									}
									// ix2g.getFlowGraph(templatesDir
									// + "text-input-etl_agn.xml");
									IOProcessor.writeStringToFile(ETLTransformer.efgToXlm(efgIn),
											templatesDir + "efgInAsXlm.xml");
									// // remove its (1) node
									// efgIn.getEtlFlowOperations().remove(
									// efgIn.getEtlFlowOperations()
									// .values().iterator().next()
									// .getNodeID());
									// efgIn.addNode(efoIn);
									// export as xlm
									String efgInAsKtr = ImportXLMExportPDI
											.importXLMtoPDI(templatesDir + "efgInAsXlm.xml");
									IOProcessor.writeStringToFile(efgInAsKtr, templatesDir + "efgInAsKtr.ktr");
									// import the xlm for parsing
									DocumentBuilderFactory factory42 = DocumentBuilderFactory.newInstance();
									DocumentBuilder builder42 = factory42.newDocumentBuilder();
									InputSource is = new InputSource(new StringReader(efgInAsKtr));
									Document docIn = builder42.parse(is);
									// get the fields element from the one and
									// only step node
									Node fields = ((Element) docIn.getElementsByTagName("step").item(0))
											.getElementsByTagName("fields").item(0);
									Node newNode = doc.importNode(inputFileNd, true);
									Node fieldsNode = doc.importNode(fields, true);
									newNode.appendChild(fieldsNode);
									((Element) ((Element) newNode).getElementsByTagName("file").item(0))
											.getElementsByTagName("name").item(0).setTextContent(inC + ".txt");
									transformation.appendChild(newNode);
									// write this to the info
								}
							}
						}
						break;
					}
				}
			}

			// removing the extra nodes
			int g = 0;
			for (Element e : removedNdElements) {
				g++;
				System.out.println(g + " removing nd: " + e.getElementsByTagName("name").item(0).getTextContent());
				transformation.removeChild(e);
			}
			// removing the extra edges
			int g1 = 0;
			for (Element e : removedEdgeElements) {
				g1++;
				System.out
						.println(g1 + " removing edge: from: " + e.getElementsByTagName("from").item(0).getTextContent()
								+ " to: " + e.getElementsByTagName("to").item(0).getTextContent());
				e.getParentNode().removeChild(e);
			}
			/*
			 * // update staff attribute NamedNodeMap attr =
			 * staff.getAttributes(); Node nodeAttr = attr.getNamedItem("id");
			 * nodeAttr.setTextContent("2");
			 * 
			 * // append a new node to staff Element age =
			 * doc.createElement("age");
			 * age.appendChild(doc.createTextNode("28"));
			 * staff.appendChild(age);
			 * 
			 * // loop the staff child node NodeList list =
			 * staff.getChildNodes();
			 * 
			 * for (int i = 0; i < list.getLength(); i++) {
			 * 
			 * Node node = list.item(i);
			 * 
			 * // get the salary element, and update the value if
			 * ("salary".equals(node.getNodeName())) {
			 * node.setTextContent("2000000"); }
			 * 
			 * // remove firstname if ("firstname".equals(node.getNodeName())) {
			 * staff.removeChild(node); }
			 * 
			 * }
			 */
			// write the content into xml file
			inc++;
			String outFilepath = outputDir
					+ new StringTokenizer(filepath.substring(filepath.lastIndexOf("/") + 1), ".").nextToken() + "-"
					+ inc + ".ktr";
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			IOProcessor.writeStringToFile("", outFilepath);
			String outFilepath2 = new StringTokenizer(filepath.substring(filepath.lastIndexOf("/") + 1), ".")
					.nextToken() + "-" + inc + ".ktr";
			// file.createNewFile();
			StreamResult result = new StreamResult(new File(outputDir, outFilepath2).getPath());
			transformer.transform(source, result);
			fspInfo.setFilePath(outFilepath);
			System.out.println("Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}

		return fspInfo;

	}

	static ArrayList<SubflowToTest> getSubflowsFromPatternOccurenceFile(String filepath) {
		ArrayList<SubflowToTest> subflows = new ArrayList<SubflowToTest>();
		String csvFile = filepath;
		String patternId = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.indexOf("."));
		// System.out.println("pattern id: " + patternId);

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			// escape first line with headers
			br.readLine();
			String flowIdPrev = null;
			String occuranceIdPrev = null;
			String flowId = null;
			String occuranceId = null;
			SubflowToTest sf = null;
			StructPattern sp = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				// System.out.println("sp in new line: " + sp);
				i++;
				// use comma as separator
				String[] pattNode = line.split(cvsSplitBy);
				flowId = pattNode[0];
				if (occuranceId == null) {
					occuranceId = pattNode[1];
					// System.out.println("reading first line");
					sf = new SubflowToTest();
					sp = new StructPattern();
					sf.setFlowId(Integer.parseInt(flowId));
					sf.setPatternOccurenceId(Integer.parseInt(occuranceId));
					sf.setPatternId(Integer.parseInt(patternId));
				} else {
					occuranceId = pattNode[1];
					if (!occuranceId.equals(occuranceIdPrev)) {
						// System.out.println("reading non-first line");
						// System.out.println("sp: " + sp);
						sf.setSp(sp);
						// add the previous sf to the returned objects
						subflows.add(sf);
						sf = new SubflowToTest();
						sp = new StructPattern();
						sf.setFlowId(Integer.parseInt(flowId));
						sf.setPatternOccurenceId(Integer.parseInt(occuranceId));
						sf.setPatternId(Integer.parseInt(patternId));
					}
				}
				PatternNode pn = new PatternNode(pattNode[2].substring(pattNode[2].indexOf(".") + 1), i);
				sp.addNode(pn);
				occuranceIdPrev = occuranceId;
			}
			// for the last one
			if (sf != null) {
				sf.setSp(sp);
				subflows.add(sf);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done creating subflows for pattern");
		return subflows;
	}

	static void analyzePattPerformance() {
		File dir = new File("tests/Exhaustive testing/");
		String pattId;
		String pattSize;
		String occSize;
		String flowName;
		String curOcc = null;
		String prevOcc = null;
		int maxRuntime;
		int i = 0;
		String csvContent = "";
		csvContent += "pattId,pattSize,occuranceSize,flowName,time,input1size,input2size,input3size,input4size" + "\n";
		Integer[] time4OnePatt = new Integer[4];
		ArrayList<String> inputSizes = new ArrayList<String>();
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("1: " + child.getAbsolutePath());
				pattId = child.getAbsolutePath().substring(child.getAbsolutePath().lastIndexOf("/") + 1);
				if (pattId.equals(".DS_Store") || pattId.equals("analysis")) {
					continue;
				}
				pattSize = getPatternFromId(Integer.valueOf(pattId)).edgeSet().size() + "";
				occSize = pattSize;
				System.out.println("pattId: " + pattId);
				System.out.println("pattSize: " + pattSize);
				File[] directoryListing2 = child.listFiles();
				if (directoryListing2 != null) {
					for (File child2 : directoryListing2) {
						System.out.println("2: " + child2.getAbsolutePath());
						flowName = child2.getAbsolutePath().substring(child2.getAbsolutePath().lastIndexOf("/") + 1,
								child2.getAbsolutePath().lastIndexOf("."));
						System.out.println("flowName: " + flowName);
						if (consistentFlows.contains(flowName)) {
							File[] directoryListing3 = child2.listFiles();
							if (directoryListing3 != null) {
								curOcc = null;
								prevOcc = null;
								boolean newOcc = false;
								for (File child3 : directoryListing3) {
									newOcc = false;
									if (child3.getAbsolutePath().lastIndexOf("_") == -1) {
										continue;
									}
									curOcc = child3.getAbsolutePath().substring(
											child3.getAbsolutePath().lastIndexOf("/") + 1,
											child3.getAbsolutePath().lastIndexOf("_"));
									System.out.println("3: " + child3.getAbsolutePath());
									if ((prevOcc == null) || !(curOcc.equals(prevOcc))) {
										System.out.println("CHANGED!!!");
										if (prevOcc != null) {
											csvContent += pattId + "," + pattSize + "," + occSize + "," + flowName + ","
													+ getMedianTime(time4OnePatt);
											for (String size : inputSizes) {
												csvContent += "," + size;
											}
											csvContent += "\n";
										}
										occSize = pattSize;
										System.out.println("occsize when new: " + occSize);
										newOcc = true;
										time4OnePatt = new Integer[4];
										inputSizes = new ArrayList<String>();
										i = -1;
										// ...
									}
									i++;

									prevOcc = curOcc;
									System.out.println("prevOcc: " + prevOcc);
									System.out.println("curOcc: " + curOcc);
									String csvFile = child3.getAbsolutePath();
									BufferedReader br = null;
									String line = "";
									String cvsSplitBy = ",";
									String Copy_nr, Read, Status, Errors, Input, Runtime, Written, Updated, Output,
											Rejected;
									try {

										br = new BufferedReader(new FileReader(csvFile));
										// escape first line with headers
										br.readLine();
										maxRuntime = -1;
										while ((line = br.readLine()) != null) {

											// use comma as separator
											String[] opMetrics = line.split(cvsSplitBy);
											Copy_nr = opMetrics[0];
											Read = opMetrics[1];
											Status = opMetrics[2];
											Errors = opMetrics[3];
											Input = opMetrics[4];
											if (newOcc == true) {
												if (!Input.equals("0")) {
													inputSizes.add(Input);
													occSize = (1 + Integer.parseInt(occSize)) + "";
												}
											}
											Runtime = opMetrics[5];
											if (Integer.parseInt(Runtime) > maxRuntime) {
												maxRuntime = Integer.valueOf(Runtime);
											}
											Written = opMetrics[6];
											Updated = opMetrics[7];
											Output = opMetrics[8];
											if (newOcc == true) {
												if (!Output.equals("0")) {
													occSize = (1 + Integer.parseInt(occSize)) + "";
												}
											}
											Rejected = opMetrics[9];
										}
										time4OnePatt[i] = maxRuntime;
										System.out.println("exec time: " + time4OnePatt[i]);
										System.out.println("inputSizes: " + inputSizes);

									} catch (FileNotFoundException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									} finally {
										if (br != null) {
											try {
												br.close();
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}

								}
								csvContent += pattId + "," + pattSize + "," + occSize + "," + flowName + ","
										+ getMedianTime(time4OnePatt);
								for (String size : inputSizes) {
									csvContent += "," + size;
								}
								csvContent += "\n";
							}
						}
					}
				}
			}

		}
		IOProcessor.writeStringToFile(csvContent, "tests/Exhaustive testing/analysis/results.csv");
	}

	static String getMedianTime(Integer[] timesInteger) {
		if (timesInteger == null) {
			return "-1";
		} else {
			int[] times = new int[4];
			for (int i = 0; i < 4; i++) {
				if (timesInteger[i] == null) {
					return "-1";
				} else {
					System.out.println(timesInteger[i]);
					times[i] = timesInteger[i].intValue();
				}
			}
			Arrays.sort(times);
			double median = -1;
			if (times.length % 2 == 0)
				median = ((double) times[times.length / 2] + (double) times[times.length / 2 - 1]) / 2;
			else
				median = (double) times[times.length / 2];

			return median + "";
		}
	}

	static StructPattern getPatternFromId(int pattId) {
		for (StructPattern sp : allPatterns) {
			if (sp.getPattId() == pattId) {
				return sp;
			}
		}
		return null;
	}
}
