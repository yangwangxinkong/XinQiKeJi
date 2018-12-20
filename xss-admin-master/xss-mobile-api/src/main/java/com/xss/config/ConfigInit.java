package com.xss.config;

import com.xss.domain.Configs;
import com.xss.service.ConfigsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzl
 * @date 2018-09-13
 */
@Component
public class ConfigInit implements InitializingBean {
    public static final Map<String, Configs> SYSTEM_CONFIGS = new HashMap<>();

    @Autowired
    private ConfigsService configsService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Configs> configs = configsService.findAll();
        for (Configs config: configs){
            SYSTEM_CONFIGS.put(config.getCode(), config);
        }
    }
}
