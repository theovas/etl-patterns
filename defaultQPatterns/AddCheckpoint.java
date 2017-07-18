package defaultQPatterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;
import qPatterns.QPattern;
import qPatterns.QPatternName;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;
import structures.AddOperation;
import structures.ApplPoint;
import structures.ElementApplPoint;
import structures.ETLStep;
import structures.NodeApplPoint;
import structures.StepSequence;
import utilities.ObjectPair;
/**
 * 
 * @author vasileios
 *
 */

public class AddCheckpoint extends QPattern {
	
	private static Integer lid = 0;
	private String filename = "c/c//c/c/c";
	private OperationTypeName operationTypeName = OperationTypeName.FileOutput;
	private String storage_type = "LocalFileSystem";
	private static final int patternId = 2;

	public AddCheckpoint() {
//		TopologyCondition tc = new SimpleConditionExpr();
//		// There must be preceding and a succeeding operation of any type
//		tc.getPrecNodeOpTypes().add("ANY");
//		tc.getSucNodeOpTypes().add("ANY");
//		this.setApplicPrereqs(tc);
//		this.

		//TopologyCondition tc = new SimpleConditionExpr();
		
		StepSequence ss = new StepSequence();
		//lid++;
		this.addAPTypes(QPattern.applPointType.node);
		ETLFlowOperation efo = new ETLFlowOperation(ETLNodeKind.Datastore,
			//ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
				ETLOperationType.etlOperationTypes.get(operationTypeName.name()),
				"FCP"+this.getPatternId()+"_Checkpoint_" + lid);
		ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic(
				"file_name", "", "file_name", "=", "", filename);
		efo.addoProperty("file_name", efc);
		ETLNonFunctionalCharacteristic efc2 = new ETLNonFunctionalCharacteristic(
				"storage_type", "", "storage_type", "=", "", storage_type);
		efo.addoProperty("storage_type", efc2);
		ETLStep es = new AddOperation(efo);
		ss.addStepChild(es);
this.addImplementation(ss);
		

	}
	
	/* 
	 * Deploys the pattern by adding to the file output operation the output schema of the application point operation.
	 * The application point is a node.
	*/
	@Override
	public ETLFlowGraph deploy(ApplPoint applPoint) {
		NodeApplPoint napplPoint = (NodeApplPoint) applPoint;
		// get the first and default implementation 
		StepSequence impl = (StepSequence) this.getImplementations().get(0);
		StepSequence ss = new StepSequence();
		// if it is not clone, the actual EFOperation is being modified
		ETLFlowOperation efop = (ETLFlowOperation) napplPoint.getEfOperator().clone();
		efop.setNodeID(Integer.MAX_VALUE);
		//ss.addStepChild(new AddOperation(napplPoint.getEfOperator()));
		ss.addStepChild(new AddOperation(efop));
		ss.addStepChild(impl);
		ETLFlowGraph pefg = ss.getAsETLFlowGraph(applPoint);
		//System.out.println("dinw: "+pefg);
		Iterator it = pefg.iterator();
		// the following works because the iteration is in topological order
		int cloneNodeId = (int) it.next(); 
		// int outputNodeId = (int) it.next(); 
		ArrayList<ObjectPair> connectionPoints = new ArrayList<ObjectPair>();
		ObjectPair op = new ObjectPair();
		op.left = napplPoint.getId();
		//System.out.println("left: "+op.left);
		op.right = cloneNodeId;
		//System.out.println("right: "+op.right);
		connectionPoints.add(op);
		//System.out.println("size: "+connectionPoints.size());
		return applPoint.getEfGraph().connectToGraph(pefg, connectionPoints);
		
	}

	
	/* 
	 * Deploys the pattern by adding to the file output operation the output schema of the application point operation.
	 * The application point is a node.

	@Override
	public ETLFlowGraph deploy-old(ApplPoint applPoint) {
		ElementApplPoint elapplPoint = (ElementApplPoint) applPoint;
		StepSequence impl = (StepSequence) this.getImplementations().get(0);
		ETLFlowGraph pefg = impl.getAsETLFlowGraph();
		// accessing the etlflowgraph values to find id of the filter operator
		Collection c = pefg.getEtlFlowOperations().values();
		Iterator itr = c.iterator();
		// gets the first and only element of the collection, which is the filter operation
		ETLFlowOperation fefo = (ETLFlowOperation) itr.next();
		Schema fInSch = (Schema) elapplPoint.getOutputSchemata().get(0).clone();
		ArrayList<Schema> fis = new ArrayList<Schema>();
		fis.add(fInSch);
		fefo.setInputSchemata(fis);
		ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic("card_origin","","1","=","",fefo.getOperationName());
		fefo.addoProperty("card_origin", efc);
		--------
		StepSequence ss = new StepSequence();
		this.addAPTypes(QPattern.applPointType.node);
		ETLStep es = new AddOperation(ETLNodeKind.Datastore,
			//ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
				ETLOperationType.etlOperationTypes.get(OperationTypeName.FileOutput.name()),
				"Checkpoint_" + lid);
		ss.addStepChild(es);
this.addImplementation(ss);
		
		
		return null;
		
	}
	 */
	/* (non-Javadoc)
	 * @see qPatterns.QPattern#getFitness(structures.ApplPoint)
	 */
	/* (non-Javadoc)
	 * @see qPatterns.QPattern#getFitness(structures.ApplPoint)
	 */
	@Override
	public ObjectPair<Integer, String> getFitness(ApplPoint ap) {
		// for now it is always default value
		ObjectPair<Integer, String> op = new ObjectPair<Integer, String>();
		op.left = 50;
		op.right = "no checks implemented yet";
		return op;
	}

	/**
	 * @return the lid
	 */
	public static Integer getLid() {
		return lid;
	}

	/**
	 * @param lid the lid to set
	 */
	public static void setLid(Integer lid) {
		AddCheckpoint.lid = lid;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the operationTypeName
	 */
	public OperationTypeName getOperationTypeName() {
		return operationTypeName;
	}

	/**
	 * @param operationTypeName the operationTypeName to set
	 */
	public void setOperationTypeName(OperationTypeName operationTypeName) {
		this.operationTypeName = operationTypeName;
	}

	/**
	 * @return the storage_type
	 */
	public String getStorage_type() {
		return storage_type;
	}

	/**
	 * @param storage_type the storage_type to set
	 */
	public void setStorage_type(String storage_type) {
		this.storage_type = storage_type;
	}

	@Override
	public QPatternName getName() {
		// TODO Auto-generated method stub
		return QPatternName.AddCheckpoint;
	}

	@Override
	public int getPatternId() {
		return this.patternId;
	}

	@Override
	public QPattern getUpdatedInstance() {

		AddCheckpoint qpUpd = new AddCheckpoint();
//			//increases the lid AND updated accordingly the name of the join operator
//			qpUpd.increaseLid();
			return qpUpd;
	}


	
	

}
