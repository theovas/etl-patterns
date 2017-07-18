package structures;

import java.util.ArrayList;

import etlFlowGraph.schema.Schema;
import qPatterns.SimpleConditionExpr.applAspect;
import qPatterns.SimpleConditionExpr.exprOperation;
import utilities.ObjectPair;

public abstract class ElementApplPoint extends ApplPoint {
	
	/**
	 * Applies an operation (boolean check) at a specific aspect of this element application
	 * point. The right part argument is an array list of strings representing
	 * either the attributes of a schema (perhaps can be extended using a set to
	 * be independent of the order of the attributes) or having size 1, if it
	 * only contains one string that has to be compared/evaluated. Returns an
	 * object pair of a boolean indicating whether or not the operation has been
	 * estimated to true and a string that contains related comments.
	 */
	public abstract ObjectPair<Boolean,String> applyElementOperationOnAspect(applAspect al, exprOperation eo, ArrayList<String> rightPart);
	
	public ObjectPair<Boolean,String> applyOperationAtLevel(applAspect al, exprOperation eo, ArrayList<String> rightPart){
		return applyElementOperationOnAspect(al,eo,rightPart);
	};
	
	public abstract ArrayList<Schema> getInputSchemata();
	
	public abstract ArrayList<Schema> getOutputSchemata();

}
