/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.api.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.evasion.cloud.api.data.IModuleDescriptor;

/**
 *
 * @author sgl
 */
public interface IModuleService {

    @GET
    @Path("/module/Descriptor")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    IModuleDescriptor getModuleDescriptor();
}
