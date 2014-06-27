/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.common;

import com.google.api.services.oauth2.model.Userinfo;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import org.evasion.cloud.service.security.EvasionSecurityContext;
import org.junit.Assert;

/**
 *
 * @author sgl
 */
@PreMatching
public class SecurityContextFilter implements ContainerRequestFilter {

    static public Userinfo user;
    static public String googleid;
    private static final String SKIP_FILTER = "skipFilter";

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        Assert.assertNotNull(context.getSecurityContext());
        String header = context.getHeaders().getFirst(SKIP_FILTER);
        if ("true".equals(header)) {
            return;
        }

        context.setSecurityContext(new EvasionSecurityContext(false, googleid, user));
    }

}
