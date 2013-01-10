/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evasion.web;

import com.evasion.web.security.User;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author seglon
 */
@ApplicationPath("/")
public class ServletApplication extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(User.class);
        return s;
    }
}
