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
import org.evasion.cloud.api.data.IAuthor;
import org.evasion.cloud.service.User;

/**
 *
 * @author sgl
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)

public class CAuthor implements IAuthor, IConverter<User> {

    public CAuthor(User entity) {
        this.entity = entity;
    }

    User entity;

    @XmlTransient
    public User getEntity() {
        return entity;
    }

}
