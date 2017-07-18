package structures;

import etlFlowGraph.attribute.Attribute;

public class IdAttrPair{
	/**
	 * The nodeid of the operation where the attribute is found
	 */
	private int id;
	/**
	 * The attribute object
	 */
	private Attribute attr;
	
	public IdAttrPair(int id, Attribute attr) {
		this.id = id;
		this.attr = attr;
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
	 * @return the attr
	 */
	public Attribute getAttr() {
		return attr;
	}
	/**
	 * @param attr the attr to set
	 */
	public void setAttr(Attribute attr) {
		this.attr = attr;
	}
	
}