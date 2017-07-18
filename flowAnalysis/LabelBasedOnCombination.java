package flowAnalysis;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import graphUtils.LabelledNode;

public class LabelBasedOnCombination implements LabellingStrategy {

	/**
	 * a list of labaling strategies that need to be combined (exhaustively)
	 */
	ArrayList<LabellingStrategy> stratCombination = new ArrayList<LabellingStrategy>();

	public LabelBasedOnCombination(ArrayList<LabellingStrategy> stratCombination) {
		super();
		this.stratCombination = stratCombination;
	}

	@Override
	public LabelledNode labelNode(ETLFlowOperation o, ETLFlowGraph efg) {
		// TODO Auto-generated method stub
		String label = "";
		for (LabellingStrategy ls : this.stratCombination) {
			label += ls.labelNode(o, efg).getNodeLabel();
		}
		return new LabelledNode(o.getNodeID(), label);
	}

	@Override
	public String getLabel() {
		String label = "";
		if (!stratCombination.isEmpty()) {
			for (LabellingStrategy ls : this.stratCombination) {
				label += ls.getLabel() + "-";
			}
			return label.substring(0, label.length() - 1);
		} else
			return label;
	}

	/**
	 * @return the stratCombination
	 */
	public ArrayList<LabellingStrategy> getStratCombination() {
		return stratCombination;
	}

	/**
	 * @param stratCombination
	 *            the stratCombination to set
	 */
	public void setStratCombination(
			ArrayList<LabellingStrategy> stratCombination) {
		this.stratCombination = stratCombination;
	}

}