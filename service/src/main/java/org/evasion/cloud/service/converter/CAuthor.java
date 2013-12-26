/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.service.converter;

import org.evasion.cloud.api.data.IAuthor;
import org.evasion.cloud.service.User;

/**
 *
 * @author sgl
 */
public class CAuthor extends IConverter<User> implements IAuthor{

    public CAuthor(User entity) {
        super(entity);
    }
    
}
