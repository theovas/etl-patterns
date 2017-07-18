package wfPatterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import operationDictionary.ExpressionOperator;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionConstant;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.operation.ETLFlowOperation;
import patternUtils.GenericFilter;
import qPatterns.QPattern;
import qPatterns.QPatternName;
import structures.NodeApplPoint;

/**
 * This pattern adds a filter that applies the condition: "lower than a random
 * integer" to one random numeric attribute (actually the first numeric
 * attribute found) of the filter input schema
 * 
 * @author vasileios
 *
 */
public class FilterLTRandom extends GenericFilter {

	private int RangeMax = Integer.MAX_VALUE;
	private int RangeMin = 0;
	public static int counter=0;

	@Override
	public void setFilterCondition(ArrayList<Attribute> attrs) {
		// gets the first and only element of the attrs arraylist
		Attribute a = attrs.iterator().next();
		if (a != null) {
			ExpressionAttribute lex = new ExpressionAttribute(a);
			// lex.getComparableValue()
			System.out.println("left comparable value: "
					+ lex.getComparableValue());
			int max = this.getRangeMax();
			int min = this.getRangeMin();
			String value = "0";
			if (max > min) {
				value = Integer.toString(min + new Random().nextInt(max - min));
			}
			ExpressionConstant rex = new ExpressionConstant(
					a.getAttrDatatype(), value);
			System.out.println("right comparable value: "
					+ rex.getComparableValue());
			ExpressionOperator eop = ExpressionOperator.expressionOperators.get("<");
			Expression fex = new Expression(lex, eop, rex,
					ExpressionKind.EXPRESSION);
			fex.addUsedAttribute(a);
			this.filterEFO.addSemanticsExpressionTree("filter_cond", fex);
			
			// add router dests
			ExpressionConstant lex2 = new ExpressionConstant(
					a.getAttrDatatype(), "TRUE");
			ExpressionConstant rex2 = new ExpressionConstant(
					a.getAttrDatatype(), "1");
			System.out.println("router dest right comparable value: "
					+ rex2.getComparableValue());
			ExpressionOperator eop2 = ExpressionOperator.expressionOperators.get("=");
			Expression fex2 = new Expression(lex2, eop2, rex2,
					ExpressionKind.EXPRESSION);
			fex2.addUsedAttribute(a);
			this.filterEFO.addSemanticsExpressionTree("router_dest", fex2);
			
			ExpressionConstant lex3 = new ExpressionConstant(
					a.getAttrDatatype(), "FALSE");
			ExpressionConstant rex3 = new ExpressionConstant(
					a.getAttrDatatype(), "2");
			System.out.println("router dest right comparable value: "
					+ rex3.getComparableValue());
			ExpressionOperator eop3 = ExpressionOperator.expressionOperators.get("=");
			Expression fex3 = new Expression(lex3, eop3, rex3,
					ExpressionKind.EXPRESSION);
			fex2.addUsedAttribute(a);
			this.filterEFO.addSemanticsExpressionTree("router_dest", fex3);
			
			ExpressionConstant lex4 = new ExpressionConstant(
					a.getAttrDatatype(), "1");
			ExpressionConstant rex4 = new ExpressionConstant(
					a.getAttrDatatype(), "TRUE");
			System.out.println("router dest right comparable value: "
					+ rex4.getComparableValue());
			ExpressionOperator eop4 = ExpressionOperator.expressionOperators.get("=");
			Expression fex4 = new Expression(lex4, eop4, rex4,
					ExpressionKind.EXPRESSION);
			fex2.addUsedAttribute(a);
			this.filterEFO.addSemanticsExpressionTree("card_dest", fex4);
			
			ExpressionConstant lex5 = new ExpressionConstant(
					a.getAttrDatatype(), "2");
			ExpressionConstant rex5 = new ExpressionConstant(
					a.getAttrDatatype(), "FALSE");
			System.out.println("router dest right comparable value: "
					+ rex5.getComparableValue());
			ExpressionOperator eop5 = ExpressionOperator.expressionOperators.get("=");
			Expression fex5 = new Expression(lex5, eop5, rex5,
					ExpressionKind.EXPRESSION);
			fex2.addUsedAttribute(a);
			this.filterEFO.addSemanticsExpressionTree("card_dest", fex5);
			
			
		}
	}

	@Override
	public ArrayList<Attribute> getConditionAttributes() {
		ArrayList<Attribute> atts = (ArrayList<Attribute>) this.filterEFO
				.getInputSchemata().iterator().next().getAttributes();
		Iterator itrAttr = atts.iterator();
		boolean found = false;
		Attribute a = null;
		while ((itrAttr.hasNext()) && (!found)) {
			a = (Attribute) itrAttr.next();
			operationDictionary.Datatype dt = a.getAttrDatatype();
			if (NodeApplPoint.numericAttrTypes.contains(dt)) {
				found = true;
			}
		}
		ArrayList<Attribute> returnAttrs = new ArrayList<Attribute>();
		returnAttrs.add(a);
		return returnAttrs;
	}

	@Override
	public String getOperationName() {
		this.counter++;
		return "Filter_LT_random"+this.counter;
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

	@Override
	public QPatternName getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPatternId() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public QPattern getUpdatedInstance() {

		FilterLTRandom qpUpd = new FilterLTRandom();
//			//increases the lid AND updated accordingly the name of the join operator
//			qpUpd.increaseLid();
			return qpUpd;
	}
}
