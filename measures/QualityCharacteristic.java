package measures;

import java.util.ArrayList;
import java.util.Hashtable;

import measureDictionary.QualityCharacteristicName;
import measures.Measures;

/**
 * Class implementing an ETL quality characteristic.  
 * @author vasileios
 *
 */
public class QualityCharacteristic {
	//TODO: add a field to show hierarchy
	public static Hashtable<String,QualityCharacteristic> qualityCharacteristics = new Hashtable<String, QualityCharacteristic>(); 
	static{
		qualityCharacteristics.put("Usability", new QualityCharacteristic("Usability", 0));
		qualityCharacteristics.put("Understandability", new QualityCharacteristic("Understandability", 1));
		qualityCharacteristics.put("Manageability", new QualityCharacteristic("Manageability", 2));
		qualityCharacteristics.put("Maintainability", new QualityCharacteristic("Maintainability", 3));
		qualityCharacteristics.put("Testability", new QualityCharacteristic("Testability", 4));
		qualityCharacteristics.put("Adaptability", new QualityCharacteristic("Adaptability", 5));
		qualityCharacteristics.put("Scalability", new QualityCharacteristic("Scalability", 6));
		qualityCharacteristics.put("Flexibility", new QualityCharacteristic("Flexibility", 7));
		qualityCharacteristics.put("Reusability", new QualityCharacteristic("Reusability", 8));
		qualityCharacteristics.put("DataQuality", new QualityCharacteristic("DataQuality", 9));
		qualityCharacteristics.put("DataConsistency", new QualityCharacteristic("DataConsistency", 10));
		qualityCharacteristics.put("DataAccuracy", new QualityCharacteristic("DataAccuracy", 11));
		qualityCharacteristics.put("DataFreshness", new QualityCharacteristic("DataFreshness", 12));
		qualityCharacteristics.put("DataCompleteness", new QualityCharacteristic("DataCompleteness", 13));
		qualityCharacteristics.put("DataInterpretability", new QualityCharacteristic("DataInterpretability", 14));
		qualityCharacteristics.put("Auditability", new QualityCharacteristic("Auditability", 15));
		qualityCharacteristics.put("Traceability", new QualityCharacteristic("Traceability", 16));
		qualityCharacteristics.put("UpstreamOverheadLimitation", new QualityCharacteristic("UpstreamOverheadLimitation", 17));
		qualityCharacteristics.put("Security", new QualityCharacteristic("Security", 18));
		qualityCharacteristics.put("Confidentiality", new QualityCharacteristic("Confidentiality", 19));
		qualityCharacteristics.put("Integrity", new QualityCharacteristic("Integrity", 20));
		qualityCharacteristics.put("Reliability", new QualityCharacteristic("Reliability", 21));
		qualityCharacteristics.put("Availability", new QualityCharacteristic("Availability", 22));
		qualityCharacteristics.put("FaultTolerance", new QualityCharacteristic("FaultTolerance", 23));
		qualityCharacteristics.put("Robustness", new QualityCharacteristic("Robustness", 24));
		qualityCharacteristics.put("Recoverability", new QualityCharacteristic("Recoverability", 25));
		qualityCharacteristics.put("Performance", new QualityCharacteristic("Performance", 26));
		qualityCharacteristics.put("TimeEfficiency", new QualityCharacteristic("TimeEfficiency", 27));
		qualityCharacteristics.put("ResourceUtilization", new QualityCharacteristic("ResourceUtilization", 28));
		qualityCharacteristics.put("Capacity", new QualityCharacteristic("Capacity", 29));
		qualityCharacteristics.put("Modes", new QualityCharacteristic("Modes", 30));
		
		// traverse the measures enum to find which measures influence which characteristics and add them accordingly
		for (Measure measure:Measures.values()){
			QualityCharacteristicName qcn = measure.getQCharacteristicName();
			if (qcn!=null){
				qualityCharacteristics.get(qcn.name()).addMeasure(measure);
			}
		}
	}
	
	private String name;
	private int id;
	private ArrayList<Measure> measures = new ArrayList<Measure>();
	
	
	public QualityCharacteristic(String name, int id) {
		this.name = name;
		this.id = id;
	}
	



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}




	/**
	 * @return the measures
	 */
	public ArrayList<Measure> getMeasures() {
		return measures;
	}
	
	public void addMeasure(Measure measure) {
		measures.add(measure);
	}




	/**
	 * @param measures the measures to set
	 */
	public void setMeasures(ArrayList<Measure> measures) {
		this.measures = measures;
	}




	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String measuresStr = "";
        for (Measure m:measures){
        	measuresStr += "[measure="+ m.getName()+"]"; 
        }
		return "QualityCharacteristic [name=" + name + ", id=" + id +", measures ["+measuresStr+"]]";
	}


	
	
	

}
