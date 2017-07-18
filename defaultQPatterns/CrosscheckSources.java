package defaultQPatterns;

import java.util.ArrayList;
import java.util.Iterator;

import operationDictionary.ETLOperationType;
import operationDictionary.ExpressionOperator;
import operationDictionary.OperationTypeName;
import etlFlowGraph.ETLNonFunctionalCharacteristic;
import etlFlowGraph.attribute.Attribute;
import etlFlowGraph.expressionTree.Expression;
import etlFlowGraph.expressionTree.ExpressionAttribute;
import etlFlowGraph.expressionTree.ExpressionKind;
import etlFlowGraph.operation.ETLFlowOperation;
import etlFlowGraph.operation.ETLNodeKind;
import patternGeneration.ApplConfiguration;
import patternUtils.JoinPreserveSchema;
import qPatterns.QPattern;
import qPatterns.QPatternName;
import structures.AddOperation;
import structures.ApplPoint;
import structures.DataSource;
import structures.ETLStep;
import structures.IdAttrPair;
import structures.JoinKeys;
import structures.StepComponent;
import structures.StepSequence;
import utilities.ObjectPair;

public class CrosscheckSources extends JoinPreserveSchema {

	private ArrayList<DataSource> alternativeSources;
	// by default use the first data source
	private int sourceToBeUsedIndex = 0;
	//static int lid = 0;
	protected ETLFlowOperation inputSrcOp;
	protected AddOperation ao;
	private static final int patternId = 1;
	

	
	
	
	public CrosscheckSources(ArrayList<DataSource> alternativeSources, JoinKeys joinKeys) {
		this.alternativeSources = alternativeSources;
		this.setJoinKeys(joinKeys);
	}

	@Override
	public StepComponent getFollowingStepComponentLeft() {
		// Here is all the logic of comparison of values. An attributeAlteration part should be added.
		return null;
	}

	@Override
	public void configFollowingPartLeft() {
		// Here is all the logic of comparison and enhancement of values
		//replace the initial values with the ones from the alternative source
		
	}

	@Override
	public StepComponent getIncomingStepComponent() {
		//lid++;
//		System.out.println("edw kolaw: "+sourceToBeUsedIndex+" "+this.alternativeSources.size());
//		DataSource dc = this.alternativeSources.get(sourceToBeUsedIndex);
//		inputSrcOp = new ETLFlowOperation(ETLNodeKind.Datastore,
//					ETLOperationType.etlOperationTypes.get(dc.getOpTypeName().name()),
//					"AltDataSourceInput" + lid);
//			ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic(
//					"file_name", "", "file_name", "=", "", dc.getFilepath());
//			inputSrcOp.addoProperty("file_name", efc);
//			ETLNonFunctionalCharacteristic efc2 = new ETLNonFunctionalCharacteristic(
//					"storage_type", "", "storage_type", "=", "", dc.getStorage_type());
//			inputSrcOp.addoProperty("storage_type", efc2);
//			inputSrcOp.addInputSchema(1, dc.getInputSchema());
		//just a dummy setting
		inputSrcOp = new ETLFlowOperation(ETLNodeKind.Datastore,
				ETLOperationType.etlOperationTypes.get(OperationTypeName.FileInput.name()),
				"FCP"+this.getPatternId()+"_AltDataSourceInput_" + this.getLid());
		inputSrcOp.setNodeID(Integer.MAX_VALUE-1);
			//AddOperation ao = new AddOperation(inputSrcOp);
		this.ao = new AddOperation(inputSrcOp);
			ao.setImmutableSchema(true);
			StepSequence ss = new StepSequence();
			ss.addStepChild(ao);
			// here i can add more stuff...
			//ss.addStepChild(...)
			return ss;
      
	}

	@Override
	public void setJoinCondition(JoinKeys joinKeys) {
		        // for this implementation, it is assumed that attributes with the same name refer to the same characteristic
		// also, we assume only one join key to be present, therefore we just take the first element
		        IdAttrPair joinkey = this.getJoinKeys().getKeys().iterator().next();
		        Attribute key1 = joinkey.getAttr();
		String keyName = key1.getAttrName();
		Attribute key2 = null;
		Attribute itAttr;
		Iterator<Attribute> it = this.alternativeSources.get(sourceToBeUsedIndex).getInputSchema().getAttributes().iterator();
		while (it.hasNext()){
			itAttr = it.next();
			System.out.println("to prwto cand key: "+itAttr.getAttrName().substring(itAttr.getAttrName().indexOf("_")));
			System.out.println("to deftero cand key: "+keyName.substring(keyName.indexOf("_")));

			if (itAttr.getAttrName().substring(itAttr.getAttrName().indexOf("_")).equals(keyName.substring(keyName.indexOf("_")))){
				key2 = itAttr;
			}
		}
		        if (key2!=null){
		        // set the join expression
				ExpressionAttribute lex = new ExpressionAttribute(key1);
				//lex.getComparableValue()
				//System.out.println("left comparable value: "+lex.getComparableValue());
				ExpressionAttribute rex = new ExpressionAttribute(key2);
				//System.out.println("right comparable value: "+rex.getComparableValue());
				ExpressionOperator eop = ExpressionOperator.expressionOperators.get("==");;
				//System.out.println("operator comparable value: "+eop.getComparableValue());
				Expression fex = new Expression(lex, eop, rex, ExpressionKind.EXPRESSION);
				fex.addUsedAttribute(key1);
				fex.addUsedAttribute(key2);
				this.joinEFO.addSemanticsExpressionTree("join_attr", fex);
		        }
		        else{
		        	System.out.println("no join key found");
		        }
System.out.println("joinefo after additiopn: "+joinEFO);
	}

	@Override
	public void configIncomingPart(ApplPoint applPoint) {
		// here i can configure the incoming part and also change the input source operation if the source to be used index has been changed

		System.out.println("edw kolaw: "+sourceToBeUsedIndex+" "+this.alternativeSources.size());
		DataSource dc = this.alternativeSources.get(sourceToBeUsedIndex);
		inputSrcOp.setNodeKind(ETLNodeKind.Datastore);
		inputSrcOp.setOperationType(ETLOperationType.etlOperationTypes.get(dc.getOpTypeName().name()));
//		= new ETLFlowOperation(ETLNodeKind.Datastore,
//					ETLOperationType.etlOperationTypes.get(dc.getOpTypeName().name()),
//					"AltDataSourceInput" + lid);
			ETLNonFunctionalCharacteristic efc = new ETLNonFunctionalCharacteristic(
					"file_name", "", "file_name", "=", "", dc.getFilepath());
			inputSrcOp.addoProperty("file_name", efc);
			ETLNonFunctionalCharacteristic efc2 = new ETLNonFunctionalCharacteristic(
					"storage_type", "", "storage_type", "=", "", dc.getStorage_type());
			inputSrcOp.addoProperty("storage_type", efc2);
			inputSrcOp.addInputSchema(1, dc.getInputSchema());
			System.out.println("aaa ooo"+ao.getAssociatedETLOperation().getOperationName());
			
		
	}

	/**
	 * @return the alternativeSources
	 */
	public ArrayList<DataSource> getAlternativeSources() {
		return alternativeSources;
	}

	/**
	 * @param alternativeSources
	 *            the alternativeSources to set
	 */
	public void setAlternativeSources(ArrayList<DataSource> alternativeSources) {
		this.alternativeSources = alternativeSources;
	}

	@Override
	public QPatternName getName() {
		// TODO Auto-generated method stub
		return QPatternName.CrosscheckSources;
	}

	@Override
	public int getPatternId() {
		return this.patternId;
	}

	@Override
	public QPattern getUpdatedInstance() {
		CrosscheckSources qpUpd = new CrosscheckSources(this.alternativeSources,this.getJoinKeys());
//		//increases the lid AND updated accordingly the name of the join operator
//		qpUpd.increaseLid();
		return qpUpd;
	}
	
	public int getLid(){
		return super.getLid();
	}
	
	public void increaseLid(){
		int newLid = super.getLid();
		newLid++;
		super.setLid(newLid);
		super.joinEFO.setOperationName(this.getJoinOName());
	}

	/* (non-Javadoc)
	 * @see qPatterns.QPattern#getConfiguration()
	 */
	@Override
	public ApplConfiguration getConfiguration() {
		ApplConfiguration config = new ApplConfiguration();
		config.setAlternativeSources(this.getAlternativeSources());
		config.setJoinKeys(this.getJoinKeys());
		return config;
	}
	
	

}
