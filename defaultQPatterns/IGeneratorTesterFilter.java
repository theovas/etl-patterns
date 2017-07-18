/**
 * 
 */
package defaultQPatterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import operationDictionary.ETLOperationType;
import operationDictionary.ExpressionOperator;
import operationDictionary.OperationTypeName;
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
import qPatterns.QPattern;
import qPatterns.SimpleConditionExpr;
import qPatterns.TopologyCondition;
import structures.AddOperation;
import structures.ApplPoint;
import structures.ETLStep;
import structures.EdgeApplPoint;
import structures.ElementApplPoint;
import structures.StepComponent;
import structures.StepComposite;
import structures.StepSequence;
import utilities.ObjectPair;

/**
 * 
 * A pattern class to test addition of filter operations. 
 * 
 * 
 * @author vasileios
 *
 */
public class IGeneratorTesterFilter extends QPattern{
	private static Integer lid = 0;
	private int RangeMax = Integer.MAX_VALUE;
	private int RangeMin = 0;
	
	public static ArrayList<ExpressionOperator> availOps = new ArrayList<ExpressionOperator>();
	static{
		//availOps.add(ExpressionOperator.expressionOperators.get("=="));
		availOps.add(ExpressionOperator.expressionOperators.get("<"));
		availOps.add(ExpressionOperator.expressionOperators.get(">"));
	}
	
	public IGeneratorTesterFilter() {
		//TopologyCondition tc = new SimpleConditionExpr();
		
		StepSequence ss = new StepSequence();
		lid++;
		this.addAPTypes(QPattern.applPointType.edge);
		ETLStep es = new AddOperation(ETLNodeKind.Operator,
			//ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()"Filter"),
				ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter.name()),
				"Filter" + lid);
		ss.addStepChild(es);
this.addImplementation(ss);
		

	}
	
	
	/* 
	 * Deploys the pattern by adding to the filter the input schema of the preceding operation and the output schema of the succeeding operation.
	 * It also creates a random condition with a numeric field of the preceding operation. The application point is an edge.
	 */
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
		Schema fOutSch = (Schema) targetAP.get(0).getInputSchemata().get(0).clone();
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
		while ((itrAttr.hasNext())&&(!found)){
			a = (Attribute) itrAttr.next();
			operationDictionary.Datatype dt = a.getAttrDatatype();
			if ((dt == operationDictionary.Datatype.Double)||(dt == operationDictionary.Datatype.Float)||(dt == operationDictionary.Datatype.Integer)||(dt == operationDictionary.Datatype.Long)){
			found =true;	
			}
		}
		// create and set the filter expression
		System.out.println("attr name: "+a.getAttrName());
		ExpressionAttribute lex = new ExpressionAttribute(a);
		//lex.getComparableValue()
		System.out.println("left comparable value: "+lex.getComparableValue());
		int max = this.getRangeMax();
		int min = this.getRangeMin();
		String value = "0";
		if (max>min){
		value = Integer.toString(min + new Random().nextInt(max - min));
		}
		ExpressionConstant rex = new ExpressionConstant(a.getAttrDatatype(),value);
		System.out.println("right comparable value: "+rex.getComparableValue());
		ExpressionOperator eop = availOps.get(new Random().nextInt(availOps.size()));
		//System.out.println("operator comparable value: "+eop.getComparableValue());
		Expression fex = new Expression(lex, eop, rex, ExpressionKind.EXPRESSION);
		fex.addUsedAttribute(a);
		fefo.addSemanticsExpressionTree("filter_cond", fex);
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


	public int getRangeMax() {
		return RangeMax;
	}


	public void setRangeMax(int rangeMax) {
		RangeMax = rangeMax;
	}


	public int getRangeMin() {
		return RangeMin;
	}


	public void setRangeMin(int rangeMin) {
		RangeMin = rangeMin;
	}

	
}
