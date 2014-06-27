/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import javax.jdo.PersistenceManager;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.evasion.cloud.api.data.IModuleDescriptor;
import org.evasion.cloud.api.data.booktravel.IBook;
import org.evasion.cloud.api.service.IBookTravelService;
import org.evasion.cloud.api.service.IModuleService;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.mapper.MapperUtils;
import org.evasion.cloud.service.model.Content;
import org.evasion.cloud.service.model.ContentConst;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.View;
import org.evasion.cloud.service.model.booktravel.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
@Path("booktravel")
public class BooktravelService extends AbstractService<IBook, Book> implements IBookTravelService, IModuleService {

    @Context
    private SecurityContext securityContext;

    private static final Logger LOG = LoggerFactory.getLogger(BooktravelService.class);

    private static final Integer API_VERSION = 1;
    private static final String API_MODULE_KEY = "booktravel";

    @Override
    public String getVersion() {
        return API_VERSION.toString();
    }

    @Override
    public IBook create(String siteid, IBook book) {

        if (null == siteid || null == book || book.getTitle() == null || book.getTitle().isEmpty()
                || book.getShortName() == null || book.getShortName().isEmpty()
                || (book.getId() != null && !book.getId().isEmpty())) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Site site = pm.getObjectById(Site.class, siteid);
        if (null == securityContext.getUserPrincipal() || !site.getUserId().equals(getUser().getId())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        // check if shortname is unique
        this.isAvailable(book.getShortName());
        Book bookBase = MapperUtils.convertToBook(book);
        try {

            pm.makePersistent(bookBase);

            View booktravelView = new View();
            booktravelView.setUrl("/voyage/" + bookBase.getShortName());
            Content btContent = new Content();
            btContent.setType(ContentConst.TEMPLATE_URL);
            btContent.setValue("partials/booktravel.html");
            btContent.setDataKey(bookBase.getEncodedKey());
            booktravelView.setContents(btContent);
            booktravelView.setTitle("carnet de voyage");
            booktravelView.setIndex(site.getViews().size() + 1);
            booktravelView.setAccessRole(new String[] {"ANONYMOUS","AUTHOR", "ADMIN"});
            booktravelView.setRelatedModule(API_MODULE_KEY+"/"+API_VERSION);
            site.getViews().add(booktravelView);

            LOG.debug("Book to create :{}", bookBase);
            pm.makePersistent(site);
        } finally {
            pm.close();
        }

        return MapperUtils.convertFromBook(bookBase);
    }

    @Override
    public IBook update(IBook book) {
        if (null == book
                || book.getTitle() == null
                || book.getTitle().isEmpty()
                || book.getShortName() == null
                || book.getShortName().isEmpty()) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Book oldBook = pm.getObjectById(Book.class, book.getId());
        Site site = pm.getObjectById(Site.class, oldBook.getSiteKey());

        if (null == securityContext.getUserPrincipal() || !site.getUserId().equals(getUser().getId())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        // the shortName book can't be changed.
        if (!book.getShortName().equals(oldBook.getShortName())) {
            throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
        }
        Book newBook = MapperUtils.convertToBook(book);
        // update site modif date
        site.setDateRevision(new Date());
        try {
            pm.makePersistent(newBook);
            pm.makePersistent(site);

        } finally {
            pm.close();
        }

        return MapperUtils.convertFromBook(newBook);
    }

    @Override
    public Response isAvailable(String shortName) {
        if (shortName == null || shortName.isEmpty()) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
        Collection<Book> books = new ArrayList<Book>();

        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            books = (Collection<Book>) pm.newNamedQuery(Book.class, "isAvailable")
                    .execute(KeyFactory.stringToKey(getUSerSite().getEncodedKey()), shortName);
        } finally {
            pm.close();
        }
        if (books.isEmpty()) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @Override
    public IBook get(String id) {
        Book book = null;
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            book = pm.getObjectById(Book.class, KeyFactory.stringToKey(id));
        } finally {
            pm.close();
        }
        return MapperUtils.convertFromBook(book);
    }

    @Override
    public IModuleDescriptor getModuleDescriptor() {

        return descriptor;
    }

    private static final IModuleDescriptor descriptor;

    static {
        descriptor = new IModuleDescriptor("1",
                "Booktravel",
                "1.0",
                new HashMap<String, String>());
    }

}
