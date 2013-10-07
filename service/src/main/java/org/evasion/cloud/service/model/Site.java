/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author sgl
 */
@PersistenceCapable
public class Site {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    @Persistent
    private String title;
    @Persistent
    private String description;
    @Persistent
    private User author;
    @Persistent
    private List<String> keywords;
    @Persistent
    private Date dateCreation;
    @Persistent
    private Date dateRevision;
    @Persistent
    private SortedSet<View> views;

    public Key getKey() {
        return key;
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

    public SortedSet<View> getViews() {
        return views;
    }

    public void setViews(SortedSet<View> views) {
        this.views = views;
    }
}
