/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.evasion.cloud.api.data;

import java.io.Serializable;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sgl
 */
@XmlRootElement
public class IModuleDescriptor implements Serializable {
    
        private final String id;
        private final String name;
        private final String version;
        private final Map<String, String> entryPoint;

    public IModuleDescriptor(String id, String name, String version, Map<String, String> entryPoint) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.entryPoint = entryPoint;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getEntryPoint() {
        return entryPoint;
    }
    
}
