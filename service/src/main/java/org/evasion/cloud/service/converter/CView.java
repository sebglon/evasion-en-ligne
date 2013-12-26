/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.converter;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.evasion.cloud.api.data.IBloc;
import org.evasion.cloud.api.data.IView;
import org.evasion.cloud.service.model.Bloc;
import org.evasion.cloud.service.model.View;

/**
 *
 * @author sgl
 */
public class CView extends IConverter<View> implements IView {

    public CView(View view) {
        super(view);
    }
    public CView() {
        super(new View());
    }

    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getEntity().setTitle(title);
    }

    @Override
    public String getUrl() {
        return getEntity().getUrl();
    }

    @Override
    public void setUrl(String url) {
        getEntity().setUrl(url);
    }

    @Override
    public String getDescription() {
        return getEntity().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getEntity().setDescription(description);
    }

    @Override
    public String getContent() {
        return getEntity().getContent();
    }

    @Override
    public void setContent(String content) {
        getEntity().setContent(content);
    }

    @Override
    public Set<IBloc> getBlocs() {
         Set<IBloc> result = new HashSet<IBloc>();
        
        CollectionUtils.collect(getEntity().getBlocs(), new Transformer() {
            @Override
            public Object transform(Object o) {
                return new CBloc((Bloc) o);
            }
        }, result);
         return result;
                 
    }

    @Override
    public void setBlocs(SortedSet<IBloc> blocs) {
        getEntity().setBlocs(null);
    }

}
