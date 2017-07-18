package structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import defaultQPatterns.CrosscheckSources;
import patternGeneration.ApplConfiguration;
import qPatterns.QPattern;
import qPatterns.QPatternApplication;
import qPatterns.QPatternName;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;

public class EFGPatternApplicationFlyweight {
	
	public static int efid=0;
	private int flowId;
	
	
	public EFGPatternApplicationFlyweight() {
		efid++;
		this.flowId  = efid;
	}

	private ArrayList<QPatternApplication> qPatternApplications = new ArrayList<QPatternApplication>();
	
	public ETLFlowGraph getEFG(ETLFlowGraph initialEFG) {
		ETLFlowGraph derivedEFG = initialEFG;
		ETLFlowGraph startingEFG = initialEFG;
		ApplPoint applPoint = null;
		for (QPatternApplication qpa:this.qPatternApplications){
			startingEFG = derivedEFG;
			if (qpa.getaPId().getaPKind() == ApplPointKind.edge){
				EdgeApplPoint eap = new EdgeApplPoint();
				eap.setEfGraph(startingEFG);
				ETLEdge eEdge = null;
				Set<ETLEdge> edges = startingEFG.edgeSet();
				for (ETLEdge edge:edges){
					if (startingEFG.getEtlFlowOperations().get(edge.getSource()).getOperationName().equals(qpa.getaPId().getLeftPart())){
						if (startingEFG.getEtlFlowOperations().get(edge.getTarget()).getOperationName().equals(qpa.getaPId().getRightPart())){
							eEdge = edge;
							break;
						}
					}
				}
				if (eEdge!=null){
				eap.seteEdge(eEdge);
				applPoint = eap;
				}
			}
			else if (qpa.getaPId().getaPKind() == ApplPointKind.node){
				NodeApplPoint nap = new NodeApplPoint();
				nap.setEfGraph(startingEFG);
				Iterator it = startingEFG.iterator();
				ETLFlowOperation efo;
				while (it.hasNext()){
					efo = startingEFG.getEtlFlowOperations().get(it.next());
					if (efo.getOperationName().equals(qpa.getaPId().getLeftPart())){
						nap.setEfOperator(efo);
						applPoint = nap;
						break;
					}
				}

			}
			else if (qpa.getaPId().getaPKind() == ApplPointKind.graph){
				// do stuff...
				//TODO: complete this part
			}
			if (applPoint != null){
			derivedEFG = this.getPatternInstanceFromName(qpa.getPatternName(),qpa.getApconf()).deploy(applPoint);
			}
		}
		return derivedEFG;
	}
	
//	public static EFGFlyweight getFlyweightFromEFG(ETLFlowGraph efg) {
//		
//	}

	/**
	 * IMPORTANT! The order of the qPatternApplications is important!
	 * 
	 * @return the qPatternApplications
	 */
	public ArrayList<QPatternApplication> getqPatternApplications() {
		return qPatternApplications;
	}
	
	public ArrayList<QPatternApplication> getqPatternApplicationsCopy() {
		ArrayList<QPatternApplication> appsCopy = new ArrayList<QPatternApplication>(qPatternApplications.size());
		for (QPatternApplication app:qPatternApplications){
			QPatternApplication newqpa= new QPatternApplication(app.getPatternName(),app.getaPId(), app.getApconf());
			appsCopy.add(newqpa);
		}
		return appsCopy;
	}

	/**
	 * @param qPatternApplications the qPatternApplications to set
	 */
	public void setqPatternApplications(
			ArrayList<QPatternApplication> qPatternApplications) {
		this.qPatternApplications = qPatternApplications;
	}
	
	/**
	 * @param
	 */
	public void addqPatternApplication(
			QPatternApplication qPatternApplication) {
		this.qPatternApplications.add(qPatternApplication);
	}

	// maybe i can refactor to not need reflection
	public QPattern getPatternInstanceFromName(QPatternName pname, ApplConfiguration apcon){
		try {
			// TODO: fix to be non-hardcoded! It is now separate from the others because it takes arguments
			if (pname == QPatternName.CrosscheckSources){
				return new CrosscheckSources(apcon.getAlternativeSources(),apcon.getJoinKeys());
			}
			else{
			return (QPattern) Class.forName("defaultQPatterns."+pname.name()).newInstance();
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the flowId
	 */
	public int getFlowId() {
		return flowId;
	}

	/**
	 * @param flowId the flowId to set
	 */
	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}
	
	
}
