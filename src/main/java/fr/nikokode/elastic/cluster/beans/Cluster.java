/**
 * 
 */
package fr.nikokode.elastic.cluster.beans;

import java.util.Map;

/**
 * A Spring bean representing an ElasticSearch cluster setup.
 * 
 * @author ngiraud
 *
 */
public class Cluster {
	
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
	 * The time to wait for process completion after issuing a shutdown request to a node.
	 */
    private int shutdownWaitInSeconds;
    
    /**
     * The cluster nodes, the key being the startup/shutdown order.
     */
    private Map<Integer, Node> nodes;

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
	 * @return the nodes
	 */
	public Map<Integer, Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(Map<Integer, Node> nodes) {
		this.nodes = nodes;
	}
    

}
