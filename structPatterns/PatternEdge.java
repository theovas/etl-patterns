package structPatterns;

import org.jgrapht.graph.DefaultEdge;

public class PatternEdge extends DefaultEdge {

	
	public Object getTarget(){
		return super.getTarget();
	}
	
	public Object getSource(){
		return super.getSource();
	}
	
	public boolean equals(Object o){
		return ((PatternEdge)o).getSource().equals(getSource()) && ((PatternEdge)o).getTarget().equals(getTarget());
	}
	
	
}