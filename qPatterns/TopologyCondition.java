package qPatterns;

import java.util.ArrayList;

import structures.ApplPoint;
import utilities.ObjectPair;

/**
 * 
 * Conditions that have to stand for a pattern to be applied at a specified
 * point of the ETL flow.
 * 
 * To be extended with xml-like expressions in future implementation...
 * 
 * @author vasileios
 * 
 */
public abstract class TopologyCondition {

	/**
	 * 
	 * This method checks if the condition stands for a specific application
	 * point and returns an object pair, the left part being a boolean - true if
	 * it does and false if it does not- and the second part being specific
	 * information for each condition, or empty if not applicable
	 * 
	 * @param ap
	 * @return
	 */
	public abstract ObjectPair<Boolean, String> check(ApplPoint ap);

	/**
	 * 
	 * This method checks if the condition stands for a specific application
	 * point and returns an object pair, the left part being a boolean - true if
	 * it does and false if it does not- and the second part being specific
	 * information for each condition, or empty if not applicable. It also takes
	 * as an argument a quantifier to indicate the quantifiers "there exists at
	 * least one" (e.g., application point that is referenced in through the 
	 * expression), if set to true and "for all", if set to false.
	 * 
	 * @param ap
	 * @return
	 */
	public abstract ObjectPair<Boolean, String> check(ApplPoint ap,
			Boolean quantifier);

}
