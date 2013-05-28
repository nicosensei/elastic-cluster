/**
 * 
 */
package fr.nikokode.elastic.cluster;

/**
 * @author ngiraud
 *
 */
public enum Template {
	
	nodeConfig("templates/config/elasticsearch.yml"),
	nodeLogConfig("templates/config/logging.yml"),
	nodeMgmtScript("templates/bash/node.sh"),
	nodeDeployScript("templates/bash/deployNode.sh");
	
	private final String relativePath;

	private Template(final String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 * @return the relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}

}
