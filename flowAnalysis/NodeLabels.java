package flowAnalysis;

public enum NodeLabels implements NodeLabel {
	src() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInpFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "src";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a source node, i.e., has no incoming flows";
		}

	},

	snglin() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInpFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "snglin";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a single input node, i.e., has exactly one incoming flow";
		}

	},
	mrg() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInpFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "mrg";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a merging node, i.e., has more than one incoming flow";
		}

	},
	snk() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfOutpFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "snk";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a sink node, i.e., has no outgoing flows";
		}

	},
	snglout() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfOutpFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "snglout";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a single output node, i.e., has exactly one outgoing flow";
		}

	},
	splt() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfOutpFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "splt";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a splitting node, i.e., has more than one outgoing flow";
		}

	},
	fltr() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "fltr";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a filter node, i.e., has exactly one incoming and exactly one outgoing flow";
		}

	},
	plainsnk() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "plainsnk";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a plain sink node, i.e., has exactly one incoming flow and no outgoing flow";
		}

	},
	plainsplt() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "plainsplt";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a plain splitting node, i.e., has exactly one incoming flow and more than one outgoing flow";
		}

	},
	plainsrc() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "plainsrc";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a plain source node, i.e., has no incoming flow and exactly one outgoing flow";
		}

	},

	sprt() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "sprt";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a separate node, i.e., has no incoming or outgoing flows";
		}

	},
	multisrc() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "multisrc";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a source with multiple outgoing flows";
		}

	},
	
	plainmrg() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "plainmrg";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a merging node with exactly one outgoing flow";
		}

	},
	multisnk() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "multisnk";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is a sink node with multiple one incoming flows";
		}

	},
	mrgsplt() {

		@Override
		public ClassificationType getClassificationType() {
			// TODO Auto-generated method stub
			return ClassificationType.narityOfInAndOutFlowsBased;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return "mrgsplt";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "indicates that the node is both a splitting and a merging node";
		}

	},

	;

}
