/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.impl.ClientRequestImpl;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletRequestListener;
import javax.ws.rs.core.MediaType;
import org.evasion.cloud.api.data.ISite;
import org.evasion.cloud.api.data.booktravel.IBook;
import org.evasion.cloud.service.common.BeanInjector;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.model.Site;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
    protected AppDescriptor configure() {
        return new WebAppDescriptor.Builder(BooktravelService.class.getPackage().getName())
                .contextPath("/").servletPath("ws")
                .initParam("com.sun.jersey.api.json.POJOMappingFeature", "true")
                .initParam("com.sun.jersey.spi.container.ContainerResponseFilters", "org.evasion.cloud.service.AppSecurityFilter")
                .initParam("com.sun.jersey.spi.container.ContainerRequestFilters", "org.evasion.cloud.service.AppSecurityFilter")
                .initParam("com.sun.jersey.config.feature.Trace", "true")
                .requestListenerClass(ServletRequestListener.class)
                .build();

    }

    /**
     * Sets the username in the injected security context, to simulate the user
     * being logged in during the test.
     */
    private void setUPN(String googleId, Userinfo user) {
        BeanInjector.googleid = googleId;
        BeanInjector.user = user;
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
        WebResource resource = resource().path("site");

        Form form = new Form();
        form.add("subdomain", "test");
        
        ISite result = resource.type(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).post(ISite.class, form);
        assertNotNull("1. get success with encoded Key", result.getId());
        assertNotNull("2. title is not null", result.getTitle());

    }

    @Test
    public void testGetBySubDomain() {
        System.out.println("getBySubDomain");
        WebResource resource = resource().path("site/bySubdmain/"+testSite.getSubdomain());
        ISite result = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .get(ISite.class);
        assertNotNull("Site trouvé", result);
        assertEquals("Same title",testSite.getTitle(), result.getTitle());
        assertEquals("Same subdomain",testSite.getSubdomain(), result.getSubDomain());
    }

    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        String resp = resource().path("site/version").accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        assertEquals(
                "1. get the right version", "1", resp);
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        WebResource resource = resource().path("site");
        ISite updateSite = new ISite();
        updateSite.setId(testSite.getEncodedKey());
        updateSite.setSubDomain(testSite.getSubdomain());
        updateSite.setUserId(testSite.getUserId());
        updateSite.setTitle("new title");
        updateSite.setDateCreation(testSite.getDateCreation());
        updateSite.setDateRevision(testSite.getDateRevision());
        
        ISite resultat = resource.put(ISite.class, updateSite);
        assertNotNull("Site return",resultat);
        assertTrue("same creation date",updateSite.getDateCreation().compareTo(resultat.getDateCreation())==0);
        assertTrue("Not same modification date",updateSite.getDateRevision().compareTo(resultat.getDateRevision())<0);
    }
}
