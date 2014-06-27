/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.evasion.cloud.api.data.ISite;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.common.SecurityContextFilter;
import org.evasion.cloud.service.model.Content;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.View;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author sgl
 */
@RunWith(MockitoJUnitRunner.class)
public class SiteServiceTest extends JerseyTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @Override
    protected Application configure() {
        Application app = new ResourceConfig(SecurityContextFilter.class,
                SiteService.class);
        return app;
    }

    /**
     * Sets the username in the injected security context, to simulate the user
     * being logged in during the test.
     */
    private void setUPN(String googleId, Userinfo user) {
        SecurityContextFilter.googleid = googleId;
        SecurityContextFilter.user = user;
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        helper.setUp();
        initSite();

    }

    Site testSite;

    private void initSite() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Userinfo user = new Userinfo();
        user.setName("test user");
        user.setId("googleId");
        this.setUPN("googleId", user);

        Site site = new Site();
        site.setSubdomain("testXX");
        site.setTitle("Titre du site");
        site.setDateCreation(new Date(99000));
        site.setUserId(user.getId());
        site.setDateRevision(site.getDateCreation());

        View view = new View();
        view.setTitle("title");

        Content content = new Content();
        view.setContents(content);
        site.getViews().add(view);
        testSite = pm.makePersistent(site);
    }

    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        helper.tearDown();
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        WebTarget resource = target().path("site");

        Form form = new Form();
        form.param("subdomain", "test");

        ISite result = resource.request(MediaType.APPLICATION_JSON_TYPE).
                accept(MediaType.APPLICATION_JSON_TYPE).post(
                        Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
                        ISite.class);
        assertNotNull("1. get success with encoded Key", result.getId());
        assertNotNull("2. title is not null", result.getTitle());
        assertFalse("3. view not empty", result.getViews().isEmpty());
        assertFalse("4. content on first view not empty", result.getViews().iterator().next().getContents().isEmpty());

    }

    @Test
    public void testGetBySubDomain() {
        System.out.println("getBySubDomain");
        WebTarget resource = target().path("site/bySubdmain/" + testSite.getSubdomain());
        ISite result = resource.request(MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ISite.class);

        assertNotNull("Site trouvé", result);
        assertEquals("Same title", testSite.getTitle(), result.getTitle());
        assertEquals("Same subdomain", testSite.getSubdomain(), result.getSubDomain());
        assertFalse("3. view not empty", result.getViews().isEmpty());
        assertFalse("4. content on first view not empty", result.getViews().iterator().next().getContents().isEmpty());
    }

    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        String resp = target().path("site/version").request().accept(MediaType.APPLICATION_JSON_TYPE)
                .get(String.class);

        assertEquals(
                "1. get the right version", "1", resp);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        WebTarget resource = target().path("site");
        ISite updateSite = new ISite();
        updateSite.setId(testSite.getEncodedKey());
        updateSite.setSubDomain(testSite.getSubdomain());
        updateSite.setUserId(testSite.getUserId());
        updateSite.setTitle("new title");
        updateSite.setDateCreation(testSite.getDateCreation());
        updateSite.setDateRevision(testSite.getDateRevision());

        ISite resultat = resource.request().put(Entity.entity(updateSite, MediaType.APPLICATION_JSON_TYPE), ISite.class);
        assertNotNull("Site return", resultat);
        assertTrue("same creation date", updateSite.getDateCreation().compareTo(resultat.getDateCreation()) == 0);
        assertTrue("Not same modification date", updateSite.getDateRevision().compareTo(resultat.getDateRevision()) < 0);
    }
}
