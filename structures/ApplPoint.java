/**
 * 
 */
package structures;

import java.util.ArrayList;

import operationDictionary.ETLOperationType;
import qPatterns.SimpleConditionExpr;
import qPatterns.SimpleConditionExpr.applAspect;
import qPatterns.SimpleConditionExpr.exprOperation;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;
import utilities.ObjectPair;

/**
 * @author vasileios
 *
 */
public abstract class ApplPoint {
	
	protected String id = null;
	protected ETLFlowGraph efGraph = null;
	
	public abstract int getId();
	
	public abstract ReferenceProperties getRefProperties();
	
	public abstract void setRefProperties(ReferenceProperties rp);
	
	public abstract ArrayList<ElementApplPoint> getSuccessors();

	public abstract ArrayList<ElementApplPoint> getPredecessors();
	
	public abstract ArrayList<Integer> getSources();
	
//	public abstract ArrayList<Schema> getInputSchemata();
	
//	public abstract ArrayList<Schema> getOutputSchemata();
	
	public abstract ObjectPair<Boolean,String> applyOperationAtLevel(applAspect al, exprOperation eo, ArrayList<String> rightPart);
	
	public abstract ArrayList<ETLOperationType> getOperationTypes();
	
	public abstract ArrayList<ETLNodeKind> getNodeKinds();

	public ETLFlowGraph getEfGraph() {
		return efGraph;
	}

	public void setEfGraph(ETLFlowGraph efGraph) {
		this.efGraph = efGraph;
	}
	
	public abstract ApplPointId getApplPointId();
	public abstract ApplPointKind getApplPointKind();
	
	
	

}
