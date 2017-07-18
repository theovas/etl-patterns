package MeasureUtils;

import java.util.Iterator;

import operationDictionary.ETLOperationType;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;

/**
 * A class that contains methods for performing estimations of basic metrics
 * regarding the static structure of the ETL flows.
 * 
 * @author vasileios
 *
 */
public class ETLStaticMeasures {

	// a private constructor to block instantiation and subclassing
	private ETLStaticMeasures() {
	}
	
	public static int opTypeCardinality(ETLFlowGraph efg, ETLOperationType eot) {
		int card = 0;
		int nxt = -1;
		for (Iterator i = efg.iterator(); i.hasNext();) {
			nxt = (Integer) i.next();
			if (efg.getEtlFlowOperations().get(nxt).getOperationType().getOpTypeName()==eot.getOpTypeName()) {
				card++;
			}
		}
		return card;
	}
	
	public static int nodeKindCardinality(ETLFlowGraph efg, ETLNodeKind enk) {
		int card = 0;
		int nxt = -1;
		for (Iterator i = efg.iterator(); i.hasNext();) {
			nxt = (Integer) i.next();
			if (efg.getEtlFlowOperations().get(nxt).getNodeKind() == enk) {
				card++;
			}
		}
		return card;
	}
	
}
