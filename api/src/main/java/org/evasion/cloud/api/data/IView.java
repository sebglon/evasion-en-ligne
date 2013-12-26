/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.api.data;

import java.util.Set;
import java.util.SortedSet;

/**
 *
 * @author sgl
 */
public interface IView {
    
    String getTitle();
    void setTitle(String title);
    String getUrl();
    void setUrl(String url);
    String getDescription();
    void setDescription(String description);
    String getContent();
    void setContent(String content);
    Set<IBloc> getBlocs();
    void setBlocs(SortedSet<IBloc> blocs);
    
}
