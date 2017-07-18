package structPatterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.traverse.BreadthFirstIterator;

public class StructPatternAnalyzer {

	public static Hashtable<String, LabelCatalog> getStructPatternInfo(
			StructPattern sp) {
		// Hashtable containing information about the node labels of the
		// specific graph, e.g., how many nodes have one specific label; indexed
		// by label
		Hashtable<String, LabelCatalog> lcs = new Hashtable<String, LabelCatalog>();
		// parse the flow
		Iterator it = new BreadthFirstIterator(sp);
		String label;
		while (it.hasNext()) {
			PatternNode pn = sp.getPattNodes().get(it.next());
			label = pn.getLabel();
			LabelCatalog lc = lcs.get(label);
			// System.out.println("lc: " + lc);
			if (lc == null) {
				lc = new LabelCatalog(label);
				lc.addNode(pn);
				lcs.put(label, lc);
			} else {
				lc.addNode(pn);
			}

		}
		sp.setLabelCatalogs(lcs);
		return lcs;

	}

	public static Set<Integer> getNodeIdSet(ArrayList<StructPattern> sps) {
		Set<Integer> nids = new HashSet<Integer>();
		for (StructPattern sp : sps) {
			Iterator it = sp.getPattNodes().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				nids.add((Integer) pair.getKey());
			}
		}

		return nids;

	}

	public static Set<Integer> getNodeIdSetFromAllPatterns(
			ArrayList<ArrayList<CandidatePatternMatch>> spsL) {
		Set<Integer> nids = new HashSet<Integer>();
		for (ArrayList<CandidatePatternMatch> sps : spsL) {
			for (StructPattern sp : sps) {
				Iterator it = sp.getPattNodes().entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					System.out.println(pair.getKey() + " = " + pair.getValue());
					nids.add((Integer) pair.getKey());
				}
			}
		}
		// for (Integer i : nids) {
		// System.out.println("i: " + i);
		// }

		return nids;

	}

}
