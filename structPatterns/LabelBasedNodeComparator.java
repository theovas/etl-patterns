package structPatterns;

import org.jgrapht.experimental.equivalence.EquivalenceComparator;


public class LabelBasedNodeComparator<P extends PatternNode, C>  implements EquivalenceComparator<P, C>{

	@Override
	public boolean equivalenceCompare(P arg0, P arg1, C arg2, C arg3) {
		// TODO Auto-generated method stub
		return arg0.getLabel().equals(arg1.getLabel());
	}

	@Override
	public int equivalenceHashcode(P arg0, C arg1) {
		// TODO Auto-generated method stub
		return 0;
	}




}
