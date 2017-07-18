package patternGeneration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import measureDictionary.MeasureName;
import measures.FlowMeasures;
import measures.Measure;
import measures.Measures;
import measures.MeasuresEstimator;
import measures.QualityCharacteristic;
import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import api.exportFlow.xLM.WriteGraphToXLM;
import core.tEngineType;
import core.tGraphLib.tFlowGraph;
import defaultQPatterns.AddCheckpoint;
import defaultQPatterns.CrosscheckSources;
import defaultQPatterns.FilterLTRandom;
import defaultQPatterns.FilterNullValues;
import defaultQPatterns.IGeneratorTesterFilter;
import defaultQPatterns.IGeneratorTesterJoin;
import drivers.ImportPDIExportSQL_run;
import drivers.ImportXLMExportSQL;
import qPatterns.QPattern;
import structures.AddOperation;
import structures.ApplPoint;
import structures.CrossFlow;
import structures.DataSource;
import structures.EFGPatternApplicationFlyweight;
import structures.ETLStep;
import structures.EdgeApplPoint;
import structures.GraphApplPoint;
import structures.IdAttrPair;
import structures.JoinKeys;
import structures.NodeApplPoint;
import structures.StepFlow;
import structures.StepSequence;
import utilities.ObjectPair;
import importXLM.ImportXLMToETLGraph;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.graph.*;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class PatternLocator {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "org.postgresql.Driver";
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/TPCH";

	// Database credentials
	static final String USER = "postgres";
	static final String PASS = "password";

	// private static String jsonPath =
	// "C:\\Users\\petar\\Desktop\\PhD\\Development\\workspace_eclipse\\CoAl\\visualization\\test.json";
	public static String jsonPath = "/Users/btheo/Documents/workspace/poiesis/visualization/test2.json";

	public static void main(String[] args) {
		System.out.println("finally!");
	}
	// ETLFlowGraph efg = new ETLFlowGraph();
	// ImportXLMToETLGraph ix2g = new ImportXLMToETLGraph();
	// //ImportKTRToETLGraph ik2g = new ImportKTRToETLGraph();
	// System.out.println("what?1");
	// try {
	// efg = ix2g.getFlowGraph("tests/etl-initial_agn.xml");
	// //efg = ix2g.getFlowGraph("tests/flow1_simple.xml");
	// //efg = ix2g.getFlowGraph("tests/flow8StreamLookup_agn.xml");
	// //efg = ix2g.getFlowGraph("tests/flow2_complex.xml");
	// System.out.println(efg.toStringJSON());
	// Iterator it = efg.iterator();
	// while (it.hasNext()) {
	// System.out.println(efg.getEtlFlowOperations().get(it.next()));
	// }
	//
	// // System.out.println(efg.iterator().next());
	// // System.out.println(efg.iterator().next());
	//
	// } catch (CycleFoundException e) { // TODO Auto-generated catch block
	// System.out.println("what?3");
	// e.printStackTrace();
	// } finally {
	// System.out.println("what?4");
	// }
	//
	// NodeApplPoint nap = new NodeApplPoint();
	// nap.setEfGraph(efg);
	// //System.out.println(efg.getEtlFlowOperations().values().iterator().next().getOperationName());
	// nap.setEfOperator(efg.getEtlFlowOperations().values().iterator().next());
	// StepSequence ss = new StepSequence();
	// StepSequence ss1 = new StepSequence();
	// ETLStep es = new AddOperation(ETLNodeKind.Operator,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter
	// .name()), "FilterNulls_1");
	// ss1.addStepChild(es);
	// ETLStep es2 = new AddOperation(ETLNodeKind.Operator,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter
	// .name()), "FilterNulls_2");
	// ss.addStepChild(ss1);
	// ss.addStepChild(es2);
	// ETLStep es3 = (ETLStep) es2.clone();
	// es3.getAssociatedETLOperation().setNodeID(400);
	// ss.addStepChild(es3);
	// ETLFlowGraph efg2 = ss.getAsETLFlowGraph(nap);
	// System.out.println("second graph:");
	// System.out.println(efg2);
	// Iterator it2 = efg2.iterator();
	// while (it2.hasNext()) {
	// System.out.println(efg2.getEtlFlowOperations().get(it2.next()));
	// }
	//
	// StepFlow sf = new StepFlow();
	// sf.addSequenceChild(ss);
	// sf.addSequenceChild((StepSequence) ss.clone());
	// ETLFlowGraph efg3 = sf.getAsETLFlowGraph(nap);
	// System.out.println("third graph:");
	// System.out.println(efg3);
	// Iterator it3 = efg3.iterator();
	// while (it3.hasNext()) {
	// System.out.println(efg3.getEtlFlowOperations().get(it3.next()));
	// }
	//
	//
	//
	// CrossFlow cf = new CrossFlow();
	// ETLFlowOperation o421 = new ETLFlowOperation(ETLNodeKind.Operator,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter
	// .name()), "Filter1");
	// ETLStep es42_1 = new AddOperation(o421);
	// ETLFlowOperation o422 = new ETLFlowOperation(ETLNodeKind.Operator,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Join
	// .name()), "Join1");
	// ETLStep es42_2 = new AddOperation(o422);
	// ETLFlowOperation o423 = new ETLFlowOperation(ETLNodeKind.Operator,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Project
	// .name()), "Project1");
	// ETLStep es42_3 = new AddOperation(o423);
	// for (Attribute a:nap.getOutputSchemata().get(0).getAttributes()){
	// ExpressionAttribute lex2 = new ExpressionAttribute(a);
	// Expression fex2 = new Expression(lex2, null, null,
	// ExpressionKind.EXPRESSION);
	// fex2.addUsedAttribute(a);
	// o423.addSemanticsExpressionTree("project", fex2);
	// }
	// StepSequence ss42 = new StepSequence();
	// ss42.addStepChild(es42_1);
	// ss42.addStepChild(es42_2);
	// ss42.addStepChild(es42_3);
	// ETLFlowOperation o431 = new ETLFlowOperation(ETLNodeKind.Datastore,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.FileInput
	// .name()), "Fileinput2");
	// AddOperation es43_1 = new AddOperation(o431);
	// o431.setInputSchemata(nap.getRefProperties().getOutSchemataClone());
	// for(Attribute
	// a:o431.getInputSchemata().iterator().next().getAttributes()){
	// //a.setAttrName(a.getAttrName()+"2");
	// a.setAttrName(a.getAttrName());
	// }
	// o431.setOutputSchemata(nap.getRefProperties().getOutSchemataClone());
	// for(Attribute
	// a:o431.getOutputSchemata().iterator().next().getAttributes()){
	// //a.setAttrName(a.getAttrName()+"2");
	// a.setAttrName(a.getAttrName());
	// }
	// es43_1.setImmutableSchema(true);
	//
	// //ETLStep es43_2 = new AddOperation(o422);
	// ETLStep es43_2 = es42_2;
	// ETLFlowOperation o433 = new ETLFlowOperation(ETLNodeKind.Operator,
	// //
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
	// ETLOperationType.etlOperationTypes.get(OperationTypeName.Project
	// .name()), "Project2");
	// ETLStep es43_3 = new AddOperation(o433);
	// for (Attribute a:nap.getOutputSchemata().get(0).getAttributes()){
	// ExpressionAttribute lex2 = new ExpressionAttribute(a);
	// Expression fex2 = new Expression(lex2, null, null,
	// ExpressionKind.EXPRESSION);
	// //
	// //
	// // Iterator itgamw =
	// o433.getSemanticsExpressionTrees().entrySet().iterator();
	// // while (itgamw.hasNext()){
	// // System.out.println("used attributes right before addition: "+
	// ((Expression) (((Entry) itgamw.next()).getValue())).getUsedAttributes());
	// // }
	// //
	// // System.out.println("a:"+a);
	// fex2.addUsedAttribute(a);
	// o433.addSemanticsExpressionTree("project", fex2);
	// // itgamw = o433.getSemanticsExpressionTrees().entrySet().iterator();
	// // while (itgamw.hasNext()){
	// // System.out.println("used attributes right after addition: "+
	// ((Expression) (((Entry) itgamw.next()).getValue())).getUsedAttributes());
	// // }
	// }
	// StepSequence ss43 = new StepSequence();
	// ss43.addStepChild(es43_1);
	// ss43.addStepChild(es43_2);
	// ss43.addStepChild(es43_3);
	// cf.setHorizontalSequence(ss42);
	// cf.setVerticalSequence(ss43);
	// cf.setCrossStepHorIndex(1);
	// cf.setCrossStepVertIndex(1);
	// ETLFlowGraph efg21 = cf.getAsETLFlowGraph(nap);
	// System.out.println("crossflow graph:");
	// System.out.println(efg21.toStringXLM());
	// Iterator it5 = efg21.iterator();
	// while (it5.hasNext()) {
	// System.out.println(efg21.getEtlFlowOperations().get(it5.next()));
	// }
	//
	//
	// PatternDeployer pd = new PatternDeployer();
	// pd.setEfgraph(efg);
	// ArrayList<QPattern> activePs = new ArrayList<QPattern>();
	// // IGeneratorTesterFilter filterTstP = new IGeneratorTesterFilter();
	// FilterNullValues filterNV = new FilterNullValues();
	// FilterLTRandom filterLTR = new FilterLTRandom();
	// JoinKeys joinKeys = new JoinKeys();
	// // here i arbitrarily chose l_suppkey
	// Attribute attr =
	// efg.getEtlFlowOperationByName("etl-initial.LINEITEM").getInputSchemata().iterator().next().getAttributes().get(2);
	// System.out.println("attr to be used as key: "+attr);
	// IdAttrPair iap = new IdAttrPair(-1, attr);
	// joinKeys.addKey(iap );
	// //cross.setJoinKeys(joinKeys );
	// ArrayList<DataSource> alternativeSources = new ArrayList<DataSource>();
	// DataSource e = new DataSource("d/d/d/d/d/d", OperationTypeName.FileInput,
	// nap.getRefProperties().getOutSchemata().iterator().next());
	// System.out.println("filepath: "+e.getFilepath());
	// alternativeSources.add(e);
	// //cross.setAlternativeSources(alternativeSources);
	// CrosscheckSources cross = new CrosscheckSources(alternativeSources,
	// joinKeys);
	// //cross.setMaxNumberOfOccurencesInSameFlow(50);
	// //filterNV.setMaxNumberOfOccurencesInSameFlow(50);
	//
	// AddCheckpoint addCP = new AddCheckpoint();
	// // filterTstP.setRangeMin(5);
	// // filterTstP.setRangeMax(10);
	// // IGeneratorTesterJoin joinTstP = new IGeneratorTesterJoin();
	// // activePs.add(filterTstP);
	// activePs.clear();
	// activePs.add(cross);
	// activePs.add(filterNV);
	//
	// // activePs.add(filterNV);
	// pd.setActiveQPatterns(activePs);
	// ExhaustiveDeployment randomP = new ExhaustiveDeployment();
	// ETLFlowGraph efgAddedFltr = pd.deployPatterns(randomP);
	// System.out.println("deployed cross check:");
	// ArrayList<EFGFlyweight> efgsLite =
	// pd.getDeploymentWalkthroughLite(randomP);
	// FlowFamily ff= new FlowFamily();
	// ff.setInitialEfg(efg);
	// ff.setFlows(efgsLite);
	// ArrayList<MeasureName> activeMeasures = new ArrayList<MeasureName>();
	// activeMeasures.add(MeasureName.C2aM3);
	// activeMeasures.add(MeasureName.C4civM1);
	// activeMeasures.add(MeasureName.E1aM2);
	// ArrayList<FlowMeasures> allMeasures =
	// MeasuresEstimator.produceMeasures(ff, activeMeasures);
	// System.out.println(allMeasures.toString());
	// /*
	// int i = 0;
	// for (EFGFlyweight efly:efgsLite){
	// i++;
	// if (i<5){
	// ETLFlowGraph efgpana = efly.getEFG(efg);
	// FileOutputStream fop = null;
	// File file;
	// //String content = efgAddedFltr.toStringJSONNames();
	// String content = efgAddedFltr.toStringXLM();
	// String content2 = efgpana.toStringJSONNames();
	// //String content = efg.toStringXLM();
	//
	// try {
	//
	// file = new File("visualization/test.json");
	// //file = new File("tests/etl-initial_agn.json");
	// fop = new FileOutputStream(file);
	//
	// // if file doesnt exists, then create it
	// if (!file.exists()) {
	// file.createNewFile();
	// }
	//
	// // get the content in bytes
	// byte[] contentInBytes = content2.getBytes();
	//
	// fop.write(contentInBytes);
	// fop.flush();
	// fop.close();
	//
	// System.out.println("Done kara done");
	// efgpana.visualizeGraph("/Users/btheo/Documents/workspace/poiesis/visualization/test.json",
	// "/Users/btheo/Documents/workspace/poiesis/visualization/index.html");
	//
	//
	// } catch (IOException ef) {
	// ef.printStackTrace();
	// } finally {
	// try {
	// if (fop != null) {
	// fop.close();
	// }
	// } catch (IOException ef) {
	// ef.printStackTrace();
	// }
	// }
	// //efgAddedFltr.visualizeGraph("/Users/btheo/Documents/workspace/poiesis/visualization/test.json",
	// "/Users/btheo/Documents/workspace/poiesis/visualization/index.html");
	//
	// }
	// }
	// */
	// //System.out.println(efgAddedFltr.toStringXLM());
	// //efgAddedFltr.visualizeGraph("/Users/btheo/Documents/workspace/poiesis/visualization/test.json",
	// "/Users/btheo/Documents/workspace/poiesis/visualization/index.html");
	// Iterator itf = efgAddedFltr.iterator();
	// // while (itf.hasNext()) {
	// // System.out.println(efgAddedFltr.getEtlFlowOperations().get(
	// // itf.next()));
	// // }
	// System.out.println("-------");
	// //System.out.println(efgAddedFltr.toStringXLM());
	// System.out.println("-------");
	// //System.out.println(JSONETL_to_D3_ETL.parse(efgAddedFltr.toStringXLM()));
	//
	// FileOutputStream fop = null;
	// File file;
	// //String content = efgAddedFltr.toStringJSONNames();
	// String content = efgAddedFltr.toStringXLM();
	// String content2 = efgAddedFltr.toStringJSONNames();
	// //String content = efg.toStringXLM();
	//
	// try {
	//
	// file = new File("visualization/test.json");
	// //file = new File("tests/etl-initial_agn.json");
	// fop = new FileOutputStream(file);
	//
	// // if file doesnt exists, then create it
	// if (!file.exists()) {
	// file.createNewFile();
	// }
	//
	// // get the content in bytes
	// byte[] contentInBytes = content2.getBytes();
	//
	// fop.write(contentInBytes);
	// fop.flush();
	// fop.close();
	//
	// System.out.println("Done kara done");
	//
	// } catch (IOException ef) {
	// ef.printStackTrace();
	// } finally {
	// try {
	// if (fop != null) {
	// fop.close();
	// }
	// } catch (IOException ef) {
	// ef.printStackTrace();
	// }
	// }
	//
	// System.out.println("ti ginetai re? ");
	// System.out.println("ti ginetai re?
	// "+efgAddedFltr.getEtlFlowOperationByName("Join1"));
	// //
	// System.out.println(efgAddedFltr.getEtlFlowOperationByName("Project_1"));
	// //
	// System.out.println(efgAddedFltr.getEtlFlowOperationByName("etl-initial.Sort
	// on l_suppkey"));
	// //
	// System.out.println(efgAddedFltr.getEtlFlowOperationByName("AltDataSourceInput1"));
	// System.out.println(nap.getEfOperator().getOperationName());
	//
	// System.out.println("q char tests:
	// "+QualityCharacteristic.qualityCharacteristics);
	// System.out.println("measures for graph");
	// for (Measure m:Measures.values()){
	// System.out.println("Characteristic: "+m.getQCharacteristicName()+",
	// Measure: "+m.getName()+
	// "Value: "+m.estimateValue(efgAddedFltr));
	// }
	//
	// efgAddedFltr.visualizeGraph("/Users/btheo/Documents/workspace/poiesis/visualization/test.json",
	// "/Users/btheo/Documents/workspace/poiesis/visualization/index.html");
	//
	//
	//
	//
	// //String outputSQL =
	// ImportPDIExportSQL_run.importPDItoSQL("/Users/btheo/Documents/workspace/poiesis/tests/etl-initial.ktr");
	// //System.out.println(outputSQL);
	// long startTime1 = System.currentTimeMillis();
	// // String sqlStr =
	// ImportXLMExportSQL.importXLMtoSQL("tests/etl-produced.xml");
	// String sqlStr =
	// ImportXLMExportSQL.importXLMtoSQL("tests/etl-initial_agn.xml");
	// long endTime1 = System.currentTimeMillis();
	// long totalTime1 = endTime1 - startTime1;
	// System.out.println("total time of turning to sql in millis:
	// "+totalTime1);
	// // //String sqlStr =
	// ImportXLMExportSQL.importXLMtoSQL("tests/flow1_simple.xml");
	// System.out.println("----------------------------------");
	// System.out.println(sqlStr);
	// System.out.println("-------");
	//
	// Connection connection = null;
	// Statement stmt = null;
	// try{
	// //STEP 2: Register JDBC driver
	// Class.forName("org.postgresql.Driver");
	// System.out.println("PostgreSQL JDBC Driver Registered!");
	// //STEP 3: Open a connection
	// System.out.println("Connecting to database...");
	// connection = DriverManager.getConnection(DB_URL,USER,PASS);
	//
	// //STEP 4: Execute a query
	// stmt = connection.createStatement();
	// String sql;
	// sql = "drop table output;";
	// stmt.executeUpdate(sql);
	// System.out.println("Creating statement...");
	// long startTime = System.currentTimeMillis();
	// //stmt = connection.createStatement();
	// sql = sqlStr;
	// stmt.executeUpdate(sql);
	// long endTime = System.currentTimeMillis();
	// long totalTime = endTime - startTime;
	// System.out.println("total time of turning to sql in millis:
	// "+totalTime1);
	// System.out.println("total time of executing sql in millis: "+totalTime);
	//
	// //STEP 5: Extract data from result set
	// // while(rs.next()){
	// // //Retrieve by column name
	// // int id = rs.getInt("id");
	// // int age = rs.getInt("age");
	// // String first = rs.getString("first");
	// // String last = rs.getString("last");
	// //
	// // //Display values
	// // System.out.print("ID: " + id);
	// // System.out.print(", Age: " + age);
	// // System.out.print(", First: " + first);
	// // System.out.println(", Last: " + last);
	// // }
	// //STEP 6: Clean-up environment
	// //rs.close();
	// stmt.close();
	// connection.close();
	// }catch (ClassNotFoundException ex) {
	//
	// System.out.println("Where is your PostgreSQL JDBC Driver? "
	// + "Include in your library path!");
	// ex.printStackTrace();
	// return;
	//
	// }catch (SQLException eq) {
	//
	// System.out.println("Connection Failed! Check output console");
	// eq.printStackTrace();
	// return;
	//
	// }
	// finally{
	// //finally block used to close resources
	// try{
	// if(stmt!=null)
	// stmt.close();
	// }catch(SQLException se2){
	// }// nothing we can do
	// try{
	// if(connection!=null)
	// connection.close();
	// }catch(SQLException se){
	// se.printStackTrace();
	// }//end finally try
	// }//end try
	// System.out.println("Goodbye!");
	//
	//
	//
	// // //------------------
	// // tFlowGraph engAgnosticTFlowGraph =
	// ImportPDIExportSQL_run.importFlowToXlmAgnostic("tests/etl-initial.ktr",
	// tEngineType.PDI.name());
	// // engAgnosticTFlowGraph.printDetailedGraph("");
	// // WriteGraphToXLM xlmWriter = new WriteGraphToXLM();
	// // String xLMString = xlmWriter.printXLM(engAgnosticTFlowGraph);
	// // //-------------------
	//
	// //System.out.println(efgAddedFltr.toStringXLM());
	//
	// // AddOperation ao = new AddOperation(ETLNodeKind.Operator,
	// // ETLOperationType.etlOperationTypes.get("Filter"),"FilterNull");
	// // System.out.println(ao.getAssociatedETLOperation()); AddOperation ao2
	// // =
	// // new AddOperation(ETLNodeKind.Operator,
	// // ETLOperationType.etlOperationTypes.get("Filter"),"FilterNull2");
	// // System.out.println(ao2.getAssociatedETLOperation()); AddOperation ao3
	// // =
	// // new AddOperation(ETLNodeKind.Operator,
	// // ETLOperationType.etlOperationTypes.get("Filter"),"FilterNull3");
	// // AddOperation ao4 = new AddOperation(ETLNodeKind.Operator,
	// // ETLOperationType.etlOperationTypes.get("Filter"),"FilterNull4");
	// // System.out.println(ao3.getAssociatedETLOperation()); StepSequence
	// // sseq =
	// // new StepSequence(); sseq.addStepChild(ao); sseq.addStepChild(ao2);
	// // sseq.addStepChild(ao3); StepSequence sseq2 = new StepSequence();
	// // sseq2.addStepChild(ao); sseq2.addStepChild(ao2);
	// // sseq2.addStepChild(ao3);
	// // sseq2.addStepChild(ao4); StepFlow sflow = new StepFlow();
	// // sflow.addSequenceChild(sseq); sflow.addSequenceChild(sseq2);
	// // System.out.println("sequence: "+sseq.getAsETLFlowGraph());
	// // System.out.println("flow: "+sflow.getAsETLFlowGraph()); Iterator it =
	// // sseq.getAsETLFlowGraph().iterator(); ETLFlowGraph efg1 =
	// // (ETLFlowGraph)
	// // sseq.getAsETLFlowGraph().clone(); ETLFlowGraph efg2 = (ETLFlowGraph)
	// // sseq.getAsETLFlowGraph().clone(); System.out.println("efg1: "+efg1);
	// // System.out.println("efg2: "+efg2);
	// //
	// //
	// //
	// // ObjectPair op1 = new ObjectPair(); op1.left= 2; op1.right= 1;
	// // ObjectPair
	// // op2 = new ObjectPair(); op2.left= 3; op2.right= 2;
	// // ArrayList<ObjectPair>
	// // cp= new ArrayList<ObjectPair>(); cp.add(op1); //cp.add(op2);
	// // System.out.println(efg1.connectToGraph(efg2, cp));
	//
	// /*
	// *
	// * PatternDeployer pd = new PatternDeployer(); pd.setEfgraph(efg);
	// * ArrayList<QPattern> activePs = new ArrayList<QPattern>();
	// * IGeneratorTesterFilter filterTstP = new IGeneratorTesterFilter();
	// * FilterNullValues filterNV = new FilterNullValues();
	// * filterTstP.setRangeMin(5); filterTstP.setRangeMax(10);
	// * IGeneratorTesterJoin joinTstP = new IGeneratorTesterJoin();
	// * //activePs.add(filterTstP); activePs.clear();
	// * activePs.add(filterTstP);
	// *
	// * // activePs.add(filterNV); pd.setActiveQPatterns(activePs);
	// * RandomDeployment randomP = new RandomDeployment(); ETLFlowGraph
	// * efgAddedFltr = pd.deployPatterns(randomP);
	// * System.out.println(efgAddedFltr); Iterator itf =
	// * efgAddedFltr.iterator(); while (itf.hasNext()){
	// * System.out.println(efgAddedFltr
	// * .getEtlFlowOperations().get(itf.next())); }
	// *
	// * System.out.println(efgAddedFltr.toStringXLM());
	// * efgAddedFltr.visualizeGraph(jsonPath);
	// *
	// * // Iterator iteratore = efgAddedFltr.edgeSet().iterator(); // ETLEdge
	// * eee = (ETLEdge) iteratore.next(); // try { //
	// * System.out.println("adding "+eee.toString()); //
	// * efgAddedFltr.addDagEdge(eee.getSource(), eee.getTarget()); // // eee
	// * = (ETLEdge) iteratore.next(); //
	// * System.out.println("adding "+eee.toString()); //
	// * efgAddedFltr.addDagEdge(eee.getSource(), eee.getTarget()); // // }
	// * catch (CycleFoundException e) { // // TODO Auto-generated catch block
	// * // e.printStackTrace(); // } // //
	// * System.out.println(efgAddedFltr.toStringXLM()); //
	// * System.out.println("---------------------------"); //
	// * efgAddedFltr.makeEdgesDistinct(); //
	// * System.out.println(efgAddedFltr.toStringXLM());
	// */
	//
	// }
	//

	/**
	 * 
	 * Returns an array list of object pairs. Each object pair has a left part
	 * containing an application point and a right part, which is an object pair
	 * containing the fitness value for that application point as an integer
	 * (left part) and comments about applicability/fitness of that application
	 * point as a string (right part). These are the application points that
	 * pass the applicability tests (conditions) for the specific pattern. If no
	 * potential points are detected it returns an empty array list.
	 * 
	 * @param efg
	 * @param qp
	 * @return
	 */
	public ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> locatePotentialApplPoints(ETLFlowGraph efg,
			QPattern qp) {
		ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>> finalList = new ArrayList<ObjectPair<ApplPoint, ObjectPair<Integer, String>>>();
		if (qp.getAPTypes().contains(QPattern.applPointType.edge)) {
			Set<ETLEdge> edges = efg.edgeSet();
			ObjectPair<Boolean, String> objp = new ObjectPair<Boolean, String>();
			String comments = "";
			String srcName, trgName;
			for (ETLEdge e : edges) {
				// ===================// TODO: needs to change to take under
				// considerations where the same pattern can be deployed within
				// itself if it has different configurations!
				// discard edges that are connected to previous deployment of
				// the same pattern.
				srcName = efg.getEtlFlowOperations().get(e.getSource()).getOperationName();
				trgName = efg.getEtlFlowOperations().get(e.getTarget()).getOperationName();
				// System.out.println("srcName"+srcName+" and target:
				// "+trgName);
				if (!((srcName.startsWith("FCP" + qp.getPatternId()))
						|| (trgName.startsWith("FCP" + qp.getPatternId())))) {
					// ===================
					EdgeApplPoint eap = new EdgeApplPoint();
					// IMPORTANT!!! first set the graph and then the edge so
					// that
					// the elements of the edge correspond to the actual
					// elements of
					// the graph
					eap.setEfGraph(efg);
					eap.seteEdge(e);
					objp = qp.isApplicable(eap);
					if (objp.left == true) {
						ObjectPair<Integer, String> objpfit = new ObjectPair<Integer, String>();
						objpfit = qp.getFitness(eap);
						// add the applicability comments to the fitness
						// comments
						comments = "fitness comments: ";
						if ((objpfit != null)) {
							comments += objpfit.right;
						}
						comments += "applicability comments: ";
						if ((objp.right != null)) {
							comments += objp.right;
						}
						objpfit.right = comments;
						ObjectPair<ApplPoint, ObjectPair<Integer, String>> opfull = new ObjectPair<ApplPoint, ObjectPair<Integer, String>>();
						opfull.left = eap;
						opfull.right = objpfit;
						finalList.add(opfull);
					}
				}
			}
		}
		if (qp.getAPTypes().contains(QPattern.applPointType.node)) {
			String nodeName;
			Iterator nodes = efg.iterator();
			ObjectPair<Boolean, String> objp2 = new ObjectPair<Boolean, String>();
			String comments = "";
			while (nodes.hasNext()) {
				int nodeId = (int) nodes.next();
				ETLFlowOperation efo = efg.getEtlFlowOperations().get(nodeId);
				// ===================// TODO: needs to change to take under
				// considerations where the same pattern can be deployed within
				// itself if it has different configurations!

				// discard nodes that are previous deployment of the same
				// pattern.
				nodeName = efo.getOperationName();
				if (!(nodeName.startsWith("FCP" + qp.getPatternId()))) {
					// ===================
					NodeApplPoint nap = new NodeApplPoint();
					nap.setEfOperator(efo);
					nap.setEfGraph(efg);
					objp2 = qp.isApplicable(nap);
					if (objp2.left == true) {
						ObjectPair<Integer, String> objpfit = new ObjectPair<Integer, String>();
						objpfit = qp.getFitness(nap);
						// add the applicability comments to the fitness
						// comments
						comments = "fitness comments: ";
						comments += objpfit.right;
						comments += "applicability comments: ";
						comments += objp2.right;
						objpfit.right = comments;
						ObjectPair<ApplPoint, ObjectPair<Integer, String>> opfull = new ObjectPair<ApplPoint, ObjectPair<Integer, String>>();
						opfull.left = nap;
						opfull.right = objpfit;
						finalList.add(opfull);
					}
				}
			}
		}
		if (qp.getAPTypes().contains(QPattern.applPointType.graph)) {
			ObjectPair<Boolean, String> objp2 = new ObjectPair<Boolean, String>();
			String comments = "";
			GraphApplPoint gap = new GraphApplPoint();
			gap.setEfGraph(efg);
			objp2 = qp.isApplicable(gap);
			if (objp2.left == true) {
				ObjectPair<Integer, String> objpfit = new ObjectPair<Integer, String>();
				objpfit = qp.getFitness(gap);
				// add the applicability comments to the fitness comments
				comments = "fitness comments: ";
				comments += objpfit.right;
				comments += " applicability comments: ";
				comments += objp2.right;
				objpfit.right = comments;
				ObjectPair<ApplPoint, ObjectPair<Integer, String>> opfull = new ObjectPair<ApplPoint, ObjectPair<Integer, String>>();
				opfull.left = gap;
				opfull.right = objpfit;
				finalList.add(opfull);
			}
		}

		return finalList;

	}
}
