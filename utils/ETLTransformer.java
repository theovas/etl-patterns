package utils;

import importXLM.ImportXLMToETLGraph;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import etlFlowGraph.graph.ETLFlowGraph;

public class ETLTransformer {
	public static String xlmToKtr(String xlmPath) {
		// xlm2efg --> efg2ktr
		return null;

	}

	public static String ktrToXlm(String ktrPath) {
		// return efgToXlm(ktrToEfg(ktrPath));
		return null;
	}

	public static ETLFlowGraph xlmToEfg(String xlmPath) {
		File f = new File(xlmPath);
		List<String> contents;
		try {
			contents = java.nio.file.Files.readAllLines(f.toPath());
			String xLM = String.join("", contents);

			ImportXLMToETLGraph importXlm = new ImportXLMToETLGraph();
			ETLFlowGraph G = null;
			G = importXlm.getFlowGraph(xLM);
			return G;
		} catch (CycleFoundException exc) {
			System.out.println("    Fail!");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String efgToXlm(ETLFlowGraph efg) {
		return efg.toStringXLM();
	}

	public static String efgToKtr(ETLFlowGraph efg) {
		return null;
	}

	public static ETLFlowGraph ktrToEfg(String ktrPath) {
		return null;
	}
}
