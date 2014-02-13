/**
 * 
 */
package com.github.nicosensei.elastic.cluster.beans;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.nicosensei.elastic.cluster.SubstitutionSource;

/**
 * A Spring bean representing an ElasticSearch cluster setup.
 * 
 * @author ngiraud
 *
 */
public class Cluster implements SubstitutionSource {
	
	/**
	 * The cluster name.
	 */
	private String name;
	
	/**
	 * The number of shards in every data node.
	 */
	private int shardCount;
    
	/**
	 * The number of replica for a given data shard.
	 */
	private int replicaCount;
	
	/**
	 * @see http://architects.dzone.com/articles/our-experience-creating-large
	 * When using ElasticSearch, you will see OutOfMemory errors frequently. This error occurs when 
	 * the field cache exceeds the maximum heap size. If you change the setting for 
	 * index.cache.field.type from resident (default) to soft, soft reference 
	 * will be used and the cache area will be preferentially GC, and this problem can be resolved.
	 */
	private String cacheFieldType;
	
	/**
	 * The time to wait for process completion after issuing a shutdown request to a node.
	 */
    private int shutdownWaitInSeconds;
    
    /**
	 * The time to wait after starting a node.
	 */
    private int startupWaitInSeconds;
    
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
	 * The deployment hosts.
	 */
	private List<Host> hosts;
	
	/**
	 * By default discovery uses multicast
	 */
	private boolean multicastEnabled = true;
	
	/**
	 * If {@link #multicastEnabled} is set to false, use this property to define 
	 * a list of master hosts (host:port)
	 */
	private Set<String> unicastMasterHosts = new LinkedHashSet<>();
    
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
	 * @return the shardCount
	 */
	public int getShardCount() {
		return shardCount;
	}

	/**
	 * @param shardCount the shardCount to set
	 */
	public void setShardCount(int shardCount) {
		this.shardCount = shardCount;
	}

	/**
	 * @return the replicaCount
	 */
	public int getReplicaCount() {
		return replicaCount;
	}

	/**
	 * @param replicaCount the replicaCount to set
	 */
	public void setReplicaCount(int replicaCount) {
		this.replicaCount = replicaCount;
	}

	/**
	 * @return the shutdownWaitInSeconds
	 */
	public int getShutdownWaitInSeconds() {
		return shutdownWaitInSeconds;
	}

	/**
	 * @param shutdownWaitInSeconds the shutdownWaitInSeconds to set
	 */
	public void setShutdownWaitInSeconds(int shutdownWaitInSeconds) {
		this.shutdownWaitInSeconds = shutdownWaitInSeconds;
	}

	/**
	 * @return the startupWaitInSeconds
	 */
	public final int getStartupWaitInSeconds() {
		return startupWaitInSeconds;
	}

	/**
	 * @param startupWaitInSeconds the startupWaitInSeconds to set
	 */
	public final void setStartupWaitInSeconds(int startupWaitInSeconds) {
		this.startupWaitInSeconds = startupWaitInSeconds;
	}

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
	public void setEsVersionName(String esVersionName) {
		this.esVersionName = esVersionName;
	}

	/**
	 * @return the hosts
	 */
	public List<Host> getHosts() {
		return hosts;
	}

	/**
	 * @param hosts the hosts to set
	 */
	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}

	public String getCacheFieldType() {
		return cacheFieldType;
	}

	public void setCacheFieldType(String cacheFieldType) {
		this.cacheFieldType = cacheFieldType;
	}

	public boolean isMulticastEnabled() {
		return multicastEnabled;
	}

	public void setMulticastEnabled(boolean multicastEnabled) {
		this.multicastEnabled = multicastEnabled;
	}

	public Set<String> getUnicastMasterHosts() {
		return unicastMasterHosts;
	}

	public void setUnicastMasterHosts(Set<String> unicastMasterHosts) {
		this.unicastMasterHosts = unicastMasterHosts;
	}

	@Override
	public Map<String, String> getPropertyMap() {
		HashMap<String, String> props = new HashMap<>();
		String namePrefix = Cluster.class.getSimpleName().toLowerCase() + ".";
		props.put(namePrefix + "name", this.name);
		props.put(namePrefix + "shardCount", Integer.toString(this.shardCount));
		props.put(namePrefix + "replicaCount", Integer.toString(this.replicaCount));
		props.put(namePrefix + "cacheFieldType", this.cacheFieldType);
		props.put(namePrefix + "shutdownWaitInSeconds", Integer.toString(this.shutdownWaitInSeconds));
		props.put(namePrefix + "startupWaitInSeconds", Integer.toString(this.startupWaitInSeconds));
		props.put(namePrefix + "esZipFile", this.esZipFile.resolvePath());
		props.put(namePrefix + "localOutputPath", this.localOutputPath.resolvePath());		
		props.put(namePrefix + "esVersionName", this.esVersionName);
		props.put(namePrefix + "multicastEnabled", Boolean.toString(this.multicastEnabled));
		String masterHosts = "";
		if (!this.multicastEnabled && !this.unicastMasterHosts.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			for (String mh : this.unicastMasterHosts) {
				sb.append("\"" + mh + "\", ");
			}
			masterHosts = masterHosts.substring(0, masterHosts.lastIndexOf(","));
		}
		props.put(namePrefix + "unicastMasterHosts", "[" + masterHosts + "]");
		
		return props;
	}

}
