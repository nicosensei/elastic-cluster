/**
 * 
 */
package fr.nikokode.elastic.cluster;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * @author ngiraud
 *
 */
public class VariableSubstitution {
	
	private static final Logger LOGGER = Logger.getLogger(VariableSubstitution.class);
	
	private static final String VAR_REGEX = "\\$\\{([\\w\\.]+)\\}";
	
	private Map<String, String> propertyMap = new TreeMap<String, String>();
	
	public VariableSubstitution() {
		
	}

	public void putProperties(Map<String, String> propertyMap) {
		this.propertyMap.putAll(propertyMap);
	}
	
	public void removeProperties(Map<String, String> propertyMap) {
		for (String key : propertyMap.keySet()) {
			this.propertyMap.remove(key);
		}
	}
	
	public void putProperty(String key, String value) {
		this.propertyMap.put(key, value);
	}
	
	public void removeProperty(String key) {
		this.propertyMap.remove(key);
	}

	/**
	 * Substitutes a variable declared in the form of "${varName}" in the given text.
	 * @param text the text to substitute.
	 * @return a copy with all variable expressions substituted.
	 */
	public String substitute(final String text) {
		Pattern p = Pattern.compile(VAR_REGEX);
		Matcher m = p.matcher(text);
		StringBuffer result = new StringBuffer();
		
		while (m.find()) {
			String propName = m.group(1);
			String value = propertyMap.get(propName);
			if (value == null || value.isEmpty()) {
				LOGGER.warn("Found null or empty value for variable '" + propName + "'");				
			} else {
				m.appendReplacement(result, value);
			}			
		}
		m.appendTail(result);
		return result.toString();
	}

}
