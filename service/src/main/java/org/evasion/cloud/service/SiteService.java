/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import javax.annotation.security.DeclareRoles;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.model.Site;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.evasion.cloud.service.model.View;
import org.evasion.cloud.service.security.EvasionPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
@Path("site")
@DeclareRoles({"admin", "user"})
public class SiteService {

    @Context
    private SecurityContext securityContext;
    @Context
    HttpHeaders request;

    private static Logger LOG = LoggerFactory.getLogger(SiteService.class);

    @GET()
    @Path("{siteid}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Site get(@PathParam("siteid") long id) {
        LOG.debug("Request: {}", request);
        Site site;
        PersistenceManager pm = PMF.getPm();
        try {
            site = pm.detachCopy(pm.getObjectById(Site.class, id));
        } finally {
            pm.close();
        }
        return site;

    }

    @GET()
    @Path("bySubdmain/{subdomain}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Site getBySubDomain(@PathParam("subdomain") String subdmain) {
        PersistenceManager pm = PMF.getPm();
        Query query = pm.newQuery(Site.class, ":p.contains(subdomain)");
        query.setUnique(true);
        return (Site) query.execute(subdmain);

    }

    @PUT()
    
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response create(@FormParam("subdomain") String subdomain) {
        if (null == securityContext.getUserPrincipal()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        if (null == subdomain || subdomain.isEmpty()) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();
        // create User
        User user;
        LOG.debug("UPN: {}", securityContext.getUserPrincipal());
        user = new User(((EvasionPrincipal) securityContext.getUserPrincipal()).getUserInfo().getName(), ((EvasionPrincipal) securityContext.getUserPrincipal()).getUserInfo().getId());
        Site site;

        try {
            site = new Site();
            View defaultView = new View();
            defaultView.setTitle("Page par défaut");
            defaultView.setUrl("/");
            defaultView.setContent("Contenue par défaut");
            site.setViews(Sets.newHashSet(defaultView));
            
            site.setAuthor(user);
            site.setSubdomain(subdomain);
            LOG.debug("Site to create :{}", site);
            pm.makePersistent(site);
        } finally {
            pm.close();
        }

        return Response.ok(site).build();
    }
}
