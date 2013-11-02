/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.appengine.api.utils.SystemProperty;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seglon
 */
@Path("user")
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static final Collection scopes = Arrays.asList(Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE);

    public static final String COOKIE_NAME = "eelid";

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final String clientid = "148280693971.apps.googleusercontent.com";
    private static final HttpExecuteInterceptor clientCredential = new ClientParametersAuthentication(clientid, "UMTJRicVrvN572uLTQ_6i8l9");
    @Context
    private UriInfo uri;

    private AuthorizationCodeFlow flow;

    private boolean isSecureMode() {
        return SystemProperty.environment.value()==SystemProperty.Environment.Value.Production;
    }
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
    public Response getToken(@QueryParam("code") String code) throws IOException, URISyntaxException {
        try {
            final String userId = UUID.randomUUID().toString();
            NewCookie userIdCookie = new NewCookie(COOKIE_NAME, userId, "/", uri.getBaseUri().toASCIIString(), null, NewCookie.DEFAULT_MAX_AGE, false);

            LOG.debug("ClientID fount on cookie :{}", userId);
            if (flow == null) {
                flow = initializeFlow();
            }
            TokenResponse token = flow.newTokenRequest(code).setRedirectUri(getUrlBase(isSecureMode()) + "/callback.html").execute();
            LOG.debug("http response {}", token);
            initializeFlow().createAndStoreCredential(token, userId);
            return Response.ok(token, MediaType.APPLICATION_JSON).cookie(userIdCookie).build();
        } catch (IOException ex) {
            LOG.error("Can't optain token", ex);
            throw ex;
        }
    }

    @POST
    @Path("info")
    @Produces({MediaType.APPLICATION_JSON})
    public Userinfo getInfo(@CookieParam(COOKIE_NAME) String clientId) throws IOException {
        //  Credential credential = initializeFlow().loadCredential(clientId);
        //  Oauth2 service = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();
        //  return service.userinfo().get().execute();
        Userinfo user = new Userinfo();
        user.setEmail("test@test.fr");
        return user;
    }

    private AuthorizationCodeFlow initializeFlow() throws IOException {
        return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl("https://accounts.google.com/o/oauth2/token"),
                clientCredential, clientid, "https://accounts.google.com/o/oauth2/auth").setScopes(scopes).build();
    }

    private String getUrlBase(boolean secure) {
        StringBuilder build = new StringBuilder();
        if (secure) {
            build.append("https://");
        } else {
            build.append("http://");
        }
        return build.append(uri.getBaseUri().getAuthority()).toString();
    }

}
