package graphUtils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import etlFlowGraph.graph.ETLFlowGraph;

public class PathTrace {
	private int id = -1;
	private boolean finalPath = false;
	private ArrayList<TracePart> trace = new ArrayList<TracePart>();
	private String signature;
	private static final AtomicInteger count = new AtomicInteger(0); 
	
	
	public PathTrace() {
		id = count.incrementAndGet(); 
	}

	public String toString() {
		String traceString = "";
		for (TracePart tp : this.trace) {
			traceString += "." + tp.toString();
		}
		return traceString;
	}
	
	public String toStringOpNames(ETLFlowGraph efg) {
		String traceString = "";
		for (TracePart tp : this.trace) {
			traceString += "." + tp.toStringOpNames(efg);
		}
		return traceString;
	}
	
	public PathTrace clone(){
		PathTrace pt2 = new PathTrace();
		for (TracePart tp:this.trace){
			TracePart tp2 = tp.clone();
			pt2.trace.add(tp2);
		}
		pt2.setFinalPath(this.finalPath);
		return pt2;
		
	}

	/**
	 * @return the finalPath
	 */
	public boolean isFinalPath() {
		return finalPath;
	}

	/**
	 * @param finalPath the finalPath to set
	 */
	public void setFinalPath(boolean finalPath) {
		this.finalPath = finalPath;
	}

	/**
	 * @return the trace
	 */
	public ArrayList<TracePart> getTrace() {
		return trace;
	}

	/**
	 * @param trace the trace to set
	 */
	public void setTrace(ArrayList<TracePart> trace) {
		this.trace = trace;
	}
	
	public void addPartToTrace(TracePart tp){
		trace.add(tp);		
	}
	
	public void addPartsToTrace(ArrayList<TracePart> tps){
		if (!tps.isEmpty()){
		trace.addAll(tps);
		}
	}
	
	public void updateSignature(){
		signature = this.toString();
	}
	
	public boolean isIncludedLeftIn(PathTrace pt){
		// if the path trace signature has not been initialized
		if (this.signature==null){
			this.updateSignature();
			// if the path trace is empty
			if (this.signature==""){
				return false;
			}
		}
		if (pt.signature.startsWith(this.signature)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean contains(String pathPart){
		// if the path trace signature has not been initialized
				if (this.signature==null){
					this.updateSignature();
					// if the path trace is empty
					if (this.signature==""){
						return false;
					}
				}
				if (this.signature.contains(pathPart)){
					return true;
				}else{
					return false;
				}
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
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
	
	
}
