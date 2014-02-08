/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.model.booktravel;

import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

/**
 *
 * @author sgl
 */
@PersistenceCapable()
@Queries(value = {
    @Query(name = "isAvailable", value = "SELECT FROM org.evasion.cloud.service.model.booktravel.Book WHERE "
            + "siteKey == :siteKeyParam && shortName == :shortNameParam")
})
public class Book {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String encodedKey;

    @Persistent
    private Key siteKey;
    public static final String PROP_SITE_KEY = "siteKey";

    @Index
    @Persistent
    private String shortName;
   @Index
    @Persistent
    private String title;
    @Persistent
    private String description;
    @Persistent
    private Date dateDebut;
    @Persistent
    private Date dateFin;
    @Index
    @Persistent
    private List<String> countries;
    @Persistent
    @Element
    @Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "index asc"))
    private List<RoadMap> roadmaps = new ArrayList<RoadMap>();

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<RoadMap> getRoadmaps() {
        return roadmaps;
    }

    public void setRoadmaps(List<RoadMap> roadmaps) {
        this.roadmaps = roadmaps;
    }
    /**
     * Get the value of siteKey
     *
     * @return the value of siteKey
     */
    public Key getSiteKey() {
        return siteKey;
    }

    /**
     * Set the value of siteKey
     *
     * @param siteKey new value of siteKey
     */
    public void setSiteKey(Key siteKey) {
        this.siteKey = siteKey;
    }
}
