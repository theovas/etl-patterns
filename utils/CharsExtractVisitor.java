package utils;

import java.util.ArrayList;
import java.util.HashMap;

import operationDictionary.ETLOperationType;
import structPatterns.TestFlow;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;

public class CharsExtractVisitor implements MyVisitor {

	@Override
	public void visit(ETLFlowGraph efg, TestFlow tf) {
		HashMap<ETLOperationType, ETLOCharacteristics> ece = ETLsCharsExtractor.etlChars;
		ETLOCharacteristics ec;
		ArrayList<ETLFlowOperation> nextHops;
		for (ETLFlowOperation efo : efg.getEtlFlowOperations().values()) {
			nextHops = new ArrayList<ETLFlowOperation>();
			for (Object eo : efg.edgeSet()) {
				ETLEdge ee = (ETLEdge) eo;
				if ((Integer) ee.getSource() == efo.getNodeID()) {
					nextHops.add(efg.getEtlFlowOperations().get(ee.getTarget()));
				}
			}
			if (ece.get(efo.getOperationType()) == null) {
				ec = new ETLOCharacteristics();
				ec.setNumberOfOccurrences(1);
				for (ETLFlowOperation efo2 : nextHops) {
					ec.setNumberOfNextHops(ec.getNumberOfNextHops() + 1);
					if (ec.getOccurrencesOneAfter()
							.get(efo2.getOperationType()) == null) {
						ec.getOccurrencesOneAfter().put(
								efo2.getOperationType(), 1);
					} else {
						ec.getOccurrencesOneAfter().put(
								efo2.getOperationType(),
								ec.getOccurrencesOneAfter().get(
										efo2.getOperationType()) + 1);
					}
				}
				// ...
				ece.put(efo.getOperationType(), ec);
			} else {
				ec = ece.get(efo.getOperationType());
				ec.setNumberOfOccurrences(ec.getNumberOfOccurrences() + 1);
				for (ETLFlowOperation efo2 : nextHops) {
					ec.setNumberOfNextHops(ec.getNumberOfNextHops() + 1);
					if (ec.getOccurrencesOneAfter()
							.get(efo2.getOperationType()) == null) {
						ec.getOccurrencesOneAfter().put(
								efo2.getOperationType(), 1);
					} else {
						ec.getOccurrencesOneAfter().put(
								efo2.getOperationType(),
								ec.getOccurrencesOneAfter().get(
										efo2.getOperationType()) + 1);
					}
				}
				// ...
			}
		}

	}
}
