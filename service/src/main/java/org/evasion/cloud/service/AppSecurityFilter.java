/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.Oauth2;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.evasion.cloud.service.security.EvasionSecurityContext;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
public class AppSecurityFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AppSecurityFilter.class);

    private String userId=null;
    private boolean isNew = true;

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        // Generation d'un Coockie de suivi pour tous nouveaux utilisateurs
        if (isNew) {
            NewCookie userIdCookie = new NewCookie(Constant.COOKIE_NAME, userId, "/", null, 1, "no cmoment", 999999, false);
            javax.ws.rs.core.Response cookieResponse = Response.fromResponse(response.getResponse()).cookie(userIdCookie).build();
            response.setResponse(cookieResponse);
        }
        return response;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        LOG.debug("user cookie: {}", request.getCookies().get(Constant.COOKIE_NAME));
        if (!request.getCookies().containsKey(Constant.COOKIE_NAME)) {
            userId = UUID.randomUUID().toString();
            isNew = true;
        } else {
            userId = request.getCookies().get(Constant.COOKIE_NAME).getValue();
            isNew = false;
            try {
                Credential credential = OauthCodeFlow.getFlow().loadCredential(userId);
                if (credential != null) {
                    Oauth2 service = new Oauth2.Builder(OauthCodeFlow.HTTP_TRANSPORT, OauthCodeFlow.JSON_FACTORY, credential).build();
                    request.setSecurityContext(new EvasionSecurityContext(userId, service.userinfo().get().execute()));
                }
            } catch (IOException ex) {
                LOG.error("FAIL to load Google credential", ex);
            }

        }
        return request;
    }
}