package flowAnalysis;

import operationDictionary.ETLOpTypeCharacteristic;
import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import graphUtils.FlowPathsAnalyzer;

/**
 * A class to contain static helper methods for characterization of operators
 * that can lead to their labelling
 * 
 * @author btheo
 *
 */
public class OperationClassification {

	// a private constructor to block instantiation and subclassing
	private OperationClassification() {
	}

	public static NodeLabel inFlowNarityGrouping(ETLFlowOperation efo) {
		if (efo.getInputSchemata() != null) {
			// System.out.println("bika");
			int iNarity = efo.getInputSchemata().size();

			if ((iNarity == 0)
					|| (FlowPathsAnalyzer.inputSourceTypes.contains(efo
							.getOperationType()))) {
				return NodeLabels.src;
			} else if (iNarity == 1) {
				// if (efo.getOperationType() ==
				// ETLOperationType.etlOperationTypes.get("getXMLData")){
				// System.out.println(efo.getInputSchemata());}
				return NodeLabels.snglin;
			} else if (iNarity > 1) {
				return NodeLabels.mrg;
			}

			else
				return null;
		}
		return null;
	}

	public static NodeLabel outFlowNarityGrouping(ETLFlowOperation efo) {
		if (efo.getOutputSchemata() != null) {
			// System.out.println("bika");
			int oNarity = efo.getOutputSchemata().size();

			if ((oNarity == 0)
					|| (FlowPathsAnalyzer.outputSourceTypes.contains(efo
							.getOperationType()))) {
				return NodeLabels.snk;
			} else if (oNarity == 1) {
				// if (efo.getOperationType() ==
				// ETLOperationType.etlOperationTypes.get("getXMLData")){
				// System.out.println(efo.getInputSchemata());}
				return NodeLabels.snglout;
			} else if (oNarity > 1) {
				return NodeLabels.splt;
			}

			else
				return null;
		}
		return null;
	}

	public static NodeLabel inAndOutFlowNarityGrouping(ETLFlowOperation efo) {
		// all cases for single input flow
		if (inFlowNarityGrouping(efo).equals(NodeLabels.snglin)) {
			if (outFlowNarityGrouping(efo).equals(NodeLabels.snglout)) {
				return NodeLabels.fltr;
			} else if (outFlowNarityGrouping(efo).equals(NodeLabels.snk)) {
				return NodeLabels.plainsnk;
			} else if (outFlowNarityGrouping(efo).equals(NodeLabels.splt)) {
				return NodeLabels.plainsplt;
			}
		}
		// all cases for source nodes
		else if (inFlowNarityGrouping(efo).equals(NodeLabels.src)) {
			if (outFlowNarityGrouping(efo).equals(NodeLabels.snglout)) {
				return NodeLabels.plainsrc;
			} else if (outFlowNarityGrouping(efo).equals(NodeLabels.snk)) {
				return NodeLabels.sprt;
			} else if (outFlowNarityGrouping(efo).equals(NodeLabels.splt)) {
				return NodeLabels.multisrc;
			}
		}
		// all cases for merging nodes
		else if (inFlowNarityGrouping(efo).equals(NodeLabels.mrg)) {
			if (outFlowNarityGrouping(efo).equals(NodeLabels.snglout)) {
				return NodeLabels.plainmrg;
			} else if (outFlowNarityGrouping(efo).equals(NodeLabels.snk)) {
				return NodeLabels.multisnk;
			} else if (outFlowNarityGrouping(efo).equals(NodeLabels.splt)) {
				return NodeLabels.mrgsplt;
			}
		}
		return null;
	}

	/**
	 * Returns a STRING which consists of three letters, corresponding to
	 * schema, value and order in this order. 'c' stands for consumes, 'p'
	 * stands for produces, 'b' stands for both consumes and produces and 'n'
	 * stands for neither consumes or produces. It returns a string instead of a
	 * label just for brevity, because then we would need 3^4 (81) different
	 * labels in the corresponding enum
	 * 
	 * @param efo
	 * @return
	 */
	public static String consumeProduceGrouping(ETLFlowOperation efo) {
		String schema = "";
		String value = "";
		String order = "";
		if (efo.getOperationType().getSchema()
				.equals(ETLOpTypeCharacteristic.consumes)) {
			schema = "c";
		} else if (efo.getOperationType().getSchema()
				.equals(ETLOpTypeCharacteristic.produces)) {
			schema = "p";
		} else if (efo.getOperationType().getSchema()
				.equals(ETLOpTypeCharacteristic.consumesProduces)) {
			// b stands for both
			schema = "b";
		} else
		// n stands for nothing
		{
			schema = "n";
		}
		if (efo.getOperationType().getValue()
				.equals(ETLOpTypeCharacteristic.consumes)) {
			value = "c";
		} else if (efo.getOperationType().getValue()
				.equals(ETLOpTypeCharacteristic.produces)) {
			value = "p";
		} else if (efo.getOperationType().getValue()
				.equals(ETLOpTypeCharacteristic.consumesProduces)) {
			// b stands for both
			value = "b";
		} else
		// n stands for nothing
		{
			value = "n";
		}
		if (efo.getOperationType().getOrder()
				.equals(ETLOpTypeCharacteristic.consumes)) {
			order = "c";
		} else if (efo.getOperationType().getOrder()
				.equals(ETLOpTypeCharacteristic.produces)) {
			order = "p";
		} else if (efo.getOperationType().getOrder()
				.equals(ETLOpTypeCharacteristic.consumesProduces)) {
			// b stands for both
			order = "b";
		} else
		// n stands for nothing
		{
			order = "n";
		}
		return schema + value + order;
	}

	public static String schemaDiffGrouping(ETLFlowOperation efo) {
		// if the operation is a filter or a union or a splitter
		if ((efo.getOperationType().getOpTypeName() == OperationTypeName.Filter)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Union)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.RoundRobinMerge)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Splitter)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Router)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Dummy)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Sort)) {
			// Same Schema to the output
			return "ss";
		} else if (efo.getOperationType().getOpTypeName() == OperationTypeName.Rename) {
			// Same Schema, but not really.. maybe it needs its own category
			return "ss";

		} else if ((efo.getOperationType().getOpTypeName() == OperationTypeName.Join)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.LeftOuterJoin)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.FullOuterJoin)) {
			// Joining Schemata
			return "js";

		} else if (efo.getOperationType().getOpTypeName() == OperationTypeName.Project) {
			// "Reduced" Schemata
			return "rs";
		} else if ((efo.getOperationType().getOpTypeName() == OperationTypeName.AttributeAddition)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.UserDefinedFunction)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Sequence)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Grouper)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.Sequence)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.RegexEval)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.WSLookup)) {
			// "Increased" Schemata: more attributes are being introduced
			// TODO: check if "is" should be same as "js"
			return "is";
		} else if ((efo.getOperationType().getOpTypeName() == OperationTypeName.FileInput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.TableInput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.XMLInput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.ExcelInput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.getXMLData)) {
			// input source
			return "in";
		} else if ((efo.getOperationType().getOpTypeName() == OperationTypeName.FileOutput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.TableOutput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.XMLOutput)
				|| (efo.getOperationType().getOpTypeName() == OperationTypeName.ExcelOutput)) {
			// output source
			return "ou";
		}
		return "other";
	}
}
