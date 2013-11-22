package com.github.nicosensei.elastic.cluster.beans;


import org.junit.Test;

import com.github.nicosensei.elastic.cluster.beans.FilePath;

import junit.framework.TestCase;

public class FilePathTest extends TestCase {

	@Test
	public void testResolvePath() {

		// Test absolute path
		FilePath rootFp = new FilePath();
		String path = "/some/path/to/file";
		rootFp.setPath(path);
		assertEquals(path, rootFp.resolvePath());

		// Test compound path
		FilePath fp = new FilePath();
		fp.setPath("and/even/deeper");
		fp.setRoot(rootFp);
		assertEquals(path + "/" + "and/even/deeper", fp.resolvePath());

	}

}
