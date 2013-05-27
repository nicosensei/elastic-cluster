/**
 * 
 */
package fr.nikokode.elastic.cluster.beans;

/**
 * A Spring bean representing an ElasticSearch node setup.
 * 
 * @author ngiraud
 *
 */
public class Node {
	
	/**
	 * The remote host on which the node will be deployed.
	 */
	private String host;
	
	/**
	 * The user to connect as on the remote host.
	 */
	private String user;
	
	/**
	 * The base ElasticSearch directory (where the binary package was extracted).
	 */
	private FilePath elasticSearchHome;
	
	/**
	 * The JDK home directory.
	 */
	private FilePath javaHome;
	
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
	 * Path to the process ID file.
	 */
	private FilePath pidFile;
	
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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
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
	 * @return the pidFile
	 */
	public FilePath getPidFile() {
		return pidFile;
	}

	/**
	 * @param pidFile the pidFile to set
	 */
	public void setPidFile(FilePath pidFile) {
		this.pidFile = pidFile;
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

}
