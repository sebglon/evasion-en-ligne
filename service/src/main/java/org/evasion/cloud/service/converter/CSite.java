/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.converter;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CSite implements ISite, IConverter<Site> {

    public CSite() {
        this.entity = new Site();
    }

    public CSite(Site entity) {
        this.entity = entity;
    }

    private final Site entity;

    @XmlTransient
    @Override
    public Site getEntity() {
        return entity;
    }

    @XmlAttribute
    @Override
    public String getId() {
        return getEntity().getEncodedKey();
    }

    @Override
    public void setId(String id) {
        getEntity().setEncodedKey(id);
    }

    @XmlAttribute
    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getEntity().setTitle(title);
    }

    @XmlAttribute
    @Override
    public String getDescription() {
        return getEntity().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getEntity().setDescription(description);
    }

    @XmlElement
    @XmlList
    @Override
    public List<String> getKeywords() {
        return getEntity().getKeywords();
    }

    @Override
    public void setKeywords(List<String> keywords) {
        getEntity().setKeywords(keywords);
    }

    @XmlAttribute
    @Override
    public Date getDateCreation() {
        return getEntity().getDateCreation();
    }

    @Override
    public void setDateCreation(Date dateCreation) {
        getEntity().setDateCreation(dateCreation);
    }

    @XmlAttribute
    @Override
    public Date getDateRevision() {
        return getEntity().getDateRevision();
    }

    @Override
    public void setDateRevision(Date dateRevision) {
        getEntity().setDateRevision(dateRevision);
    }

    @XmlElement
    @Override
    public IAuthor getAuthor() {
        return new CAuthor(getEntity().getAuthor());
    }

    @Override
    public void setAuthor(IAuthor author) {
        getEntity().setAuthor(null);
    }

    @XmlElement
    @XmlElementWrapper
    @Override
    public Set<IView> getViews() {
        Set<IView> result = new TreeSet<IView>(new Comparator<IView>() {

            @Override
            public int compare(IView o1, IView o2) {
                return o1.getIndex() - o2.getIndex();
            }
        });

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
                final View view = ((CView) o).getEntity();
                return view;
            }
        }, result);
        getEntity().setViews(result);
    }

    @XmlAttribute
    @Override
    public String getSubdomain() {
        return getEntity().getSubdomain();
    }

    @Override
    public void setSubdomain(String subdomain) {
        getEntity().setSubdomain(subdomain);
    }

}
