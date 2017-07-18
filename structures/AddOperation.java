/**
 * 
 */
package structures;

import importXLM.ImportXLMToETLGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import operationDictionary.ETLOperationType;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;

/**
 * @author vasileios
 *
 */
public class AddOperation extends ETLStep {

	private ETLFlowOperation efo = null;
	private ETLOperationType opType;
	private ETLNodeKind nodeKind;
	private String operationName;
	// whether or not the schemata of the associated etl flow operator are
	// immutable or not
	private boolean immutableSchema = false;

	public AddOperation(ETLFlowOperation efo) {
		super();
		this.nodeKind = efo.getNodeKind();
		this.opType = efo.getOperationType();
		this.operationName = efo.getOperationName();
		this.efo = efo;
	}

	public AddOperation(ETLNodeKind nodeKind, ETLOperationType opType,
			String operationName) {
		super();
		this.nodeKind = nodeKind;
		this.opType = opType;
		this.operationName = operationName;
		// TODO: for the non-null case i must check that the fields match
		if (this.efo == null) {
			this.efo = new ETLFlowOperation(nodeKind, opType, operationName);
		}
		// this.integrateToGraph(efg, ap);

	}

	public ETLFlowOperation getEfo() {
		return efo;
	}

	public void setEfo(ETLFlowOperation efo) {
		this.efo = efo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * structures.ETLIntegrable#integrateToGraph(etlFlowGraph.graph.ETLFlowGraph
	 * , structures.ApplPoint)
	 */
	@Override
	public ETLFlowGraph integrateToGraph(ETLFlowGraph efg, ApplPoint applPoint) {
		EdgeApplPoint eap = (EdgeApplPoint) applPoint;
		ArrayList<ElementApplPoint> sourceAP = eap.getPredecessors();
		ArrayList<ElementApplPoint> targetAP = eap.getPredecessors();
		Integer sourceId = eap.getSourceNodeId();
		Integer targetId = eap.getTargetNodeId();
		// there is only one predecessor so it gets the first and only element
		Schema fInSch = (Schema) sourceAP.get(0).getOutputSchemata().get(0)
				.clone();
		ArrayList<Schema> fis = new ArrayList<Schema>();
		fis.add(fInSch);
		efo.setInputSchemata(fis);
		// there is only one successor so it gets the first and only element
		Schema fOutSch = (Schema) targetAP.get(0).getInputSchemata().get(0)
				.clone();
		ArrayList<Schema> fos = new ArrayList<Schema>();
		fos.add(fOutSch);
		efo.setOutputSchemata(fos);
		// ETL NFC must be set for the flow to work
		ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic(
				"card_origin", "", "1", "=", "", efg.getEtlFlowOperations()
						.get(sourceId).getOperationName());
		efo.addoProperty("card_origin", efc);

		return (efg.insetSubgraphToEdge(this.getAsETLFlowGraph(),
				eap.geteEdge(), efo.getNodeID(), efo.getNodeID()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see structures.StepComponent#getAsETLFlowGraph()
	 */
	@Override
	public ETLFlowGraph getAsETLFlowGraph() {
		ETLFlowGraph efg = new ETLFlowGraph();
		efg.addNode(this.efo);
		return efg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see structures.ETLStep#getAssociatedETLOperation()
	 */
	@Override
	public ETLFlowOperation getAssociatedETLOperation() {
		// TODO Auto-generated method stub
		return this.getEfo();
	}

	/*
	 * @see structures.StepComponent#getAsETLFLowGraph(structures.ApplPoint)
	 * 
	 * public ETLFlowGraph getAsETLFLowGraphOld(ApplPoint referencePoint) {
	 * ETLFlowGraph efg = new ETLFlowGraph(); EdgeApplPoint eap =
	 * (EdgeApplPoint) referencePoint; ArrayList<ElementApplPoint> sourceAP =
	 * eap.getPredecessors(); ArrayList<ElementApplPoint> targetAP =
	 * eap.getPredecessors(); Integer sourceId = eap.getSourceNodeId(); Integer
	 * targetId = eap.getTargetNodeId(); //there is only one predecessor so it
	 * gets the first and only element Schema fInSch = (Schema)
	 * sourceAP.get(0).getOutputSchemata().get(0).clone(); ArrayList<Schema> fis
	 * = new ArrayList<Schema>(); fis.add(fInSch); efo.setInputSchemata(fis);
	 * //there is only one successor so it gets the first and only element
	 * Schema fOutSch = (Schema)
	 * targetAP.get(0).getInputSchemata().get(0).clone(); ArrayList<Schema> fos
	 * = new ArrayList<Schema>(); fos.add(fOutSch); efo.setOutputSchemata(fos);
	 * //ETL NFC must be set for the flow to work ETLNonFunctionalCharacteristic
	 * efc = new ETLNonFunctionalCharacteristic("card_origin","","1","=","",efg.
	 * getEtlFlowOperations().get(sourceId).getOperationName());
	 * efo.addoProperty("card_origin", efc); efg.addNode(this.efo); return efg;
	 * }
	 */

	/**
	 * @see structures.StepComponent#getAsETLFLowGraph(structures.ApplPoint)
	 */
	@Override
	public ETLFlowGraph getAsETLFlowGraph(ApplPoint referencePoint) {

		ETLFlowGraph efg = new ETLFlowGraph();
		// if schemata have not been set and set to immutable, meaning that they
		// can change
		if (!((this.isImmutableSchema())
				&& (this.efo.getInputSchemata() != null) && (this.efo
					.getOutputSchemata() != null))) {
			ArrayList<Schema> fis = referencePoint.getRefProperties()
					.getInSchemataClone();
			efo.setInputSchemata(fis);
			// by convention, data store operations only have input schemata
			if (efo.getNodeKind()!=ETLNodeKind.Datastore){
			ArrayList<Schema> fos = this.getOutSchemata(referencePoint);
			efo.setOutputSchemata(fos);
			}

		}
		// ETL NFC must be set for the flow to work
		//
		// if it is not an input source data store node
		if (!(NodeApplPoint.inputSourceTypes.contains(efo.getOperationType()))){
		efo.addoProperty("card_origin", referencePoint.getRefProperties()
				.getEfcClone());
		}
		efg.addNode(this.efo);
		return efg;
	}

	/**
	 * Returns the output schemata that should be assigned to the operation with
	 * regards to a reference point. This covers the cases when the schema is
	 * changed by the operation, e.g., adding or deleting attributes.
	 */
	public ArrayList<Schema> getOutSchemata(ApplPoint referencePoint) {
		ArrayList<Schema> outSchemata = null;
		
		// TODO: This part must be refactored to just call a method that is
		// specific to each operation, e.g.,
		// getModifiedSchemata(ETLFlowOperation.OperationType efoT,
		// ArrayList<Schema> inputSchemata)
		
		// In case of a "Project" operation
		if (this.opType == ETLOperationType.etlOperationTypes.get("Project")) {
			ArrayList<Attribute> projAttrs = null;
			
			outSchemata = new ArrayList<Schema>();
			Schema outSch = new Schema();
			projAttrs = ImportXLMToETLGraph.traverseExTreesGetInputAttributes(this.efo.getSemanticsExpressionTrees(), referencePoint.getRefProperties().getInSchemataClone());
			for (Attribute a : projAttrs) {
				outSch.addAttribute(a);
			}

			outSch.setSchemaCard(1);

			outSchemata.add(outSch);
			
		}
		//
		//
		// here i must take care of ALL the operations that cause the addition
		// or deletion of attributes!!!
		//
		//
		// int the default case there is no schema change, so the output
		// schemata are just copied
		else {
			outSchemata = referencePoint.getRefProperties()
					.getOutSchemataClone();
		}
		return outSchemata;
	}

	@Override
	public void instillProperties(ApplPoint applPoint) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc} A node application point is returned with the properties of
	 * the etl flow operator of this class.
	 * 
	 */
	@Override
	public ApplPoint getReferenceAP() {
		NodeApplPoint nap = new NodeApplPoint();
		// nap.setEfGraph(<somegraph>);
		nap.setEfOperator((ETLFlowOperation) this.efo.clone());
		return nap;
	}

	@Override
	public ETLStep getInitialEndPoint() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public ETLStep getFinalEndPoint() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public StepComponent clone() {
		// TODO Auto-generated method stub
		return new AddOperation((ETLFlowOperation) this.efo.clone());
	}

	/**
	 * @return the immutableSchema
	 */
	public boolean isImmutableSchema() {
		return immutableSchema;
	}

	/**
	 * @param immutableSchema
	 *            the immutableSchema to set
	 */
	public void setImmutableSchema(boolean immutableSchema) {
		this.immutableSchema = immutableSchema;
	}

	@Override
	public ETLFlowGraph getAsETLFlowGraph(ApplPoint referencePoint,
			ArrayList<ETLFlowOperation> opsToBePassed) {
		ETLFlowGraph efg = this.getAsETLFlowGraph(referencePoint);
		//TODO:do something with opsToBePassed
		return efg;
	}

}
