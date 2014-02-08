/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import org.evasion.cloud.service.updator.SiteUpdator;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.util.Date;
import javax.annotation.security.DeclareRoles;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.Path;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.model.Site;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.evasion.cloud.api.data.ISite;
import org.evasion.cloud.api.service.ISiteService;
import org.evasion.cloud.service.mapper.MapperUtils;
import org.evasion.cloud.service.model.Content;
import org.evasion.cloud.service.model.ContentConst;
import org.evasion.cloud.service.model.View;
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
        if (site.getVersion() == null) {
            try {
                site = updator.upgrade(site);
                pm.makePersistent(site);
            } catch (EntityNotFoundException ex) {
                LOG.error("Fail to upgrade site {}", site.getSubdomain());
            }
        }
        return MapperUtils.convertFromSite(site);

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
            site.getViews().add(defaultView);

            View booktravelView = new View();
            booktravelView.setUrl("/voyage");
            Content btContent = new Content();
            btContent.setType(ContentConst.TEMPLATE_URL);
            btContent.setValue("partials/booktravel.html");
            btContent.setDataKey(Integer.toString(5));
            booktravelView.setContents(btContent);
            booktravelView.setTitle("carnet de voyage");
            booktravelView.setIndex(1);
            site.getViews().add(booktravelView);
            site.setUserId(getUser().getId());
            site.setFullName(getUser().getName());
            site.setSubdomain(subdomain);
            site.setTitle("Titre par défaut");
            site.setDateCreation(new Date());
            site.setDateRevision(new Date());

            LOG.debug("Site to create :{}", site);
            pm.makePersistent(site);
        } finally {
            pm.close();
        }

        return MapperUtils.convertFromSite(site);
    }

    @Override
    public ISite update(ISite site) {
        String user = getUser().getId();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Site eSite = MapperUtils.convertToSite(site);
        try {
            // recuperation du site en base pour verification du proprietaire
            Site siteBdd = (Site) pm.getObjectById(Site.class, eSite.getEncodedKey());
            if (siteBdd == null || siteBdd.getUserId() == null) {
                LOG.warn("Site not found or no author for user: {} {}", siteBdd, user);
                throw new WebApplicationException(Response.Status.NOT_FOUND);
            }
            if (null == user || !siteBdd.getUserId().equals(user)) {
                LOG.warn("Not same user on update site: {}/ {}", siteBdd.getUserId(), user);
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

            // Reset du sous domain pour s'assurer qu'il ne soit pas changer
            eSite.setSubdomain(siteBdd.getSubdomain());
            eSite.setUserId(siteBdd.getUserId());
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
