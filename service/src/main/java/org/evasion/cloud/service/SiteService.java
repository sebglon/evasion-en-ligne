/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import javax.annotation.security.RolesAllowed;
import javax.jdo.PersistenceManager;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.model.Site;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
@Path("site")
public class SiteService {

    @Context
    private SecurityContext securityContext;
    @Context
    HttpHeaders request;
    
    private static Logger LOG = LoggerFactory.getLogger(SiteService.class);

    @GET()
    @RolesAllowed({ "admin" })
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
        Site result = null;
        return result;

    }

    @PUT()
    @RolesAllowed({ "Editor", "Contributor" })
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response create() {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Site site;
        try {
            site = new Site();
            LOG.debug("User connected :{}", securityContext.getUserPrincipal());
            site.setAuthor(null);
            pm.makePersistent(site);
        } finally {
            pm.close();
        }

        return Response.ok(site).build();
    }
}
