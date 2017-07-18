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
public class ParallelizeTask extends QPattern {

	private static final int patternId = 4;
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
		return QPatternName.ParallelizeTask;
	}

	@Override
	public int getPatternId() {
		// TODO Auto-generated method stub
		return this.patternId;
	}
	
	@Override
	public QPattern getUpdatedInstance() {

		ParallelizeTask qpUpd = new ParallelizeTask();
//			//increases the lid AND updated accordingly the name of the join operator
//			qpUpd.increaseLid();
			return qpUpd;
	}

}
