/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;
import org.evasion.cloud.service.User;

/**
 *
 * @author sgl
 */
@PersistenceCapable()
public class Site {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String encodedKey;
    @Persistent
    @Unique
    private String subdomain;
    @Persistent
    private String version = "1";
    @Persistent
    private String title;
    @Persistent
    private String description;
    @Persistent
    private User author;
    @Persistent
    private List<String> keywords = new ArrayList<String>();
    @Persistent
    private Date dateCreation;
    @Persistent
    private Date dateRevision;
    @Persistent
    @Element(dependent = "true")
    @Order(extensions = @Extension(vendorName="datanucleus", key="list-ordering", value="index asc"))
    private List<View> views = new ArrayList<View>();

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateRevision() {
        return dateRevision;
    }

    public void setDateRevision(Date dateRevision) {
        this.dateRevision = dateRevision;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
