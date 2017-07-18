package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import operationDictionary.ETLOperationType;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.operation.ETLFlowOperation;
import structPatterns.PatternNode;
import structPatterns.StructPattern;

public class SyntheticFlowGenerator {

	static HashSet<PatternNode> existingJoiningOps = new HashSet<PatternNode>();
	static HashSet<ETLOperationType> calculatedProbs = new HashSet<ETLOperationType>();
	static Random randomno = new Random();
	static HashMap<ETLOperationType, OPTPercentageBounds> percentageBoundsPerOPT = new HashMap<ETLOperationType, OPTPercentageBounds>();
	private static int nodeIDCnt = 0;
	public static HashSet<String> joiningTypes = new HashSet<String>();
	static {
		joiningTypes.add("RightOuterJoin");
		joiningTypes.add("FullOuterJoin");
		joiningTypes.add("LeftOuterJoin");
		joiningTypes.add("Join");
	}

	public static StructPattern generateFlow(GenParams gp) {
		StructPattern sp = new StructPattern();
		HashSet<Trail> disconnectedTrails = new HashSet<Trail>();
		ArrayList<Trail> activeTrails = new ArrayList<Trail>();
		ArrayList<Trail> activeTrails2 = new ArrayList<Trail>();
		// generate initial input operations
		for (int i = 0; i < gp.getStartInputNo(); i++) {
			// // System.out.println("creating initial points: " + i);
			Trail tr = new Trail();
			PatternNode pn = new PatternNode("TableInput", ++nodeIDCnt);
			sp.addNode(pn);
			tr.setEndPoint(pn);
			activeTrails.add(tr);
		}
		PatternNode pn;
		PatternNode pnNew;
		int numberOfNext;
		String newOPType;
		//
		activeTrails2 = new ArrayList<Trail>();
		while (!activeTrails.isEmpty()) {
			// int ij = 0;
			// while (ij < 10) {
			// ij++;
			// // System.out.println("^^^^ active trails number: "
			// // + activeTrails.size());
			for (Trail tr : activeTrails) {
				if (sp.getPattNodes().size() >= gp.getFlowSize()
						- activeTrails.size()) {
					pn = tr.getEndPoint();
					pnNew = new PatternNode("TableOutput", ++nodeIDCnt);
					sp.addNode(pnNew);
					// // System.out
					// // .println("##### adding node: " + pnNew.getLabel());
					sp.addEdge(pn.getNodeID(), pnNew.getNodeID());
					// Trail tr2 = new Trail();
					// tr2.gatherAllJoinsFromOtherTrail(tr);
					// tr2.setEndPoint(pnNew);
					activeTrails2.clear();

				} else {
					// // System.out.println("entering active trail");
					pn = tr.getEndPoint();
					// // System.out.println("get pn.label" + pn.getLabel());
					// // System.out.println("pn.label" + pn.getLabel());
					numberOfNext = getNumberOfNext(
							ETLOperationType.etlOperationTypes.get(pn
									.getLabel()), gp);
					for (int j = 0; j < numberOfNext; j++) {
						newOPType = getSucceedingLabel(
								ETLOperationType.etlOperationTypes.get(pn
										.getLabel()), gp).getOpTypeName()
								.toString();
						if (!joiningTypes.contains(newOPType)) {
							Trail tr2 = new Trail();
							pnNew = new PatternNode(newOPType, ++nodeIDCnt);
							// // System.out.println("new operator type: "
							// // + pnNew.getLabel());
							sp.addNode(pnNew);
							// // System.out.println("##### adding node: "
							// // + pnNew.getLabel());
							sp.addEdge(pn.getNodeID(), pnNew.getNodeID());
							tr2.gatherAllJoinsFromOtherTrail(tr);
							tr2.setEndPoint(pnNew);
							activeTrails2.add(tr2);
						} else {
							String joinGen = getTypeOfJoinGeneration(gp);
							if (joinGen.equals("newJoin")) {
								// // System.out.println("newJoin!!!");
								Trail tr2 = new Trail();
								pnNew = new PatternNode(newOPType, ++nodeIDCnt);
								// // System.out.println("new operator type: "
								// // + pnNew.getLabel());
								sp.addNode(pnNew);
								// // System.out.println("##### adding node: "
								// // + pnNew.getLabel());
								sp.addEdge(pn.getNodeID(), pnNew.getNodeID());
								tr2.gatherAllJoinsFromOtherTrail(tr);
								tr2.getJoinOps().add(pnNew);
								tr2.setEndPoint(pnNew);
								activeTrails2.add(tr2);
								existingJoiningOps.add(pnNew);

							} else if (joinGen.equals("merge")) {
								// // System.out.println("Merge join!!!");
								HashSet<PatternNode> existingJoiningOpsCp = copyJoinSet(existingJoiningOps);
								existingJoiningOpsCp.removeAll(tr.getJoinOps());
								if (existingJoiningOpsCp.isEmpty()) {
									// //
									// System.out.println("newJoin instead!!!");
									Trail tr2 = new Trail();
									pnNew = new PatternNode(newOPType,
											++nodeIDCnt);
									// //
									// System.out.println("new operator type: "
									// // + pnNew.getLabel());
									sp.addNode(pnNew);
									// //
									// System.out.println("##### adding node: "
									// // + pnNew.getLabel());
									sp.addEdge(pn.getNodeID(),
											pnNew.getNodeID());
									tr2.gatherAllJoinsFromOtherTrail(tr);
									tr2.getJoinOps().add(pnNew);
									tr2.setEndPoint(pnNew);
									activeTrails2.add(tr2);
									existingJoiningOps.add(pnNew);
								} else {
									int index = randomno
											.nextInt(existingJoiningOpsCp
													.size());
									int i = 0;
									PatternNode pn2 = null;
									for (PatternNode pn3 : existingJoiningOpsCp) {
										if (i == index) {
											pn2 = pn3;
										}
										i = i + 1;
									}
									if (pn2 != null) {
										sp.addNode(pn2);
										sp.addEdge(pn.getNodeID(),
												pn2.getNodeID());
									} else {
										// //
										// System.out.println("pn2 is null!");
									}
								}
							}
						}
					}
				}
			}
			activeTrails = activeTrails2;
			activeTrails2 = new ArrayList<Trail>();
			;
		}
		// // System.out.println("Size of graph: " + sp.getPattNodes().size());
		// TODO: connect the trails, by making sure that (ignoring
		// directionality) if
		// I start from any trail I can reach any other
		return sp;

	}

	public static HashSet<PatternNode> copyJoinSet(HashSet<PatternNode> js) {
		HashSet<PatternNode> js2 = new HashSet<PatternNode>();
		for (PatternNode pn : js) {
			js2.add(pn);
		}
		return js2;
	}

	public static int getNumberOfNext(ETLOperationType eot, GenParams gp) {

		int n = Math.round(gp.getCharsPerOp().get(eot).getNumberOfNextHops()
				/ (float) gp.getCharsPerOp().get(eot).getNumberOfOccurrences());
		// // System.out.println(eot.getOpTypeName() + " ::number of next:: " +
		// n);
		return n;
	}

	public static String getTypeOfJoinGeneration(GenParams gp) {
		int randomInt = randomno.nextInt(101);
		int newJoinPr = gp.getNewJoinProbability();
		if (randomInt < newJoinPr) {
			return "newJoin";
		} else {
			return "merge";
		}
	}

	public static ETLOperationType getSucceedingLabel(ETLOperationType eot,
			GenParams gp) {
		int maxPerc = 0;
		int randomInt;
		if (calculatedProbs.contains(eot)) {
			OPTPercentageBounds bounds = percentageBoundsPerOPT.get(eot);
			for (MyPair<Integer, ETLOperationType> ie : bounds
					.getBoundsAndEOTs()) {
				maxPerc = ie.getFirst();
			}
			randomInt = randomno.nextInt(maxPerc);
			// // System.out.println("random number: " + randomInt);
			for (MyPair<Integer, ETLOperationType> ie : bounds
					.getBoundsAndEOTs()) {
				if (randomInt < ie.getFirst()) {
					return ie.getSecond();
				}
			}
		} else {
			ETLOCharacteristics optChars = gp.getCharsPerOp().get(eot);
			int numberOfOccs = optChars.getNumberOfOccurrences();
			// // System.out.println("number of occs: " + numberOfOccs);
			HashMap<ETLOperationType, Integer> occOneAfter = optChars
					.getOccurrencesOneAfter();
			OPTPercentageBounds bounds = new OPTPercentageBounds();
			bounds.setEot(eot);
			Iterator it = occOneAfter.entrySet().iterator();
			int percentage;
			int runningPerc = 0;
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				percentage = Math
						.round((1000 * (Integer) pair.getValue() / (float) numberOfOccs));
				// // System.out.println("percentage: " + percentage);
				runningPerc += percentage;
				// // System.out.println("running percentage: " + runningPerc);
				bounds.getBoundsAndEOTs().add(
						new MyPair(runningPerc, (ETLOperationType) pair
								.getKey()));
				// // System.out.println(pair.getKey() + " = " +
				// pair.getValue());
				it.remove(); // avoids a ConcurrentModificationException
			}
			percentageBoundsPerOPT.put(eot, bounds);
			// ...
			calculatedProbs.add(eot);
			randomInt = randomno.nextInt(runningPerc);
			// OPTPercentageBounds bounds = percentageBoundsPerOPT.get(eot);
			for (MyPair<Integer, ETLOperationType> ie : bounds
					.getBoundsAndEOTs()) {
				// // System.out.println("cpmparing random " + randomInt + "to "
				// // + ie.getFirst());
				if (randomInt < ie.getFirst()) {
					return ie.getSecond();
				}
			}
		}
		// // System.out.println("oops!");
		return null;

	}
}
