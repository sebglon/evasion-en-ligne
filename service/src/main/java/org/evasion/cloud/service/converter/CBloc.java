/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.service.converter;

import org.evasion.cloud.api.data.IBloc;
import org.evasion.cloud.service.model.Bloc;

/**
 *
 * @author sgl
 */
public class CBloc extends IConverter<Bloc> implements IBloc{

    public CBloc(Bloc entity) {
        super(entity);
    }

    public CBloc() {
        super(new Bloc());
    }
    
}
