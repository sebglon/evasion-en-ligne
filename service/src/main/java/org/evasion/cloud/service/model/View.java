/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.model;

import com.google.appengine.api.datastore.Key;
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
public class View {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    @Persistent
    private String title;
    
    @Persistent
    private String url;
    
    @Persistent
    private String description;
    
    @Persistent
    private String content;
    
    @Persistent
    private SortedSet<Bloc> blocs;

    public Key getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SortedSet<Bloc> getBlocs() {
        return blocs;
    }

    public void setBlocs(SortedSet<Bloc> blocs) {
        this.blocs = blocs;
    }
    
}
