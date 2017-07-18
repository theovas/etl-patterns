package measures;

import measureDictionary.MeasureName;

public class MeasureAndValue {
private MeasureName mName;
private double value;



public MeasureAndValue(MeasureName mName, double value) {
	super();
	this.mName = mName;
	this.value = value;
}
/**
 * @return the mName
 */
public MeasureName getmName() {
	return mName;
}
/**
 * @param mName the mName to set
 */
public void setmName(MeasureName mName) {
	this.mName = mName;
}
/**
 * @return the value
 */
public double getValue() {
	return value;
}
/**
 * @param value the value to set
 */
public void setValue(double value) {
	this.value = value;
}
/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	// TODO Auto-generated method stub
	return "[Measure[Name: "+this.mName+", Value: "+this.value+"]]";
}


}
