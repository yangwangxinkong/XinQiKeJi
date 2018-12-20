package com.xss.service;

import com.xss.dao.ConfigsDao;
import com.xss.domain.Configs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 *  Service - 系统配置
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class ConfigsService extends BaseService<Configs,Long> {
    @Resource
    private ConfigsDao configsDao;
    @Resource
    public void setBaseDao(ConfigsDao configsDao) {
        super.setBaseDao(configsDao);
    }

    public ConfigsDao getConfigsDao() {
        return configsDao;
    }

    public void setConfigsDao(ConfigsDao configsDao) {
        this.configsDao = configsDao;
    }

    @Transactional(readOnly = true)
    public Configs getConfigsByCode(String code) {
        if (!StringUtils.hasText(code)) {
            return null;
        }
        String jpql = "select config from Configs config where lower(config.code) = lower(:code)";
        List<Configs> ConfigsList = entityManager.createQuery(jpql, Configs.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getResultList();
        if(ConfigsList != null && ConfigsList.size() > 0) {
            return ConfigsList.get(0);
        } else {
            return null;
        }
    }
}
