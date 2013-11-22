/**
 * 
 */
package com.github.nicosensei.elastic.cluster;

import java.util.Map;

/**
 * @author ngiraud
 *
 */
public interface SubstitutionSource {
	
	/**
	 * @return a map of property names pointing to values, to be used for text substitution 
	 * in template files.
	 */
	Map<String, String> getPropertyMap();

}
