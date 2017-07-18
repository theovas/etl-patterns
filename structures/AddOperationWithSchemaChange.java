package structures;

import java.util.ArrayList;

import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.schema.Schema;

public abstract class AddOperationWithSchemaChange extends AddOperation{

	public AddOperationWithSchemaChange(ETLFlowOperation efo) {
		super(efo);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see structures.AddOperation#getOutSchemata(structures.ApplPoint)
	 */
	@Override
	public abstract ArrayList<Schema> getOutSchemata(ApplPoint referencePoint);

	
}
