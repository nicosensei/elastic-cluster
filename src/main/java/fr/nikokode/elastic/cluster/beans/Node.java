/**
 * 
 */
package fr.nikokode.elastic.cluster.beans;

import java.util.HashMap;
import java.util.Map;

import fr.nikokode.elastic.cluster.SubstitutionSource;

/**
 * A Spring bean representing an ElasticSearch node setup.
 * 
 * @author ngiraud
 *
 */
public class Node implements SubstitutionSource {
	
	/**
	 * The node id, used to distinguish nodes (that can possibly reside on same hosts).
	 */
	private String id;
	
	/**
	 * The node name.
	 */
	private String name;
	
	/**
	 * The directory where cluster management scripts reside.
	 */
	private FilePath scriptsPath;
	
	/**
	 * The JVM heap size string. Provide a valid string (e.g. "512m", "2g")
	 */
	private String heapSize;
	
	/**
	 * Whether the JVM should lock all the heap size upon startup (recommended true).
	 */
	private boolean lockAllMemory;
	
	/**
	 * Whether the node can be elected as master.
	 */
	private boolean master;
	
	/**
	 * Whether the node stores data.
	 */
	private boolean data;
	
	/**
	 * The node's HTTP port (on which REST requests are sent).
	 */
	private int httpPort;
	
	/**
	 * The node's TCP port (transport port).
	 */
	private int tcpPort;
	
	/**
	 * Path to the folder storing configuration files.
	 */
	private FilePath confPath;
	
	/**
	 * Path to the folder storing data files.
	 */
	private FilePath dataPath;
	
	/**
	 * Path to the folder storing work files.
	 */
	private FilePath workPath;
	
	/**
	 * Path to the folder storing log files.
	 */
	private FilePath logsPath;
	
	/**
	 * Path to the folder storing plugins.
	 */
	private FilePath pluginsPath;
	
	/**
	 * If set to true, will generate head plugin installation command
	 */
	private boolean installHeadPlugin;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

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
	 * @return the scriptsPath
	 */
	public FilePath getScriptsPath() {
		return scriptsPath;
	}

	/**
	 * @param scriptsPath the scriptsPath to set
	 */
	public void setScriptsPath(FilePath scriptsPath) {
		this.scriptsPath = scriptsPath;
	}

	/**
	 * @return the heapSize
	 */
	public String getHeapSize() {
		return heapSize;
	}

	/**
	 * @param heapSize the heapSize to set
	 */
	public void setHeapSize(String heapSize) {
		this.heapSize = heapSize;
	}

	/**
	 * @return the lockAllMemory
	 */
	public boolean isLockAllMemory() {
		return lockAllMemory;
	}

	/**
	 * @param lockAllMemory the lockAllMemory to set
	 */
	public void setLockAllMemory(boolean lockAllMemory) {
		this.lockAllMemory = lockAllMemory;
	}

	/**
	 * @return the master
	 */
	public boolean isMaster() {
		return master;
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(boolean master) {
		this.master = master;
	}

	/**
	 * @return the data
	 */
	public boolean isData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(boolean data) {
		this.data = data;
	}

	/**
	 * @return the httpPort
	 */
	public int getHttpPort() {
		return httpPort;
	}

	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the tcpPort
	 */
	public int getTcpPort() {
		return tcpPort;
	}

	/**
	 * @param tcpPort the tcpPort to set
	 */
	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	/**
	 * @return the confPath
	 */
	public FilePath getConfPath() {
		return confPath;
	}

	/**
	 * @param confPath the confPath to set
	 */
	public void setConfPath(FilePath confPath) {
		this.confPath = confPath;
	}

	/**
	 * @return the dataPath
	 */
	public FilePath getDataPath() {
		return dataPath;
	}

	/**
	 * @param dataPath the dataPath to set
	 */
	public void setDataPath(FilePath dataPath) {
		this.dataPath = dataPath;
	}

	/**
	 * @return the workPath
	 */
	public FilePath getWorkPath() {
		return workPath;
	}

	/**
	 * @param workPath the workPath to set
	 */
	public void setWorkPath(FilePath workPath) {
		this.workPath = workPath;
	}

	/**
	 * @return the logsPath
	 */
	public FilePath getLogsPath() {
		return logsPath;
	}

	/**
	 * @param logsPath the logsPath to set
	 */
	public void setLogsPath(FilePath logsPath) {
		this.logsPath = logsPath;
	}

	/**
	 * @return the pluginsPath
	 */
	public FilePath getPluginsPath() {
		return pluginsPath;
	}

	/**
	 * @param pluginsPath the pluginsPath to set
	 */
	public void setPluginsPath(FilePath pluginsPath) {
		this.pluginsPath = pluginsPath;
	}

	/**
	 * @return the installHeadPlugin
	 */
	public boolean isInstallHeadPlugin() {
		return installHeadPlugin;
	}

	/**
	 * @param installHeadPlugin the installHeadPlugin to set
	 */
	public void setInstallHeadPlugin(boolean installHeadPlugin) {
		this.installHeadPlugin = installHeadPlugin;
	}

	@Override
	public Map<String, String> getPropertyMap() {
		HashMap<String, String> props = new HashMap<>();
		String namePrefix = Node.class.getSimpleName().toLowerCase() + ".";
		props.put(namePrefix + "id", this.id);
		props.put(namePrefix + "name", this.name);
		props.put(namePrefix + "scriptsPath", this.scriptsPath.resolvePath());
		props.put(namePrefix + "heapSize", this.heapSize);
		props.put(namePrefix + "lockAllMemory", Boolean.toString(this.lockAllMemory));
		props.put(namePrefix + "master", Boolean.toString(this.master));
		props.put(namePrefix + "data", Boolean.toString(this.data));
		props.put(namePrefix + "httpPort", Integer.toString(this.httpPort));
		props.put(namePrefix + "tcpPort", Integer.toString(this.tcpPort));
		props.put(namePrefix + "confPath", this.confPath.resolvePath());
		props.put(namePrefix + "dataPath", this.dataPath.resolvePath());
		props.put(namePrefix + "workPath", this.workPath.resolvePath());
		props.put(namePrefix + "logsPath", this.logsPath.resolvePath());
		props.put(namePrefix + "pluginsPath", this.pluginsPath.resolvePath());
		props.put(namePrefix + "installHeadPlugin", Boolean.toString(this.installHeadPlugin));
		return props;
	}
	
}
