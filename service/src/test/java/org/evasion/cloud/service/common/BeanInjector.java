// Copyright 2013 Broad Institute, Inc.  All rights reserved.
package org.evasion.cloud.service.common;

import com.google.api.services.oauth2.model.Userinfo;
import java.lang.reflect.Type;
import java.security.Principal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import org.evasion.cloud.service.security.EvasionSecurityContext;

/**
 * This class enables testing of Jersey resources using JerseyTest.
 * It gets injected into the Jersey WebResource with the @Context annotation
 * to provide security info, in particular the username.
 * 
 * original author mocana
 */
public class BeanInjector {
	/** set this to log the user in, in a junit test */
    static public Userinfo user;
    static public String googleid;

    @Provider
    public static class DummySecurityContextProvider implements InjectableProvider<Context, Type> {

	public ComponentScope getScope() {
	    return ComponentScope.PerRequest;
	}

	public Injectable<SecurityContext> getInjectable(ComponentContext ic, Context a, Type c) {
	    if (c != SecurityContext.class)
		return null;

	    return new Injectable<SecurityContext>() {
		public EvasionSecurityContext getValue() {
		    return new EvasionSecurityContext(false, googleid, user);
		}
	    };
	}

    }

}