package utils;

import importXLM.ImportXLMToETLGraph;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph.CycleFoundException;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import etlFlowGraph.graph.ETLFlowGraph;

/**
 * @author vasileios
 * 
 *         from xlm file to etl flow graph
 *
 */
public class TestForVasileios {

	private static String xLM_Path = "tpch";
	private static String Experiments_Path = "experiments";
	private static int N_SAMPLES_ITERATOR = 10;
	private static int N_FORGE_EXECUTIONS = 10;

	private static File experimentsFile = null;

	private static ETLFlowGraph parseXLM(File f) throws IOException {
		List<String> contents = java.nio.file.Files.readAllLines(f.toPath());
		String xLM = String.join("", contents);

		ImportXLMToETLGraph importXlm = new ImportXLMToETLGraph();
		ETLFlowGraph G = null;
		try {
			G = importXlm.getFlowGraph(xLM);
		} catch (CycleFoundException exc) {
			System.out.println("    Fail!");
			return null;
		}
		return G;
	}

	public static void main(String[] args) throws Exception {
		xLM_Path = "/Users/btheo/Desktop/Vasileios/experiments/Quarry/forge/xLM/tpch/vas/financial_agn.xml";

		File f = new File(xLM_Path);
		parseXLM(f);

	}

}