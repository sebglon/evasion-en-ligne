/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.service.security;

import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import com.google.apphosting.api.ApiProxy;
import java.security.Principal;
import javax.ws.rs.core.SecurityContext;
import org.evasion.cloud.service.OauthCodeFlow;

/**
 *
 * @author sgl
 */
public class EvasionSecurityContext implements SecurityContext{

    private Principal principal;
    
    public EvasionSecurityContext(String cookieId, Userinfo user) {
        if (user!=null) {
            principal = new EvasionPrincipal(cookieId, user);
        }
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return ApiProxy.getCurrentEnvironment().isAdmin()&& "admin".equalsIgnoreCase(role);
    }

    @Override
    public boolean isSecure() {
        return ((EvasionPrincipal)principal).getUserInfo()!=null;
    }

    @Override
    public String getAuthenticationScheme() {
        return "GoogleAuth";
    }
   
    public static final class EvasionPrincipal implements Principal{

        private final String cookieId;
        private final Userinfo userInfo;
        
        private EvasionPrincipal(String cookieId, Userinfo user) {
            this.cookieId = cookieId;
            userInfo = user;
        }

        @Override
        public String getName() {
            return userInfo.getName();
        }

        public Userinfo getUserInfo() {
            return userInfo;
        }
        public String getCookieValue() {
            return this.cookieId;
        }
        
    }
}
