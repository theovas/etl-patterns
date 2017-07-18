package measures;

import java.math.BigDecimal;

import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;
import MeasureUtils.ETLStaticMeasures;
import MeasureUtils.GraphComplexityMeasures;
import etlFlowGraph.graph.ETLFlowGraph;
import measureDictionary.InfluenceKind;
import measureDictionary.QualityCharacteristicName;

/**
 * this enum groups all the implemented measures so far, but if this class file
 * gets too big in the future, it can be split to multiple enums, each
 * implementing the Measure interface.
 * 
 * @author vasileios
 *
 */
public enum Measures implements Measure {
	E1bM1() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Flexibility;
		}

		@Override
		public String getDescription() {

			return "Number of precedence dependences between activities";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {

			return GraphComplexityMeasures.activityRelationshipsNo(efg);
		}

		@Override
		public String getName() {

			return "E1bM1";
		}

	},
	E2aM2() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Understandability;
		}

		@Override
		public String getDescription() {

			return "Number of precedence dependences between activities";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {

			return GraphComplexityMeasures.activityRelationshipsNo(efg);
		}

		@Override
		public String getName() {

			return "E2aM2";
		}

	},
	E2aM1() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Understandability;
		}

		@Override
		public String getDescription() {

			return "Number of activities of the software process model";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {

			return GraphComplexityMeasures.activitiesNo(efg);
		}

		@Override
		public String getName() {

			return "E2aM1";
		}

	},
	E3aM2() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Maintainability;
		}

		@Override
		public String getDescription() {

			return "Number of relationships among workflow components";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {

			return GraphComplexityMeasures.activityRelationshipsNo(efg);
		}

		@Override
		public String getName() {

			return "E3aM2";
		}

	},
	E3aM1() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Maintainability;
		}

		@Override
		public String getDescription() {

			return "Length of process workflow longest path";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {

			return (double) GraphComplexityMeasures.longestPathLength(efg);
		}

		@Override
		public String getName() {

			return "E3aM1";
		}

	},
	E3aM4() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Maintainability;
		}

		@Override
		public String getDescription() {

			return "Number of output elements in the process model";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {

			return (double) GraphComplexityMeasures.outputActivitiesNo(efg);
		}

		@Override
		public String getName() {

			return "E3aM4";
		}

	},
	E3aM5() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Maintainability;
		}

		@Override
		public String getDescription() {

			return "Number of merge elements in the process model";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {
			int mergeElNo = 0;
			// TODO: instead of hard-coding the types of operators that are
			// considered merge elements, group them in a set and iterate
			// through the set and make a corresponding method that takes as
			// argument a set of attributes and does not need to iterate
			// multiple times over the efg nodes.
			mergeElNo += ETLStaticMeasures.opTypeCardinality(efg,
					ETLOperationType.etlOperationTypes
							.get(OperationTypeName.Join.name()));
			mergeElNo += ETLStaticMeasures.opTypeCardinality(efg,
					ETLOperationType.etlOperationTypes
							.get(OperationTypeName.LeftOuterJoin.name()));
			mergeElNo += ETLStaticMeasures.opTypeCardinality(efg,
					ETLOperationType.etlOperationTypes
							.get(OperationTypeName.Union.name()));
			// possibly more types of operators in the future...

			return (double) mergeElNo;
		}

		@Override
		public String getName() {

			return "E3aM5";
		}

	},
	E3bM1() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Testability;
		}

		@Override
		public String getDescription() {

			return "Cyclomatic Complexity of the ETL process workflow";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {
			return (double) GraphComplexityMeasures.cyclomaticComplexity(efg);
		}

		@Override
		public String getName() {

			return "E3bM1";
		}

	},
	C2aM3() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.TimeEfficiency;
		}

		@Override
		public String getDescription() {

			return "Number of blocking operations";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {
			int blockElNo = 0;
			// TODO: instead of hard-coding the types of operators that are
			// considered merge elements, group them in a set and iterate
			// through the set and make a corresponding method that takes as
			// argument a set of attributes and does not need to iterate
			// multiple times over the efg nodes.
			blockElNo += ETLStaticMeasures.opTypeCardinality(efg,
					ETLOperationType.etlOperationTypes
							.get(OperationTypeName.Grouper.name()));
			blockElNo += ETLStaticMeasures.opTypeCardinality(efg,
					ETLOperationType.etlOperationTypes
							.get(OperationTypeName.Sort.name()));
			// possibly more types of operators in the future...

			return (double) blockElNo;
		}

		@Override
		public String getName() {

			return "C2aM3";
		}

	},
	C4civM1() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Recoverability;
		}

		@Override
		public String getDescription() {

			return "Number of recovery points used";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.positive;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {
			return (double) GraphComplexityMeasures.outputActivitiesNo(efg);
		}

		@Override
		public String getName() {

			return "C4civM1";
		}

	},
	E1aM2() {

		@Override
		public QualityCharacteristicName getQCharacteristicName() {

			return QualityCharacteristicName.Scalability;
		}

		@Override
		public String getDescription() {

			return "Number of work products of the process model, i.e., documents and models produced during process execution";
		}

		@Override
		public InfluenceKind getInfluenceToCharacteristic() {

			return InfluenceKind.negative;
		}

		@Override
		public boolean requiresSimulation() {

			return false;
		}

		@Override
		public double estimateValue(ETLFlowGraph efg) {
			return (double) GraphComplexityMeasures.outputActivitiesNo(efg);
		}

		@Override
		public String getName() {

			return "E1aM2";
		}

	},

}
