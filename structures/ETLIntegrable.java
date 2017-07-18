/**
 * 
 */
package structures;

import etlFlowGraph.graph.ETLFlowGraph;

/**
 * @author vasileios
 *
 */
public interface ETLIntegrable {

	/**
	 * 
	 * Integrates an ETL construct to an existing ETL flow, at a specified
	 * application point.
	 * 
	 * @param efg
	 * @param applPoint
	 * @return
	 */
	public ETLFlowGraph integrateToGraph(ETLFlowGraph efg, ApplPoint applPoint);

	/**
	 * 
	 * Instills the properties of the application point (e.g., schemas, NFRs) to
	 * all parts of the structure that implements this method.
	 * 
	 * @param applPoint
	 */
	public void instillProperties(ApplPoint applPoint);

}
