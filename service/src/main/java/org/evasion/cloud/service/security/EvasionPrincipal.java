/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.security;

import com.google.api.services.oauth2.model.Userinfo;
import java.security.Principal;

/**
 *
 * @author sgl
 */
public final class EvasionPrincipal implements Principal {

    private final String cookieId;
    private final Userinfo userInfo;

    public EvasionPrincipal(String cookieId, Userinfo user) {
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

    @Override
    public String toString() {
        return new StringBuilder("EvasionPrincipal{").append("cookieId=").
                append(cookieId).append(cookieId).append(", userInfo=").
                append(userInfo).append('}').toString();
    }

}
