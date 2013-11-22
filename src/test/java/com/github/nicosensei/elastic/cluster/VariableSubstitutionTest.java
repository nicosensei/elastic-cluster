package com.github.nicosensei.elastic.cluster;

import junit.framework.TestCase;

import org.junit.Test;

import com.github.nicosensei.elastic.cluster.VariableSubstitution;

public class VariableSubstitutionTest extends TestCase {

	@Test
	public void testSubstitute() {

		// Test single variable substitution		
		VariableSubstitution vs = new VariableSubstitution();
		vs.putProperty("test.prop", "yeah cool!");
		assertEquals(
				"blah $ blah yeah cool! foo bar?", 
				vs.substitute("blah $ blah ${test.prop} foo bar?"));

		// Test config style substitution		
		vs = new VariableSubstitution();
		vs.putProperty("java.home", "/usr/jvm/jdk-1.7");
		assertEquals(
				"java.home=/usr/jvm/jdk-1.7", 
				vs.substitute("java.home=${java.home}"));
	}

}
