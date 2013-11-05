/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import java.util.UUID;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author sgl
 */
public class AppSecurityFilter implements ContainerResponseFilter {

    @Context
    private UriInfo uri;

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

        if (!request.getCookies().containsKey(Constant.COOKIE_NAME)) {
            final String userId = UUID.randomUUID().toString();
            NewCookie userIdCookie = new NewCookie(Constant.COOKIE_NAME, userId, "/", null, 1, "no cmoment", 999999, false);
            javax.ws.rs.core.Response cookieResponse = Response.fromResponse(response.getResponse()).cookie(userIdCookie).build();

            response.setResponse(cookieResponse);
        }
        return response;
    }

}
