/**
 * 
 */
package defaultQPatterns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import operationDictionary.ETLOperationType;
import operationDictionary.ExpressionOperator;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionConstant;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.graph.ETLEdge;
import etlFlowGraph.graph.ETLFlowGraph;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import etlFlowGraph.schema.Schema;
import qPatterns.QPattern;
import qPatterns.SimpleConditionExpr;
import qPatterns.TopologyCondition;
import structures.AddOperation;
import structures.ApplPoint;
import structures.ETLStep;
import structures.EdgeApplPoint;
import structures.NodeApplPoint;
import structures.StepComponent;
import structures.StepComposite;
import structures.StepSequence;
import utilities.ObjectPair;

/**
 * 
 * A pattern class for the addition of outer join operations. When it is used, a
 * list of joining keys can be defined 
 * 
 * @author vasileios
 * 
 */
public class IGeneratorTesterJoin extends QPattern {
	private static Integer lid = 0;

	// these are the joining keys, object pairs of integer (for the node id) and
	// attribute (for the output schema attribute of the corresponding operator)
	private ArrayList<ObjectPair<Integer, Attribute>> keys = new ArrayList<ObjectPair<Integer, Attribute>>();

	public IGeneratorTesterJoin() {
		// TopologyCondition tc = new SimpleConditionExpr();

		StepSequence ss = new StepSequence();
		lid++;

		this.addAPTypes(QPattern.applPointType.node);
		ETLStep es1 = new AddOperation(ETLNodeKind.Datastore,
				ETLOperationType.etlOperationTypes.get("FileInput"),
				"FileInput_" + lid);
		ETLStep es2 = new AddOperation(ETLNodeKind.Operator,
				ETLOperationType.etlOperationTypes.get("Join"), "Join_" + lid);
		ETLStep es3 = new AddOperation(ETLNodeKind.Operator,
				ETLOperationType.etlOperationTypes.get("Project"), "Project_"
						+ lid);
		ss.addStepChild(es1);
		ss.addStepChild(es2);
		ss.addStepChild(es3);
		this.addImplementation(ss);

	}

	/*
	 * Deploys the pattern by adding to the operators the input schemata of the
	 * preceding operation and the output schemata of the succeeding operation.
	 * It also makes the input source identical to a source that is found as a
	 * predecessor of the application point, for which the (join) key is known.
	 * If it cannot find a predefined join key, it uses a random attribute of
	 * the operator's output schema. The application point is a node.
	 */
	@Override
	public ETLFlowGraph deploy(ETLFlowGraph efg, ApplPoint applPoint) {
		NodeApplPoint nap = (NodeApplPoint) applPoint;
		Integer foundSource = -1;
		Attribute joinKey = null;
		// the output schema of the nap node, to be one of the input schemata of the join operation
		Schema opOutSchema = null;
		// the attributes of the opOutSchema
		ArrayList<Attribute> ouAtts = null;
		// this must be refined and it should be a prerequisite that there exists a succeeding node, so that such cases must be avoided
		if (nap.getEfOperator().getOperationType() == ETLOperationType.etlOperationTypes.get("FileOutput")){
			System.out.println("Cannot deploy join pattern on fileoutput operator");
			return null;
		}
		// in case that the application node is an input source
		// MUST BE EXTENDED TO INCLUDE OTHER TYPES OF INPUT SOURCES!!!
		if (nap.getEfOperator().getOperationType() == ETLOperationType.etlOperationTypes.get("FileInput")){
			// the input source node becomes the found source
			foundSource = nap.getEfOperator().getNodeID();
			// choose a random attribute from the input schema of the file input operation
			ArrayList<Attribute> srcAtts = (ArrayList<Attribute>) nap.getEfOperator().getInputSchemata().get(0).getAttributes();
			joinKey = srcAtts.get(new Random().nextInt(srcAtts.size()));
			opOutSchema = (Schema) nap.getEfOperator().getInputSchemata().get(0).clone();
		    ouAtts = (ArrayList<Attribute>) opOutSchema
					.getAttributes();
		} else {
		ArrayList<Integer> sources = nap.getSources();
		opOutSchema = (Schema) nap.getOutputSchemata().get(0).clone();
		ouAtts = (ArrayList<Attribute>) opOutSchema
				.getAttributes();
		// find one source for which the (join) key is known
		// needs a better algorithm here!

		
		boolean foundS = false;
		Iterator iter = sources.iterator();
		while (iter.hasNext() && (foundS == false)) {
			Integer srcId = (Integer) iter.next();
			for (ObjectPair<Integer, Attribute> opr : this.keys) {
				if (srcId.equals(opr.left)) {
					foundS = true;
					foundSource = srcId;
					joinKey = opr.right;
					break;
				}
			}
			if (foundS == false) {
				// in the future we should try to identify keys from the
				// attribute name

				// get a random source and a random attribute of the operator's
				// output schema
				foundSource = sources.get(new Random().nextInt(sources.size()));
				joinKey = ouAtts.get(new Random().nextInt(ouAtts.size()));
			}
		}
		}
		// get the first and only implementation
		StepSequence impl = (StepSequence) this.getImplementations().get(0);
		// replace the input source of the implementation with the found one
		// !!! this must be replaced with the PROVIDED input source!
		AddOperation srcStep = (AddOperation) impl.getChildAtIndex(0);
		ETLFlowOperation newSrc = (ETLFlowOperation) efg.getEtlFlowOperations().get(foundSource).clone();
		Schema oldSch = newSrc.getInputSchemata().get(0);
		Schema newSch = new Schema();
		Attribute joinKey2 = null;
		// create new attributes that are copies of the old attributes with different names
		for(Attribute aOld:oldSch.getAttributes()){
			Attribute aNew = Attribute.newAttribute(srcStep.getAssociatedETLOperation().getOperationName()+"_"+aOld.getAttrName(), aOld.getAttrDatatype());
			newSch.addAttribute(aNew);
			// get reference to the attribute that is equivalent to the join key
			if (aOld.getAttrName().equals(joinKey.getAttrName())){
				joinKey2 = aNew;
			}
		}
		ArrayList<Schema> fis = new ArrayList<Schema>();
		fis.add(newSch);
		newSrc.setInputSchemata(fis);
		newSrc.setOperationName("FileInput_" + lid);
		srcStep.setEfo(newSrc);
		// check if the join key attribute exists in the data source (for the cases of projected out attributes)
				boolean foundMatch = false;
				for (Attribute aOld:oldSch.getAttributes()){
				if (joinKey.getAttrName().equals(aOld.getAttrName())){
					foundMatch = true;
					break;
				}
				}
				if (foundMatch == false){
					//------------------------------------------------------------------------------------------------------------
					// this should be changed to pick another attribute or change the datasource
					System.out.println("Potential join attribute is not present, probably because of projection or grouping");
					return null;
				}
		
		ETLFlowGraph pefg = impl.getAsETLFlowGraph();
		// accessing the etlflowgraph values to find id of the different
		// operators
		Iterator itr = pefg.iterator();
		// gets the first element of the graph, which is the
		// input source operation id
		Integer s2id = (Integer) itr.next();
		// gets the second element of the graph, which is the
		// join operation id
		Integer joinId = (Integer) itr.next();
		// gets the third element of the graph, which is the
		// projection operation id
		Integer projectId = (Integer) itr.next();
		
		ETLFlowOperation source2 = pefg.getEtlFlowOperations().get(s2id);
		ETLFlowOperation join = pefg.getEtlFlowOperations().get(joinId);
		ETLFlowOperation project = pefg.getEtlFlowOperations().get(projectId);
		
		// set the input schemata of the join operator
		opOutSchema.setSchemaCard(1);
		Schema src2outSch = source2.getOutputSchemata().get(0);
		src2outSch.setSchemaCard(2);
		join.addInputSchema(1, opOutSchema);
		join.addInputSchema(2, src2outSch);
		// set the join expression
		ExpressionAttribute lex = new ExpressionAttribute(joinKey);
		//lex.getComparableValue()
		//System.out.println("left comparable value: "+lex.getComparableValue());
		ExpressionAttribute rex = new ExpressionAttribute(joinKey2);
		//System.out.println("right comparable value: "+rex.getComparableValue());
		ExpressionOperator eop = ExpressionOperator.expressionOperators.get("==");;
		//System.out.println("operator comparable value: "+eop.getComparableValue());
		Expression fex = new Expression(lex, eop, rex, ExpressionKind.EXPRESSION);
		fex.addUsedAttribute(joinKey);
		fex.addUsedAttribute(joinKey2);
		join.addSemanticsExpressionTree("join_cond", fex);
		ArrayList<Attribute> inAttrs1 = (ArrayList<Attribute>) join.getInputSchemata().get(0).getAttributes();
		ArrayList<Attribute> inAttrs2 = (ArrayList<Attribute>) join.getInputSchemata().get(1).getAttributes();
		ArrayList<Attribute> inAttrs1cp = (ArrayList<Attribute>) inAttrs1.clone();
		for (Attribute a2:inAttrs2){
			inAttrs1cp.add(a2);
		}
		Schema joinOuSch = new Schema();
		joinOuSch.setAttributes(inAttrs1cp);
		joinOuSch.setSchemaCard(1);
		ArrayList<Schema> arsch = new ArrayList<Schema>();
		arsch.add(joinOuSch);
		join.setOutputSchemata(arsch);		
		//set the input and output schema of the project operation
		project.setInputSchemata(arsch);
		ArrayList<Schema> osCp = new ArrayList<Schema>();
		for (Schema s:nap.getOutputSchemata()){
			osCp.add((Schema) s.clone());
		}
		project.setOutputSchemata(osCp);
		
		//set the expressions for each of the attributes that does not get projected out
		for (Attribute a:project.getOutputSchemata().get(0).getAttributes()){
		ExpressionAttribute lex2 = new ExpressionAttribute(a);
		Expression fex2 = new Expression(lex2, null, null, ExpressionKind.EXPRESSION);
		fex2.addUsedAttribute(a);
		project.addSemanticsExpressionTree("project", fex2);
		}
		ETLEdge applEdge = nap.getOutEdges().iterator().next();
		ETLFlowGraph returnGraph = efg.insetSubgraphToEdge(pefg, applEdge, joinId, projectId);
		return (returnGraph);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qPatterns.QPattern#getFitness(structures.ApplPoint)
	 */
	@Override
	public ObjectPair<Integer, String> getFitness(ApplPoint ap) {
		// for now it is always default value
		ObjectPair<Integer, String> op = new ObjectPair<Integer, String>();
		op.left = 50;
		op.right = "no checks implemented yet";
		return op;
	}

	public ArrayList<ObjectPair<Integer, Attribute>> getKeys() {
		return keys;
	}

	public void setKeys(ArrayList<ObjectPair<Integer, Attribute>> keys) {
		this.keys = keys;
	}

	public void addKey(ObjectPair<Integer, Attribute> key) {
		this.keys.add(key);
	}

}
