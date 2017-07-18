/**
 * 
 */
package patternUtils;

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
import structures.NodeApplPoint;
import structures.StepComponent;
import structures.StepComposite;
import structures.StepSequence;
import utilities.ObjectPair;

/**
 * 
 * A pattern class for the addition of filter operations. Subclasses should only
 * specify the attributes to be included in the filter and the filter operation(s)
 * on them.
 * 
 * 
 * @author vasileios
 *
 */
public abstract class GenericFilter extends QPattern {
	private static Integer lid = 0;
	protected String operationName = "FCP"+this.getPatternId()+"_Filter";
	protected ETLFlowOperation filterEFO;

	// protected ArrayList<Attribute> FAttributes = new ArrayList<Attribute>();

	// public static ArrayList<ExpressionOperator> availOps = new
	// ArrayList<ExpressionOperator>();
	// static{
	// //availOps.add(ExpressionOperator.expressionOperators.get("=="));
	// availOps.add(ExpressionOperator.expressionOperators.get("<"));
	// availOps.add(ExpressionOperator.expressionOperators.get(">"));
	// }

	public GenericFilter() {
		// TopologyCondition tc = new SimpleConditionExpr();
		this.filterEFO = new ETLFlowOperation(ETLNodeKind.Operator,
				ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter
						.name()), this.getOperationName() + "_" + lid);
		StepSequence ss = new StepSequence();
		lid++;
		this.addAPTypes(QPattern.applPointType.edge);
		ETLStep es = new AddOperation(this.filterEFO);
		ss.addStepChild(es);
		this.addImplementation(ss);

	}

	/*
	 * 
	 * @Override public ETLFlowGraph deployOld(ETLFlowGraph efg, ApplPoint
	 * applPoint) { // EdgeApplPoint eap = (EdgeApplPoint) applPoint; //
	 * ArrayList<ElementApplPoint> sourceAP = eap.getPredecessors(); //
	 * ArrayList<ElementApplPoint> targetAP = eap.getPredecessors(); //get the
	 * first and default implementation StepSequence impl = (StepSequence)
	 * this.getImplementations().get(0); ETLFlowGraph pefg =
	 * impl.getAsETLFlowGraph(); // accessing the etlflowgraph values to find id
	 * of the filter operator Collection c =
	 * pefg.getEtlFlowOperations().values(); Iterator itr = c.iterator(); //
	 * gets the first and only element of the collection, which is the filter
	 * operation filterEFO = (ETLFlowOperation) itr.next(); // this is the
	 * filter node id Integer fid = filterEFO.getNodeID();
	 * 
	 * //// // perhaps this is unnecessary, i have already accessed object ////
	 * filterEFO = pefg.getEtlFlowOperations().get(fid); // // Integer sourceId
	 * = eap.getSourceNodeId(); // Integer targetId = eap.getTargetNodeId(); //
	 * //there is only one predecessor so it gets the first and only element //
	 * Schema fInSch = (Schema)
	 * sourceAP.get(0).getOutputSchemata().get(0).clone(); // ArrayList<Schema>
	 * fis = new ArrayList<Schema>(); // fis.add(fInSch); //
	 * filterEFO.setInputSchemata(fis); // Schema fOutSch = (Schema)
	 * targetAP.get(0).getInputSchemata().get(0).clone(); // ArrayList<Schema>
	 * fos = new ArrayList<Schema>(); // fos.add(fOutSch); //
	 * filterEFO.setOutputSchemata(fos); // set the expression for the filter //
	 * //find a numeric field. note that there will always be such a field
	 * because of the check for application points ArrayList<Attribute> atts =
	 * (ArrayList<Attribute>) fInSch.getAttributes(); Iterator itrAttr =
	 * atts.iterator(); boolean found = false; Attribute a=null; while
	 * ((itrAttr.hasNext())&&(!found)){ a = (Attribute) itrAttr.next();
	 * operationDictionary.Datatype dt = a.getAttrDatatype(); if ((dt ==
	 * operationDictionary.Datatype.Double)||(dt ==
	 * operationDictionary.Datatype.Float)||(dt ==
	 * operationDictionary.Datatype.Integer)||(dt ==
	 * operationDictionary.Datatype.Long)){ found =true; } } // create and set
	 * the filter expression this.setFilterCondition(filterEFO); //
	 * System.out.println("attr name: "+a.getAttrName()); // ExpressionAttribute
	 * lex = new ExpressionAttribute(a); // //lex.getComparableValue() //
	 * System.out.println("left comparable value: "+lex.getComparableValue());
	 * // int max = this.getRangeMax(); // int min = this.getRangeMin(); //
	 * String value = "0"; // if (max>min){ // value = Integer.toString(min +
	 * new Random().nextInt(max - min)); // } // ExpressionConstant rex = new
	 * ExpressionConstant(a.getAttrDatatype(),value); //
	 * System.out.println("right comparable value: "+rex.getComparableValue());
	 * // ExpressionOperator eop = availOps.get(new
	 * Random().nextInt(availOps.size())); //
	 * //System.out.println("operator comparable value: "
	 * +eop.getComparableValue()); // Expression fex = new Expression(lex, eop,
	 * rex, ExpressionKind.EXPRESSION); // fex.addUsedAttribute(a); //
	 * fefo.addSemanticsExpressionTree("filter_cond", fex); //
	 * ETLNonFunctionalCharacteristic efc = new
	 * ETLNonFunctionalCharacteristic("card_origin"
	 * ,"","1","=","",efg.getEtlFlowOperations
	 * ().get(sourceId).getOperationName()); // fefo.addoProperty("card_origin",
	 * efc);
	 * 
	 * // // change the card_origin of the succeeding operator to replace it
	 * with the added filter's attributes. // ETLFlowOperation sucOp =
	 * efg.getEtlFlowOperations().get(targetId); //ArrayList<String,
	 * ArrayList<ETLNonFunctionalCharacteristic>> sucoProps // Iterator itr=
	 * sucOp.getoProperties().entrySet().iterator(); // for () //
	 * ETLNonFunctionalCharacteristic efc2 = new
	 * ETLNonFunctionalCharacteristic("card_origin"
	 * ,"","1","=","",efg.getEtlFlowOperations
	 * ().get(sourceId).getOperationName()); // fefo.addoProperty("card_origin",
	 * efc2); //return(efg.insetSubgraphToEdge(pefg, eap.geteEdge(), fid, fid));
	 * return this.getImplementations().get(0).integrateToGraph(efg, applPoint);
	 * 
	 * }
	 */

	/**
	 * Deploys the pattern by adding to the filter the input schema of the
	 * preceding operation and the output schema of the succeeding operation. It
	 * also creates a desired condition. The application point is an edge.
	 */
	@Override
	public ETLFlowGraph deploy(ApplPoint applPoint) {
		EdgeApplPoint eap = (EdgeApplPoint) applPoint;
		// get the first and default implementation
		StepSequence impl = (StepSequence) this.getImplementations().get(0);
		ETLFlowGraph pefg = impl.getAsETLFlowGraph(applPoint);
		// create and set the filter expression on the selected attribute(s).
		// IMPORTANT!!! This method follows after the getAsETLFLowGraph(), so
		// that the schemata have been populated with attributes.
		this.setFilterCondition(this.getConditionAttributes());
		// the following works because the iteration is in topological order and
		// there is only one node in this efg
		int filterNodeId = (int) pefg.iterator().next();
		return applPoint.getEfGraph().insetSubgraphToEdge(pefg, eap.geteEdge(),
				filterNodeId, filterNodeId);

	}

	/**
	 * 
	 * This method adds the filter condition(s) to the filter ETLFlowOperation
	 * of the GenericFilter pattern. During its call it should modify the
	 * <it>filterEFO</it> property of the GenericFilter class.
	 * 
	 * @param attrs
	 */
	public abstract void setFilterCondition(ArrayList<Attribute> attrs);

	/**
	 * 
	 * This method returns the attributes on which the filter condition should
	 * be applied.
	 * 
	 */
	public abstract ArrayList<Attribute> getConditionAttributes();

	/*
	 * (non-Javadoc)
	 * 
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

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public ETLFlowOperation getFilterEFO() {
		return filterEFO;
	}

	public void setFilterEFO(ETLFlowOperation filterEFO) {
		this.filterEFO = filterEFO;
	}

	// public ArrayList<Attribute> getFAttributes() {
	// return FAttributes;
	// }
	//
	//
	// public void setFAttributes(ArrayList<Attribute> fAttributes) {
	// FAttributes = fAttributes;
	// }

}
