/**
 * 
 */
package com.github.nicosensei.elastic.cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.github.nicosensei.commons.utils.FileUtils;
import com.github.nicosensei.elastic.cluster.beans.Cluster;
import com.github.nicosensei.elastic.cluster.beans.Host;
import com.github.nicosensei.elastic.cluster.beans.Node;
import com.github.nicosensei.elastic.cluster.beans.Plugin;

/**
 * @author ngiraud
 *
 */
public class ElasticClusterSetup {

	private static final Logger LOGGER = Logger.getLogger(ElasticClusterSetup.class);

	private static final String RT_NODE_FOLDER = "runtime.nodeFolder";
	
	private static final String RT_PLUGINS_CMD = "runtime.pluginCommands";

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

			// Initialize a map for cluster commands
			Map<Integer, String> clusterCmds = new TreeMap<>();

			// Initialize a list for cluster deployment commands
			ArrayList<String> clusterDeployCmds = new ArrayList<>();

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

				// Add host setup deployment command
				clusterDeployCmds.add(setupHostScript.getAbsolutePath());

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
					
					// Generate plugin commands
					String pluginCommands = "echo No plugin to install.";
					Set<Plugin> plugs = node.getPlugins();
					if (! plugs.isEmpty()) {
						StringWriter pluginSw = new StringWriter();
						PrintWriter pw = new PrintWriter(pluginSw);
						for (Plugin plug : node.getPlugins()) {
							String name = plug.getName();
							pw.println(
									"if [ -d ${node.pluginsPath}/" + name + " ];");
							pw.println(
									"then rm -rf ${node.pluginsPath}/" + name);
							pw.println(
									"${host.elasticSearchHome}/bin/plugin -remove " 
											+ name);
							pw.println("fi");
							pw.println(
									"${host.elasticSearchHome}/bin/plugin -install " 
											+ plug.getId());
							pw.println("mv ${host.elasticSearchHome}/plugins/"
									+ name + " ${node.pluginsPath}");
						}
						pw.close();
						pluginCommands = varSub.substitute(pluginSw.toString());
					}
					varSub.putProperty(RT_PLUGINS_CMD, pluginCommands);

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
					clusterCmds.put(
							node.getStartupOrder(), 
							varSub.substitute("ssh ${host.user}@${host.name} \"${node.scriptsPath}/"
									+ nodeScript.getName() + " \\$1\""));

					// Create node deployment script
					File nodeDeployScript = new File(nodeFolder, "deploy_node_" + nodeId + ".sh");;
					generateFromTemplate(Template.nodeDeployScript, nodeDeployScript, varSub);
					nodeDeployScript.setExecutable(true, true);

					// Append to cluster deployment script
					clusterDeployCmds.add(nodeDeployScript.getAbsolutePath());
				}
			}

			// Write main cluster management script
			Integer[] nodeOrder = clusterCmds.keySet().toArray(new Integer[clusterCmds.size()]);
			StringBuffer sb = new StringBuffer();
			for (int o = 0; o < nodeOrder.length; o++) {
				sb.append(clusterCmds.get(nodeOrder[o]) + "\n");
			}
			varSub.putProperty("runtime.clusterOrderAscCommands", sb.toString());
			
			sb = new StringBuffer();
			for (int o = nodeOrder.length - 1; o >= 0 ; o--) {
				sb.append(clusterCmds.get(nodeOrder[o]) + "\n");
			}
			varSub.putProperty("runtime.clusterOrderDescCommands", sb.toString());
			
			File clusterMgmtScriptFile = new File(
					clusterFolder, "cluster_" + cluster.getName() + ".sh");
			generateFromTemplate(Template.clusterScript, clusterMgmtScriptFile, varSub);
			clusterMgmtScriptFile.setExecutable(true, true);
			LOGGER.info("Generated " + clusterMgmtScriptFile.getAbsolutePath());

			// Write main cluster deployment script
			File clusterDeployScriptFile = 
					new File(clusterFolder, "deploy_cluster_" + cluster.getName() + ".sh");
			BufferedWriter clusterDeployOut = new BufferedWriter(new FileWriter(clusterDeployScriptFile));
			clusterDeployOut.write("#!/bin/bash");
			clusterDeployOut.newLine();
			for (String cmd : clusterDeployCmds) {
				clusterDeployOut.write(cmd);
				clusterDeployOut.newLine();
			}			
			clusterDeployOut.close();
			clusterDeployScriptFile.setExecutable(true, true);
			LOGGER.info("Generated " + clusterDeployScriptFile.getAbsolutePath());			

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
