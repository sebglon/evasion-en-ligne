/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.evasion.cloud.api.data.ISite;

/**
 *
 * @author sgl
 */
@Path("site")
public interface ISiteService {

    @GET()
    @Path("{siteid}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    ISite get(@PathParam("siteid") long id);

    @GET()
    @Path("bySubdmain/{subdomain}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    ISite getBySubDomain(@PathParam("subdomain") String subdmain);

    @POST()
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    ISite create(@FormParam("subdomain") String subdomain);

    @PUT()
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    ISite update(ISite site);

}
