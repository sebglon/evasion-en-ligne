/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.service.converter;

/**
 *
 * @author sgl
 * @param <BEAN>
 */
public abstract class IConverter<BEAN extends Object> {
    
    BEAN entity;
    
    public BEAN getEntity() {
        return entity;
    }

    public IConverter(BEAN entity) {
        this.entity = entity;
    }

    public IConverter() {
    }
    
}
