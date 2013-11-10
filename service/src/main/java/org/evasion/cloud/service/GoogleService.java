/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.api.utils.SystemProperty;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seglon
 */
@Path("user")
public class GoogleService {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleService.class);

    private static final Collection scopes = Arrays.asList(Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE);

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final String clientid = "148280693971.apps.googleusercontent.com";
    private static final HttpExecuteInterceptor clientCredential = new ClientParametersAuthentication(clientid, "UMTJRicVrvN572uLTQ_6i8l9");
    @Context
    private UriInfo uri;

    @Context
    private SecurityContext securityContext;

    private AuthorizationCodeFlow flow;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User get() {
        User user = new User();
        user.name = "UserTest";
        return user;
    }

    @GET
    @Path("auth")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAuth(@QueryParam("redirect") String redirect) {
        try {
            if (flow == null) {
                flow = initializeFlow();
            }

            return Response.temporaryRedirect(flow.newAuthorizationUrl().setState(redirect).setScopes(scopes).setRedirectUri(getUrlBase(isSecureMode()) + "/callback.html").toURI()).build();
        } catch (IOException ex) {
            LOG.error("Can not Access to AuthorizationCodeFlow", ex);
            return Response.serverError().build();
        }
    }

    @GET
    @Path("token")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getToken(@CookieParam(Constant.COOKIE_NAME) String userId, @QueryParam("code") String code) throws IOException, URISyntaxException {
        try {
            LOG.debug("ClientID fount on cookie :{}", userId);
            if (flow == null) {
                flow = initializeFlow();
            }
            TokenResponse token = flow.newTokenRequest(code).setRedirectUri(getUrlBase(isSecureMode()) + "/callback.html").execute();
            LOG.debug("http response {}", token);
            flow.createAndStoreCredential(token, userId);
            LOG.debug("UPN: {}", securityContext.getUserPrincipal().toString());
            return Response.ok(token, MediaType.APPLICATION_JSON).build();
        } catch (IOException ex) {
            LOG.error("Can't optain token", ex);
            throw ex;
        }
    }

    @GET
    @Path("logout")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getLogout(@CookieParam(Constant.COOKIE_NAME) String userId) throws IOException {
            if (flow == null) {
                return Response.serverError().build();
            }
            flow.getCredentialDataStore().delete(userId);
            return Response.ok().build();
    }

    @GET
    @Path("info")
    @Produces({MediaType.APPLICATION_JSON})
    public Userinfo getInfo(@CookieParam(Constant.COOKIE_NAME) String clientId) throws IOException {
        Credential credential = initializeFlow().loadCredential(clientId);
        if (credential == null) {
            LOG.error("Credential not found for cientID: {}", clientId);
        }
        Oauth2 service = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
        return service.userinfo().get().execute();
    }

    /**
     * Initialise le gestionnaire d'authentification Oauth2 Google. Les
     * utilisateurs déjà connecter seront enregistré dans l'AppengineDataStore.
     *
     * @return @throws IOException
     */
    private AuthorizationCodeFlow initializeFlow() throws IOException {
        return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl("https://accounts.google.com/o/oauth2/token"),
                clientCredential, clientid, "https://accounts.google.com/o/oauth2/auth").setScopes(scopes).setDataStoreFactory(new AppEngineDataStoreFactory()).build();
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
