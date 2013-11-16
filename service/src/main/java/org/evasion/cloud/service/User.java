/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import javax.jdo.annotations.PersistenceCapable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author sgl
 */
@PersistenceCapable
public class User {

    @XmlElement(name = "name")
    String name;
    
    @XmlElement(name="googleId")
    String googleId;

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
    
}
