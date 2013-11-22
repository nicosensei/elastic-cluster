/**
 * 
 */
package com.github.nicosensei.elastic.cluster.beans;

import java.nio.file.Paths;


/**
 * A Spring bean representing a file path.
 * 
 * @author ngiraud
 *
 */
public class FilePath {
	
	/**
	 * The root path, can be null if the file path denotes an absolute path.
	 */
	private FilePath root;
	
	/**
	 * The path. If {@link #root} is non-null, then the path is considered relative to the root path.
	 */
	private String path;

	/**
	 * @return the root
	 */
	public FilePath getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(FilePath root) {
		this.root = root;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	public String resolvePath() {
		if (root == null) {
			return Paths.get("/").resolve(this.path).toAbsolutePath().toString();
		}
		return Paths.get(root.resolvePath()).resolve(this.path).toAbsolutePath().toString();
	}

}
