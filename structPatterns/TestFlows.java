package structPatterns;

import java.util.ArrayList;

public enum TestFlows implements TestFlow {

	dimCustomer() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/DimCustomer_agn.xml";
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

		public String getFileName() {

			return "DimCustomer.ktr";
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
			return 2;
		}

		public String getFileName() {

			return "DimBroker.ktr";
		}

	},
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
			return 3;
		}

		public String getFileName() {

			return "DimAccount.ktr";
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
			return 4;
		}

		public String getFileName() {

			return "DimCompany.ktr";
		}

	},
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
			return 5;
		}

		public String getFileName() {

			return "DimSecurity.ktr";
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
			return 6;
		}

		public String getFileName() {

			return "Financial.ktr";
		}

	},

	dimDateHl() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/DimDate_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.SMALL;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {

			return 1;
		}

		public String getFileName() {

			return "DimDate.ktr";
		}

	},

	dimTimeHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/DimTime_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.SMALL;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {

			return 7;
		}

		public String getFileName() {

			return "DimTime.ktr";
		}

	},

	dimTradePopulateHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/DimTradePopulate_agn.xml";
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

			return 8;
		}

		public String getFileName() {

			return "DimTradePopulate.ktr";
		}

	},

	dimTradeUpdateHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/DimTradePopulateUpdate_agn.xml";
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

			return 9;
		}

		public String getFileName() {

			return "DimTradePopulateUpdate.ktr";
		}

	},

	factcashbalancesHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/FactCashBalances_agn.xml";
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

			return 10;
		}

		public String getFileName() {

			return "FactCashBalances.ktr";
		}

	},

	factholdingsHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/FactHoldings_agn.xml";
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

			return 11;
		}

		public String getFileName() {

			return "FactHoldings.ktr";
		}

	},

	industryHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/Industry_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.SMALL;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {

			return 12;
		}

		public String getFileName() {

			return "Industry.ktr";
		}

	},

	prospectHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/Prospect_agn.xml";
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

			return 13;
		}

		public String getFileName() {

			return "Prospect.ktr";
		}

	},

	statustypeHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/StatusType_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.SMALL;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {

			return 14;
		}

		public String getFileName() {

			return "StatusType.ktr";
		}

	},

	taxrateHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/TaxRate_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.SMALL;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {

			return 15;
		}

		public String getFileName() {

			return "TaxRate.ktr";
		}

	},

	tradetypeHL() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/TradeType_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.SMALL;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalLoad;
		}

		@Override
		public int getFlowId() {

			return 16;
		}

		public String getFileName() {

			return "TradeType.ktr";
		}

	},

	dimAccountUpdateJobHU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 17;
		}

		public String getFileName() {

			return "JobUpdateDimAccount.kjb";
		}

	},
	dimAccountSurKeyHU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 18;
		}

		public String getFileName() {

			return "UpdateDimAccountSurKey.ktr";
		}

	},
	dimAccountUpdateHU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/UpdateDimAccount_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalUpdates;
		}

		@Override
		public int getFlowId() {

			return 19;
		}

		public String getFileName() {

			return "UpdateDimAccount.ktr";
		}

	},
	dimCustomerUpdateJobHU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 20;
		}

		public String getFileName() {

			return "JobUpdateDimCustomer.kjb";
		}

	},
	dimCustomerSurKeyHU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 21;
		}

		public String getFileName() {

			return "UpdateDimCustomerSurKey.ktr";
		}

	},
	dimCustomerUpdateHU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/UpdateDimCustomer_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.HistoricalUpdates;
		}

		@Override
		public int getFlowId() {

			return 22;
		}

		public String getFileName() {

			return "UpdateDimCustomer.ktr";
		}

	},

	dimAccountUpdateJobIU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 23;
		}

		public String getFileName() {

			return "JobIncUpdateDimAccount.kjb";
		}

	},

	dimAccountSurKeyIU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 24;
		}

		public String getFileName() {

			return "IncUpdateAccountSurKey.ktr";
		}

	},

	dimAccountUpdateIU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/IncUpdateDimAccount_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.IncrementalUpdates;
		}

		@Override
		public int getFlowId() {

			return 25;
		}

		public String getFileName() {

			return "IncUpdateDimAccount.ktr";
		}

	},

	dimCustomerUpdateJobIU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 26;
		}

		public String getFileName() {

			return "JobIncUpdateDimCustomer.kjb";
		}

	},

	dimCustomerSurKeyIU() {

		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 27;
		}

		public String getFileName() {

			return "IncUpdateDimCustomerSurKey.ktr";
		}

	},

	dimCustomerUpdateIU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/IncUpdateDimCustomer_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.IncrementalUpdates;
		}

		@Override
		public int getFlowId() {

			return 28;
		}

		public String getFileName() {

			return "IncUpdateDimCustomer.ktr";
		}

	},

	dimTradeUpdateJobHU() {
		@Override
		public String getXLMFilepath() {

			return null;
		}

		@Override
		public FlowCategory getFlowCategory() {

			return null;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return null;
		}

		@Override
		public int getFlowId() {

			return 33;
		}

		public String getFileName() {

			return "JobIncDimTrade.kjb";
		}

	},

	dimTradeUpdatePhase1IU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/UpdateDimTradePh1_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.IncrementalUpdates;
		}

		@Override
		public int getFlowId() {

			return 29;
		}

		public String getFileName() {

			return "UpdateDimTradePh1.ktr";
		}

	},

	dimTradeUpdatePhase2IU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/UpdateDimTradePh2_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.IncrementalUpdates;
		}

		@Override
		public int getFlowId() {

			return 30;
		}

		public String getFileName() {

			return "UpdateDimTradePh2.ktr";
		}

	},

	factcashbalancesIU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/UpdateFactCashBalances_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.IncrementalUpdates;
		}

		@Override
		public int getFlowId() {

			return 31;
		}

		public String getFileName() {

			return "UpdateFactCashBalances.ktr";
		}

	},

	factholdingsIU() {

		@Override
		public String getXLMFilepath() {

			return "tests/tpcdi/complete/UpdateFactHoldings_agn.xml";
		}

		@Override
		public FlowCategory getFlowCategory() {

			return FlowCategory.MEDIUM;
		}

		@Override
		public FlowPhase getFlowPhase() {

			return FlowPhase.IncrementalUpdates;
		}

		@Override
		public int getFlowId() {

			return 32;
		}

		public String getFileName() {

			return "UpdateFactHoldings.ktr";
		}

	}
}