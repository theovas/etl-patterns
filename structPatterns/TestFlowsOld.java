package structPatterns;

public enum TestFlowsOld implements TestFlow {

	dimAccountHL() {
		@Override
		public String getXLMFilepath() {
			// old:
			// return "tests/tpcdi/dimAccount_agn.xml";
			return "tests/tpcdi/complete/DimAccount_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.XMLLoading;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 0;
		}

	},
	dimBrokerHL() {

		@Override
		public String getXLMFilepath() {
			// old:
			// return "tests/tpcdi/dimBroker_agn.xml";
			return "tests/tpcdi/complete/DimBroker_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 1;
		}

	},
	dimCompanyHL() {

		@Override
		public String getXLMFilepath() {
			// old
			// return "tests/tpcdi/dimCompany_agn.xml";
			return "tests/tpcdi/complete/DimCompany_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 2;
		}

	},
	// dimCustomer() {
	//
	// @Override
	// public String getXLMFilepath() {
	// return "tests/tpcdi/dimCustomer_agn.xml";
	// }
	//
	// },
	// factCashBalances() {
	//
	// @Override
	// public String getXLMFilepath() {
	// return "tests/tpcdi/factCashBalances_agn.xml";
	// }
	//
	// },
	// financial() {
	//
	// @Override
	// public String getXLMFilepath() {
	// return "tests/tpcdi/financial_agn.xml";
	// }
	//
	// },
	dimSecurityHL() {
		@Override
		public String getXLMFilepath() {
			// old
			// return "tests/tpcdi/dimCompany_agn.xml";
			return "tests/tpcdi/complete/DimSecurity_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 3;
		}
	},
	financialHL() {
		@Override
		public String getXLMFilepath() {
			// old
			// return "tests/tpcdi/dimCompany_agn.xml";
			return "tests/tpcdi/complete/Financial_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 4;
		}
	},
	dimAccountHLOld() {
		@Override
		public String getXLMFilepath() {
			// old:
			return "tests/tpcdi/dimAccount_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.XMLLoading;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 200;
		}

	},
	dimBrokerHLOld() {

		@Override
		public String getXLMFilepath() {
			// old:
			return "tests/tpcdi/dimBroker_agn.xml";

		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 201;
		}

	},
	dimCompanyHLOld() {

		@Override
		public String getXLMFilepath() {
			// old
			return "tests/tpcdi/dimCompany_agn.xml";

		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 202;
		}

	},
	dimCustomerOld() {

		@Override
		public String getXLMFilepath() {
			return "tests/tpcdi/dimCustomer_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			// TODO Auto-generated method stub
			return 203;
		}

	},
	factCashBalancesOld() {

		@Override
		public String getXLMFilepath() {
			return "tests/tpcdi/factCashBalances_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			// TODO Auto-generated method stub
			return 204;
		}

	},
	financialOld() {

		@Override
		public String getXLMFilepath() {
			return "tests/tpcdi/financial_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			// TODO Auto-generated method stub
			return 205;
		}

	},
	dimSecurityHLOld() {
		@Override
		public String getXLMFilepath() {
			// old
			// return "tests/tpcdi/dimCompany_agn.xml";
			return "tests/tpcdi/complete/DimSecurity_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {
			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {
			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {
			return 206;
		}
	},
}
