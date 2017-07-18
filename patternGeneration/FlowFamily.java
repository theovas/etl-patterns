package patternGeneration;

import java.util.ArrayList;

import etlFlowGraph.graph.ETLFlowGraph;
import structures.EFGPatternApplicationFlyweight;

public class FlowFamily {
	private ArrayList<EFGPatternApplicationFlyweight> flows = new ArrayList<EFGPatternApplicationFlyweight>();
	private ETLFlowGraph initialEfg;

	/**
	 * @return the efg
	 */
	public ETLFlowGraph getInitialEfg() {
		return initialEfg;
	}

	/**
	 * @param efg
	 *            the efg to set
	 */
	public void setInitialEfg(ETLFlowGraph efg) {
		this.initialEfg = efg;
	}

	/**
	 * @return the flows
	 */
	public ArrayList<EFGPatternApplicationFlyweight> getFlows() {
		return flows;
	}

	/**
	 * @param flows
	 *            the flows to set
	 */
	public void setFlows(ArrayList<EFGPatternApplicationFlyweight> flows) {
		this.flows = flows;
	}
	
	public EFGPatternApplicationFlyweight getFlowById(int flowid){
		for (EFGPatternApplicationFlyweight efly:this.flows){
			if (efly.getFlowId() == flowid){
				return efly;
			}
		}
		// if not found
		return null;	
	}
	
}
