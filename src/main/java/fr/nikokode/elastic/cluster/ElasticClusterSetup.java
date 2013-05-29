/**
 * 
 */
package fr.nikokode.elastic.cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import fr.nikokode.commons.utils.FileUtils;
import fr.nikokode.elastic.cluster.beans.Cluster;
import fr.nikokode.elastic.cluster.beans.Host;
import fr.nikokode.elastic.cluster.beans.Node;

/**
 * @author ngiraud
 *
 */
public class ElasticClusterSetup {

	private static final Logger LOGGER = Logger.getLogger(ElasticClusterSetup.class);
	
	private static final String RT_NODE_FOLDER = "runtime.nodeFolder";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.out.println("Usage: " + ElasticClusterSetup.class.getCanonicalName()
					+ "\n\t- <cluster deployment setup file>");
			System.exit(0);
		}

		Path currentPath = Paths.get("");
		LOGGER.info("Current relative path is: " + currentPath.toAbsolutePath().toString());

		File setupFile = currentPath.resolve(args[0]).toFile();
		if (!setupFile.exists() 
				|| ! setupFile.isFile()
				|| ! setupFile.canRead()) {
			throw new IOException(
					"Cluster setup file '" + setupFile.getAbsolutePath() + " is not a readable file!");
		}

		FileSystemXmlApplicationContext appCtx = 
				new FileSystemXmlApplicationContext("file://" + setupFile.getAbsolutePath());
		LOGGER.info("Loaded cluster setup beans from " + setupFile.getAbsolutePath());
		
		try {
			// Initialize variable substitution
			VariableSubstitution varSub = new VariableSubstitution();
			
			Cluster cluster = appCtx.getBean(Cluster.class);
			varSub.putProperties(cluster.getPropertyMap());
			File outDir = new File(cluster.getLocalOutputPath().resolvePath());
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Properties: " + varSub.toString());
			}

			// Create cluster output folder
			File clusterFolder = new File(outDir, cluster.getName());
			if (clusterFolder.exists()) {
				LOGGER.info("Found existing output in " + clusterFolder.getAbsolutePath()
						+ ", which will be deleted now.");
				FileUtils.recursiveDelete(clusterFolder);
			}
			if (! clusterFolder.mkdirs()) {
				throw new IOException("Failed to create directory hierarchy " 
						+ clusterFolder.getAbsolutePath());
			}
			LOGGER.info("Cluster files will be stored in " + clusterFolder.getAbsolutePath());

			// Initialize main cluster management script
			File clusterMgmtScriptFile = new File(
					clusterFolder, "cluster_" + cluster.getName() + ".sh");
			BufferedWriter clusterMgmtOut = new BufferedWriter(new FileWriter(clusterMgmtScriptFile));
			clusterMgmtOut.write("#!/bin/bash");
			clusterMgmtOut.newLine();
			clusterMgmtOut.write("# Usage: $0 {start|stop|restart|status|plugins|destroy}");
			clusterMgmtOut.newLine();

			// Initialize main cluster deployment script
			File clusterDeployScriptFile = 
					new File(clusterFolder, "deploy_cluster_" + cluster.getName() + ".sh");
			BufferedWriter clusterDeployOut = new BufferedWriter(new FileWriter(clusterDeployScriptFile));
			clusterDeployOut.write("#!/bin/bash");
			clusterDeployOut.newLine();

			try {
				// Iterate on hosts
				for (Host host : cluster.getHosts()) {
					String hostName = host.getName(); 
					LOGGER.info("Found deployment host " + host.getUser() + "@" + hostName);
					varSub.putProperties(host.getPropertyMap());
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Properties: " + varSub.toString());
					}
					
					// Create host folder
					File hostFolder = new File(clusterFolder, hostName);
					if (! hostFolder.mkdirs()) {
						throw new IOException("Failed to create directory hierarchy " 
								+ hostFolder.getAbsolutePath());
					}
					
					// Create host setup script
					File setupHostScript = new File(hostFolder, "host_setup_" + hostName + ".sh");
					generateFromTemplate(Template.setupHostScript, setupHostScript, varSub);
					setupHostScript.setExecutable(true, true);
					
					// Add host setup to cluster deployment script
					clusterDeployOut.write(setupHostScript.getAbsolutePath());
					clusterDeployOut.newLine();
					
					// Iterate on nodes
					for (Node node : host.getNodes()) {
						LOGGER.info("Found node definition '" + node.getId() + "' '" + node.getName());
						
						// Update variable substitution
						Map<String, String> nodeProps = node.getPropertyMap();
						varSub.putProperties(nodeProps);

						String nodeId = node.getId();

						// Create node folder
						File nodeFolder = new File(hostFolder, nodeId);
						if (! nodeFolder.mkdirs()) {
							throw new IOException("Failed to create directory hierarchy " 
									+ nodeFolder.getAbsolutePath());
						}

						// Add property for node folder path
						varSub.putProperty(RT_NODE_FOLDER, nodeFolder.getAbsolutePath());
						
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Properties: " + varSub.toString());
						}

						// Create ElasticSearch config file
						generateFromTemplate(
								Template.nodeConfig, 
								new File(nodeFolder, "elasticsearch.yml"), 
								varSub);

						// Create ElasticSearch logging config file
						generateFromTemplate(
								Template.nodeLogConfig, 
								new File(nodeFolder, "logging.yml"), 
								varSub);

						// Create node management script
						File nodeScript = new File(nodeFolder, "node_" + nodeId + ".sh"); 
						generateFromTemplate(Template.nodeMgmtScript, nodeScript, varSub);
						nodeScript.setExecutable(true, true);
						
						// Append to cluster management script
						String mgmtCmd = "ssh ${host.user}@${host.name} \"${node.scriptsPath}/"
								+ nodeScript.getName() + " $1\"";
						clusterMgmtOut.write(varSub.substitute(mgmtCmd));
						clusterMgmtOut.newLine();

						// Create node deployment script
						File nodeDeployScript = new File(nodeFolder, "deploy_node_" + nodeId + ".sh");;
						generateFromTemplate(Template.nodeDeployScript, nodeDeployScript, varSub);
						nodeDeployScript.setExecutable(true, true);
						
						// Append to cluster deployment script
						clusterDeployOut.write(nodeDeployScript.getAbsolutePath());
						clusterDeployOut.newLine();
					}
				}
			} finally {				
				clusterDeployOut.close();
				clusterDeployScriptFile.setExecutable(true, true);
				LOGGER.info("Generated " + clusterDeployScriptFile.getAbsolutePath());
				
				clusterMgmtScriptFile.setExecutable(true, true);
				clusterMgmtOut.close();
				LOGGER.info("Generated " + clusterMgmtScriptFile.getAbsolutePath());
			}

		} finally {
			appCtx.close();
		}

	}

	private static void generateFromTemplate(
			Template template,
			File outputFile,
			VariableSubstitution varSub) throws IOException {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			BufferedReader br = new BufferedReader(new InputStreamReader( 
					ElasticClusterSetup.class.getClassLoader().getResourceAsStream(
							template.getRelativePath())));
			try {
				String line = null;
				while ((line = br.readLine()) != null) {
					bw.write(varSub.substitute(line));
					bw.newLine();
				}
			} finally {
				br.close();
				bw.close();
			}

			LOGGER.info("Generated " + outputFile.getAbsolutePath());
		} catch (IOException ioe) {
			throw new IOException("Failed to generate " + outputFile.getAbsolutePath()
					+ " from " + template.getRelativePath(), ioe);
		}
	}

}
