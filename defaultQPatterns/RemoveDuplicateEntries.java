package defaultQPatterns;

import qPatterns.QPattern;
import qPatterns.QPatternName;
import etlFlowGraph.graph.ETLFlowGraph;
import structures.ApplPoint;
import utilities.ObjectPair;
/**
 * 
 * @author vasileios
 *
 */

//
//
//same as filternulls, but with grouping operator with "distinct"
//

public class RemoveDuplicateEntries extends QPattern {
	private static final int patternId = 5;
	@Override
	public ETLFlowGraph deploy(ApplPoint applPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjectPair<Integer, String> getFitness(ApplPoint ap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QPatternName getName() {
		// TODO Auto-generated method stub
		return QPatternName.RemoveDuplicateEntries;
	}

	@Override
	public int getPatternId() {
		// TODO Auto-generated method stub
		return this.patternId;
	}
	
	@Override
	public QPattern getUpdatedInstance() {

		RemoveDuplicateEntries qpUpd = new RemoveDuplicateEntries();
//			//increases the lid AND updated accordingly the name of the join operator
//			qpUpd.increaseLid();
			return qpUpd;
	}

}
