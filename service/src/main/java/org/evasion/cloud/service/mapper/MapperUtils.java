/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evasion.cloud.service.mapper;

import java.util.ArrayList;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.evasion.cloud.api.data.ISite;
import org.evasion.cloud.service.model.Site;

/**
 *
 * @author sgl
 */
public class MapperUtils {


    private static final DozerBeanMapper mapper;
    static {
        mapper = new DozerBeanMapper();
    List<String> myMappingFiles = new ArrayList<String>();
        myMappingFiles.add("dozerMapper.xml");
        mapper.setMappingFiles (myMappingFiles);
    }

    public static ISite convertFromSite(Site site) {
        return mapper.map(site, ISite.class);
    }
    public static Site convertToSite(ISite site) {
        return mapper.map(site, Site.class);
    }
}
