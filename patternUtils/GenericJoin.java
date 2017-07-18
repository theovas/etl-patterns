/**
 * 
 */
package patternUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import operationDictionary.ETLOperationType;
import operationDictionary.ExpressionOperator;
import operationDictionary.OperationTypeName;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionConstant;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;
import qPatterns.QPattern;
import qPatterns.SimpleConditionExpr;
import qPatterns.TopologyCondition;
import structures.AddOperation;
import structures.ApplPoint;
import structures.CrossFlow;
import structures.ETLStep;
import structures.EdgeApplPoint;
import structures.JoinKeys;
import structures.NodeApplPoint;
import structures.StepComponent;
import structures.StepComposite;
import structures.StepSequence;
import utilities.ObjectPair;

/**
 * 
 * A pattern class for the addition of left outer join operations. It includes one
 * step component that precedes the join operation and one step component that
 * follows the join operation.
 * 
 * @author vasileios
 * 
 */
public abstract class GenericJoin extends QPattern {
	private static int lid = 0;
	// these are the joining keys that take part in the condition of the join
	// operation
	private JoinKeys joinKeys;
	protected ETLFlowOperation joinEFO;

	public GenericJoin() {
		// TopologyCondition tc = new SimpleConditionExpr();

		CrossFlow cf = new CrossFlow();
		StepSequence ssHor = new StepSequence();
		StepSequence ssVert = new StepSequence();
		lid++;
		//System.out.println("increased lid "+lid);
		this.addAPTypes(QPattern.applPointType.edge);
		this.joinEFO = new ETLFlowOperation(ETLNodeKind.Operator,
				ETLOperationType.etlOperationTypes.get("LeftOuterJoin"), getJoinOName());
		StepComponent sc1 = this.getIncomingStepComponent();
		// add a dummy element to the crossflow, because there must exist at
		// least one element before the common etl step in both the horizontal
		// and the vertical sequences! This is to cover the case when there is
		// no step before the join from the horizontal part
		ETLFlowOperation dummyOp = new ETLFlowOperation(ETLNodeKind.Operator,
				ETLOperationType.etlOperationTypes.get(OperationTypeName.Filter
						.name()), "Dummy");
		dummyOp.setNodeID(Integer.MAX_VALUE);
		AddOperation dummy = new AddOperation(dummyOp);
		ETLStep es = new AddOperation(this.joinEFO);
		StepComponent sc2 = this.getFollowingStepComponent();
		ssHor.addStepChild(dummy);
		ssHor.addStepChild(es);
		if (sc2 != null) {
			ssHor.addStepChild(sc2);
		}
		if (sc1 != null) {
			ssVert.addStepChild(sc1);
		}
		ssVert.addStepChild(es);
		cf.setHorizontalSequence(ssHor);
		cf.setVerticalSequence(ssVert);
		cf.setCrossStepHorIndex(1);
		cf.setCrossStepVertIndex(1);
		this.addImplementation(cf);

	}

	/**
	 * Returns the added step component that takes part in the join operation
	 * 
	 * @return
	 */
	public abstract StepComponent getIncomingStepComponent();

	/**
	 * Returns the added step component that follows the join operation, which
	 * includes processing, projections e.t.c..
	 * 
	 * @return
	 */
	public abstract StepComponent getFollowingStepComponent();

	/**
	 * 
	 * This method adds the join condition(s) to the join ETLFlowOperation of
	 * the GenericJoin pattern. During its call it should modify the
	 * <it>joinEFO</it> property of the GenericFilter class.
	 * 
	 * @param attrs
	 */
	public abstract void setJoinCondition(JoinKeys joinKeys);

	/**
	 * Deploys the pattern by injecting to the operators, properties relative to
	 * the application point operation. The application point is an edge. In the
	 * returned ETL flow graph, the join operator follows the node application
	 * point. A component to be one of the inputs of the join operator is added
	 * to the graph. Some component specific to the pattern is also added right
	 * after the join operator.
	 */
	@Override
	public ETLFlowGraph deploy(ApplPoint applPoint) {
		//this.lid++;
		EdgeApplPoint eap = (EdgeApplPoint) applPoint;
		// Configure the incoming step component part of the pattern, for
		// example change the datasource to be used
		this.configIncomingPart(applPoint);
		// Configure the step component part of the pattern that follows after
		// the join operator
		this.configFollowingPart(applPoint);
		// the dummy operation gets the properties of the application point
		// get the first and default implementation
		CrossFlow impl = (CrossFlow) this.getImplementations().get(0);
		//change dummy name to EdgeApplPoint source node name
		String eSrcName = eap.getEfGraph().getEtlFlowOperations().get(eap.getSourceNodeId()).getOperationName();
		impl.getFirstHorStep().getAssociatedETLOperation().setOperationName(eSrcName);
		ArrayList<ETLFlowOperation> efosToBePassed = new ArrayList<ETLFlowOperation>();
		efosToBePassed.add(joinEFO);
		ETLFlowGraph pefg = impl.getAsETLFlowGraph(applPoint, efosToBePassed);
		// remove the dummy point
		int dummyId = pefg.getEtlFlowOperationByName(eSrcName).getNodeID();
		pefg.removeVertex(dummyId);
		pefg.getEtlFlowOperations().remove(dummyId);
		// remove the dummy point edge
		Iterator it2 = pefg.edgeSet().iterator();
		ETLEdge ee;
		while (it2.hasNext()) {
			ee = (ETLEdge) it2.next();
			if ((int) ee.getSource() == dummyId) {
				pefg.removeEdge(ee.getSource(), ee.getTarget());
				break;
			}
		}
		// create and set the join condition on the selected join key
		// attribute(s).
		// IMPORTANT!!! This method follows after the getAsETLFLowGraph(), so
		// that the schemata have been populated with attributes.
		this.setJoinCondition(this.getJoinKeys());
		Iterator it = pefg.iterator();
		System.out.println(pefg.getEtlFlowOperationByName("FCP1_Join0"));
		System.out.println("trying to find: "+ this.getJoinOName());
		int joinId = pefg.getEtlFlowOperationByName(this.getJoinOName())
				.getNodeID();
		// the following work because the iteration is in topological order
		int lastHorElementId = joinId;
		while (it.hasNext()) {
			lastHorElementId = (int) it.next();
		}
		return applPoint.getEfGraph().insetSubgraphToEdge(pefg, eap.geteEdge(),
				joinId, lastHorElementId);

	}

	/**
	 * Configure the incoming step component part of the pattern
	 * 
	 * @param applPoint
	 */
	public abstract void configIncomingPart(ApplPoint applPoint);

	/**
	 * Configure the step component part of the pattern that follows after the
	 * join operator
	 * 
	 * @param applPoint
	 */
	public abstract void configFollowingPart(ApplPoint applPoint);

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

	/**
	 * @return the lid
	 */
	public int getLid() {
		return lid;
	}

	/**
	 * @param lid
	 *            the lid to set
	 */
	public void setLid(int lid) {
		GenericJoin.lid = lid;
	}

	/**
	 * @return the joinKeys
	 */
	public JoinKeys getJoinKeys() {
		return joinKeys;
	}

	/**
	 * @param joinKeys
	 *            the joinKeys to set
	 */
	public void setJoinKeys(JoinKeys joinKeys) {
		this.joinKeys = joinKeys;
	}

	/**
	 * @return the joinEFO
	 */
	public ETLFlowOperation getJoinEFO() {
		return joinEFO;
	}

	/**
	 * @param joinEFO
	 *            the joinEFO to set
	 */
	public void setJoinEFO(ETLFlowOperation joinEFO) {
		this.joinEFO = joinEFO;
	}

	public String getJoinOName() {
		
		System.out.println("join name: _Join" + lid);
		return ("FCP"+this.getPatternId()+"_Join_" + lid);
	}

}
