/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.converter;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.evasion.cloud.api.data.IBloc;
import org.evasion.cloud.api.data.IView;
import org.evasion.cloud.service.model.Bloc;
import org.evasion.cloud.service.model.ContentType;
import org.evasion.cloud.service.model.View;

/**
 *
 * @author sgl
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CView implements IView, IConverter<View> {

    public CView(View view) {
        this.entity = view;
    }

    public CView() {
        this.entity = new View();
    }


    View entity;

    @XmlTransient    
    public View getEntity() {
        return entity;
    }

    @XmlAttribute
    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public void setTitle(String title) {
        getEntity().setTitle(title);
    }

    @XmlAttribute
    @Override
    public String getUrl() {
        return getEntity().getUrl();
    }

    @Override
    public void setUrl(String url) {
        getEntity().setUrl(url);
    }

    @XmlAttribute
    @Override
    public String getDescription() {
        return getEntity().getDescription();
    }

    @Override
    public void setDescription(String description) {
        getEntity().setDescription(description);
    }

    @XmlAttribute
    @Override
    public String getContent() {
        if (ContentType.staticContent.equals(getEntity().getType())) {
            return getEntity().getContent();
        } else {
            return null;
        }
    }

    @Override
    public void setContent(String content) {
        if (content != null) {
            getEntity().setContent(content);
            getEntity().setType(ContentType.staticContent);
        }
    }

    @XmlElement
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

    @XmlAttribute
    @Override
    public String getTemplate() {
        if (ContentType.template.equals(getEntity().getType())) {
            return getEntity().getContent();
        } else {
            return null;
        }
    }

    @Override
    public void setTemplate(String template) {
        if (template != null) {
            getEntity().setContent(template);
            getEntity().setType(ContentType.template);
        }
    }

    @XmlAttribute
    @Override
    public String getTemplateUrl() {
        if (ContentType.templateUrl.equals(getEntity().getType())) {
            return getEntity().getContent();
        } else {
            return null;
        }
    }
    
    @XmlAttribute
    @Override
    public void setTemplateUrl(String templateUrl) {
        if (templateUrl != null) {
            getEntity().setContent(templateUrl);
            getEntity().setType(ContentType.templateUrl);
        }
    }

    @Override
    public int getIndex() {
        return getEntity().getIndex();
    }

    @Override
    public void setIndex(int index) {
        getEntity().setIndex(index);
    }

}
