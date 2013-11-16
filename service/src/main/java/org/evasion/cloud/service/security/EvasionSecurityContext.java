/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.service.security;

import com.google.api.services.oauth2.model.Userinfo;
import com.google.apphosting.api.ApiProxy;
import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author sgl
 */
public class EvasionSecurityContext implements SecurityContext{

    private EvasionPrincipal principal;
    private boolean secure;
    
    public EvasionSecurityContext(boolean secure, String cookieId, Userinfo user) {
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
        return ApiProxy.getCurrentEnvironment().isAdmin()&& "admin".equalsIgnoreCase(role) || "user".equalsIgnoreCase(role);
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getAuthenticationScheme() {
        return "GoogleAuth";
    }
}
