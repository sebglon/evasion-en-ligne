/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.converter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.evasion.cloud.api.data.IBloc;
import org.evasion.cloud.service.model.Bloc;

/**
 *
 * @author sgl
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CBloc implements IBloc, IConverter<Bloc> {

    Bloc entity;

    @XmlTransient
    public Bloc getEntity() {
        return entity;
    }

    public CBloc(Bloc entity) {
        this.entity = entity;
    }

    public CBloc() {
        this.entity = new Bloc();
    }

}
