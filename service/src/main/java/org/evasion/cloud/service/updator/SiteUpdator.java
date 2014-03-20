/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.updator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import org.evasion.cloud.service.model.Content;
import org.evasion.cloud.service.model.ContentConst;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.View;

/**
 *
 * @author sgl
 */
public class SiteUpdator {

    private final DatastoreService datastore;

    public SiteUpdator() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    public Site upgrade(Site site) throws EntityNotFoundException {
         if(site.getUserId()==null) {
             site.setUserId("xxxx");
         }
        for (View view : site.getViews()) {
            Entity oldView = datastore.get(KeyFactory.stringToKey(view.getEncodedKey()));
            String value = (String) oldView.getProperty("content");
            if (value != null) {
               if (view.getContents()==null) {
                   view.setContents(new Content());
               }
                view.getContents().setType(ContentConst.STATIC);
                view.getContents().setValue(value);
            }
            oldView.removeProperty("content");
            datastore.put(oldView);
            
            site.setVersion("1");
        }
        return site;

    }

}
