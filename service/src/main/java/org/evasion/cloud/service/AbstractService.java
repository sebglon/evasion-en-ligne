/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.services.oauth2.model.Userinfo;
import java.lang.reflect.ParameterizedType;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import org.evasion.cloud.service.common.PMF;
import org.evasion.cloud.service.mapper.MapperUtils;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.security.EvasionPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 * @param <BEANDTO>
 * @param <BEANMODEL>
 */
public abstract class AbstractService<BEANDTO, BEANMODEL> {

    private static final Logger LOG = LoggerFactory.getLogger(SiteService.class);

    @Context
    private SecurityContext securityContext;
    private final Class<BEANDTO> classDTO;
    private final Class<BEANMODEL> classMODEL;
    
    public AbstractService() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.classDTO = (Class<BEANDTO>) type.getActualTypeArguments()[0];
        this.classMODEL = (Class<BEANMODEL>) type.getActualTypeArguments()[1];
    }

    public BEANDTO get(long id) {
        BEANMODEL bean;
        PersistenceManager pm = PMF.getPm();
        try {
            bean = pm.detachCopy(pm.getObjectById(classMODEL, id));
        } finally {
            pm.close();
        }
        return (BEANDTO) MapperUtils.getMapper().map(bean, classDTO);
    }

    protected Userinfo getUser() {
        // create User
        LOG.debug("UPN: {}", securityContext.getUserPrincipal());
        return ((EvasionPrincipal) securityContext.getUserPrincipal()).getUserInfo();
    }
    
    protected Site getUSerSite() {
        PersistenceManager pm = PMF.getPm();
        try {
            Query query = pm.newNamedQuery(Site.class, "SiteByUser");
            query.setUnique(true);
            return (Site) query.execute(getUser().getId());
        } finally {
            pm.close();
        }
    }
    
    public abstract String getVersion();

}
