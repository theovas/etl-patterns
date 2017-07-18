package defaultQPatterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import qPatterns.QPattern;
import qPatterns.QPatternName;
import qPatterns.SimpleConditionExpr;
import qPatterns.TopologyCondition;
import operationDictionary.ETLOperationType;
import operationDictionary.ExpressionOperator;
import operationDictionary.OperationTypeName;
import patternUtils.GenericFilter;
import structures.AddOperation;
import structures.ApplPoint;
import structures.ETLStep;
import structures.EdgeApplPoint;
import structures.ElementApplPoint;
import structures.StepComponent;
import structures.StepComposite;
import structures.StepSequence;
import utilities.ObjectPair;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionConstant;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;

/**
 * 
 * A pattern to filter entries with null values. Filters all of the attributes of the input schema, meaning that if a null is found as any attribute value, the entry (tuple) is removed.
 * 
 * @author vasileios
 * 
 */
public class FilterNullValues extends GenericFilter {
	private static final int patternId = 3;

	public FilterNullValues() {
		super();
//		TopologyCondition tc = new SimpleConditionExpr();
//		// There must be preceding and a succeeding operation of any type
//		tc.getPrecNodeOpTypes().add("ANY");
//		tc.getSucNodeOpTypes().add("ANY");
//		this.setApplicPrereqs(tc);
		
		

	}

	/*
	
//	/* 
//	 * Deploys the pattern by adding to the filter the input schema of the preceding operation and the output schema of the succeeding operation.
//	 * It also creates a condition for every field to be non-null. The application point is an edge.
//	 
	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ApplPoint applPoint) {
		EdgeApplPoint eap = (EdgeApplPoint) applPoint;
		ArrayList<ElementApplPoint> sourceAP = eap.getPredecessors();
		ArrayList<ElementApplPoint> targetAP = eap.getPredecessors();
		//get the first and only implementation
		StepSequence impl = (StepSequence) this.getImplementations().get(0);
		ETLFlowGraph pefg = impl.getAsETLFlowGraph();
		// accessing the etlflowgraph values to find id of the filter operator
		Collection c = pefg.getEtlFlowOperations().values();
		Iterator itr = c.iterator();
		// gets the first and only element of the collection, which is the filter operation
		ETLFlowOperation fefo = (ETLFlowOperation) itr.next();
		// this is the filter node id
		Integer fid = fefo.getNodeID();
		// perhaps this is unnecessary, i have already accessed object
		fefo = pefg.getEtlFlowOperations().get(fid);
		Integer sourceId = eap.getSourceNodeId();
		Integer targetId = eap.getTargetNodeId();
		Schema fInSch = (Schema) sourceAP.get(0).getOutputSchemata().get(0).clone();
		ArrayList<Schema> fis = new ArrayList<Schema>();
		fis.add(fInSch);
		fefo.setInputSchemata(fis);
		Schema fOutSch = targetAP.get(0).getInputSchemata().get(0);
		ArrayList<Schema> fos = new ArrayList<Schema>();
		fos.add(fOutSch);
		fefo.setOutputSchemata(fos);
		// set the expression for the filter
		//
		//find a numeric field. note that there will always be such a field because of the check for application points
		ArrayList<Attribute> atts = (ArrayList<Attribute>) fInSch.getAttributes();
		Iterator itrAttr = atts.iterator();
		boolean found = false;
		Attribute a=null;
		while ((itrAttr.hasNext())){
			
			a = (Attribute) itrAttr.next();
			//operationDictionary.Datatype dt = a.getAttrDatatype();
			//if ((dt == operationDictionary.Datatype.Double)||(dt == operationDictionary.Datatype.Float)||(dt == operationDictionary.Datatype.Integer)||(dt == operationDictionary.Datatype.Long)){
			//found =true;	
			ExpressionOperator eop = ExpressionOperator.expressionOperators.get("NOT");
			ExpressionOperator eop2 = ExpressionOperator.expressionOperators.get("isNull");
			ExpressionAttribute eat = new ExpressionAttribute(a);
			Expression e1 = new Expression(null, eop2, eat, ExpressionKind.EXPRESSION);
			Expression e2 = new Expression(null, eop, e1, ExpressionKind.EXPRESSION);
			e2.addUsedAttribute(a);
			fefo.addSemanticsExpressionTree("filter_cond", e2);	
		}
		
		ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic("card_origin","","1","=","",efg.getEtlFlowOperations().get(sourceId).getOperationName());
		fefo.addoProperty("card_origin", efc);
//		// change the card_origin of the succeeding operator to replace it with the added filter's attributes.
//		ETLFlowOperation sucOp = efg.getEtlFlowOperations().get(targetId);
		//ArrayList<String, ArrayList<ETLNonFunctionalCharacteristic>> sucoProps
//		Iterator itr= sucOp.getoProperties().entrySet().iterator();
//		for ()
//		ETLNonFunctionalCharacteristic efc2 = new ETLNonFunctionalCharacteristic("card_origin","","1","=","",efg.getEtlFlowOperations().get(sourceId).getOperationName());
//		fefo.addoProperty("card_origin", efc2);
		return(efg.insetSubgraphToEdge(pefg, eap.geteEdge(), fid, fid));
		
	}

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

	@Override
	public void setFilterCondition(ArrayList<Attribute> attrs) {
		
		Iterator<Attribute> itrAttr = attrs.iterator();
		Attribute a=null;
		ExpressionOperator eop = null;
		ExpressionOperator eop2 = null;
		ExpressionAttribute eat = null;
		while ((itrAttr.hasNext())){
			a = (Attribute) itrAttr.next();
			eop = ExpressionOperator.expressionOperators.get("NOT");
			eop2 = ExpressionOperator.expressionOperators.get("isNull");
			eat = new ExpressionAttribute(a);
			Expression e1 = new Expression(null, eop2, eat, ExpressionKind.EXPRESSION);
			Expression e2 = new Expression(null, eop, e1, ExpressionKind.EXPRESSION);
			e2.addUsedAttribute(a);
			// IMPORTANT!!! filterEFO must have previously been initiated, to avoid null pointer exceptions!
			this.filterEFO.addSemanticsExpressionTree("filter_cond", e2);	
		}
		
	}

	@Override
	public ArrayList<Attribute> getConditionAttributes() {
		// returns ALL the attributes of the filter input schema
		return (ArrayList<Attribute>) this.filterEFO.getInputSchemata().iterator().next().getAttributes();
	}

	@Override
	public String getOperationName() {
		// TODO Auto-generated method stub
		return "FCP"+this.getPatternId()+"_Filter_nulls";
	}

	@Override
	public QPatternName getName() {
		// TODO Auto-generated method stub
		return QPatternName.FilterNullValues;
	}

	@Override
	public int getPatternId() {
		// TODO Auto-generated method stub
		return this.patternId;
	}


	@Override
	public QPattern getUpdatedInstance() {

		FilterNullValues qpUpd = new FilterNullValues();
//			//increases the lid AND updated accordingly the name of the join operator
//			qpUpd.increaseLid();
			return qpUpd;
	}



	

}
