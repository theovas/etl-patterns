/**
 * 
 */
package qPatterns;

import java.util.ArrayList;

/**
 * @author vasileios
 * 
 */
public class SimpleConditionExpr {

	/**
	 * 
	 * Enumeration to show relative position.<br>
	 * <br>
	 * preceeding: refers to the previous node from the current point or the source of the edge<br>
	 * succeeding: refers to the next node from the current point or the target of the edge<br>
	 * current: refers to the current element itself, either node or edge<br>
	 * graph: refers to the complete graph that the current point belongs to<br>
	 * 
	 * @author vasileios
	 * 
	 */
	public enum relPosition {
		preceeding, succeeding, current, graph;
	}

	/**
	 * 
	 * Enumeration to show aspect on which condition applies.<br>
	 * <br>
	 * inSchema: refers to the input schema of an operator<br>
	 * outSchema: refers to the output schema of an operator<br>
	 * opType: refers to the operation type of an operator<br>
	 * nodeKind: refers to the node kind of a node<br>
	 * 
	 * @author vasileios
	 * 
	 */
	public enum applAspect {
		nodeId, inSchema, outSchema, opType, nodeKind;
	}

	/**
	 * 
	 * Enumeration to show the operation of the expression.<br>
	 * <br>
	 * equal: equals<br>
	 * notEqual: does not equal<br>
	 * includesDT: (a schema) includes a datatype<br>
	 * 
	 * Todo: To be extended to include more operators for more expressiveness
	 * 
	 * @author vasileios
	 * 
	 */
	public enum exprOperation {
		isEqualTo, isNotEqualTo, includesDT;
	}

	// the point (at a node level) at which the condition applies,
	// relative to the (potential) pattern application point
	private relPosition condPoint;

	// the level at which condition applies
	private applAspect condLevel;

	// the operationf the expression
	private exprOperation condOp;
	private ArrayList<String> rightSide;

	public relPosition getCondPoint() {
		return condPoint;
	}

	public void setCondPoint(relPosition condPoint) {
		this.condPoint = condPoint;
	}

	public applAspect getCondLevel() {
		return condLevel;
	}

	public void setCondLevel(applAspect condLevel) {
		this.condLevel = condLevel;
	}

	public exprOperation getCondOp() {
		return condOp;
	}

	public void setCondOp(exprOperation condOp) {
		this.condOp = condOp;
	}

	public ArrayList<String> getRightSide() {
		return rightSide;
	}

	public void setRightSide(ArrayList<String> rightSide) {
		this.rightSide = rightSide;
	}

}
