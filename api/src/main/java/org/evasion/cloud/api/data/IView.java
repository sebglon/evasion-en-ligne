/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.api.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sgl
 */
@XmlRootElement
public class IView implements Serializable, Comparable<IView> {

    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    private String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String description;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Map<String, String> contents = new HashMap();

    public Map<String, String> getContents() {
        return this.contents;
    }

    public void setContents(Map<String, String> contents) {
        this.contents = contents;
    }

    private Set<IBloc> blocs;

    public Set<IBloc> getBlocs() {
        return this.blocs;
    }

    public void setBlocs(SortedSet<IBloc> blocs) {
        this.blocs = blocs;
    }

    private int index;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    private String[] accessRole;
    
    private String relatedModule;

    public String[] getAccessRole() {
        return accessRole;
    }

    public void setAccessRole(String[] accessRole) {
        this.accessRole = accessRole;
    }

    public String getRelatedModule() {
        return relatedModule;
    }

    public void setRelatedModule(String relatedModule) {
        this.relatedModule = relatedModule;
    }
    

    @Override
    public int compareTo(IView o) {
        return this.getIndex() - o.getIndex();
    }
}
