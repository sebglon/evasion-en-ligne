/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Key;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.model.Site;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sgl
 */
public class JDOTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    public JDOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void simpleJdo() {
        
        Site site = new Site();
        site.setTitle("titre TEST");
        //site.setKey(1L);
        
        PersistenceManager pm;
        pm = PMF.getPm();
        boolean notFound = false;
        try {
            pm.getObjectById(Site.class, 1L);
            fail("should have raised not found");
        } catch (JDOObjectNotFoundException e) {
            notFound = true;
        } finally {
            pm.close();
        }
        assertTrue(notFound);

        pm = PMF.getPm();
        try {
            pm.makePersistent(site);
        } finally {
            pm.close();
        }

        pm = PMF.getPm();
        try {
            site = pm.getObjectById(Site.class, 1L);
        } finally {
            pm.close();
        }

        assertNotNull(pm);
        assertEquals("titre TEST", site.getTitle());
    }
}