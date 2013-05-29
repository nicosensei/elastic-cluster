/**
 * 
 */
package fr.nikokode.elastic.cluster.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.nikokode.elastic.cluster.SubstitutionSource;

/**
 * @author ngiraud
 *
 */
public class Host implements SubstitutionSource {
	
	/**
	 * The remote name on which the node will be deployed.
	 */
	private String name;
	
	/**
	 * The user to connect as on the remote name.
	 */
	private String user;
	
	/**
	 * The directory where temporary files will be stored (currently the ES zip file).
	 */
	private FilePath tmpPath;
	
	/**
	 * The base ElasticSearch directory (where the binary package was extracted).
	 */
	private FilePath elasticSearchHome;
	
	/**
	 * The JDK home directory.
	 */
	private FilePath javaHome;
	
	/**
	 * The ElasticSearch nodes
	 */
	private List<Node> nodes;

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
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the tmpPath
	 */
	public FilePath getTmpPath() {
		return tmpPath;
	}

	/**
	 * @param tmpPath the tmpPath to set
	 */
	public void setTmpPath(FilePath tmpPath) {
		this.tmpPath = tmpPath;
	}

	/**
	 * @return the elasticSearchHome
	 */
	public FilePath getElasticSearchHome() {
		return elasticSearchHome;
	}

	/**
	 * @param elasticSearchHome the elasticSearchHome to set
	 */
	public void setElasticSearchHome(FilePath elasticSearchHome) {
		this.elasticSearchHome = elasticSearchHome;
	}

	/**
	 * @return the javaHome
	 */
	public FilePath getJavaHome() {
		return javaHome;
	}

	/**
	 * @param javaHome the javaHome to set
	 */
	public void setJavaHome(FilePath javaHome) {
		this.javaHome = javaHome;
	}

	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@Override
	public Map<String, String> getPropertyMap() {
		HashMap<String, String> props = new HashMap<>();
		String namePrefix = Host.class.getSimpleName().toLowerCase() + ".";
		props.put(namePrefix + "name", this.name);
		props.put(namePrefix + "user", this.user);
		props.put(namePrefix + "elasticSearchHome", this.elasticSearchHome.resolvePath());
		props.put(namePrefix + "javaHome", this.javaHome.resolvePath());
		props.put(namePrefix + "tmpPath", this.tmpPath.resolvePath());
		return props;
	}

}
