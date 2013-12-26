package org.evasion.cloud.api.data;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.evasion.cloud.api.data.IAuthor;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sgl
 */
public interface ISite {

    String getId();

    void setId(String id);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    List<String> getKeywords();

    void setKeywords(List<String> keywords);

    Date getDateCreation();

    void setDateCreation(Date dateCreation);

    Date getDateRevision();

    void setDateRevision(Date dateRevision);

    IAuthor getAuthor();

    void setAuthor(IAuthor author);

    Set<IView> getViews();

    void setViews(Set<IView> views);

    String getSubdomain();

    void setSubdomain(String subdomain);
}
