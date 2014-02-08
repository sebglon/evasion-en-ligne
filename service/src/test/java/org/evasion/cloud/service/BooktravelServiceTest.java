/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletRequestListener;
import javax.ws.rs.core.MediaType;
import org.evasion.cloud.api.data.booktravel.IBook;
import org.evasion.cloud.service.common.BeanInjector;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.mapper.MapperUtils;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.booktravel.Book;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author sgl
 */
@RunWith(MockitoJUnitRunner.class)
public class BooktravelServiceTest extends JerseyTest {
    
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
    
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        helper.tearDown();
    }
    
    private String encodedKeySite = null;
    private String encodedKeyBook = null;
    private String shortname = "BOOK_TEST";
    private Book bookTest;
    private Site siteTest;
    
    private void initSite() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Userinfo user = new Userinfo();
        user.setName("test user");
        user.setId("googleId");
        this.setUPN("googleId", user);
        
        Site site = new Site();
        site.setTitle("Site title");
        site.setUserId(user.getId());
        site.setFullName(user.getName());
        site.setDateRevision(new Date());
        siteTest = pm.makePersistent(site);
        encodedKeySite = siteTest.getEncodedKey();
        
        Book book = new Book();
        book.setSiteKey(KeyFactory.stringToKey(encodedKeySite));
        book.setShortName(shortname);
        book.setTitle("book de test");
        bookTest = pm.makePersistent(book);
        encodedKeyBook = bookTest.getEncodedKey();
    }

    /**
     * Test of getVersion method, of class BooktravelService.
     */
    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        String resp = resource().path("booktravel/version").accept(MediaType.APPLICATION_JSON)
                .get(String.class);
        
        assertEquals(
                "1. get the right version", "1", resp);
    }

    /**
     * Test of create method, of class BooktravelService.
     */
    @Test
    public void testCreate() {
        
        System.out.println("create");
        WebResource resource = resource().path("booktravel/" + encodedKeySite);
        
        Date date = new Date();
        IBook book = new IBook();
        book.setDateDebut(date);
        book.setDateFin(date);
        book.setTitle("Titre A");
        book.setShortName("SUCCESS1");
        book.setDescription("Ma description");
        
        IBook expResult = new IBook();
        expResult.setDateDebut(date);
        expResult.setDateFin(date);
        expResult.setTitle("Titre A");
        expResult.setShortName("SUCCESS1");
        expResult.setDescription("Ma description");
        IBook result = resource.type(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON).post(IBook.class, book);
        assertNotNull("1. get success with encoded Key", result.getId());
        assertEquals("2. title is same", expResult.getTitle(), result.getTitle());
    }

    /**
     * Test of update method, of class BooktravelService.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        IBook test = MapperUtils.convertFromBook(bookTest);
        test.setDescription("new description");

        IBook result = resource().path("booktravel/")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).put(IBook.class, test);
        assertEquals("1. check description change", "new description", result.getDescription());
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Site newSite = pm.getObjectById(Site.class, KeyFactory.stringToKey(encodedKeySite));
        pm.close();
        assertNotSame("2. updateDate change", siteTest.getDateRevision(), newSite.getDateRevision());
    }

    /**
     * Test of isAvailable method, of class BooktravelService.
     */
    @Test
    public void testIsAvailable() {
        
        System.out.println("isAvailable");
        ClientResponse resulta = resource().path("booktravel/available/" + shortname)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertEquals("test pour book deja existant: " + encodedKeySite, 406, resulta.getStatus());
        
        ClientResponse resultb = resource().path("booktravel/available/test")
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        assertEquals(200, resultb.getStatus());
        
    }

    /**
     * Test of get method, of class BooktravelService.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        
        IBook expResult = new IBook();
        expResult.setTitle(bookTest.getTitle());
        expResult.setDescription(bookTest.getDescription());
        expResult.setShortName(bookTest.getShortName());
        expResult.setId(bookTest.getEncodedKey());
        
        IBook result = resource().path("booktravel/byid/" + encodedKeyBook)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .get(IBook.class);
        assertEquals(expResult.getTitle(), result.getTitle());
        assertEquals(expResult.getDescription(), result.getDescription());
    }
    
}
