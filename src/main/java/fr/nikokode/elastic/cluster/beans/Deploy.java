/**
 * 
 */
package fr.nikokode.elastic.cluster.beans;

import java.util.HashMap;
import java.util.Map;

import fr.nikokode.elastic.cluster.SubstitutionSource;

/**
 * @author ngiraud
 *
 */
public class Deploy implements SubstitutionSource {
	
	/**
	 * Path to the local output folder
	 */
	private FilePath localOutputPath;
	
	/**
	 * Path to the ElasticSearch distribution package (zip file expected).
	 */
	private FilePath esZipFile;
	
	/**
	 * The name of the root folder in the ES zip file.
	 */
	private String esVersionName;
	
	/**
	 * @return the localOutputPath
	 */
	public FilePath getLocalOutputPath() {
		return localOutputPath;
	}

	/**
	 * @param localOutputPath the localOutputPath to set
	 */
	public void setLocalOutputPath(FilePath localOutputPath) {
		this.localOutputPath = localOutputPath;
	}

	/**
	 * @return the esZipFile
	 */
	public FilePath getEsZipFile() {
		return esZipFile;
	}

	/**
	 * @param esZipFile the esZipFile to set
	 */
	public void setEsZipFile(FilePath esZipFile) {
		this.esZipFile = esZipFile;
	}

	/**
	 * @return the esVersionName
	 */
	public String getEsVersionName() {
		return esVersionName;
	}

	/**
	 * @param esVersionName the esVersionName to set
	 */
	public void setEsVersionName(String esVersionString) {
		this.esVersionName = esVersionString;
	}

	@Override
	public Map<String, String> getPropertyMap() {
		HashMap<String, String> props = new HashMap<>();
		String namePrefix = Deploy.class.getSimpleName().toLowerCase() + ".";
		props.put(namePrefix + "esZipFile", this.esZipFile.resolvePath());
		props.put(namePrefix + "localOutputPath", this.localOutputPath.resolvePath());		
		props.put(namePrefix + "esVersionName", this.esVersionName);
		return props;
	}

}
