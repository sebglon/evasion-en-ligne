/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import javax.annotation.security.DeclareRoles;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.evasion.cloud.api.data.ISite;
import org.evasion.cloud.api.service.ISiteService;
import org.evasion.cloud.service.common.PMF;

import static org.evasion.cloud.service.common.RolesConst.*;
import org.evasion.cloud.service.mapper.MapperUtils;
import org.evasion.cloud.service.model.Content;
import org.evasion.cloud.service.model.ContentConst;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.View;
import org.evasion.cloud.service.updator.SiteUpdator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
@Path("site")
@DeclareRoles({"admin", "user"})
public class SiteService extends AbstractService<ISite, Site> implements ISiteService {

    @Context
    private SecurityContext securityContext;
    @Context
    HttpHeaders request;

    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);

    private static final Integer API_VERSION = 1;

    private static SiteUpdator updator;

    public SiteService() {
        updator = new SiteUpdator();
    }

    @Override
    public ISite getBySubDomain(String subdmain) {
        PersistenceManager pm = PMF.getPm();
        Query query = pm.newQuery(Site.class, ":p.contains(subdomain)");
        query.setUnique(true);
        Site site = (Site) query.execute(subdmain);
        /*   if (site.getVersion() == null) {
         try {
         site = updator.upgrade(site);
         pm.makePersistent(site);
         } catch (EntityNotFoundException ex) {
         LOG.error("Fail to upgrade site {}", site.getSubdomain());
         }
         }*/
        if (site == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return MapperUtils.convertFromSite(site);

    }

    private View initAdminView() {
        View admin = new View();
        admin.setTitle("Administration");
        admin.setUrl("/admin");
        Content content = new Content();
        content.setType(ContentConst.STATIC);
        content.setValue("Contenu par défaut");
        admin.setContents(content);
        admin.setAccessRole(new String[]{ADMIN, AUTHOR});
        admin.setIndex(9999);

        return admin;
    }

    @Override
    public ISite create(String subdomain) {
        if (null == securityContext.getUserPrincipal()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        if (null == subdomain || subdomain.isEmpty()) {
            throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
        }
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Site site;
        ISite result;
        try {
            site = new Site();
            View defaultView = new View();
            defaultView.setTitle("Page par défaut");
            defaultView.setUrl("/");
            Content content = new Content();
            content.setType(ContentConst.STATIC);
            content.setValue("Contenu par défaut");
            defaultView.setContents(content);
            defaultView.setIndex(0);
            site.setViews(new ArrayList<View>());
            site.getViews().add(defaultView);
            site.getViews().add(initAdminView());

            site.setUserId(getUser().getId());
            site.setFullName(getUser().getName());
            site.setSubdomain(subdomain);
            site.setTitle("Titre par défaut");
            site.setDateCreation(new Date());
            site.setDateRevision(new Date());

            LOG.debug("Site to create :{}", site);
            result = MapperUtils.convertFromSite(pm.makePersistent(site));
        } finally {
            pm.close();
        }

        return result;
    }

    @Override
    public ISite update(ISite site) {
        String user = getUser().getId();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Site eSite = MapperUtils.convertToSite(site);
        try {
            // recuperation du site en base pour verification du proprietaire
            Site siteBdd = (Site) pm.getObjectById(Site.class, KeyFactory.stringToKey(eSite.getEncodedKey()));
            try {
                updator.upgrade(siteBdd);
            } catch (EntityNotFoundException ex) {
                java.util.logging.Logger.getLogger(SiteService.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (siteBdd == null || siteBdd.getUserId() == null) {
                LOG.warn("Site not found or no author for user: {} {}", siteBdd, user);
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            if (null == user || !siteBdd.getUserId().equals(user) || securityContext.isUserInRole("admin")) {
                LOG.warn("Not same user on update site: {}/ {}", siteBdd.getUserId(), user);
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

            // Reset du sous domain pour s'assurer qu'il ne soit pas changer
            eSite.setSubdomain(siteBdd.getSubdomain());
            eSite.setUserId(siteBdd.getUserId());
            eSite.setDateCreation(siteBdd.getDateCreation());
            eSite.setDateRevision(new Date());
            pm.makePersistent(eSite);
        } finally {
            pm.close();
        }
        return MapperUtils.convertFromSite(eSite);
    }

    @Override
    public String getVersion() {
        return API_VERSION.toString();
    }
}
