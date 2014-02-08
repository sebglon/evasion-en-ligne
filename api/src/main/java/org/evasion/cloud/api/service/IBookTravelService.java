/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.evasion.cloud.api.data.booktravel.IBook;

/**
 *
 * @author sgl
 */
@Path("booktravel")
public interface IBookTravelService {

    @GET()
    @Path("/version")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    String getVersion();

    @GET()
    @Path("/available/{shortName}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response isAvailable(@PathParam("shortName") String shortName);
    
        @GET()
    @Path("/byid/{bookid}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    IBook get(@PathParam("bookid") String id);

    @POST()
    @Path("/{siteid}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    IBook create(@PathParam("siteid") String siteid, IBook book);

    @PUT()
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    IBook update(IBook book);

}
