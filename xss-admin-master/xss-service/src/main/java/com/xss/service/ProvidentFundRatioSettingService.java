package com.xss.service;

import com.xss.dao.ProvidentFundRatioSettingDao;
import com.xss.domain.City;
import com.xss.domain.ProvidentFundRatioSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.List;

/**
 *  Service - 公积金比例配置
 * @author zzl
 * @date 2018/8/29
 */
@Service
public class ProvidentFundRatioSettingService extends BaseService<ProvidentFundRatioSetting,Long> {
    @Resource
    private ProvidentFundRatioSettingDao providentFundRatioSettingDao;
    @Resource
    public void setBaseDao(ProvidentFundRatioSettingDao providentFundRatioSettingDao) {
        super.setBaseDao(providentFundRatioSettingDao);
    }

    public ProvidentFundRatioSettingDao getProvidentFundRatioSettingDao() {
        return providentFundRatioSettingDao;
    }

    public void setProvidentFundRatioSettingDao(ProvidentFundRatioSettingDao providentFundRatioSettingDao) {
        this.providentFundRatioSettingDao = providentFundRatioSettingDao;
    }

    @Transactional(readOnly = true)
    public List<ProvidentFundRatioSetting> getProvidentFundRatioSetting(City city) {
        if (city == null) {
            return null;
        }
        String jpql = "select providentFundRatioSetting from ProvidentFundRatioSetting providentFundRatioSetting where providentFundRatioSetting.city.id = :cityId";
        return entityManager.createQuery(jpql, ProvidentFundRatioSetting.class).setFlushMode(FlushModeType.COMMIT).setParameter("cityId", city.getId()).getResultList();
    }
}
