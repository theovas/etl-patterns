package utils;

import java.util.HashMap;
import java.util.Set;

import etlFlowGraph.graph.ETLFlowGraph;
import operationDictionary.ETLOperationType;
import structPatterns.IsomorphismTester;
import structPatterns.StructPattern;

public class ETLsCharsExtractor {
	public static HashMap<ETLOperationType, ETLOCharacteristics> etlChars = new HashMap<ETLOperationType, ETLOCharacteristics>();

	public static void main(String[] args) {
		int id = 4;
		System.out.println("*** pattern with id " + id + " "
				+ IOFlowFilesTester.allPatterns.get(id));
		extractChars(null);
		for (ETLOperationType name : etlChars.keySet()) {
			String value = etlChars.get(name).toString();
			System.out.println(name + " " + value);

		}
		GenParams gp = new GenParams();
		gp.setCharsPerOp(etlChars);
		gp.setFlowSize(10);
		gp.setNewJoinProbability(50);
		gp.setStartInputNo(2);
		StructPattern sp;
		long totalTime = 0;
		for (int i = 0; i < 10; i++) {
			do {
				sp = SyntheticFlowGenerator.generateFlow(gp);
				System.out.println("flow generated");
			} while (sp.getPattNodes().size() < 10);
			System.out.println("valid flow generated of size: "
					+ sp.getPattNodes().size());
			long startTime = System.nanoTime();
			System.out.println("number of pattrn models: "
					+ IOFlowFilesTester.allPatterns.size());
			for (StructPattern pmodel : IOFlowFilesTester.allPatterns) {
				IsomorphismTester.getAllPatternMatches(pmodel, sp);
			}
			long estimatedTime = System.nanoTime() - startTime;
			totalTime += estimatedTime;
			// System.out.println("estimated time: " + estimatedTime);
		}
		System.out.println("total time2: " + totalTime);
		System.out.println("*** pattern with id " + id + " "
				+ IOFlowFilesTester.allPatterns.get(id));
		// String sizes = "";
		// StructPattern sp;
		// for (int j = 0; j < 101; j += 10) {
		// gp.setNewJoinProbability(j);
		// sizes += "\n";
		// for (int i = 0; i < 10; i++) {
		// sp = SyntheticFlowGenerator.generateFlow(gp);
		// sizes += sp.getPattNodes().size() + " ";
		// }
		// }
		// System.out.println("sizes: " + sizes);
	}

	public static void extractChars(Set<ETLFlowGraph> efgs) {
		// for now I just use the set of 25 TPCDI etls but TODO: extend code to
		// do this for any set of efgs
		IOFlowFilesTester.getAndVisitAllFlows(new CharsExtractVisitor());
	}
}
