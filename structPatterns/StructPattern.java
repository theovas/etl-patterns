package structPatterns;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.xml.transform.TransformerConfigurationException;

import org.jgrapht.Graph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.SimpleGraph;
import org.xml.sax.SAXException;

import etlFlowGraph.graph.ETLEdge;
import flowAnalysis.LabelBasedOnOpType;
import flowAnalysis.LabellingStrategy;

public class StructPattern extends SimpleGraph {
	public static int pattIdCounter = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pattId = -1;

	/**
	 * a property to show the experiment type through which this pattern was
	 * extracted, for example with new or old data flows, with the labeling type
	 * and the threshold percentage. Example: "N_OpType_30" refers to new set of
	 * data flows, labeled with LabelBasedOnOpType and threshold for similarity
	 * being 30%.
	 */
	private String expType = "";

	/**
	 * the size of the pattern = the number of edges between the nodes of the
	 * pattern. The reason it is a string is to easily store it in mongodb
	 */
	private String size = "";

	/**
	 * the labellingStrategy used to extract this pattern
	 */
	private String labellingType = "";

	/**
	 * the frequency threshold used to extract this pattern
	 */
	private String threshold = "";

	/**
	 * whether or not this pattern is maximal
	 */
	private String isMax = "";

	/**
	 * Hashtable containing the structure of the pattern nodes, <br>
	 * relating the id of each node (vertex of a structural pattern) to the
	 * operation structure it represents. <br>
	 */
	private Hashtable<Integer, PatternNode> pattNodes = new Hashtable<Integer, PatternNode>();

	/**
	 * Hashtable containing information about the node labels of the specific
	 * graph, e.g., how many nodes have one specific label
	 */
	private Hashtable<String, LabelCatalog> labelCatalogs = new Hashtable<String, LabelCatalog>();

	// private Set<PatternEdge> edges = new HashSet<PatternEdge>();

	// shows if the graph is in a canonical form using canonical labeling
	// algorithms for lexicographical ordering. By default, using PAFI tool,
	// they are.
	private boolean isLexicographicallyOrdered = true;

	// private Set<Integer> workingSet = new HashSet<Integer>();

	/**
	 * Basic constructor of the StructPattern Graph.
	 * 
	 * @param arg0
	 * 
	 */
	public StructPattern() {
		super(PatternEdge.class);
		pattId = ++pattIdCounter;

	}

	/**
	 * Method that adds the newly created pattern node as a node to the
	 * StructPattern graph. <br>
	 * Method assigns the node ID to the newly added node if it was not defined
	 * before (-1).
	 * 
	 * @param newNode
	 *            new pattern node to be added
	 * @return Newly added node if the node with the given node ID does not
	 *         exist in the graph. Otherwise it returns null.
	 */
	public PatternNode addNode(PatternNode newNode) {
		// newNode.setStructPattern(this);
		if (newNode.getNodeID() == -1)
			newNode.setNodeID(PatternNode.getNodeIDCnt());

		newNode.setParentPatternID(pattId);
		// System.out.println("adding structPattern: " + this
		// + "to newly added node");
		newNode.setStructPattern(this);
		if (addVertex(newNode.getNodeID())) {
			pattNodes.put(newNode.getNodeID(), newNode);
			// System.out.println("just added new node with id: "
			// + newNode.getNodeID());
			// System.out.println(this);
			return newNode;

		} else {
			return pattNodes.get(newNode.getNodeID());
		}
	}

	/**
	 * Method that removes a pattern node from the StructPattern graph. <br>
	 * Method removes the node ID from the removed node by replacing it with -1.
	 * 
	 * @param newNode
	 *            new pattern node to be added
	 * @return Newly added node if the node with the given node ID does not
	 *         exist in the graph. Otherwise it returns null.
	 */
	public boolean removeNode(PatternNode nodeToRemove) {
		int exId = nodeToRemove.getNodeID();
		nodeToRemove.setParentPatternID(-1);
		if (removeVertex(nodeToRemove.getNodeID())) {
			// System.out.println("removing structPattern: " + this
			// + "from removed node");
			nodeToRemove.setStructPattern(null);
			pattNodes.remove(nodeToRemove.getNodeID());
			nodeToRemove.setNodeID(-1);
			// System.out.println("just removed node with id: " + exId);
			return true;

		} else {
			// System.out
			// .println("this pattern node cannot be removed because it is not part of the structural pattern");
			return false;
		}
	}

	/**
	 * @return the pattId
	 */
	public int getPattId() {
		return pattId;
	}

	/**
	 * @param pattId
	 *            the pattId to set
	 */
	public void setPattId(int pattId) {
		this.pattId = pattId;
	}

	/**
	 * @return the pattNodes
	 */
	public Hashtable<Integer, PatternNode> getPattNodes() {
		return pattNodes;
	}

	/**
	 * @param pattNodes
	 *            the pattNodes to set
	 */
	public void setPattNodes(Hashtable<Integer, PatternNode> pattNodes) {
		this.pattNodes = pattNodes;
	}

	/**
	 * @return the isLexicographicallyOrdered
	 */
	public boolean isLexicographicallyOrdered() {
		return isLexicographicallyOrdered;
	}

	/**
	 * @param isLexicographicallyOrdered
	 *            the isLexicographicallyOrdered to set
	 */
	public void setLexicographicallyOrdered(boolean isLexicographicallyOrdered) {
		this.isLexicographicallyOrdered = isLexicographicallyOrdered;
	}

	// /**
	// * @return the workingSet
	// */
	// public Set<Integer> getWorkingSet() {
	// return workingSet;
	// }
	//
	// /**
	// * @param workingSet
	// * the workingSet to set
	// */
	// public void setWorkingSet(Set<Integer> workingSet) {
	// this.workingSet = workingSet;
	// }

	/**
	 * @return the labelCatalogs
	 */
	public Hashtable<String, LabelCatalog> getLabelCatalogs() {
		return labelCatalogs;
	}

	/**
	 * @param labelCatalogs
	 *            the labelCatalogs to set
	 */
	public void setLabelCatalogs(Hashtable<String, LabelCatalog> labelCatalogs) {
		this.labelCatalogs = labelCatalogs;
	}

	public String toLabelGraphDescr() {
		PatternEdge e;
		String desc = "";
		for (Object o : this.edgeSet()) {
			e = (PatternEdge) o;
			desc += this.getPattNodes().get(e.getSource()).getNodeID();
			desc += ":";
			desc += this.getPattNodes().get(e.getSource()).getLabel();
			desc += "--";
			desc += this.getPattNodes().get(e.getTarget()).getNodeID();
			desc += ":";
			desc += this.getPattNodes().get(e.getTarget()).getLabel();
			desc += "\n";

		}
		return desc;
	}

	/**
	 * Saves a graph to GraphML format.
	 * 
	 * @param graph
	 * @param filename
	 * @throws IOException
	 */
	public String toGraphML() throws IOException {
		StructPattern sp = this;
		// GraphMLExporter<String, MyWeightedEdge> exporter = new
		// GraphMLExporter();

		// In order to be able to export edge and node labels and IDs,
		// we must implement providers for them
		VertexNameProvider<Integer> vertexIDProvider = new VertexNameProvider<Integer>() {

			@Override
			public String getVertexName(Integer vertex) {
				return vertex.toString();
			}
		};

		VertexNameProvider<Integer> vertexNameProvider = new VertexNameProvider<Integer>() {

			@Override
			public String getVertexName(Integer vertex) {
				return sp.getPattNodes().get(vertex).getLabel();
			}
		};

		EdgeNameProvider edgeIDProvider = new EdgeNameProvider() {

			@Override
			public String getEdgeName(Object edge) {
				// TODO Auto-generated method stub
				return ((PatternEdge) edge).getSource().toString() + " > "
						+ ((PatternEdge) edge).getTarget().toString();
			}
		};

		EdgeNameProvider edgeLabelProvider = new EdgeNameProvider() {

			@Override
			public String getEdgeName(Object edge) {
				return "unlbl";
			}
		};

		GraphMLExporter exporter = new GraphMLExporter(vertexIDProvider,
				vertexNameProvider, edgeIDProvider, edgeLabelProvider);

		Writer sw = new StringWriter();

		try {
			exporter.export(sw, this);
			// System.out.println(sw.toString());
			return sw.toString();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * returns true only if the two struct patterns have the same nodes (node
	 * ids) and exactly the same edges among them, taking directionality under
	 * consideration.
	 * 
	 * @param sp2
	 * @return
	 */
	public boolean equalsDirected(StructPattern sp2) {
		// check if pattern nodes are the same in the two struct patterns
		if (!this.getPattNodes().keySet().equals(sp2.getPattNodes().keySet())) {
			return false;
		}

		// check if all the edges of this struct pattern exist in the edges of
		// sp2
		PatternEdge e1;
		PatternEdge e2;
		boolean found;
		for (Object o1 : this.edgeSet()) {
			found = false;
			e1 = (PatternEdge) o1;
			for (Object o2 : sp2.edgeSet()) {
				e2 = (PatternEdge) o2;
				if ((e1.getSource() == e2.getSource())
						&& (e1.getTarget() == e2.getTarget())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				return false;
			}
		}
		// check if all the edges of sp2 exist in the edges of this struct
		// pattern
		for (Object o1 : sp2.edgeSet()) {
			found = false;
			e1 = (PatternEdge) o1;
			for (Object o2 : this.edgeSet()) {
				e2 = (PatternEdge) o2;
				if ((e1.getSource() == e2.getSource())
						&& (e1.getTarget() == e2.getTarget())) {
					found = true;
					break;
				}
			}
			if (found == false) {
				return false;
			}
		}

		return true;
	}

	/**
	 * returns true if two struct patterns are equal, without taking
	 * directionality under consideration, considering them as undirectional
	 * graphs
	 * 
	 * TODO: refactor this very bad implementation
	 * 
	 * @param sp2
	 * @return
	 */
	public boolean equals(StructPattern sp2) {
		// LabellingStrategy lbs = new LabelBasedOnOpType();
		ArrayList<CandidatePatternMatch> sp1Insp2 = IsomorphismTester
				.getAllPatternMatches(this, sp2);
		ArrayList<CandidatePatternMatch> sp2Insp1 = IsomorphismTester
				.getAllPatternMatches(sp2, this);
		return ((sp1Insp2.size() >= 1) && (sp2Insp1.size() >= 1));
	}

	public boolean isSubpatternOf(StructPattern sp2) {
		// LabellingStrategy lbs = new LabelBasedOnOpType();
		ArrayList<CandidatePatternMatch> sp1Insp2 = IsomorphismTester
				.getAllPatternMatches(this, sp2);
		// is a subpattern when it can be matched to a part of sp2 and sp2 size
		// (=number of edges) is greater
		return ((sp1Insp2.size() >= 1) && (sp2.edgeSet().size() > this
				.edgeSet().size()));
	}

	/**
	 * @return the expType
	 */
	public String getExpType() {
		return expType;
	}

	/**
	 * @param expType
	 *            the expType to set
	 */
	public void setExpType(String expType) {
		this.expType = expType;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the threshold
	 */
	public String getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the isMax
	 */
	public String getIsMax() {
		return isMax;
	}

	/**
	 * @param isMax
	 *            the isMax to set
	 */
	public void setIsMax(String isMax) {
		this.isMax = isMax;
	}

	/**
	 * @return the labellingType
	 */
	public String getLabellingType() {
		return labellingType;
	}

	/**
	 * @param labellingType
	 *            the labellingType to set
	 */
	public void setLabellingType(String labellingType) {
		this.labellingType = labellingType;
	}

}
