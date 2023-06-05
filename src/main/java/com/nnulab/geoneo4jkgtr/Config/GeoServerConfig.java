package com.nnulab.geoneo4jkgtr.Config;

import com.nnulab.geoneo4jkgtr.Util.GeoServerUtil;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.URL;

/**
 * @author : LiuXianYu
 * @date : 2023/5/16 17:32
 */
@Configuration
public class GeoServerConfig {
    @Resource
    GeoServerUtil geoServerUtil;

    @Bean
    public GeoServerRESTManager geoServerRESTManagerFactory() {
        try {
            return new GeoServerRESTManager(new URL(geoServerUtil.getUrl()), geoServerUtil.getUsername(),
                    geoServerUtil.getPassword());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
