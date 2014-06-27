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
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.evasion.cloud.api.data.booktravel.IBook;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.common.SecurityContextFilter;
import org.evasion.cloud.service.mapper.MapperUtils;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.booktravel.Book;
import org.glassfish.jersey.client.ClientResponse;
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
public class BooktravelServiceTest extends JerseyTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @Override
    protected Application configure() {
        Application app = new ResourceConfig(BooktravelService.class,
                SecurityContextFilter.class,
                SiteService.class);
       // app.getClasses().add(BooktravelService.class);
        // app.getClasses().add(AppSecurityFilter.class);
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
        String resp = target("booktravel/version").request(MediaType.APPLICATION_JSON)
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
        WebTarget resource = target().path("booktravel/" + encodedKeySite);

        Date date = new Date();
        IBook book = new IBook();
        book.setDateDebut(date);
        book.setDateFin(date);
        book.setTitle("Titre A");
        book.setShortName("SUCCESS1");
        book.setDescription("Ma description");
        Entity e = Entity.entity(book, MediaType.APPLICATION_JSON_TYPE);

        IBook expResult = new IBook();
        expResult.setDateDebut(date);
        expResult.setDateFin(date);
        expResult.setTitle("Titre A");
        expResult.setShortName("SUCCESS1");
        expResult.setDescription("Ma description");
        IBook result = resource.request(MediaType.APPLICATION_JSON_TYPE).
                accept(MediaType.APPLICATION_JSON_TYPE).post(e, IBook.class);
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

        Entity<IBook> e = Entity.entity(test, MediaType.WILDCARD_TYPE);
        Response r = target().path("booktravel/").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_TYPE).put(e);

        IBook result = r.readEntity(IBook.class);
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
        Response resulta = target().path("booktravel/available/" + shortname)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get();
        assertEquals("test pour book deja existant: " + encodedKeySite, 406, resulta.getStatus());

        Response resultb = target().path("booktravel/available/test")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get();
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

        IBook result = target().path("booktravel/byid/" + encodedKeyBook)
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(IBook.class);
        assertEquals(expResult.getTitle(), result.getTitle());
        assertEquals(expResult.getDescription(), result.getDescription());
    }

}
