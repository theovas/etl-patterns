/**
 * 
 */
package structures;

import java.util.ArrayList;

import operationDictionary.ETLOperationType;
import qPatterns.SimpleConditionExpr.applAspect;
import qPatterns.SimpleConditionExpr.exprOperation;
import utilities.ObjectPair;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;

/**
 * @author vasileios
 *
 */
public class EdgeApplPoint extends ElementApplPoint {
	private ETLEdge eEdge;
	// the id of the source ETLFlowOperator
	private int sourceNodeId = -1;
	// the id of the target ETLFlowOperator
	private int targetNodeId = -1;
	// the source ETLFlowOperator
	private ETLFlowOperation sourceNode = null;
	// the target ETLFlowOperator
	private ETLFlowOperation targetNode = null;

	public int getId() {

		return sourceNodeId;
	}
	
	public String getSignature() {

		return eEdge.toString();
	}

	public int getSourceNodeId() {
		return (int) eEdge.getSource();
	}

	public void setSourceNodeId(int sourceNodeId) {
		this.sourceNodeId = sourceNodeId;
	}

	public int getTargetNodeId() {
		return (int) eEdge.getTarget();
	}

	public void setTargetNodeId(int targetNodeId) {
		this.targetNodeId = targetNodeId;
	}

	public ETLFlowOperation getSourceNode() {
		if ((int) sourceNodeId != -1) {
			return this.efGraph.getEtlFlowOperations().get(sourceNodeId);
		} else {
			return this.efGraph.getEtlFlowOperations().get(this.getSourceNodeId());
		}
	}

	public void setSourceNode(ETLFlowOperation sourceNode) {
		this.sourceNode = sourceNode;
	}

	public ETLFlowOperation getTargetNode() {
		if ((int) targetNodeId != -1) {
			return this.efGraph.getEtlFlowOperations().get(targetNodeId);
		} else {
			return this.efGraph.getEtlFlowOperations().get(this.getTargetNodeId());
		}
	}

	public void setTargetNode(ETLFlowOperation targetNode) {
		this.targetNode = targetNode;
	}

	public ETLEdge geteEdge() {
		return eEdge;
	}

	public void seteEdge(ETLEdge eEdge) {
		this.eEdge = eEdge;
		if (this.efGraph != null){
		this.sourceNode = (ETLFlowOperation) this.efGraph.getEtlFlowOperations().get(eEdge.getSource());
		this.targetNode = (ETLFlowOperation) this.efGraph.getEtlFlowOperations().get(eEdge.getTarget());
	}
	}

	/**
	 * Returns the target node of the etl edge as a node application point
	 */
	@Override
	public ArrayList<ElementApplPoint> getSuccessors() {
		ArrayList<ElementApplPoint> aps = new ArrayList<ElementApplPoint>();
		NodeApplPoint nap = new NodeApplPoint();
		nap.setEfGraph(this.efGraph);
		nap.setEfOperator(this.efGraph.getEtlFlowOperations().get(
				this.getTargetNodeId()));
		aps.add(nap);
		return aps;
	}

	/**
	 * Returns the source node of the etl edge as a node application point
	 */
	@Override
	public ArrayList<ElementApplPoint> getPredecessors() {
		ArrayList<ElementApplPoint> aps = new ArrayList<ElementApplPoint>();
		NodeApplPoint nap = new NodeApplPoint();
		nap.setEfGraph(this.efGraph);
		nap.setEfOperator(this.efGraph.getEtlFlowOperations().get(
				this.getSourceNodeId()));
		aps.add(nap);
		return aps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see structures.ApplPoint#getSources()
	 */
	@Override
	public ArrayList<Integer> getSources() {
		NodeApplPoint nap = new NodeApplPoint();
		nap.setEfGraph(this.efGraph);
		nap.setEfOperator(this.efGraph.getEtlFlowOperations().get(
				this.getSourceNodeId()));
		return (nap.getSources());
	}

	/**
	 * 
	 * By convention this method refers to the source node of the edge of this
	 * edge application point. Applies an operation at a specific application
	 * level of this application point. The right part argument is an array list
	 * of strings representing either the attributes of a schema (perhaps can be
	 * extended using a set to be independent of the order of the attributes) or
	 * having size 1, if it only contains one string that has to be
	 * compared/evaluated. Returns an object pair of a boolean indicating
	 * whether or not the operation has been estimated to true and a string that
	 * contains related comments.
	 * 
	 */
	@Override
	public ObjectPair<Boolean, String> applyElementOperationOnAspect(
			applAspect aa, exprOperation eo, ArrayList<String> rightPart) {
		NodeApplPoint nap = new NodeApplPoint();
		nap.setEfGraph(this.efGraph);
		nap.setEfOperator(this.efGraph.getEtlFlowOperations().get(
				this.getSourceNodeId()));
		return (nap.applyElementOperationOnAspect(aa, eo, rightPart));

	}

	/**
	 * For an edge application point, we consider the input schemata to be the
	 * output schemata of its source node.
	 */
	@Override
	public ArrayList<Schema> getInputSchemata() {
		return this.efGraph.getEtlFlowOperations().get(this.getSourceNodeId())
				.getOutputSchemata();
	}

	/**
	 * For an edge application point, we consider the output schemata to be the
	 * input schemata of its target node.
	 */
	@Override
	public ArrayList<Schema> getOutputSchemata() {
		return this.efGraph.getEtlFlowOperations().get(this.getTargetNodeId())
				.getInputSchemata();
	}

	/**
	 * Returns the operation types of both the source and target nodes
	 * operations
	 */
	@Override
	public ArrayList<ETLOperationType> getOperationTypes() {
		ArrayList<ETLOperationType> ot = new ArrayList<ETLOperationType>();
		ot.add(this.efGraph.getEtlFlowOperations().get(this.getSourceNodeId())
				.getOperationType());
		ot.add(this.efGraph.getEtlFlowOperations().get(this.getTargetNodeId())
				.getOperationType());
		return ot;
	}

	/**
	 * Returns the node kinds of both the source and target nodes operations
	 */
	@Override
	public ArrayList<ETLNodeKind> getNodeKinds() {
		ArrayList<ETLNodeKind> ot = new ArrayList<ETLNodeKind>();
		ot.add(this.efGraph.getEtlFlowOperations().get(this.getSourceNodeId())
				.getNodeKind());
		ot.add(this.efGraph.getEtlFlowOperations().get(this.getTargetNodeId())
				.getNodeKind());
		return ot;
	}

	/**
	 * {@inheritDoc} The output properties of the source node of the edge
	 * application point is used as the reference point for the input properties
	 * and the input properties of the target node as reference for the output
	 * properties. Non functional characteristics are treated as input properties.
	 */
	@Override
	public ReferenceProperties getRefProperties() {
		ReferenceProperties refProps = new ReferenceProperties();
		refProps.setInSchemata(this.sourceNode.getOutputSchemata());
		refProps.setOutSchemata(this.sourceNode.getOutputSchemata());
		//// in the future, the following should be used, with some more logic for the cases that target node has multiple input schemata, e.g., Join or Union operation
		//refProps.setOutSchemata(this.targetNode.getInputSchemata());
		refProps.setEfc(new ETLNonFunctionalCharacteristic("card_origin","","1","=","",this.sourceNode.getOperationName()));
	    // perhaps more properties can be set here...
		
		return refProps;
	}

	@Override
	public void setRefProperties(ReferenceProperties rp) {
		System.out.println("setRefProperties(): this is still empty!!!");
		
	}

	@Override
	public ApplPointId getApplPointId() {
		// TODO Auto-generated method stub
		return new ApplPointId(this.getSourceNode().getOperationName(),this.getTargetNode().getOperationName(),this.getApplPointKind());
	}

	@Override
	public ApplPointKind getApplPointKind() {
		// TODO Auto-generated method stub
		return ApplPointKind.edge;
	}
	
//	public EdgeApplPoint clone(){
//		EdgeApplPoint eap = new EdgeApplPoint();
//		eap.seteEdge((ETLEdge) eEdge.clone());
//		return eap;
//	}

}
