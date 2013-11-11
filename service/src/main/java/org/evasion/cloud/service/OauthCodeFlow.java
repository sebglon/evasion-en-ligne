/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2Scopes;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sgl
 */
public class OauthCodeFlow {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(OauthCodeFlow.class);
    
    public static final Collection scopes = Arrays.asList(Oauth2Scopes.USERINFO_EMAIL, Oauth2Scopes.USERINFO_PROFILE);

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final String clientid = "148280693971.apps.googleusercontent.com";
    private static final HttpExecuteInterceptor clientCredential = new ClientParametersAuthentication(clientid, "UMTJRicVrvN572uLTQ_6i8l9");

    private static AuthorizationCodeFlow flow;
    
    static {
        try {
            flow = initializeFlow();
        } catch (IOException ex) {
           LOG.error("FAIL to init Google Flow", ex);
        }
    }

    /**
     * Initialise le gestionnaire d'authentification Oauth2 Google. Les
     * utilisateurs déjà connecter seront enregistré dans l'AppengineDataStore.
     *
     * @return @throws IOException
     */
    private static AuthorizationCodeFlow initializeFlow() throws IOException {
        return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new GenericUrl("https://accounts.google.com/o/oauth2/token"),
                clientCredential, clientid, "https://accounts.google.com/o/oauth2/auth").setScopes(scopes).setDataStoreFactory(new AppEngineDataStoreFactory()).build();
    }
    public static AuthorizationCodeFlow getFlow()  {
        return flow;
    }
}
