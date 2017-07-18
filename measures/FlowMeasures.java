package measures;

import java.util.ArrayList;

import measureDictionary.MeasureName;
import utilities.ObjectPair;



/**
 * Structure to store pairs of measures and values for a specific etl flow
 * 
 * @author btheo
 *
 */
public class FlowMeasures {
	private int flowId;
	private ArrayList<MeasureAndValue> measureValues = new ArrayList<MeasureAndValue>();
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
	
	/**
	 * @return the measureValues
	 */
	public ArrayList<MeasureAndValue> getMeasureValues() {
		return measureValues;
	}
	/**
	 * @param measureValues the measureValues to set
	 */
	public void setMeasureValues(ArrayList<MeasureAndValue> measureValues) {
		this.measureValues = measureValues;
	}
	public void addMeasureValue(MeasureAndValue mv){
		this.measureValues.add(mv);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String returnStr = "[Flow id: "+this.flowId+"[Measures and Values:";
		for (MeasureAndValue mnv:this.measureValues){
			returnStr+=mnv.toString();
		}
		returnStr+="]]";
		return returnStr;
	}
	
	
	
}
