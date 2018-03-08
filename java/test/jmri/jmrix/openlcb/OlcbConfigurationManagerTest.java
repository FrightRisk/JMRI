package jmri.jmrix.openlcb;

import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Paul Bender Copyright (C) 2017	
 */
public class OlcbConfigurationManagerTest {
        
    private OlcbSystemConnectionMemo memo;

    @Test
    public void testCTor() {
        OlcbConfigurationManager t = new OlcbConfigurationManager(memo);
        Assert.assertNotNull("exists",t);
    }

    @Test
    public void testConfigureManagers() {
        OlcbConfigurationManager t = new OlcbConfigurationManager(memo);
        // this tet verifies this does not throw an exception
        t.configureManagers(); 
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        OlcbSystemConnectionMemo memo = OlcbTestInterface.createForLegacyTests();
    }

    @After
    public void tearDown() {
        memo.getInterface().dispose();
        JUnitUtil.tearDown();
    }

    // private final static Logger log = LoggerFactory.getLogger(OlcbConfigurationManagerTest.class);

}
