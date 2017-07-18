package patternUtils;

import operationDictionary.ETLOperationType;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import structures.AddOperation;
import structures.ApplPoint;
import structures.ETLStep;
import structures.EdgeApplPoint;
import structures.StepComponent;
import structures.StepSequence;

public abstract class JoinPreserveSchema extends GenericJoin {

	protected ETLFlowOperation projectEFO;


	public StepComponent getFollowingStepComponent() {
		projectEFO = new ETLFlowOperation(ETLNodeKind.Operator,
				ETLOperationType.etlOperationTypes.get("Project"), "FCP"+this.getPatternId()+"_Project_"
						+ this.getLid());
		projectEFO.setNodeID(Integer.MAX_VALUE-3);
		ETLStep es3 = new AddOperation(projectEFO);
		StepSequence ss = new StepSequence();
		StepComponent sc2 = this.getFollowingStepComponentLeft();
		if (sc2 != null) {
			ss.addStepChild(sc2);
		}
		ss.addStepChild(es3);
		return ss;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see patternUtils.GenericJoin#configFollowingPart(structures.ApplPoint)
	 */
	@Override
	public void configFollowingPart(ApplPoint applPoint) {
		this.configFollowingPartLeft();
		EdgeApplPoint eap = (EdgeApplPoint) applPoint;
		// set as projected attributes only the ones that are found in the
		// output schema of the application point
		for (Attribute a : eap.getRefProperties().getOutSchemata().get(0).getAttributes()) {
			ExpressionAttribute lex2 = new ExpressionAttribute(a);
			Expression fex2 = new Expression(lex2, null, null,
					ExpressionKind.EXPRESSION);
			fex2.addUsedAttribute(a);
			projectEFO.addSemanticsExpressionTree("project", fex2);
		}
	}

	/**
	 * Return the left part of the component that follows the join operation,
	 * i.e., the part after the join and before the project operation that
	 * maintains the schema. If such component does not exist (i.e., the project
	 * operation directly follows the join operation) it should return null.
	 * 
	 * @return
	 */
	public abstract StepComponent getFollowingStepComponentLeft();

	/**
	 * Configure the left part of the component that follows the join operation,
	 * i.e., the part after the join and before the project operation that
	 * maintains the schema
	 */
	public abstract void configFollowingPartLeft();
	
	public int getLid(){
		return super.getLid();
	}

}
