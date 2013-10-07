/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author sgl
 */
public class User {
     @XmlElement(name = "name") String name;

    public String getName() {
        return name;
    }   
}
