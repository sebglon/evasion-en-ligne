/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import javax.jdo.annotations.PersistenceCapable;

/**
 *
 * @author sgl
 */
@PersistenceCapable
public class User {

    private String name;
    
    private String googleId;

    public User(String name, String googleId) {
        this.name = name;
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public String getGoogleId() {
        return googleId;
    }

    @Override
    public String toString() {
        return "User{" + "name=" + name + ", googleId=" + googleId + '}';
    }
    
}
