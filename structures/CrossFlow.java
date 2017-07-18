package structures;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import utilities.ObjectPair;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;

/**
 * 
 * This class represents two step sequences which cross at some ETL step (in
 * other words, they share only one common ETL step). The first step sequence is
 * referred to as the <it>horizontal step sequence</it> and the second one as
 * the <it>vertical sequence</it>. The common ETL step for each of the two
 * sequences is one of the <it>step components</it> of their step children,
 * which implies that this component is an ETL step. Both of the step sequences
 * have at least one element, that is the common ETL step.
 * 
 * IMPORTANT! There must exist at least one element before the common etl step
 * in both the horizontal and the vertical sequences!
 * 
 * 
 * @author vasileios
 * 
 */
public class CrossFlow extends StepComposite {

	private StepSequence horizontalSequence;
	private StepSequence verticalSequence;

	/**
	 * The first step of the horizontal step sequence in this step flow.
	 */
	private ETLStep firstHorStep;

	/**
	 * The last step of the horizontal step sequences in this step flow.
	 */
	private ETLStep lastHorStep;

	/**
	 * The first step of the vertical step sequence in this step flow.
	 */
	private ETLStep firstVertStep;

	/**
	 * The last step of the vertical step sequences in this step flow.
	 */
	private ETLStep lastVertStep;

	/**
	 * The common step of the crossing step sequences.
	 */
	private ETLStep crossStep;


	/**
	 * The index of the common step on the step children list of the horizontal
	 * step sequence. It means that the common step is the (crossStepHorIndex+1)
	 * element of the step children list.
	 */
	private int crossStepHorIndex;

	/**
	 * The index of the common step on the step children list of the vertical
	 * step sequence.It means that the common step is the (crossStepVertIndex+1)
	 * element of the step children list.
	 */
	private int crossStepVertIndex;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * structures.ETLIntegrable#integrateToGraph(etlFlowGraph.graph.ETLFlowGraph
	 * , structures.ApplPoint)
	 */
	@Override
	public ETLFlowGraph integrateToGraph(ETLFlowGraph efg, ApplPoint applPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see structures.StepComponent#getAsETLFlowGraph()
	 */
	@Override
	public ETLFlowGraph getAsETLFlowGraph() {
		ETLFlowGraph efg = new ETLFlowGraph();
		return efg;
	}

	@Override
	public void instillProperties(ApplPoint applPoint) {

		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @see structures.StepComponent#getAsETLFlowGraph(structures.ApplPoint)
	 * 
	 */
	@Override
	public ETLFlowGraph getAsETLFlowGraph(ApplPoint referencePoint) {
		StepSequence horSubseq1 = new StepSequence();
		StepSequence horSubseq2 = new StepSequence();
		StepSequence vertSubseq1 = new StepSequence();
		StepSequence vertSubseq2 = new StepSequence();
		
		// copy left part of horizontal sequence
		for (int i = 0; i <= this.crossStepHorIndex; i++) {
			horSubseq1.addStepChild(this.horizontalSequence.getChildAtIndex(i));
		}
		// crossOp is now a reference to the initial cross operation
		ETLFlowOperation crossStepOp = this.getCrossStep().getAssociatedETLOperation();
		// copy right part of horizontal sequence
		for (int i = this.crossStepHorIndex; i < this.horizontalSequence
				.getStepChildrenSize(); i++) {
			horSubseq2.addStepChild(this.horizontalSequence.getChildAtIndex(i));
		}
		// copy left part of vertical sequence
		for (int i = 0; i <= this.crossStepVertIndex; i++) {
			vertSubseq1.addStepChild(this.verticalSequence.getChildAtIndex(i));
		}
		// copy right part of vertical sequence
		for (int i = this.crossStepVertIndex; i < this.verticalSequence
				.getStepChildrenSize(); i++) {
			vertSubseq2.addStepChild(this.verticalSequence.getChildAtIndex(i));
		}
		
		System.out.println("prin to efg: " +((ETLStep) horSubseq1.getStepChildren().iterator().next()).getAssociatedETLOperation().getOutputSchemata());
		System.out.println("prin to efg edge: " +referencePoint.getRefProperties().getOutSchemata());
		
		// get first half copies subsequences as etl flow graphs
		ETLFlowGraph efgH1 = horSubseq1.getAsETLFlowGraph(referencePoint);
		//System.out.println(vertSubseq1.getStepChildren().get(0).getInitialEndPoint().getAssociatedETLOperation().getNodeID()+ "  " +vertSubseq1.getStepChildren().get(1).getInitialEndPoint().getAssociatedETLOperation().getNodeID() );
		ETLFlowGraph efgV1 = vertSubseq1.getAsETLFlowGraph(referencePoint);

		System.out.println("meta to efg: " +((ETLStep) horSubseq1.getStepChildren().iterator().next()).getAssociatedETLOperation().getOutputSchemata());
		System.out.println("meta to efg edge: " +referencePoint.getRefProperties().getOutSchemata());
		
		// get the ids of last and first elements
		int idH1Last = -1;
		int idHPreLast = -1;
		Iterator it1 = efgH1.iterator();
		// count the number of steps to the cross step
		int helpercounter = -1;
		while (it1.hasNext()) {
			idHPreLast = idH1Last;
			idH1Last = (int) it1.next();
			helpercounter++;
			// System.out.println("counter:"+helpercounter);
		}
		// get the operation name of the last operation (before the join) in the
		// first half horizontal sequence
		String horLastOpName = efgH1.getEtlFlowOperations().get(idHPreLast)
				.getOperationName();

		int idV1Last = -1;
		int idVPreLast = -1;
		Iterator it2 = efgV1.iterator();
		// count the number of steps to the cross step
		int helpercounter2 = -1;
		while (it2.hasNext()) {
			idVPreLast = idV1Last;
			idV1Last = (int) it2.next();
			helpercounter2++;
			// System.out.println("counter2:"+helpercounter2);
		}

		// get the operation name of the last operation in the first half
		// vertical sequence
		String vertLastOpName = efgV1.getEtlFlowOperations().get(idVPreLast)
				.getOperationName();

		// update the reference point
		Schema outSchFromVert = null;
		Schema outSchFromHor = null;
		//ETLFlowOperation crossStepOp = crossOp;
		ETLFlowGraph efgH2;
		ETLFlowGraph efgV2;
		if ((crossStepOp.getOperationType() == ETLOperationType.etlOperationTypes
				.get("Join"))||(crossStepOp.getOperationType() == ETLOperationType.etlOperationTypes
						.get("LeftOuterJoin"))) {
			outSchFromVert = (Schema) efgV1.getEtlFlowOperations()
					.get(idVPreLast).getOutputSchemata().iterator().next()
					.clone();
			System.out.println("out from vert operator: "+efgV1.getEtlFlowOperations().get(idVPreLast).getOperationName());
			
			outSchFromHor = (Schema) efgH1.getEtlFlowOperations().get(idHPreLast)
					.getOutputSchemata().iterator().next().clone();
			System.out.println("out from hor: "+outSchFromHor);
			System.out.println("out from hor operator: "+efgH1.getEtlFlowOperations().get(idHPreLast).getOperationName());
			Schema unionSchemata = new Schema();
			for (Attribute a : outSchFromHor.getAttributes()) {
				unionSchemata.addAttribute((Attribute) a.clone());
			}
			for (Attribute a : outSchFromVert.getAttributes()) {
				unionSchemata.addAttribute((Attribute) a.clone());
			}
			ArrayList<Schema> schUnionList = new ArrayList<Schema>();
			schUnionList.add(unionSchemata);
			// just a dummy node
			ETLFlowOperation rpo = new ETLFlowOperation(ETLNodeKind.Operator,
					ETLOperationType.etlOperationTypes
							.get(OperationTypeName.Filter.name()), "Dummy");
			// the schema id part is just a dummy value!
			unionSchemata.setSchemaCard(1);
			rpo.addInputSchema(1, unionSchemata);
			rpo.addOutputSchema(1, unionSchemata);
			NodeApplPoint referencePoint2 = new NodeApplPoint();
			referencePoint2.setEfOperator(rpo);
			efgH2 = horSubseq2.getAsETLFlowGraph(referencePoint2);
			// remove the dummy card_origin from the crossflow element
			ArrayList<ETLNonFunctionalCharacteristic> nfcs = efgH2
					.getEtlFlowOperationByName(crossStepOp.getOperationName())
					.getoProperties().get("card_origin");
			ETLNonFunctionalCharacteristic epntr = null;
			for (ETLNonFunctionalCharacteristic e : nfcs) {
				if (e.getRightOp().equals("Dummy")) {
					epntr = e;
				}
			}
			if (epntr != null) {
				nfcs.remove(epntr);
			}
			efgV2 = vertSubseq2.getAsETLFlowGraph(referencePoint2);
			// remove the dummy card_origin from the crossflow element
			ArrayList<ETLNonFunctionalCharacteristic> nfcs2 = efgV2
					.getEtlFlowOperationByName(crossStepOp.getOperationName())
					.getoProperties().get("card_origin");
			ETLNonFunctionalCharacteristic epntr2 = null;
			for (ETLNonFunctionalCharacteristic e : nfcs2) {
				if (e.getRightOp().equals("Dummy")) {
					epntr2 = e;
				}
			}
			if (epntr2 != null) {
				nfcs.remove(epntr2);
			}

		} else {

			efgH2 = horSubseq2.getAsETLFlowGraph(referencePoint);
			efgV2 = vertSubseq2.getAsETLFlowGraph(referencePoint);

		}
		int idH2First = (int) efgH2.iterator().next();
		int idV2First = (int) efgV2.iterator().next();

		// connect the first horizontal half to the second horizontal half
		ObjectPair op1 = new ObjectPair();
		ObjectPair op2 = new ObjectPair();
		ObjectPair op3 = new ObjectPair();
		op1.left = idH1Last;
		op1.right = idH2First;
		ArrayList<ObjectPair> cp = new ArrayList<ObjectPair>();
		cp.add(op1);
		ETLFlowGraph eFGHor = efgH1.connectToGraph(efgH2, cp);
		op2.left = idV1Last;
		op2.right = idV2First;
		ArrayList<ObjectPair> cp2 = new ArrayList<ObjectPair>();
		cp2.add(op2);
		ETLFlowGraph eFGVert = efgV1.connectToGraph(efgV2, cp2);
		Iterator it42 = eFGHor.iterator();
		int newHCrossInd = -1;
		int j = -1;
		while (it42.hasNext() && j < helpercounter) {
			newHCrossInd = (int) it42.next();
			j++;
		}
		Iterator it43 = eFGVert.iterator();
		int newVCrossInd = -1;
		j = -1;
		while (it43.hasNext() && j < helpercounter2) {
			newVCrossInd = (int) it43.next();
			j++;
		}
		op3.left = newHCrossInd;
		op3.right = newVCrossInd;
		// System.out.println("new h v"+newHCrossInd+newVCrossInd);
		ArrayList<ObjectPair> cp3 = new ArrayList<ObjectPair>();
		cp3.add(op3);
		ETLFlowGraph efgComplete = eFGHor.connectToGraph(eFGVert, cp3);
		//
		// update the crosspoint properties according to the type of the related
		// etl flow operation
		//
		if ((crossStepOp.getOperationType() == ETLOperationType.etlOperationTypes
				.get("Join"))||(crossStepOp.getOperationType() == ETLOperationType.etlOperationTypes
						.get("LeftOuterJoin"))) {
			ArrayList<Schema> inputSch = new ArrayList<Schema>();
			outSchFromHor.setSchemaCard(1);
			outSchFromVert.setSchemaCard(2);
			ETLFlowOperation crossStepInsideGraph = efgComplete
					.getEtlFlowOperationByName(crossStepOp.getOperationName());
			crossStepInsideGraph.setInputSchemata(null);
			crossStepInsideGraph.addInputSchema(1, outSchFromHor);
			crossStepInsideGraph.addInputSchema(2, outSchFromVert);
			// fix the card_origin
			ArrayList<ETLNonFunctionalCharacteristic> nfcs = crossStepInsideGraph
					.getoProperties().get("card_origin");
			for (ETLNonFunctionalCharacteristic e : nfcs) {
				// if this refers to the join input operation from the
				// horizontal sequence
				if (e.getRightOp().equals(horLastOpName)) {
					// by default the horizontal part is assumed to be the first
					// join element
					e.setLeftOp("1");
				} else if (e.getRightOp().equals(vertLastOpName)) {
					e.setLeftOp("2");
				}
			}

			
			crossStepOp.setInputSchemata(crossStepInsideGraph.getInputSchemata());
			crossStepOp.setOutputSchemata(crossStepInsideGraph.getOutputSchemata());
			crossStepOp.setoProperties(crossStepInsideGraph.getoProperties());
			crossStepOp.setNodeID(crossStepInsideGraph.getNodeID());
			crossStepOp.setSemanticsExpressionTrees(crossStepInsideGraph.getSemanticsExpressionTrees());
			// here maybe i need to copy more characteristics...
			
System.out.println("cross op"+crossStepOp);
		}
		return efgComplete;

	}

	@Override
	public StepComponent clone() {
		CrossFlow cFClone = new CrossFlow();
		cFClone.setHorizontalSequence(this.horizontalSequence.clone());
		cFClone.setVerticalSequence(this.verticalSequence.clone());
		cFClone.setCrossStepHorIndex(crossStepHorIndex);
		cFClone.setCrossStepVertIndex(crossStepVertIndex);
		return cFClone;

	}

	/**
	 * @return the horizontalSequence
	 */
	public StepSequence getHorizontalSequence() {
		return horizontalSequence;
	}

	/**
	 * @param horizontalSequence
	 *            the horizontalSequence to set
	 */
	public void setHorizontalSequence(StepSequence horizontalSequence) {
		this.horizontalSequence = horizontalSequence;
	}

	/**
	 * @return the verticalSequence
	 */
	public StepSequence getVerticalSequence() {
		return verticalSequence;
	}

	/**
	 * @param verticalSequence
	 *            the verticalSequence to set
	 */
	public void setVerticalSequence(StepSequence verticalSequence) {
		this.verticalSequence = verticalSequence;
	}

	/**
	 * @return the firstHorStep
	 */
	public ETLStep getFirstHorStep() {
		if (this.firstHorStep == null) {
			firstHorStep = this.horizontalSequence.getInitialEndPoint();
		}

		return firstHorStep;

	}

	/**
	 * @param firstHorStep
	 *            the firstHorStep to set
	 */
	public void setFirstHorStep(ETLStep firstHorStep) {
		this.firstHorStep = firstHorStep;
	}

	/**
	 * @return the lastHorStep
	 */
	public ETLStep getLastHorStep() {
		if (this.lastHorStep == null) {
			lastHorStep = this.horizontalSequence.getFinalEndPoint();
		}
		return lastHorStep;

	}

	/**
	 * @param lastHorStep
	 *            the lastHorStep to set
	 */
	public void setLastHorStep(ETLStep lastHorStep) {
		this.lastHorStep = lastHorStep;
	}

	/**
	 * @return the firstVertStep
	 */
	public ETLStep getFirstVertStep() {
		if (this.firstVertStep == null) {
			firstVertStep = this.verticalSequence.getInitialEndPoint();
		}

		return firstVertStep;

	}

	/**
	 * @param firstVertStep
	 *            the firstVertStep to set
	 */
	public void setFirstVertStep(ETLStep firstVertStep) {
		this.firstVertStep = firstVertStep;
	}

	/**
	 * @return the lastVertStep
	 */
	public ETLStep getLastVertStep() {
		if (this.lastVertStep == null) {
			lastVertStep = this.verticalSequence.getFinalEndPoint();
		}
		return lastVertStep;
	}

	/**
	 * @param lastVertStep
	 *            the lastVertStep to set
	 */
	public void setLastVertStep(ETLStep lastVertStep) {
		this.lastVertStep = lastVertStep;
	}

	/**
	 * @return the crossStep
	 */
	public ETLStep getCrossStep() {
		// if it has not been initialized
		if (crossStep == null) {
			crossStep = (ETLStep) this.horizontalSequence.getStepChildren()
					.get(crossStepHorIndex);
		}
		return crossStep;

	}

	/**
	 * @param crossStep
	 *            the crossStep to set
	 */
	public void setCrossStep(ETLStep crossStep) {
		this.crossStep = crossStep;
	}

	/**
	 * @return the crossStepHorIndex
	 */
	public int getCrossStepHorIndex() {
		return crossStepHorIndex;
	}

	/**
	 * @param crossStepHorIndex
	 *            the crossStepHorIndex to set
	 */
	public void setCrossStepHorIndex(int crossStepHorIndex) {
		this.crossStepHorIndex = crossStepHorIndex;
	}

	/**
	 * @return the crossStepVertIndex
	 */
	public int getCrossStepVertIndex() {
		return crossStepVertIndex;
	}

	/**
	 * @param crossStepVertIndex
	 *            the crossStepVertIndex to set
	 */
	public void setCrossStepVertIndex(int crossStepVertIndex) {
		this.crossStepVertIndex = crossStepVertIndex;
	}

	@Override
	public ETLStep getInitialEndPoint() {
		return this.horizontalSequence.getInitialEndPoint();
	}

	@Override
	public ETLStep getFinalEndPoint() {
		return this.horizontalSequence.getFinalEndPoint();
	}

	@Override
	public ETLFlowGraph getAsETLFlowGraph(ApplPoint referencePoint,
			ArrayList<ETLFlowOperation> opsToBePassed) {
		ETLFlowGraph efg = this.getAsETLFlowGraph(referencePoint);
		//TODO:do something with opsToBePassed
		return efg;
	}

}
