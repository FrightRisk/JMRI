// NamedBeanTest.java

package jmri.implementation;

import jmri.*;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the NamedBean interface
 * @author	Bob Jacobsen  Copyright (C) 2009
 * @version $Revision: 1.3 $
 */
public class NamedBeanTest extends TestCase {

	public void testSetParameter() {
	    NamedBean n = new AbstractNamedBean("sys", "usr"){
	        public int getState() {return 0;}
	        public void setState(int i) {}
	    };

	    n.setProperty("foo", "bar");
	}

	public void testGetParameter() {
	    NamedBean n = new AbstractNamedBean("sys", "usr"){
	        public int getState() {return 0;}
	        public void setState(int i) {}
	    };
	    
	    n.setProperty("foo", "bar");
	    Assert.assertEquals("bar", n.getProperty("foo"));
	}

	public void testGetSetNull() {
	    NamedBean n = new AbstractNamedBean("sys", "usr"){
	        public int getState() {return 0;}
	        public void setState(int i) {}
	    };
	    
	    n.setProperty("foo", "bar");
	    Assert.assertEquals("bar", n.getProperty("foo"));
	    n.setProperty("foo", null);
	    Assert.assertEquals(null, n.getProperty("foo"));
	}

	// from here down is testing infrastructure

	public NamedBeanTest(String s) {
		super(s);
	}

	// Main entry point
	static public void main(String[] args) {
		String[] testCaseName = {NamedBeanTest.class.getName()};
		junit.swingui.TestRunner.main(testCaseName);
	}

	// test suite from all defined tests
	public static Test suite() {
		TestSuite suite = new TestSuite(NamedBeanTest.class);
		return suite;
	}

}
