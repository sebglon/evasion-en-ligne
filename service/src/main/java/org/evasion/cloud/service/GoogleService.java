/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.api.utils.SystemProperty;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.annotation.security.DeclareRoles;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.evasion.cloud.service.security.EvasionPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seglon
 */
@Path("user")
public class GoogleService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleService.class);

    @Context
    private UriInfo uri;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("auth")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAuth(@CookieParam(Constant.COOKIE_NAME) String userId,
            @QueryParam("redirect") String redirect)
            throws IOException {
        if (OauthCodeFlow.getFlow().getCredentialDataStore().containsKey(userId)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.temporaryRedirect(OauthCodeFlow.getFlow().newAuthorizationUrl().setState(redirect).setScopes(OauthCodeFlow.scopes).setRedirectUri(getUrlBase(isSecureMode()) + "/callback.html").toURI()).build();
        }
    }

    @GET
    @Path("token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getToken(@CookieParam(Constant.COOKIE_NAME) String userId,
            @QueryParam("code") String code)
            throws IOException, URISyntaxException {
        LOG.debug("ClientID fount on cookie :{}", userId);
        if (userId == null || (!OauthCodeFlow.getFlow().getCredentialDataStore().containsKey(userId) && code == null)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (!OauthCodeFlow.getFlow().getCredentialDataStore().containsKey(userId) && code != null) {
            TokenResponse token = OauthCodeFlow.getFlow().newTokenRequest(code).setRedirectUri(getUrlBase(isSecureMode()) + "/callback.html").execute();
            LOG.debug("http response {}", token);
            OauthCodeFlow.getFlow().createAndStoreCredential(token, userId);
        }
        StoredCredential credential = OauthCodeFlow.getFlow().getCredentialDataStore().get(userId);
        LOG.debug("http response {}", credential.getAccessToken());
        return Response.ok(credential.getAccessToken(), MediaType.APPLICATION_JSON)
                .build();

    }

    @GET
    @Path("logout")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLogout() throws IOException {
        if (null == securityContext.getUserPrincipal()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        OauthCodeFlow.getFlow().getCredentialDataStore().delete(((EvasionPrincipal) securityContext.getUserPrincipal()).getCookieValue());
        return Response.ok().build();
    }

    @GET
    @Path("info")
    @Produces({MediaType.APPLICATION_JSON})
    public Userinfo getInfo() throws IOException {
        if (null == securityContext.getUserPrincipal()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        return ((EvasionPrincipal) securityContext.getUserPrincipal()).getUserInfo();
    }

    /**
     * Construit une URL de base du serveur d'authentification.
     *
     * @param secure {@code true} utilisatio de l'https.
     * @return url de la forme http(s)://host
     */
    private String getUrlBase(boolean secure) {
        StringBuilder build = new StringBuilder();
        if (secure) {
            build.append("https://");
        } else {
            build.append("http://");
        }
        return build.append(uri.getBaseUri().getAuthority()).toString();
    }

    /**
     * Détermise le protocole à utiliser.
     *
     * @return {
     * @cod true} en environneent de production, sinon {@code false}
     */
    private boolean isSecureMode() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
    }

}
