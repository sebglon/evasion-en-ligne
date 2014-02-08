package org.evasion.cloud.api.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sgl
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ISite implements Serializable {

    private String id;

    @XmlAttribute
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String title;

    @XmlAttribute
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;

    @XmlAttribute
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<String> keywords = new ArrayList<String>();

    @XmlElement
    @XmlList
    public List<String> getKeywords() {
        return this.keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    private Date dateCreation;

    @XmlAttribute
    public Date getDateCreation() {
        return this.dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    private Date dateRevision;

    @XmlAttribute
    public Date getDateRevision() {
        return this.dateRevision;
    }

    public void setDateRevision(Date dateRevision) {
        this.dateRevision = dateRevision;
    }

     @XmlAttribute
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
 
     @XmlAttribute
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private Set<IView> views = new TreeSet<IView>();

    @XmlElement
    @XmlElementWrapper
    public Set<IView> getViews() {
        return this.views;
    }

    public void setViews(Set<IView> views) {
        this.views = views;
    }

    private String subDomain;

    @XmlAttribute
    public String getSubDomain() {
        return this.subDomain;
    }

    public void setSubDomain(String subdomain) {
        this.subDomain = subdomain;
    }
}
