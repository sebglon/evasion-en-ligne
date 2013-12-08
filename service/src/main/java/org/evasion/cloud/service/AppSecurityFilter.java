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
import java.net.URI;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import org.evasion.cloud.service.security.EvasionSecurityContext;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
@Provider
public class AppSecurityFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AppSecurityFilter.class);

    private String userId = null;
    private boolean isNew = true;

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        // Generation d'un Coockie de suivi pour tous nouveaux utilisateurs
        if (isNew) {
            LOG.debug("domain .{}", getDomain(request.getBaseUri()));
            NewCookie userIdCookie = new NewCookie(Constant.COOKIE_NAME, userId, "/", getDomain(request.getBaseUri()), 1, "no comment", 999999, false);
            javax.ws.rs.core.Response cookieResponse = Response.fromResponse(response.getResponse()).cookie(userIdCookie).build();
            response.setResponse(cookieResponse);
        }
        return response;
    }

    private String getDomain(URI url) {
        String domain;
        Pattern p = Pattern.compile("[.]");
        String s[] = p.split(url.getHost());
        if (s.length>1) {
        domain = s[s.length-2] +"."+ s[s.length-1];
        } else {
            domain =  s[s.length-1];
        }
        return domain;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        LOG.debug("user cookie: {} / ", request.getCookies().get(Constant.COOKIE_NAME), request.getCookies().get("X-"+Constant.COOKIE_NAME));
        if (!request.getCookies().containsKey(Constant.COOKIE_NAME)) {
            userId = UUID.randomUUID().toString();
            isNew = true;
        } else {
            userId = request.getCookies().get(Constant.COOKIE_NAME).getValue();
            isNew = false;
            try {
                Credential credential = OauthCodeFlow.getFlow().loadCredential(userId);
                if (credential != null) {
                    credential.refreshToken();
                    Oauth2 service = new Oauth2.Builder(OauthCodeFlow.HTTP_TRANSPORT, OauthCodeFlow.JSON_FACTORY, credential).build();
                    request.setSecurityContext(new EvasionSecurityContext("https".equalsIgnoreCase(request.getBaseUri().getScheme()), userId, service.userinfo().get().execute()));
                }
            } catch (IOException ex) {
                LOG.error("FAIL to load Google credential", ex);
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }

        }
        return request;
    }
}
