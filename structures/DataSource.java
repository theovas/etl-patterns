package structures;

import etlFlowGraph.schema.Schema;
import operationDictionary.ETLOperationType;
import operationDictionary.OperationTypeName;

public class DataSource {

	private String filepath;
	private OperationTypeName opTypeName;
	private String storage_type = "LocalFileSystem";
	private Schema inputSchema;
	
	public DataSource(String filepath, OperationTypeName opTypeName, Schema inputSchema) {
		this.filepath = filepath;
		this.opTypeName = opTypeName;
		this.inputSchema = inputSchema;
	}


	/**
	 * @return the filepath
	 */
	public String getFilepath() {
		return filepath;
	}


	/**
	 * @param filepath the filepath to set
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}


	/**
	 * @return the opTypeName
	 */
	public OperationTypeName getOpTypeName() {
		return opTypeName;
	}


	/**
	 * @param opTypeName the opTypeName to set
	 */
	public void setOpTypeName(OperationTypeName opTypeName) {
		this.opTypeName = opTypeName;
	}


	/**
	 * @return the storage_type
	 */
	public String getStorage_type() {
		return storage_type;
	}


	/**
	 * @param storage_type the storage_type to set
	 */
	public void setStorage_type(String storage_type) {
		this.storage_type = storage_type;
	}


	/**
	 * @return the inputSchema
	 */
	public Schema getInputSchema() {
		return inputSchema;
	}


	/**
	 * @param inputSchema the inputSchema to set
	 */
	public void setInputSchema(Schema inputSchema) {
		this.inputSchema = inputSchema;
	}

	
	
	
}
