/**
 * 
 */
package fr.nikokode.elastic.cluster;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author ngiraud
 *
 */
public class ElasticClusterSetup {
	
	private static final Logger LOGGER = Logger.getLogger(ElasticClusterSetup.class);

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		if (args.length != 2) {
			System.out.println("Usage: " + ElasticClusterSetup.class.getCanonicalName()
					+ "\n\t- <cluster setup file>"
					+ "\n\t- <output directory (must exist and be writable)>");
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
		
		File outDir = currentPath.resolve(args[1]).toFile();
		if (!outDir.exists()
				|| ! outDir.isDirectory()
				|| ! outDir.canRead()
				|| ! outDir.canWrite()) {
			throw new IOException("Output directory '" 
				+ outDir.getAbsolutePath() + " is not a writable folder!");
		}
		
		FileSystemXmlApplicationContext appCtx = 
				new FileSystemXmlApplicationContext("file://" + setupFile.getAbsolutePath());
		try {
			LOGGER.info("Instanciated Spring context from " + setupFile.getAbsolutePath());
		} finally {
			appCtx.close();
		}

	}

}
