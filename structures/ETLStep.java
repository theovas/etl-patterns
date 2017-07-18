package structures;

import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;

/**
 * @author vasileios
 *
 */
public abstract class ETLStep extends StepComponent{

public abstract ETLFlowOperation getAssociatedETLOperation();

}
