/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.model;

import java.util.SortedSet;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 *
 * @author sgl
 * @param <T>
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class View implements Comparable<View>{

 @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey;

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    @Persistent
    private String title;

    @Persistent
    private String url;

    @Persistent
    private String description;

    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private Content contents;

    @Persistent(defaultFetchGroup = "true")
    @Element(dependent = "true")
    private SortedSet<Bloc> blocs;
    
    private int index;
    
    public String getEncodedKey() {
        return encodedKey;
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

    public Content getContents() {
        return contents;
    }

    public void setContents(Content contents) {
        this.contents = contents;
    }

    public SortedSet<Bloc> getBlocs() {
        return blocs;
    }

    public void setBlocs(SortedSet<Bloc> blocs) {
        this.blocs = blocs;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(View o) {
                return this.getIndex() - o.getIndex();
    }
    
}
