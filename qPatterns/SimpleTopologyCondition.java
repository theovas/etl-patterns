/**
 * 
 */
package qPatterns;

import java.util.ArrayList;


import qPatterns.SimpleConditionExpr.exprOperation;


import structures.ApplPoint;
import utilities.ObjectPair;

/**
 * @author vasileios
 * 
 */
public class SimpleTopologyCondition extends TopologyCondition {

	private SimpleConditionExpr expression;
	private static final String separator = "@@";

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see structures.TopologyCondition#check(structures.ApplPoint)
	 */
	@Override
	public ObjectPair<Boolean, String> check(ApplPoint ap) {
		//the default is with the quantifier "there exists at least one"
		return this.check(ap, false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see structures.TopologyCondition#check(structures.ApplPoint)
	 */
	@Override
	public ObjectPair<Boolean, String> check(ApplPoint ap, Boolean quantifier) {
		ArrayList<? extends ApplPoint> aps = this.getCondPoint(ap);
		ObjectPair<Boolean, String> objp = null;
		ObjectPair<Boolean, String> objpFin = new ObjectPair<Boolean, String>();
		String comments = "";
		
		for (ApplPoint apoint:aps ){
		objp = apoint.applyOperationAtLevel(this.getExpression().getCondLevel(), this.getExpression().getCondOp(), this.getExpression().getRightSide());
		comments += separator + objp.right;
		// if at least one application point exists that satisfies the condition
		if (quantifier == false){
		if (objp.left == true)	{
			objpFin.left = true;
			objpFin.right = comments;
			return objpFin;
		}
		}
		// if we are interested for all appliation points
		else if (quantifier == true){
			// if at least on application point does not satisfy the condition
			if ((objp.left == false)||(objp.left == null)){
				objpFin.left = false;
				objpFin.right = comments;
				return objpFin;
			}
		}
		}
		if (quantifier == false){		
				objpFin.left = false;
				objpFin.right = comments;
				return objpFin;		
			}
		else if (quantifier == true){		
			objpFin.left = true;
			objpFin.right = comments;
			return objpFin;		
		}
		
		return null;
	}

	private ArrayList<? extends ApplPoint> getCondPoint(ApplPoint ap){
		switch (this.expression.getCondPoint()) {
        case preceeding:  return ap.getPredecessors();
		case succeeding:  return ap.getSuccessors();
		// the application point inside an arraylist
		case current:  ArrayList<ApplPoint> aps = new ArrayList<ApplPoint>();
		aps.add(ap);
			return aps;
		// if it is a graph application point it will return itself inside an array list 
		// IMPORTANT: this case does not check whether the application point is actually
		// a graph application point - it takes that for granted	
        case graph:  ArrayList<ApplPoint> aps2 = new ArrayList<ApplPoint>();
		aps2.add(ap);
		return aps2;
		default: ArrayList<ApplPoint> aps3 = new ArrayList<ApplPoint>();
		aps3.add(ap);
		return aps3;
    }
	}
	
	public SimpleConditionExpr getExpression() {
		return expression;
	}

	public void setExpression(SimpleConditionExpr expression) {
		this.expression = expression;
	}
	
	

}
