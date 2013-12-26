/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.evasion.cloud.api.data.IAuthor;
import org.evasion.cloud.api.data.ISite;
import org.evasion.cloud.api.data.IView;
import org.evasion.cloud.service.model.Site;
import org.evasion.cloud.service.model.View;

/**
 *
 * @author sgl
 */
@XmlRootElement
public class CSite extends IConverter<Site> implements ISite {

    public CSite() {
        super(new Site());
    }

    public CSite(Site entity) {
        super(entity);
    }

    @Override
    public String getId() {
        return getEntity().getEncodedKey();
    }

    @Override
    public void setId(String id) {
        getEntity().setEncodedKey(id);
    }

    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getEntity().setTitle(title);
    }

    @Override
    public String getDescription() {
        return getEntity().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getEntity().setDescription(description);
    }

    @Override
    public List<String> getKeywords() {
        return getEntity().getKeywords();
    }

    @Override
    public void setKeywords(List<String> keywords) {
        getEntity().setKeywords(keywords);
    }

    @Override
    public Date getDateCreation() {
        return getEntity().getDateCreation();
    }

    @Override
    public void setDateCreation(Date dateCreation) {
        getEntity().setDateCreation(dateCreation);
    }

    @Override
    public Date getDateRevision() {
        return getEntity().getDateRevision();
    }

    @Override
    public void setDateRevision(Date dateRevision) {
        getEntity().setDateRevision(dateRevision);
    }

    @Override
    public IAuthor getAuthor() {
        return new CAuthor(getEntity().getAuthor());
    }

    @Override
    public void setAuthor(IAuthor author) {
        getEntity().setAuthor(null);
    }

    @Override
    public Set<IView> getViews() {
        Set<IView> result = new HashSet<IView>();

        CollectionUtils.collect(getEntity().getViews(), new Transformer() {
            @Override
            public Object transform(Object o) {
                return new CView((View) o);
            }
        }, result);
        return result;
    }

    @Override
    public void setViews(Set<IView> views) {
        Set<View> result = new HashSet<View>();
        CollectionUtils.collect(views, new Transformer() {
            @Override
            public Object transform(Object o) {
                return ((CView) o).getEntity();
            }
        }, result);
        getEntity().setViews(result);
    }

    @Override
    public String getSubdomain() {
        return getEntity().getSubdomain();
    }

    @Override
    public void setSubdomain(String subdomain) {
        getEntity().setSubdomain(subdomain);
    }

}
